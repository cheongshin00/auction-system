package com.example.auction_system.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.Data;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String itemName;
    private String description;
    private BigDecimal startingPrice;
    private BigDecimal currentPrice;
    private LocalDateTime endTime;
    private boolean active = true;

    @Version // Marks this field for optimistic locking
    private Integer version;
}
