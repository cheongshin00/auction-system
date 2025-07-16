package com.example.auction_system.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

// This class is purely for Kafka serialization/deserialization
@Data // Provides getters, setters, equals, hashCode, toString, and a no-arg constructor
@NoArgsConstructor // Explicitly adds a no-argument constructor (important for Jackson)
@AllArgsConstructor // Adds a constructor with all fields (useful for producing messages)
@Builder // Optional: Provides a builder pattern for easier object creation
public class BidPlacedKafkaMessage {
    private Long auctionId;
    private BigDecimal amount;
    private String bidder;
}