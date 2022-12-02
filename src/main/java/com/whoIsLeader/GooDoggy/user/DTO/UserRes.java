package com.whoIsLeader.GooDoggy.user.DTO;

import com.whoIsLeader.GooDoggy.subscription.DTO.TotalRes;
import lombok.*;

@NoArgsConstructor
public class UserRes {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class randomInfo {
        private String string1;
        private String string2;
        private String string3;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class userInfo {
        private Long userIdx;
        private String id;
        private String profileImg;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class mainInfo {
        private randomInfo randomInfo;
        private userInfo userInfo;
        private TotalRes.calendar calendar;
    }
}
