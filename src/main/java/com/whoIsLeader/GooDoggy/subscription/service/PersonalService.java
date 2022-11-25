package com.whoIsLeader.GooDoggy.subscription.service;

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

    private UserService userService;

    public PersonalService(PersonalRepository personalRepository, UserRepository userRepository, UserService userService){
        this.personalRepository = personalRepository;
        this.userRepository = userRepository;

        this.userService = userService;
    }

    public void addSubscription(PersonalReq.GetPersonalInfo subInfo, HttpServletRequest request) throws BaseException {
        UserEntity user = this.userService.getSessionUser(request);
        PersonalEntity personalEntity = PersonalEntity.builder()
                .userEntity(user)
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
        UserEntity user = this.userService.getSessionUser(request);
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

    public PersonalRes.paymentReport getPaymentReport(Long personalIdx, HttpServletRequest request) throws BaseException{
        UserEntity user = this.userService.getSessionUser(request);
        Optional<PersonalEntity> personal = this.personalRepository.findByPersonalIdx(personalIdx);
        if(personal.isEmpty()){
            throw new BaseException(BaseResponseStatus.NON_EXIST_PERSONALIDX);
        }

        return calculatePaymentReport(personal.get());
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
            if(paymentHistoryList.size() > 5){
                paymentHistoryList.remove(0);
            }
        }
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
}