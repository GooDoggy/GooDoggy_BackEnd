package com.whoIsLeader.GooDoggy.subscription.repository;

import com.whoIsLeader.GooDoggy.subscription.entity.PersonalEntity;
import com.whoIsLeader.GooDoggy.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface PersonalRepository extends JpaRepository<PersonalEntity, Long>{
    List<PersonalEntity> findAllByUserIdx(UserEntity userEntity);
    Optional<PersonalEntity> findByPersonalIdx(Long personalIdx);
}
