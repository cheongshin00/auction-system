package com.example.auction_system.service;

import com.example.auction_system.event.BidPlacedEvent;
import com.example.auction_system.model.Auction;
import com.example.auction_system.repository.AuctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service // Marks this class as a Service bean
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final ApplicationEventPublisher eventPublisher;

    // @Autowired tells Spring to inject the AuctionRepository bean
    @Autowired
    public AuctionService(AuctionRepository auctionRepository, ApplicationEventPublisher eventPublisher) {
        this.auctionRepository = auctionRepository;
        this.eventPublisher = eventPublisher;
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

        // *** Publish the internal event ***
        BidPlacedEvent event = new BidPlacedEvent(this, auctionId, amount, bidder);
        eventPublisher.publishEvent(event);

        return updatedAuction;
    }
}