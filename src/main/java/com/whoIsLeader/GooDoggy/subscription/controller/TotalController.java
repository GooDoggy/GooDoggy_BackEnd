package com.whoIsLeader.GooDoggy.subscription.controller;

import com.whoIsLeader.GooDoggy.subscription.DTO.PersonalReq;
import com.whoIsLeader.GooDoggy.subscription.DTO.TotalRes;
import com.whoIsLeader.GooDoggy.subscription.service.PersonalService;
import com.whoIsLeader.GooDoggy.subscription.service.TotalService;
import com.whoIsLeader.GooDoggy.util.BaseException;
import com.whoIsLeader.GooDoggy.util.BaseResponse;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/subscriptions")
public class TotalController {

    private TotalService totalService;

    public TotalController(TotalService totalService) {
        this.totalService = totalService;
    }

    @ResponseBody
    @GetMapping("/")
    public BaseResponse<TotalRes.allSubscription> getSubscription(HttpServletRequest request) {
        try {
            TotalRes.allSubscription result = this.totalService.getSubscriptionList(request);
            return new BaseResponse<>(result);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}