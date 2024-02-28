package com.kt.yapp.web;

import com.kt.yapp.annotation.NotCheckLogin;
import com.kt.yapp.domain.Item;
import com.kt.yapp.service.MissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Random;

@Slf4j
@Controller
public class MissionController {

    @Autowired
    private MissionService missionService;

    @GetMapping("/draw")
    @NotCheckLogin
    public String draw(Model model) {
        // 확률 설정 (여기서는 50%)
        double winProbability = 0.1;

        // 뽑기 시도
        boolean isWinner = new Random().nextDouble() < winProbability;
        log.debug("isWinner : {}",isWinner);

        if (isWinner) {
            // 당첨 시 쿠폰 지급
            model.addAttribute("coupon", "50% 할인 쿠폰"); // 당첨 시 쿠폰 내용
        } else {
            model.addAttribute("coupon", null); // 당첨 실패 시 쿠폰은 없음
        }

        // 뽑힌 아이템
        Item item = missionService.drawItem();
        model.addAttribute("item", item);

        return "/mission/draw";
    }

    @GetMapping("/drawForm")
    @NotCheckLogin
    public String drawForm(Model model) {
        return "/mission/draw";
    }
}
