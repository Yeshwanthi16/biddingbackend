package com.example.bidding.repository;


import com.example.bidding.entity.BidRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BidRecordRepository extends JpaRepository<BidRecord, Long> {
}