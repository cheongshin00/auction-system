package com.example.auction_system.controller;

import com.example.auction_system.model.Auction;
import com.example.auction_system.model.BidRequest;
import com.example.auction_system.service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Marks this class as a controller that returns JSON data
@RequestMapping("/api/auctions") // Base URL for all endpoints in this controller
public class AuctionController {

    private final AuctionService auctionService;

    @Autowired
    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    // Handles HTTP GET requests to /api/auctions
    @GetMapping
    public List<Auction> getAllActiveAuctions() {
        return auctionService.getAllActiveAuctions();
    }

    // Handles HTTP GET requests to /api/auctions/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Auction> getAuctionById(@PathVariable Long id) {
        return auctionService.getAuctionById(id)
                .map(ResponseEntity::ok) // If found, return 200 OK with the auction
                .orElse(ResponseEntity.notFound().build()); // If not found, return 404
    }

    // Handles HTTP POST requests to /api/auctions
    @PostMapping
    public Auction createAuction(@RequestBody Auction auction) {
        return auctionService.createAuction(auction);
    }

    @PostMapping("/{id}/bids")
    public ResponseEntity<Auction> placeBid(@PathVariable Long id, @RequestBody BidRequest bidRequest) {
        try {
            Auction updatedAuction = auctionService.placeBid(id, bidRequest.getAmount(), bidRequest.getBidder());
            return ResponseEntity.ok(updatedAuction);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}