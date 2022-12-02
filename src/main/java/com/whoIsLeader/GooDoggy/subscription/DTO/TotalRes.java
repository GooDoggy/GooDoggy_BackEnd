package com.whoIsLeader.GooDoggy.subscription.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.whoIsLeader.GooDoggy.subscription.entity.Category;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@NoArgsConstructor
public class TotalRes {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class allSubscription {
        private List<PersonalRes.subscription> personalSub;
        private List<GroupRes.subscription> groupSub;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class calendar{
        private int year;
        private int month;
        private HashSet<Integer> dateList;
    }
}
