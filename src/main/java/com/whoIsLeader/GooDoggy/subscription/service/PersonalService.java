package com.whoIsLeader.GooDoggy.subscription.service;

import com.whoIsLeader.GooDoggy.subscription.DTO.PersonalReq;
import com.whoIsLeader.GooDoggy.subscription.DTO.PersonalRes;
import com.whoIsLeader.GooDoggy.subscription.entity.Category;
import com.whoIsLeader.GooDoggy.subscription.entity.PersonalEntity;
import com.whoIsLeader.GooDoggy.subscription.repository.PersonalRepository;
import com.whoIsLeader.GooDoggy.user.DTO.UserReq;
import com.whoIsLeader.GooDoggy.user.entity.UserEntity;
import com.whoIsLeader.GooDoggy.user.repository.UserRepository;
import com.whoIsLeader.GooDoggy.util.BaseException;
import com.whoIsLeader.GooDoggy.util.BaseResponseStatus;

import net.bytebuddy.asm.Advice;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PersonalService {

    private PersonalRepository personalRepository;
    private UserRepository userRepository;

    public PersonalService(PersonalRepository personalRepository, UserRepository userRepository){
        this.personalRepository = personalRepository;
        this.userRepository = userRepository;
    }

    public void addSubscription(PersonalReq.GetPersonalInfo subInfo, HttpServletRequest request) throws BaseException {
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
        PersonalEntity personalEntity = PersonalEntity.builder()
                .userEntity(optional.get())
                .serviceName(subInfo.getServiceName())
                .planName(subInfo.getPlanName())
                .price(subInfo.getPrice())
                .firstDayOfPayment(subInfo.getFirstDayOfPayment())
                .paymentCycle(subInfo.getPaymentCycle())
                .category(subInfo.getCategory())
                .account(subInfo.getAccount())
                .build();
        try{
            this.personalRepository.save(personalEntity);
        } catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_INSERT_ERROR);
        }
    }

    public List<PersonalRes.subscription> getSubscriptionList(HttpServletRequest request) throws BaseException{
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
        List<PersonalEntity> personalEntityList = this.personalRepository.findAllByUserIdx(optional.get());
        List<PersonalRes.subscription> subscriptionList = new ArrayList<>();
        for(PersonalEntity temp : personalEntityList){
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
        return subscriptionList;
    }

    public PersonalRes.paymentReport getPaymentReport(Long personalIdx, HttpServletRequest request) throws BaseException{
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
        Optional<PersonalEntity> personalEntity = this.personalRepository.findByPersonalIdx(personalIdx);
        if(personalEntity.isEmpty()){
            throw new BaseException(BaseResponseStatus.NON_EXIST_PERSONALIDX);
        }
        List<PersonalRes.paymentHistory> paymentHistoryList = new ArrayList<>();
        LocalDate nextPayment = personalEntity.get().getFirstDayOfPayment();
        Long totalCost = 0L;
        Long price = personalEntity.get().getPrice();
        Long count = 0L;
        String account = personalEntity.get().getAccount();
        if(personalEntity.get().getPaymentCycle() < 0){
            while(nextPayment.isBefore(LocalDate.now())){
                totalCost += price;
                PersonalRes.paymentHistory paymentHistory = new PersonalRes.paymentHistory(nextPayment, totalCost, price, account);
                paymentHistoryList.add(paymentHistory);
                nextPayment = nextPayment.plusMonths(personalEntity.get().getPaymentCycle()*(-1));
                count++;
                if(paymentHistoryList.size() > 5){
                    paymentHistoryList.remove(0);
                }
            }
        }
        else{
            while(nextPayment.isBefore(LocalDate.now())){
                totalCost += price;
                PersonalRes.paymentHistory paymentHistory = new PersonalRes.paymentHistory(nextPayment, totalCost, price, account);
                paymentHistoryList.add(paymentHistory);
                nextPayment = nextPayment.plusDays(personalEntity.get().getPaymentCycle());
                count++;
                if(paymentHistoryList.size() > 5){
                    paymentHistoryList.remove(0);
                }
            }
        }
        PersonalRes.paymentReport paymentReport = new PersonalRes.paymentReport(paymentHistoryList, count);
        return paymentReport;
    }

    public void inactiveSubscriptions(List<Long> personalIdxList, HttpServletRequest request) throws BaseException{
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

}
