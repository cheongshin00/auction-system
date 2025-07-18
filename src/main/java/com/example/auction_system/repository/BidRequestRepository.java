package com.example.auction_system.repository;

import com.example.auction_system.model.BidRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BidRequestRepository extends JpaRepository<BidRequest, Long> {

}
