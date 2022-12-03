package com.whoIsLeader.GooDoggy.subscription.controller;

import com.whoIsLeader.GooDoggy.subscription.DTO.GroupRes;
import com.whoIsLeader.GooDoggy.subscription.DTO.MemberRes;
import com.whoIsLeader.GooDoggy.subscription.entity.Category;
import com.whoIsLeader.GooDoggy.subscription.service.MemberService;
import com.whoIsLeader.GooDoggy.util.BaseException;
import com.whoIsLeader.GooDoggy.util.BaseResponse;
import org.springframework.web.bind.annotation.*;

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

    @ResponseBody
    @GetMapping("/{category}")
    public BaseResponse<List<MemberRes.subscription>> getCategorizedList(@PathVariable Category category, HttpServletRequest request) {
        try {
            List<MemberRes.subscription> subscriptionList = this.memberService.getCategorizedList(category, request);
            return new BaseResponse<>(subscriptionList);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/{groupIdx}")
    public BaseResponse<String> joinGroupSubscription(@PathVariable Long groupIdx, HttpServletRequest request) {
        try {
            this.memberService.joinGroupSubscription(groupIdx, request);
            return new BaseResponse<>("다인 구독 그룹에 참여하였습니다.");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}