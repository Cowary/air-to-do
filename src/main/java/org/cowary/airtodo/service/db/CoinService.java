package org.cowary.airtodo.service.db;

import jakarta.transaction.Transactional;
import org.cowary.airtodo.entity.Priority;

public interface CoinService {
    @Transactional
    boolean addCoin(Priority priority);

    @Transactional
    boolean removeCoin(Long count);
}
