package io.github.deepaganesh.funding.repository;

import io.github.deepaganesh.funding.comon.MintStatus;
import io.github.deepaganesh.funding.entity.FundingTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FundingRepository extends JpaRepository<FundingTransaction, Long> {
    List<FundingTransaction> findByMintStatus(MintStatus failed);
}
