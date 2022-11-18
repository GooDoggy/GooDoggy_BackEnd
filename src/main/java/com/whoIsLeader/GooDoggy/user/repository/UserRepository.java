package com.whoIsLeader.GooDoggy.user.repository;

import com.whoIsLeader.GooDoggy.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findById(String id);
    Optional<UserEntity> findByUserIdx(Long userIdx);
    Optional<UserEntity> findByNameAndEmail(String name, String email);
}
