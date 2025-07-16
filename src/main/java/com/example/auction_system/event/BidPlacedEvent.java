package com.example.auction_system.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import java.math.BigDecimal;

// Our custom internal event
@Getter
public class BidPlacedEvent extends ApplicationEvent {

    private final Long auctionId;
    private final BigDecimal amount;
    private final String bidder;

    public BidPlacedEvent(Object source, Long auctionId, BigDecimal amount, String bidder) {
        super(source);
        this.auctionId = auctionId;
        this.amount = amount;
        this.bidder = bidder;
    }

    public BidPlacedEvent(Object source, BidPlacedKafkaMessage kafkaMessage) {
        super(source);
        this.auctionId = kafkaMessage.getAuctionId();
        this.amount = kafkaMessage.getAmount();
        this.bidder = kafkaMessage.getBidder();
    }
}