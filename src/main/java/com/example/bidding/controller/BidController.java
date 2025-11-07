package com.example.bidding.controller;


import com.example.bidding.dto.BidRequest;
import com.example.bidding.dto.SimpleResponse;
import com.example.bidding.service.BidService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/bid")
public class BidController {
    private final BidService bidService;


    public BidController(BidService bidService) {
        this.bidService = bidService;
    }


    @PostMapping("/start/{auctionId}")
    public ResponseEntity<SimpleResponse> start(@PathVariable String auctionId) {
        bidService.startAuction(auctionId);
        return ResponseEntity.ok(new SimpleResponse("auction started"));
    }


    @PostMapping("/end/{auctionId}")
    public ResponseEntity<SimpleResponse> end(@PathVariable String auctionId) {
        bidService.endAuction(auctionId);
        return ResponseEntity.ok(new SimpleResponse("auction ended"));
    }


    @PostMapping("/{auctionId}/increase")
    public ResponseEntity<SimpleResponse> increase(@PathVariable String auctionId, @RequestBody BidRequest req) {
        boolean ok = bidService.increaseBid(auctionId, req);
        if (ok) return ResponseEntity.ok(new SimpleResponse("bid accepted"));
        else return ResponseEntity.badRequest().body(new SimpleResponse("bid rejected"));
    }


    @GetMapping("/{auctionId}/price")
    public ResponseEntity<Long> price(@PathVariable String auctionId) {
        Long p = bidService.getCurrentPrice(auctionId);
        return ResponseEntity.ok(p);
    }
}