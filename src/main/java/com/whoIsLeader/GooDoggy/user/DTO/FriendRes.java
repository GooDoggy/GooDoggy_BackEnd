package com.whoIsLeader.GooDoggy.user.DTO;

import lombok.*;

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
    }
}