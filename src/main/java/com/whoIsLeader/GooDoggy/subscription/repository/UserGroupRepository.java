package com.whoIsLeader.GooDoggy.subscription.repository;

import com.whoIsLeader.GooDoggy.subscription.entity.GroupEntity;
import com.whoIsLeader.GooDoggy.subscription.entity.PersonalEntity;
import com.whoIsLeader.GooDoggy.subscription.entity.UserGroupEntity;
import com.whoIsLeader.GooDoggy.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroupEntity, Long>{
    List<UserGroupEntity> findAllByUserIdx(UserEntity userEntity);
    Optional<UserGroupEntity> findByUserIdxAndGroupIdx(UserEntity userEntity, GroupEntity groupEntity);
    Optional<UserGroupEntity> findFirstByGroupIdx(GroupEntity groupEntity);
    List<UserGroupEntity> findAllByGroupIdx(GroupEntity groupEntity);
}

