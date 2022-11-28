package com.whoIsLeader.GooDoggy.subscription.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.whoIsLeader.GooDoggy.subscription.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
public class GroupReq {

    @Getter
    @AllArgsConstructor
    @Builder
    public static class GetGroupInfo {
        private String serviceName;
        private String planName;
        private Long price;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate firstDayOfPayment;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate lastDayOfPayment;
        private Long paymentCycle;
        private Category category;
        private String account;
        private Long num;
        private String contents;
        private String phone;
    }
}