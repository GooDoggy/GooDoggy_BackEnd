package com.whoIsLeader.GooDoggy.subscription.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.whoIsLeader.GooDoggy.subscription.entity.Category;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
public class GroupRes {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class subscription {
        private Long groupIdx;
        private String serviceName;
        private Long price;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate nextPayment;
        private Category category;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class paymentHistory{
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate paymentDate;
        private Long totalCost;
        private Long price;
        private String account;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class paymentReport{
        private List<GroupRes.paymentHistory> paymentHistoryList;
        private Long count;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class groupDetails {
        private Long groupIdx;
        private String serviceName;
        private String planName;
        private Long price;
        private String firstDayOfPayment;
        private String lastDayOfPayment;
        private Long paymentCycle;
        private Category category;
        private String account;
        private Long targetNum;
        private String contents;
        private String phone;
    }
}