package com.example.auction_system.kafka;

import com.example.auction_system.event.BidPlacedEvent;
import com.example.auction_system.event.BidPlacedKafkaMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AuctionEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(AuctionEventConsumer.class);

    // Listens for messages on the "auction-bids" topic
    @KafkaListener(topics = "auction-bids", groupId = "auction-group")
    public void consumeBidEvent(BidPlacedKafkaMessage message) {
        logger.info("<< KAFKA MESSAGE RECEIVED >>");
        BidPlacedEvent internalEvent = new BidPlacedEvent(this, // 'this' is a common source for Kafka listeners
                message.getAuctionId(),
                message.getAmount(),
                message.getBidder());
        logger.info("New Bid Placed on Auction #{}: Amount: {}, Bidder: {}",
                internalEvent.getAuctionId(), internalEvent.getAmount(), internalEvent.getBidder());
        // In the future, we would use this to push a WebSocket message to the frontend
    }
}
