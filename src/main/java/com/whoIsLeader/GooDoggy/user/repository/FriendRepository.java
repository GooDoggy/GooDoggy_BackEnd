package com.whoIsLeader.GooDoggy.user.repository;

import com.whoIsLeader.GooDoggy.user.entity.FriendEntity;
import com.whoIsLeader.GooDoggy.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<FriendEntity, Long> {
    Optional<FriendEntity> findByReqUserIdxAndResUserIdx(UserEntity reqUserIdx, UserEntity resUserIdx);
}
