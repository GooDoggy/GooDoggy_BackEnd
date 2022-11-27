package com.whoIsLeader.GooDoggy.subscription.service;

import com.whoIsLeader.GooDoggy.subscription.DTO.GroupRes;
import com.whoIsLeader.GooDoggy.subscription.DTO.PersonalRes;
import com.whoIsLeader.GooDoggy.subscription.DTO.TotalRes;
import com.whoIsLeader.GooDoggy.subscription.entity.PersonalEntity;
import com.whoIsLeader.GooDoggy.subscription.repository.PersonalRepository;
import com.whoIsLeader.GooDoggy.user.entity.UserEntity;
import com.whoIsLeader.GooDoggy.user.repository.UserRepository;
import com.whoIsLeader.GooDoggy.util.BaseException;
import com.whoIsLeader.GooDoggy.util.BaseResponseStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TotalService {

    private PersonalService personalService;
    private GroupService groupService;

    public TotalService(PersonalService personalService, GroupService groupService) {
        this.personalService = personalService;
        this.groupService = groupService;
    }

    public TotalRes.allSubscription getSubscriptionList(HttpServletRequest request) throws BaseException {
        TotalRes.allSubscription allSubscription = new TotalRes.allSubscription();
        allSubscription.setPersonalSub(this.personalService.getUserSubList(request));
        allSubscription.setGroupSub(this.groupService.getSubscriptionList(request));
        return allSubscription;
    }
}