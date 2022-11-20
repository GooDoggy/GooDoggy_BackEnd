package com.whoIsLeader.GooDoggy.subscription.repository;

import com.whoIsLeader.GooDoggy.subscription.entity.UserGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroupEntity, Long>{
}

