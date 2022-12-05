package com.whoIsLeader.GooDoggy.subscription.controller;

import com.whoIsLeader.GooDoggy.subscription.service.StatisticsService;
import com.whoIsLeader.GooDoggy.subscription.DTO.StatisticsRes;
import com.whoIsLeader.GooDoggy.util.BaseException;
import com.whoIsLeader.GooDoggy.util.BaseResponse;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/statistics")
public class StatisticsController {

    private StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService){
        this.statisticsService = statisticsService;
    }

    @ResponseBody
    @GetMapping("/category")
    public BaseResponse<List<StatisticsRes.category>> getCategory(HttpServletRequest request) {
        try {
            List<StatisticsRes.category> categoryList = this.statisticsService.getCategoryStatistics(request);
            return new BaseResponse<>(categoryList);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/list")
    public BaseResponse<List<StatisticsRes.briefSub>> getSubscriptionList(HttpServletRequest request) {
        try {
            List<StatisticsRes.briefSub> briefSubList = this.statisticsService.getBriefList(request);
            return new BaseResponse<>(briefSubList);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/personal")
    public BaseResponse<List<StatisticsRes.personal>> getPersonal(HttpServletRequest request) {
        try {
            List<StatisticsRes.personal> personalList = this.statisticsService.getPersonalStatistics(request);
            return new BaseResponse<>(personalList);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
