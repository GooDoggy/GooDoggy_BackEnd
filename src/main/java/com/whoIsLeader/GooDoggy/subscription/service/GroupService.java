package com.whoIsLeader.GooDoggy.subscription.service;

import com.whoIsLeader.GooDoggy.subscription.DTO.GroupReq;
import com.whoIsLeader.GooDoggy.subscription.DTO.GroupRes;
import com.whoIsLeader.GooDoggy.subscription.DTO.PersonalRes;
import com.whoIsLeader.GooDoggy.subscription.entity.GroupEntity;
import com.whoIsLeader.GooDoggy.subscription.entity.PersonalEntity;
import com.whoIsLeader.GooDoggy.subscription.entity.UserGroupEntity;
import com.whoIsLeader.GooDoggy.subscription.repository.GroupRepository;
import com.whoIsLeader.GooDoggy.subscription.repository.UserGroupRepository;
import com.whoIsLeader.GooDoggy.user.entity.UserEntity;
import com.whoIsLeader.GooDoggy.user.repository.UserRepository;
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

    private UserGroupService userGroupService;

    public GroupService(UserRepository userRepository, GroupRepository groupRepository, UserGroupService userGroupService,
                        UserGroupRepository userGroupRepository){
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.userGroupRepository = userGroupRepository;

        this.userGroupService = userGroupService;
    }

    public void addSubscription(GroupReq.GetGroupInfo subInfo, HttpServletRequest request) throws BaseException {
        HttpSession session = request.getSession(false);
        if(session == null){
            throw new BaseException(BaseResponseStatus.NON_EXIST_SESSION);
        }
        Long userIdx = (Long)session.getAttribute("LOGIN_USER");
        Optional<UserEntity> optional = this.userRepository.findByUserIdx(userIdx);
        if(optional.isEmpty()){
            throw new BaseException(BaseResponseStatus.NON_EXIST_USERIDX);
        }
        if(optional.get().getStatus().equals("inactive")){
            throw new BaseException(BaseResponseStatus.INACTIVE_USER);
        }
        GroupEntity groupEntity = GroupEntity.builder()
                .serviceName(subInfo.getServiceName())
                .planName(subInfo.getPlanName())
                .price(subInfo.getPrice())
                .firstDayOfPayment(subInfo.getFirstDayOfPayment())
                .lastDayOfPayment(subInfo.getLastDayOfPayment())
                .paymentCycle(subInfo.getPaymentCycle())
                .category(subInfo.getCategory())
                .account(subInfo.getAccount())
                .targetNum(subInfo.getNum())
                .contents(subInfo.getContents())
                .build();
        try{
            this.groupRepository.save(groupEntity);
        } catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_INSERT_ERROR);
        }
        Long groupsIdx = userGroupService.addUserGroup(optional.get());
        Optional<GroupEntity> optional1 = this.groupRepository.findByGroupIdx(groupsIdx);
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

    public List<GroupRes.subscription> getSubscriptionList(HttpServletRequest request) throws BaseException{
        HttpSession session = request.getSession(false);
        if(session == null){
            throw new BaseException(BaseResponseStatus.NON_EXIST_SESSION);
        }
        Long userIdx = (Long)session.getAttribute("LOGIN_USER");
        Optional<UserEntity> optional = this.userRepository.findByUserIdx(userIdx);
        if(optional.isEmpty()){
            throw new BaseException(BaseResponseStatus.NON_EXIST_USERIDX);
        }
        if(optional.get().getStatus().equals("inactive")){
            throw new BaseException(BaseResponseStatus.INACTIVE_USER);
        }

        List<UserGroupEntity> userGroupEntityList = this.userGroupRepository.findAllByUserIdx(optional.get());
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
        HttpSession session = request.getSession(false);
        if(session == null){
            throw new BaseException(BaseResponseStatus.NON_EXIST_SESSION);
        }
        Long userIdx = (Long)session.getAttribute("LOGIN_USER");
        Optional<UserEntity> optional = this.userRepository.findByUserIdx(userIdx);
        if(optional.isEmpty()){
            throw new BaseException(BaseResponseStatus.NON_EXIST_USERIDX);
        }
        if(optional.get().getStatus().equals("inactive")){
            throw new BaseException(BaseResponseStatus.INACTIVE_USER);
        }

        Optional<GroupEntity> groupEntity = this.groupRepository.findByGroupIdx(groupIdx);
        if(groupEntity.isEmpty()){
            throw new BaseException(BaseResponseStatus.NON_EXIST_GROUPIDX);
        }
        List<GroupRes.paymentHistory> paymentHistoryList = new ArrayList<>();
        LocalDate nextPayment = groupEntity.get().getFirstDayOfPayment();
        Long totalCost = 0L;
        Long price = groupEntity.get().getPrice();
        Long count = 0L;
        String account = groupEntity.get().getAccount();
        if(groupEntity.get().getPaymentCycle() < 0){
            while(nextPayment.isBefore(LocalDate.now())){
                totalCost += price;
                GroupRes.paymentHistory paymentHistory = new GroupRes.paymentHistory(nextPayment, totalCost, price, account);
                paymentHistoryList.add(paymentHistory);
                nextPayment = nextPayment.plusMonths(groupEntity.get().getPaymentCycle()*(-1));
                count++;
                if(paymentHistoryList.size() > 5){
                    paymentHistoryList.remove(0);
                }
            }
        }
        else{
            while(nextPayment.isBefore(LocalDate.now())){
                totalCost += price;
                GroupRes.paymentHistory paymentHistory = new GroupRes.paymentHistory(nextPayment, totalCost, price, account);
                paymentHistoryList.add(paymentHistory);
                nextPayment = nextPayment.plusDays(groupEntity.get().getPaymentCycle());
                count++;
                if(paymentHistoryList.size() > 5){
                    paymentHistoryList.remove(0);
                }
            }
        }
        GroupRes.paymentReport paymentReport = new GroupRes.paymentReport(paymentHistoryList, count);
        return paymentReport;
    }
}
