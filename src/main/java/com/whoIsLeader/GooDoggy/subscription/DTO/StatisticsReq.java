package com.whoIsLeader.GooDoggy.subscription.DTO;

import com.whoIsLeader.GooDoggy.subscription.entity.Category;
import lombok.*;

import java.util.List;

@NoArgsConstructor
public class StatisticsReq {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class appUsageList{
        private List<appUsage> appUsageList;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class appUsage{
        private String serviceName;
        private int usingTime;
    }
}