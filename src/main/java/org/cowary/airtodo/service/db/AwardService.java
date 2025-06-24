package org.cowary.airtodo.service.db;

import org.cowary.airtodo.entity.Award;

import java.util.List;

public interface AwardService {
    boolean createAward(String title, Long cost);

    List<Award> getAwards();
}
