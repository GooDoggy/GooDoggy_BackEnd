package com.whoIsLeader.GooDoggy.user.DTO;

import lombok.*;

import java.util.List;

@NoArgsConstructor
public class FriendRes {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FriendInfo {
        private Long friendIdx;
        private String id;
        private String profileImg;
        private BriefInfo briefInfo;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Setter
    @Getter
    public static class SubInfo {
        private String profileImg;
        private String serviceName;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Setter
    @Getter
    public static class BriefInfo {
        private Long num;
        private List<SubInfo> subInfoList;
    }
}