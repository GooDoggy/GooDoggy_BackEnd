package com.whoIsLeader.GooDoggy.subscription.repository;

import com.whoIsLeader.GooDoggy.subscription.entity.GroupEntity;
import com.whoIsLeader.GooDoggy.subscription.entity.PersonalEntity;
import com.whoIsLeader.GooDoggy.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface GroupRepository extends JpaRepository<GroupEntity, Long> {
    Optional<GroupEntity> findByStatus(String status);
    Optional<GroupEntity> findByGroupIdx(Long groupIdx);
    List<GroupEntity> findAllByStatus(String status);
}
