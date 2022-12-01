package com.whoIsLeader.GooDoggy.subscription.service;

import com.whoIsLeader.GooDoggy.gcs.service.GCSService;
import com.whoIsLeader.GooDoggy.subscription.DTO.GroupRes;
import com.whoIsLeader.GooDoggy.subscription.DTO.PersonalReq;
import com.whoIsLeader.GooDoggy.subscription.DTO.PersonalRes;
import com.whoIsLeader.GooDoggy.subscription.entity.Category;
import com.whoIsLeader.GooDoggy.subscription.entity.GroupEntity;
import com.whoIsLeader.GooDoggy.subscription.entity.PersonalEntity;
import com.whoIsLeader.GooDoggy.subscription.repository.PersonalRepository;
import com.whoIsLeader.GooDoggy.user.DTO.UserReq;
import com.whoIsLeader.GooDoggy.user.entity.UserEntity;
import com.whoIsLeader.GooDoggy.user.repository.UserRepository;
import com.whoIsLeader.GooDoggy.user.service.UserService;
import com.whoIsLeader.GooDoggy.util.BaseException;
import com.whoIsLeader.GooDoggy.util.BaseResponseStatus;

import net.bytebuddy.asm.Advice;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PersonalService {

    private PersonalRepository personalRepository;
    private UserRepository userRepository;

    private UserService userService;
    @Autowired
    private GCSService gcsService;

    public PersonalService(PersonalRepository personalRepository, UserRepository userRepository, UserService userService){
        this.personalRepository = personalRepository;
        this.userRepository = userRepository;

        this.userService = userService;
    }

    public void addSubscription(PersonalReq.GetPersonalInfo subInfo, HttpServletRequest request) throws BaseException {
        UserEntity user = this.userService.getSessionUser(request);

        String profileUrl = this.gcsService.getServiceImg(subInfo.getServiceName());

        PersonalEntity personalEntity = PersonalEntity.builder()
                .userEntity(user)
                .serviceName(subInfo.getServiceName())
                .planName(subInfo.getPlanName())
                .price(subInfo.getPrice())
                .firstDayOfPayment(subInfo.getFirstDayOfPayment())
                .paymentCycle(subInfo.getPaymentCycle())
                .category(subInfo.getCategory())
                .account(subInfo.getAccount())
                .profileImg(profileUrl)
                .build();
        try{
            this.personalRepository.save(personalEntity);
        } catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_INSERT_ERROR);
        }
    }

    public List<PersonalRes.subscription> getUserSubList(HttpServletRequest request) throws BaseException{
        UserEntity user = this.userService.getSessionUser(request);
        return getSubscriptionList(user);
    }

    public List<PersonalRes.subscription> getOthersSubList(Long friendIdx, HttpServletRequest request) throws BaseException{
        UserEntity user = this.userService.getSessionUser(request);
        Optional<UserEntity> optional = this.userRepository.findByUserIdx(friendIdx);
        if(optional.isEmpty()){
            throw new BaseException(BaseResponseStatus.NON_EXIST_FRIENDIDX);
        }
        return getSubscriptionList(optional.get());
    }

    public List<PersonalRes.subscription> getSubscriptionList(UserEntity user) throws BaseException{
        List<PersonalEntity> personalEntityList = this.personalRepository.findAllByUserIdx(user);
        List<PersonalRes.subscription> subscriptionList = new ArrayList<>();
        for(PersonalEntity temp : personalEntityList){
            if(temp.getStatus().equals("active")){
                PersonalRes.subscription subscription = new PersonalRes.subscription();
                subscription.setPersonalIdx(temp.getPersonalIdx());
                subscription.setServiceName(temp.getServiceName());
                subscription.setPrice(temp.getPrice());
                LocalDate nextPayment = temp.getFirstDayOfPayment();
                if(temp.getPaymentCycle() < 0){
                    while(nextPayment.isBefore(LocalDate.now())){
                        nextPayment = nextPayment.plusMonths(temp.getPaymentCycle()*(-1));
                    }
                }
                else{
                    while(nextPayment.isBefore(LocalDate.now())){
                        nextPayment = nextPayment.plusDays(temp.getPaymentCycle());
                    }
                }
                subscription.setNextPayment(nextPayment);
                subscription.setCategory(temp.getCategory());
                subscriptionList.add(subscription);
            }
        }
        return subscriptionList;
    }

    public PersonalRes.paymentReport getAllPaymentReport(Long personalIdx, HttpServletRequest request) throws BaseException{
        UserEntity user = this.userService.getSessionUser(request);
        Optional<PersonalEntity> personal = this.personalRepository.findByPersonalIdx(personalIdx);
        if(personal.isEmpty()){
            throw new BaseException(BaseResponseStatus.NON_EXIST_PERSONALIDX);
        }
        return calculatePaymentReport(personal.get());
    }

    public PersonalRes.paymentReport getPaymentReport(Long personalIdx, HttpServletRequest request) throws BaseException{
        PersonalRes.paymentReport paymentReport = getAllPaymentReport(personalIdx, request);
        if(paymentReport.getPaymentHistoryList().size() > 3){
            List<PersonalRes.paymentHistory> paymentHistoryList = new ArrayList<>();
            for(int i = 0; i < 3; i++){
                paymentHistoryList.add(paymentReport.getPaymentHistoryList().get(i));
            }
            paymentReport.setPaymentHistoryList(paymentHistoryList);
        }
        return paymentReport;
    }

    public PersonalRes.paymentReport calculatePaymentReport(PersonalEntity personal) throws BaseException{
        List<PersonalRes.paymentHistory> paymentHistoryList = new ArrayList<>();
        LocalDate nextPayment = personal.getFirstDayOfPayment();
        String account = personal.getAccount();
        Long price = personal.getPrice();
        Long totalCost = 0L;
        Long count = 0L;

        while(nextPayment.isBefore(LocalDate.now())){
            totalCost += price;
            PersonalRes.paymentHistory paymentHistory = new PersonalRes.paymentHistory(nextPayment, totalCost, price, account);
            paymentHistoryList.add(paymentHistory);
            if(personal.getPaymentCycle() < 0){
                nextPayment = nextPayment.plusMonths(personal.getPaymentCycle()*(-1));
            }
            else{
                nextPayment = nextPayment.plusDays(personal.getPaymentCycle());
            }
            count++;
        }
        Collections.reverse(paymentHistoryList);
        PersonalRes.paymentReport paymentReport = new PersonalRes.paymentReport(paymentHistoryList, count);
        return paymentReport;
    }

    public void inactiveSubscriptions(List<Long> personalIdxList, HttpServletRequest request) throws BaseException{
        UserEntity user = this.userService.getSessionUser(request);

        for(Long temp : personalIdxList){
            Optional<PersonalEntity> personalEntity = this.personalRepository.findByPersonalIdx(temp);
            if(personalEntity.isEmpty()){
                throw new BaseException(BaseResponseStatus.NON_EXIST_PERSONALIDX);
            }
            try{
                personalEntity.get().changeStatus("inactive");
                this.personalRepository.save(personalEntity.get());
            } catch (Exception e) {
                throw new BaseException(BaseResponseStatus.DATABASE_PATCH_ERROR);
            }
        }
    }

    public PersonalRes.personalDetails getDetails(Long personalIdx, HttpServletRequest request) throws BaseException{
        UserEntity user = this.userService.getSessionUser(request);
        Optional<PersonalEntity> personal = this.personalRepository.findByPersonalIdx(personalIdx);
        if(personal.isEmpty()){
            throw new BaseException(BaseResponseStatus.NON_EXIST_PERSONALIDX);
        }
        PersonalRes.personalDetails personalDetails = new PersonalRes.personalDetails(personalIdx,personal.get().getServiceName(),
                personal.get().getPlanName(),personal.get().getPrice(),personal.get().getFirstDayOfPayment(),personal.get().getPaymentCycle(),
                personal.get().getCategory(),personal.get().getAccount());

        return personalDetails;
    }

    public List<PersonalEntity> getPersonalList(UserEntity user){
        return this.personalRepository.findAllByUserIdx(user);
    }
}