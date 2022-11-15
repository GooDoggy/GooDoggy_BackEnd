package com.whoIsLeader.GooDoggy.user.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
public class SessionRes {

    @AllArgsConstructor
    @Builder
    public static class SessionInfo{
        private String sessionName;
        private String sessionValue;
        private String sessionId;
        private Integer maxInactiveInterval;
        private Date creationTime;
        private Date lastAccessedTime;
        private boolean isNew;
    }

}