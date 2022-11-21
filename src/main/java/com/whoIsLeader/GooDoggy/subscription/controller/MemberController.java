package com.whoIsLeader.GooDoggy.subscription.controller;

import com.whoIsLeader.GooDoggy.subscription.DTO.GroupRes;
import com.whoIsLeader.GooDoggy.subscription.DTO.MemberRes;
import com.whoIsLeader.GooDoggy.subscription.service.MemberService;
import com.whoIsLeader.GooDoggy.util.BaseException;
import com.whoIsLeader.GooDoggy.util.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/subscriptions/member")
public class MemberController {

    private MemberService memberService;

    public MemberController(MemberService memberService){
        this.memberService = memberService;
    }

    @ResponseBody
    @GetMapping("/")
    public BaseResponse<List<MemberRes.subscription>> getSubscriptionList(HttpServletRequest request) {
        try {
            List<MemberRes.subscription> subscriptionList = this.memberService.getSubscriptionList(request);
            return new BaseResponse<>(subscriptionList);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}