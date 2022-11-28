package com.whoIsLeader.GooDoggy.subscription.controller;

import com.whoIsLeader.GooDoggy.subscription.DTO.GroupReq;
import com.whoIsLeader.GooDoggy.subscription.DTO.GroupRes;
import com.whoIsLeader.GooDoggy.subscription.DTO.PersonalRes;
import com.whoIsLeader.GooDoggy.subscription.service.GroupService;
import com.whoIsLeader.GooDoggy.util.BaseException;
import com.whoIsLeader.GooDoggy.util.BaseResponse;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.swing.*;
import java.util.List;

@RestController
@RequestMapping(value = "/subscriptions/group")
public class GroupController {

    private GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @ResponseBody
    @PostMapping("/")
    public BaseResponse<String> addSubscription(@RequestBody GroupReq.GetGroupInfo subInfo, HttpServletRequest request) {
        try {
            this.groupService.addSubscription(subInfo, request);
            return new BaseResponse<>("다인 구독을 등록하였습니다.");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/")
    public BaseResponse<List<GroupRes.subscription>> getSubscriptionList(HttpServletRequest request){
        try{
            List<GroupRes.subscription> subscriptionList = this.groupService.getUserSubList(request);
            return new BaseResponse<>(subscriptionList);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/payment/{groupIdx}")
    public BaseResponse<GroupRes.paymentReport> getPaymentReport(@PathVariable Long groupIdx, HttpServletRequest request){
        try{
            GroupRes.paymentReport paymentReport = this.groupService.getPaymentReport(groupIdx, request);
            return new BaseResponse<>(paymentReport);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/{groupIdx}")
    public BaseResponse<GroupRes.groupDetails> getGroupDetails(@PathVariable Long groupIdx, HttpServletRequest request){
        try{
            GroupRes.groupDetails groupDetails = this.groupService.groupDetails(groupIdx, request);
            return new BaseResponse<>(groupDetails);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
}