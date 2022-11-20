package com.whoIsLeader.GooDoggy.subscription.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.whoIsLeader.GooDoggy.subscription.entity.Category;
import lombok.*;

import java.time.LocalDate;

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
}