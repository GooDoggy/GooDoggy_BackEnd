package com.whoIsLeader.GooDoggy.subscription.service;

import com.whoIsLeader.GooDoggy.subscription.DTO.GroupRes;
import com.whoIsLeader.GooDoggy.subscription.DTO.MemberRes;
import com.whoIsLeader.GooDoggy.subscription.DTO.PersonalRes;
import com.whoIsLeader.GooDoggy.subscription.DTO.TotalRes;
import com.whoIsLeader.GooDoggy.subscription.entity.Category;
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
        allSubscription.setGroupSub(this.groupService.getUserSubList(request));
        return allSubscription;
    }

    public TotalRes.allSubscription getOthersSubscriptionList(Long friendIdx, HttpServletRequest request) throws BaseException {
        TotalRes.allSubscription allSubscription = new TotalRes.allSubscription();
        allSubscription.setPersonalSub(this.personalService.getOthersSubList(friendIdx, request));
        allSubscription.setGroupSub(this.groupService.getOthersSubList(friendIdx, request));
        return allSubscription;
    }

    public TotalRes.allSubscription getCategorizedList(Category category, TotalRes.allSubscription subList) throws BaseException {
        List<PersonalRes.subscription> personalSubList = new ArrayList<>();
        for(PersonalRes.subscription temp : subList.getPersonalSub()){
            if(temp.getCategory().equals(category)) {
                personalSubList.add(temp);
            }
        }
        List<GroupRes.subscription> groupSubList = new ArrayList<>();
        for(GroupRes.subscription temp : subList.getGroupSub()){
            if(temp.getCategory().equals(category)) {
                groupSubList.add(temp);
            }
        }
        subList.setPersonalSub(personalSubList);
        subList.setGroupSub(groupSubList);
        return subList;
    }
}