package com.example.auction_system.model;

import lombok.Data;
import java.math.BigDecimal;

// A DTO for the incoming bid request from the API
@Data
public class BidRequest {
    private BigDecimal amount;
    private String bidder; // In a real app, this would be a user ID
}