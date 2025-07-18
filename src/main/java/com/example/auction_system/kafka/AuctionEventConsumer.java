package com.example.auction_system.kafka;

import com.example.auction_system.event.BidPlacedEvent;
import com.example.auction_system.event.BidPlacedKafkaMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class AuctionEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(AuctionEventConsumer.class);
    private final SimpMessagingTemplate messagingTemplate; // <-- Inject template

    @Autowired
    public AuctionEventConsumer(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

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

        // Define the WebSocket destination topic
        String destination = "/topic/bids/" + internalEvent.getAuctionId();

        // Send the event to the WebSocket topic
        messagingTemplate.convertAndSend(destination, internalEvent);

        logger.info("Pushed bid update to WebSocket destination: {}", destination);

    }
}
