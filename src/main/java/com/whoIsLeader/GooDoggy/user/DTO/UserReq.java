package com.whoIsLeader.GooDoggy.user.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserReq {

    @Getter
    @AllArgsConstructor
    @Builder
    public static class GetUserInfo{
        private String name;
        private String email;
        private String id;
        private String password;
        private String password_check;
    }

}
