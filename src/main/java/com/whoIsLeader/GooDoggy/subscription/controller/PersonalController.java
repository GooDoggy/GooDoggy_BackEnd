package com.whoIsLeader.GooDoggy.subscription.controller;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.whoIsLeader.GooDoggy.subscription.DTO.PersonalReq;
import com.whoIsLeader.GooDoggy.subscription.DTO.PersonalRes;
import com.whoIsLeader.GooDoggy.subscription.service.PersonalService;
import com.whoIsLeader.GooDoggy.user.service.UserService;
import com.whoIsLeader.GooDoggy.util.BaseException;
import com.whoIsLeader.GooDoggy.util.BaseResponse;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    @ResponseBody
    @GetMapping("/")
    public BaseResponse<List<PersonalRes.subscription>> getSubscriptionList(HttpServletRequest request){
        try{
            List<PersonalRes.subscription> subscriptionList = this.personalService.getUserSubList(request);
            return new BaseResponse<>(subscriptionList);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/payment/{personalIdx}")
    public BaseResponse<PersonalRes.paymentReport> getPaymentReport(@PathVariable Long personalIdx, HttpServletRequest request){
        try{
            PersonalRes.paymentReport paymentReport= this.personalService.getPaymentReport(personalIdx, request);
            return new BaseResponse<>(paymentReport);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @PatchMapping("/{personalIdxList}")
    public BaseResponse<String> inactiveSubscriptions(@PathVariable List<Long> personalIdxList, HttpServletRequest request){
        try{
            this.personalService.inactiveSubscriptions(personalIdxList, request);
            return new BaseResponse<>("개인 구독이 삭제되었습니다.");
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
}
