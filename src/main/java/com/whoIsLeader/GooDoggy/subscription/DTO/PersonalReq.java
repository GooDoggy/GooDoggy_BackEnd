package com.whoIsLeader.GooDoggy.subscription.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.whoIsLeader.GooDoggy.subscription.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@NoArgsConstructor
public class PersonalReq {

    @Getter
    @AllArgsConstructor
    @Builder
    public static class GetPersonalInfo {
        private String serviceName;
        private String planName;
        private Long price;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate firstDayOfPayment;
        private Long paymentCycle;
        private Category category;
        private String account;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class GetProfileImg{
        private String profileimg;
    }
}