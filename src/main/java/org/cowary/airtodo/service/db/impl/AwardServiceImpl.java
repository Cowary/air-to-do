package org.cowary.airtodo.service.db.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cowary.airtodo.entity.Award;
import org.cowary.airtodo.repository.AwardRepository;
import org.cowary.airtodo.service.db.AwardService;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AwardServiceImpl implements AwardService {
    AwardRepository awardRepository;

    @Override
    public boolean createAward(String title, Long cost) {
        Award award = Award.builder()
                .title(title)
                .cost(cost)
                .build();
        awardRepository.save(award);
        return true;
    }

    @Override
    public List<Award> getAwards() {
        return awardRepository.findAll();
    }
}
