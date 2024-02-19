package com.kt.yapp.web;

import com.kt.yapp.annotation.NotCheckLogin;
import com.kt.yapp.util.YappUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Calendar;

/***
 * 일반적인 단순 알림으로 사용예정 컨트롤러..
 * 준회원 휴면처리 관련 기능 추가
 */
@Controller
public class InfoController {

    @GetMapping("/info/dorAccInfo")
    @NotCheckLogin
    public ModelAndView getDorAccInfo(ModelAndView mv){
        String accTransDate = YappUtil.getCurDate("yyyy.MM.dd", Calendar.MONTH,1);
        mv.addObject("accTransDate",accTransDate);
        mv.setViewName("/info/dorAccInfo");
        return mv;
    }
}
