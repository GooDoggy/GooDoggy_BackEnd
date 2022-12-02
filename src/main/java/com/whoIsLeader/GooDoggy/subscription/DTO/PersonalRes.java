package com.whoIsLeader.GooDoggy.subscription.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.whoIsLeader.GooDoggy.subscription.entity.Category;
import lombok.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
public class PersonalRes {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class subscription {
        private Long personalIdx;
        private String serviceName;
        private Long price;
        private String nextPayment;
        private Category category;
        private String profileImg;
    }


    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class paymentHistory{
        private String paymentDate;
        private Long totalCost;
        private Long price;
        private String account;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class paymentReport{
        private List<paymentHistory> paymentHistoryList;
        private Long count;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class personalDetails {
        private Long personalIdx;
        private String serviceName;
        private String planName;
        private Long price;
        private String firstDayOfPayment;
        private Long paymentCycle;
        private Category category;
        private String account;
    }
}