package com.whoIsLeader.GooDoggy.subscription.service;

import com.whoIsLeader.GooDoggy.subscription.entity.GroupEntity;
import com.whoIsLeader.GooDoggy.subscription.entity.UserGroupEntity;
import com.whoIsLeader.GooDoggy.subscription.repository.GroupRepository;
import com.whoIsLeader.GooDoggy.subscription.repository.UserGroupRepository;
import com.whoIsLeader.GooDoggy.user.entity.UserEntity;
import com.whoIsLeader.GooDoggy.user.service.UserService;
import com.whoIsLeader.GooDoggy.util.BaseException;
import com.whoIsLeader.GooDoggy.util.BaseResponseStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserGroupService {

    private GroupRepository groupRepository;
    private UserGroupRepository userGroupRepository;

    private UserService userService;

    public UserGroupService(GroupRepository groupRepository, UserGroupRepository userGroupRepository, UserService userService) {
        this.groupRepository = groupRepository;
        this.userGroupRepository = userGroupRepository;
        this.userService = userService;
    }

    public Long addUserGroup(UserEntity userEntity) throws BaseException {
        Optional<GroupEntity> optional = this.groupRepository.findByStatus("half-active");
        if(optional.isEmpty()){
            throw new BaseException(BaseResponseStatus.NON_EXIST_STATUS);
        }
        UserGroupEntity userGroupEntity = UserGroupEntity.builder()
                .userEntity(userEntity)
                .groupEntity(optional.get())
                .build();
        try{
            this.userGroupRepository.save(userGroupEntity);
        } catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_INSERT_ERROR);
        }
        return optional.get().getGroupIdx();
    }
}