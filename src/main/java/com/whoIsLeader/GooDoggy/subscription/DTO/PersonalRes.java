package com.whoIsLeader.GooDoggy.subscription.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.whoIsLeader.GooDoggy.subscription.entity.Category;
import lombok.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate nextPayment;
        private Category category;
    }
}