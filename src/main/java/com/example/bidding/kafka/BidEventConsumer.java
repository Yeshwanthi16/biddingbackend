package com.example.bidding.kafka;


import com.example.bidding.entity.BidRecord;
import com.example.bidding.entity.ErrorRecord;
import com.example.bidding.repository.BidRecordRepository;
import com.example.bidding.repository.ErrorRecordRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


import java.time.Instant;


@Component
public class BidEventConsumer {
    private static final Logger log = LoggerFactory.getLogger(BidEventConsumer.class);


    private final ObjectMapper mapper = new ObjectMapper();
    private final BidRecordRepository bidRepo;
    private final ErrorRecordRepository errRepo;


    public BidEventConsumer(BidRecordRepository bidRepo, ErrorRecordRepository errRepo) {
        this.bidRepo = bidRepo;
        this.errRepo = errRepo;
    }


    @KafkaListener(topics = "bids", groupId = "bidding-consumer-group")
    public void onBid(ConsumerRecord<String, String> record) {
        try {
            String payload = record.value();
            JsonNode node = mapper.readTree(payload);
            BidRecord br = new BidRecord();
            br.setAuctionId(node.path("auctionId").asText());
            br.setBidderId(node.path("bidderId").asText());
            br.setAmount(node.path("amount").asLong());
            br.setTimestamp(Instant.ofEpochMilli(node.path("timestamp").asLong()));
            br.setStatus(node.path("status").asText("ACCEPTED"));
            bidRepo.save(br);
        } catch (Exception e) {
            log.error("Failed to process bid event", e);
            ErrorRecord er = new ErrorRecord();
            er.setAuctionId("unknown");
            er.setErrorMessage(e.getMessage());
            er.setTimestamp(Instant.now());
            errRepo.save(er);
        }
    }


    @KafkaListener(topics = "errors", groupId = "bidding-consumer-group")
    public void onError(ConsumerRecord<String, String> record) {
        try {
            String payload = record.value();
            ErrorRecord er = new ErrorRecord();
            er.setAuctionId("system");
            er.setErrorMessage(payload);
            er.setTimestamp(Instant.now());
            errRepo.save(er);
        } catch (Exception e) {
            log.error("Failed to persist error event", e);
        }
    }
}