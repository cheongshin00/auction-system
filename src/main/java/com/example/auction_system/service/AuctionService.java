package com.example.auction_system.service;

import com.example.auction_system.model.BidRequest;
import com.example.auction_system.repository.BidRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.auction_system.event.BidPlacedEvent;
import com.example.auction_system.model.Auction;
import com.example.auction_system.repository.AuctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service // Marks this class as a Service bean
public class AuctionService {

    private static final Logger logger = LoggerFactory.getLogger(AuctionService.class);
    private final AuctionRepository auctionRepository;
    private final BidRequestRepository bidRequestRepository;
    private final ApplicationEventPublisher eventPublisher;

    // @Autowired tells Spring to inject the AuctionRepository bean
    @Autowired
    public AuctionService(AuctionRepository auctionRepository, ApplicationEventPublisher eventPublisher, BidRequestRepository bidRequestRepository) {
        this.auctionRepository = auctionRepository;
        this.eventPublisher = eventPublisher;
        this.bidRequestRepository = bidRequestRepository;
    }

    public List<Auction> getAllActiveAuctions() {
        return auctionRepository.findByActive(true);
    }

    public Optional<Auction> getAuctionById(Long id) {
        return auctionRepository.findById(id);
    }

    public Auction createAuction(Auction auction) {
        // Here you could add validation logic, e.g., ensure end time is in the future
        auction.setCurrentPrice(auction.getStartingPrice());
        auction.setActive(true);
        return auctionRepository.save(auction);
    }

    public Auction placeBid(Long auctionId, BigDecimal amount, String bidder) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new IllegalStateException("Auction not found"));
        if (!auction.isActive()){
            throw new IllegalStateException("Auction is inactive");
        }
        if (amount.compareTo(auction.getCurrentPrice()) <= 0) {
            throw new IllegalStateException("Bid must be higher than the current price.");
        }
        // Update the price
        auction.setCurrentPrice(amount);
        Auction updatedAuction = auctionRepository.save(auction);

        BidRequest newBid = new BidRequest(updatedAuction, amount, bidder);
        bidRequestRepository.save(newBid);

        // *** Publish the internal event ***
        BidPlacedEvent event = new BidPlacedEvent(this, auctionId, amount, bidder);
        eventPublisher.publishEvent(event);

        return updatedAuction;
    }

    @Async
    @Scheduled(fixedRate = 60000) // Runs every 60,000 milliseconds (1 minute)
    @Transactional
    public void closeExpiredAuctions() {
        logger.info("Running scheduled job to close expired auctions...");

        List<Auction> activeAuctions = auctionRepository.findByActive(true);

        for (Auction auction : activeAuctions) {
            if (auction.getEndTime().isBefore(LocalDateTime.now())) {
                logger.info("Closing auction #{}: {}", auction.getId(), auction.getItemName());
                auction.setActive(false);
                auctionRepository.save(auction);
                // Optional: You could publish an AuctionEndedEvent to Kafka here.
            }
        }
    }
}