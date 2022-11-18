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

    @Getter
    public static class GetUserIdPw{
        private String id;
        private String password;
    }

    @Getter
    public static class GetUserNameEmail{
        private String name;
        private String email;
    }

    @Getter
    public static class GetUserNameId{
        private String name;
        private String id;
    }

    @Getter
    public static class GetUserPws{
        private Long userIdx;
        private String newPassword;
        private String newPassword_check;
    }
}
