package io.github.deepaganesh.wallet.repository;

import io.github.deepaganesh.wallet.entity.TokenBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TokenBalanceRepository extends JpaRepository<TokenBalance, Long> {
    List<TokenBalance> findByWalletId(Long walletId);
}
