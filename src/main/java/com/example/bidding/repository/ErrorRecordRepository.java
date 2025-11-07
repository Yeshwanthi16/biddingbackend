package com.example.bidding.repository;


import com.example.bidding.entity.ErrorRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErrorRecordRepository extends JpaRepository<ErrorRecord, Long> {
}