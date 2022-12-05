package com.whoIsLeader.GooDoggy.subscription.DTO;

import com.whoIsLeader.GooDoggy.subscription.entity.Category;
import lombok.*;

@NoArgsConstructor
public class StatisticsRes {

    @Setter
    @Getter
    @Builder
    @AllArgsConstructor
    public static class category{
        private Category category;
        private Integer num;
        private float percent;
    }

    @Setter
    @Getter
    @Builder
    @AllArgsConstructor
    public static class briefSub{
        private String profileImg;
        private String serviceName;
    }

    @Setter
    @Getter
    @Builder
    @AllArgsConstructor
    public static class personal{
        private String serviceName;
        private Integer num;
        private float percent;
    }

    @Setter
    @Getter
    @Builder
    @AllArgsConstructor
    public static class group{
        private String serviceName;
        private Integer num;
        private float percent;
    }
}
