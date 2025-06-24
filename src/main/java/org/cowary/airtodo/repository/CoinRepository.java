package org.cowary.airtodo.repository;

import org.cowary.airtodo.entity.Coin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoinRepository extends JpaRepository<Coin, Long> {
}
