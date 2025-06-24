package org.cowary.airtodo.service.db.impl;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cowary.airtodo.entity.Priority;
import org.cowary.airtodo.repository.CoinRepository;
import org.cowary.airtodo.repository.CoinValueRepository;
import org.cowary.airtodo.service.db.CoinService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class CoinServiceImpl implements CoinService {
    CoinRepository coinRepository;
    CoinValueRepository coinValueRepository;

    @Transactional
    @Override
    public boolean addCoin(Priority priority) {
        var coin = coinRepository.findById(1L).orElseThrow();
        var coinValue = coinValueRepository.findCoinValueByPriority(priority);
        coin.setAmount(coin.getAmount() + coinValue.getAmount());
        return true;
    }

    @Transactional
    @Override
    public boolean removeCoin(Long count) {
        var coin = coinRepository.findById(1L).orElseThrow();
        coin.setAmount(coin.getAmount() - count);
        return true;
    }
}
