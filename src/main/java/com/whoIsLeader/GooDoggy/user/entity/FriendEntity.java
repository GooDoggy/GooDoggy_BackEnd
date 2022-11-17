package com.whoIsLeader.GooDoggy.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.whoIsLeader.GooDoggy.subscription.entity.PersonalEntity;
import com.whoIsLeader.GooDoggy.util.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "friend")
@NoArgsConstructor
@DynamicInsert
public class FriendEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long friendIdx;

    @ManyToOne
    @JoinColumn(name = "reqUserIdx")
    private UserEntity reqUserIdx;

    @ManyToOne
    @JoinColumn(name = "resUserIdx")
    private UserEntity resUserIdx;

    @Column(columnDefinition = "varchar(10) default 'inactive'")
    private String status;

    @Builder
    public FriendEntity(UserEntity userEntity1, UserEntity userEntity2, String status){
        this.reqUserIdx = userEntity1;
        this.resUserIdx = userEntity2;
        this.status = status;
    }

    public void changeStatus(String status){
        this.status = status;
    }
}
