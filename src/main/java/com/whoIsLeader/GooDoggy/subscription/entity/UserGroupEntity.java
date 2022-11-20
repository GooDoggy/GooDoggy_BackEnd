package com.whoIsLeader.GooDoggy.subscription.entity;

import com.whoIsLeader.GooDoggy.user.entity.UserEntity;
import com.whoIsLeader.GooDoggy.util.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Table(name = "user_group")
@NoArgsConstructor
@DynamicInsert
public class UserGroupEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userGroupIdx;

    @ManyToOne
    @JoinColumn(name = "userIdx")
    private UserEntity userIdx;

    @ManyToOne
    @JoinColumn(name = "groupIdx")
    private GroupEntity groupIdx;

    @Builder
    public UserGroupEntity(UserEntity userEntity, GroupEntity groupEntity) {
        this.userIdx = userEntity;
        this.groupIdx = groupEntity;
    }
}