package org.cowary.airtodo.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.cowary.airtodo.controller.dto.AwardRq;
import org.cowary.airtodo.controller.dto.CoinReq;
import org.cowary.airtodo.entity.Award;
import org.cowary.airtodo.service.db.AwardService;
import org.cowary.airtodo.service.db.CoinService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/award")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AwardController {
    CoinService coinService;
    AwardService awardService;

    @PostMapping("/coin/remov")
    public ResponseEntity<String> removeCoin(@RequestBody CoinReq coinReq){
        coinService.removeCoin(coinReq.getAmount());
        return ResponseEntity.ok("Removed " + coinReq.getAmount() + " coin");
    }

    @GetMapping("/get-award-list")
    public ResponseEntity<List<Award>> getAwardList(){
        return ResponseEntity.ok(awardService.getAwards());
    }

    @PostMapping("/add-award")
    public ResponseEntity<String> addAward(@RequestBody AwardRq awardRq){
        awardService.createAward(awardRq.getTitle(), awardRq.getCost());
        return ResponseEntity.ok("Added " + awardRq.getTitle() + " coin");
    }
}
