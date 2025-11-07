package com.example.bidding.service;


import com.example.bidding.dto.BidRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Service
public class BidService {
    private static final Logger log = LoggerFactory.getLogger(BidService.class);


    private final RedisTemplate<String, Object> redisTemplate;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper = new ObjectMapper();


    public BidService(RedisTemplate<String, Object> redisTemplate, KafkaTemplate<String, String> kafkaTemplate) {
        this.redisTemplate = redisTemplate;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void startAuction(String auctionId) {
        String key = redisKey(auctionId);
// initialize highest bid to 0
        redisTemplate.opsForValue().set(key, 0L);
// set a TTL optionally
        redisTemplate.expire(key, 7, TimeUnit.DAYS);
        log.info("Auction {} started", auctionId);
    }


    public Long getCurrentPrice(String auctionId) {
        String key = redisKey(auctionId);
        Object val = redisTemplate.opsForValue().get(key);
        if (val == null) return 0L;
        return (Long) val;
    }

    public synchronized boolean increaseBid(String auctionId, BidRequest req) {
        String key = redisKey(auctionId);
        Object current = redisTemplate.opsForValue().get(key);
        long curr = current == null ? 0L : (Long) current;
        if (req.getAmount() <= curr) {
// publish reject event
            publishBidEvent(auctionId, req.getBidderId(), req.getAmount(), "REJECTED");
            publishError("Bid too low for auction " + auctionId + " amount=" + req.getAmount());
            return false;
        }
// accept
        redisTemplate.opsForValue().set(key, req.getAmount());
        publishBidEvent(auctionId, req.getBidderId(), req.getAmount(), "ACCEPTED");
        return true;
    }

    public void endAuction(String auctionId) {
// optional: mark ended in redis by setting special key
        String key = redisKey(auctionId);
        redisTemplate.delete(key);
        publishError("Auction " + auctionId + " ended");
        log.info("Auction {} ended", auctionId);
    }


    private void publishBidEvent(String auctionId, String bidderId, long amount, String status) {
        try {
            var node = mapper.createObjectNode();
            node.put("auctionId", auctionId);
            node.put("bidderId", bidderId);
            node.put("amount", amount);
            node.put("status", status);
            node.put("timestamp", Instant.now().toEpochMilli());
            String payload = mapper.writeValueAsString(node);
            kafkaTemplate.send("bids", auctionId, payload);
        } catch (Exception e) {
            log.error("Failed to publish bid event", e);
        }
    }

    private void publishError(String message) {
        try {
            kafkaTemplate.send("errors", "err", message);
        } catch (Exception e) {
            log.error("Failed to publish error", e);
        }
    }


    private String redisKey(String auctionId) {
        return "auction:price:" + auctionId;
    }
}