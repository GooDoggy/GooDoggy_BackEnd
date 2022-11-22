package com.whoIsLeader.GooDoggy.user.repository;

import com.whoIsLeader.GooDoggy.user.entity.FriendEntity;
import com.whoIsLeader.GooDoggy.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<FriendEntity, Long> {
    Optional<FriendEntity> findByFriendIdx(Long friendIdx);
    Optional<FriendEntity> findByReqUserIdxAndResUserIdx(UserEntity reqUserIdx, UserEntity resUserIdx);
    List<FriendEntity> findAllByReqUserIdxOrResUserIdx(UserEntity reqUserIdx, UserEntity resUserIdx);
    List<FriendEntity> findAllByResUserIdx(UserEntity resUserIdx);
    List<FriendEntity> findAllByReqUserIdx(UserEntity reqUserIdx);
}
