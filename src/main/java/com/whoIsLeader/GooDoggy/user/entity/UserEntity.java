package com.whoIsLeader.GooDoggy.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.whoIsLeader.GooDoggy.subscription.entity.PersonalEntity;
import com.whoIsLeader.GooDoggy.subscription.entity.UserGroupEntity;
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
@Table(name = "user")
@NoArgsConstructor
@DynamicInsert
public class UserEntity extends BaseEntity {

    //userIdx, name, email, id, password, profileImgUrl, status
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIdx;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, length = 30)
    private String email;

    @Column(nullable = false, unique = true, length = 20)
    private String id;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(columnDefinition = "varchar(10) default 'active'")
    private String status;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "userIdx", orphanRemoval = true)
    private List<PersonalEntity> personalEntityList = new ArrayList<>();

//    @OneToMany(fetch = FetchType.EAGER, mappedBy = "userIdx", orphanRemoval = true)
//    private List<UserGroupEntity> userGroupEntityList = new ArrayList<>();

    @Column(columnDefinition = "varchar(200) default 'https://storage.googleapis.com/goodoggy_bucket/goodoggy_profile.jpg'") //기본 이미지
    private String profileImg;

    @Builder
    public UserEntity(String name, String email, String id, String password, String status,
                      List<PersonalEntity> personalEntityList){
        this.name = name;
        this.email = email;
        this.id = id;
        this.password = password;
        this.status = status;
        this.personalEntityList = personalEntityList;
    }

    public void changePw(String newPassword){
        this.password = newPassword;
    }
    public void changeProfileImg(String newProfileImg){this.profileImg = newProfileImg;}
}
