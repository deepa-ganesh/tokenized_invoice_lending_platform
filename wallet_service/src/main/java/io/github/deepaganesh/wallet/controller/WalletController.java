package io.github.deepaganesh.wallet.controller;

import io.github.deepaganesh.wallet.entity.TokenBalance;
import io.github.deepaganesh.wallet.entity.Wallet;
import io.github.deepaganesh.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;

@RestController
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping("/create/{userId}")
    public Wallet createWallet(@PathVariable Long userId) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        return walletService.createWallet(userId);
    }

    @GetMapping("/balances/{userId}")
    public List<TokenBalance> getBalances(@PathVariable Long userId) {
        return walletService.getBalances(userId);
    }

    @PostMapping("/credit")
    public void creditWallet(@RequestParam Long userId,
                             @RequestParam String tokenSymbol,
                             @RequestParam BigDecimal amount) {

        walletService.creditBalance(userId, tokenSymbol, amount);
    }

    @GetMapping("/{userId}/address")
    public ResponseEntity<String> getWalletAddress(@PathVariable Long userId) {
        return ResponseEntity.ok(walletService.getWalletAddress(userId));
    }
}
