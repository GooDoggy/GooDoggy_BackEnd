package com.whoIsLeader.GooDoggy.subscription.controller;

import com.whoIsLeader.GooDoggy.subscription.DTO.PersonalReq;
import com.whoIsLeader.GooDoggy.subscription.service.PersonalService;
import com.whoIsLeader.GooDoggy.user.service.UserService;
import com.whoIsLeader.GooDoggy.util.BaseException;
import com.whoIsLeader.GooDoggy.util.BaseResponse;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/subscriptions/personal")
public class PersonalController {

    private PersonalService personalService;

    public PersonalController(PersonalService personalService){
        this.personalService = personalService;
    }

    @ResponseBody
    @PostMapping("/")
    public BaseResponse<String> addSubscription(@RequestBody PersonalReq.GetPersonalInfo subInfo, HttpServletRequest request) {
        try {
            this.personalService.addSubscription(subInfo, request);
            return new BaseResponse<>("개인 구독을 등록하였습니다.");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
