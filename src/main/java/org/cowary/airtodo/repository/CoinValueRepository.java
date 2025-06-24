package org.cowary.airtodo.repository;

import org.cowary.airtodo.entity.CoinValue;
import org.cowary.airtodo.entity.Priority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoinValueRepository extends JpaRepository<CoinValue, Long> {
    CoinValue findCoinValueByPriority(Priority priority);
}
