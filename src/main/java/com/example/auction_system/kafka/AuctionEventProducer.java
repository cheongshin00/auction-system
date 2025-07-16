package com.example.auction_system.kafka;

import com.example.auction_system.event.BidPlacedEvent;
import com.example.auction_system.event.BidPlacedKafkaMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class AuctionEventProducer {

    private static final Logger logger = LoggerFactory.getLogger(AuctionEventProducer.class);
    private static final String TOPIC = "auction-bids";

    private final KafkaTemplate<String, BidPlacedKafkaMessage> kafkaTemplate;

    @Autowired
    public AuctionEventProducer(KafkaTemplate<String, BidPlacedKafkaMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // This method listens for our internal BidPlacedEvent
    @EventListener
    public void handleBidPlacedEvent(BidPlacedEvent event) {
        logger.info("Received internal bid event. Sending to Kafka topic: {}", TOPIC);
        // The event object itself is sent as the message payload
        BidPlacedKafkaMessage bidPlacedKafkaMessage = new BidPlacedKafkaMessage(
                event.getAuctionId(),
                event.getAmount(),
                event.getBidder());
        kafkaTemplate.send(TOPIC, bidPlacedKafkaMessage);
    }
}
