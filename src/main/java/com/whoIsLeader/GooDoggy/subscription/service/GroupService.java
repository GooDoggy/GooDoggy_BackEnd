package com.whoIsLeader.GooDoggy.subscription.service;

import com.whoIsLeader.GooDoggy.subscription.DTO.GroupReq;
import com.whoIsLeader.GooDoggy.subscription.DTO.GroupRes;
import com.whoIsLeader.GooDoggy.subscription.DTO.PersonalRes;
import com.whoIsLeader.GooDoggy.subscription.entity.GroupEntity;
import com.whoIsLeader.GooDoggy.subscription.entity.PersonalEntity;
import com.whoIsLeader.GooDoggy.subscription.entity.UserGroupEntity;
import com.whoIsLeader.GooDoggy.subscription.repository.GroupRepository;
import com.whoIsLeader.GooDoggy.subscription.repository.UserGroupRepository;
import com.whoIsLeader.GooDoggy.user.DTO.FriendRes;
import com.whoIsLeader.GooDoggy.user.entity.UserEntity;
import com.whoIsLeader.GooDoggy.user.repository.UserRepository;
import com.whoIsLeader.GooDoggy.user.service.UserService;
import com.whoIsLeader.GooDoggy.util.BaseException;
import com.whoIsLeader.GooDoggy.util.BaseResponseStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GroupService {

    private UserRepository userRepository;
    private GroupRepository groupRepository;
    private UserGroupRepository userGroupRepository;

    private UserService userService;
    private UserGroupService userGroupService;

    public GroupService(UserRepository userRepository, GroupRepository groupRepository, UserGroupService userGroupService,
                        UserGroupRepository userGroupRepository, UserService userService){
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.userGroupRepository = userGroupRepository;

        this.userService = userService;
        this.userGroupService = userGroupService;
    }

    public void addSubscription(GroupReq.GetGroupInfo subInfo, HttpServletRequest request) throws BaseException {
        UserEntity user = this.userService.getSessionUser(request);
        GroupEntity groupEntity = GroupEntity.builder()
                .serviceName(subInfo.getServiceName())
                .planName(subInfo.getPlanName())
                .price(subInfo.getPrice())
                .firstDayOfPayment(subInfo.getFirstDayOfPayment())
                .lastDayOfPayment(subInfo.getLastDayOfPayment())
                .paymentCycle(subInfo.getPaymentCycle())
                .category(subInfo.getCategory())
                .account(subInfo.getAccount())
                .joinNum(1L)
                .targetNum(subInfo.getNum())
                .contents(subInfo.getContents())
                .phone(subInfo.getPhone())
                .build();
        try{
            this.groupRepository.save(groupEntity);
        } catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_INSERT_ERROR);
        }
        Long groupIdx = userGroupService.addUserGroup(user);
        Optional<GroupEntity> optional1 = this.groupRepository.findByGroupIdx(groupIdx);
        if(optional1.isEmpty()){
            throw new BaseException(BaseResponseStatus.NON_EXIST_GROUPIDX);
        }

        try{
            optional1.get().changeStatus("active");
            this.groupRepository.save(optional1.get());
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_PATCH_ERROR);
        }
    }

    public List<GroupRes.subscription> getUserSubList(HttpServletRequest request) throws BaseException{
        UserEntity user = this.userService.getSessionUser(request);
        return getSubscriptionList(user);
    }

    public List<GroupRes.subscription> getOthersSubList(Long friendIdx, HttpServletRequest request) throws BaseException{
        UserEntity user = this.userService.getSessionUser(request);
        Optional<UserEntity> optional = this.userRepository.findByUserIdx(friendIdx);
        if(optional.isEmpty()){
            throw new BaseException(BaseResponseStatus.NON_EXIST_FRIENDIDX);
        }
        return getSubscriptionList(optional.get());
    }

    public List<GroupRes.subscription> getSubscriptionList(UserEntity user) throws BaseException{
        List<UserGroupEntity> userGroupEntityList = this.userGroupRepository.findAllByUserIdx(user);
        List<GroupRes.subscription> subscriptionList = new ArrayList<>();
        for(UserGroupEntity temp : userGroupEntityList){
            GroupRes.subscription subscription = new GroupRes.subscription();
            subscription.setGroupIdx(temp.getGroupIdx().getGroupIdx());
            subscription.setServiceName(temp.getGroupIdx().getServiceName());
            subscription.setPrice(temp.getGroupIdx().getPrice());
            LocalDate nextPayment = temp.getGroupIdx().getFirstDayOfPayment();
            if(temp.getGroupIdx().getPaymentCycle() < 0){
                if(nextPayment.isBefore(LocalDate.now())){
                    nextPayment = nextPayment.plusMonths(temp.getGroupIdx().getPaymentCycle()*(-1));
                }
            }
            else{
                if(nextPayment.isBefore(LocalDate.now())){
                    nextPayment = nextPayment.plusDays(temp.getGroupIdx().getPaymentCycle());
                }
            }
            if(checkTermination(nextPayment, temp.getGroupIdx().getLastDayOfPayment())){
                subscription.setNextPayment(nextPayment);
                subscription.setCategory(temp.getGroupIdx().getCategory());
                subscriptionList.add(subscription);
            }
            else{
                temp.getGroupIdx().setStatus("terminated");
                this.groupRepository.save(temp.getGroupIdx());
            }
        }
        return subscriptionList;
    }

    public boolean checkTermination(LocalDate nextPayment, LocalDate lastDayOfPayment){
        if(nextPayment.isAfter(lastDayOfPayment)){
            return false;
        }
        return true;
    }

    public GroupRes.paymentReport getPaymentReport(Long groupIdx, HttpServletRequest request) throws BaseException{
        UserEntity user = this.userService.getSessionUser(request);
        Optional<GroupEntity> group = this.groupRepository.findByGroupIdx(groupIdx);
        if(group.isEmpty()){
            throw new BaseException(BaseResponseStatus.NON_EXIST_GROUPIDX);
        }
        return calculatePaymentReport(group.get());
    }

    public GroupRes.paymentReport calculatePaymentReport(GroupEntity group) throws BaseException{
        List<GroupRes.paymentHistory> paymentHistoryList = new ArrayList<>();
        LocalDate nextPayment = group.getFirstDayOfPayment();
        String account = group.getAccount();
        Long price = group.getPrice();
        Long totalCost = 0L;
        Long count = 0L;

        while(nextPayment.isBefore(LocalDate.now())){
            totalCost += price;
            GroupRes.paymentHistory paymentHistory = new GroupRes.paymentHistory(nextPayment, totalCost, price, account);
            paymentHistoryList.add(paymentHistory);
            if(group.getPaymentCycle() < 0){
                nextPayment = nextPayment.plusMonths(group.getPaymentCycle()*(-1));
            }
            else{
                nextPayment = nextPayment.plusDays(group.getPaymentCycle());
            }
            count++;
            if(paymentHistoryList.size() > 5){
                paymentHistoryList.remove(0);
            }
        }
        GroupRes.paymentReport paymentReport = new GroupRes.paymentReport(paymentHistoryList, count);
        return paymentReport;
    }
}
