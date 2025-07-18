package com.example.auction_system.model;

import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// A DTO for the incoming bid request from the API
@Entity
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class BidRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "auction_id", nullable = false)
    private Auction auction;

    private BigDecimal amount;

    private String bidder; // In a real app, this would be a user ID

    @CreatedDate
    private LocalDateTime bidTime;

    public BidRequest(Auction auction, BigDecimal amount, String bidder) {
        this.auction = auction;
        this.amount = amount;
        this.bidder = bidder;
    }


}