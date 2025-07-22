package io.github.deepaganesh.funding.controller;

import io.github.deepaganesh.funding.entity.FundingTransaction;
import io.github.deepaganesh.funding.service.FundingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/api/funding")
@RequiredArgsConstructor
public class FundingController {

    private final FundingService fundingService;

    @PostMapping
    public FundingTransaction fundInvoice(
            @RequestParam Long lpUserId,
            @RequestParam Long invoiceId,
            @RequestParam Long smeUserId,
            @RequestParam BigDecimal amount) {

        return fundingService.fundInvoice(lpUserId, invoiceId, smeUserId, amount);
    }

    @GetMapping
    public List<FundingTransaction> getAll() {
        return fundingService.getAll();
    }

    @GetMapping("/token-balance/{walletAddress}")
    public ResponseEntity<BigInteger> getTokenBalance(@PathVariable String walletAddress) throws Exception {
        BigInteger balance = fundingService.getTokenBalance(walletAddress);
        return ResponseEntity.ok(balance);
    }
}
