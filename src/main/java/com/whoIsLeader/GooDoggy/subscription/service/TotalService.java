package com.whoIsLeader.GooDoggy.subscription.service;

import com.whoIsLeader.GooDoggy.subscription.DTO.GroupRes;
import com.whoIsLeader.GooDoggy.subscription.DTO.MemberRes;
import com.whoIsLeader.GooDoggy.subscription.DTO.PersonalRes;
import com.whoIsLeader.GooDoggy.subscription.DTO.TotalRes;
import com.whoIsLeader.GooDoggy.subscription.entity.Category;
import com.whoIsLeader.GooDoggy.subscription.entity.GroupEntity;
import com.whoIsLeader.GooDoggy.subscription.entity.PersonalEntity;
import com.whoIsLeader.GooDoggy.subscription.repository.PersonalRepository;
import com.whoIsLeader.GooDoggy.user.DTO.UserRes;
import com.whoIsLeader.GooDoggy.user.entity.UserEntity;
import com.whoIsLeader.GooDoggy.user.repository.UserRepository;
import com.whoIsLeader.GooDoggy.user.service.UserService;
import com.whoIsLeader.GooDoggy.util.BaseException;
import com.whoIsLeader.GooDoggy.util.BaseResponseStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.*;

@Service
public class TotalService {

    private PersonalService personalService;
    private GroupService groupService;
    private UserService userService;

    public TotalService(PersonalService personalService, GroupService groupService, UserService userService) {
        this.personalService = personalService;
        this.groupService = groupService;
        this.userService = userService;
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

    public TotalRes.calendar getCalendar(HttpServletRequest request) throws BaseException{
        UserEntity user = this.userService.getSessionUser(request);
        List<PersonalEntity> personal = this.personalService.getPersonalList(user);
        List<GroupEntity> group = this.groupService.getGroupList(user);
        List<Integer> dateList = new ArrayList<>();
        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonth().getValue();
        for(PersonalEntity temp : personal){
            LocalDate payDate = temp.getFirstDayOfPayment();
            if(temp.getPaymentCycle() < 0){
                while(payDate.getYear() < year || (payDate.getYear() == year && payDate.getMonth().getValue() < month)){
                    payDate = payDate.plusMonths(temp.getPaymentCycle()*(-1));
                }
                while(payDate.getYear() == year && payDate.getMonth().getValue() == month){
                    dateList.add(payDate.getDayOfMonth());
                    payDate = payDate.plusMonths(temp.getPaymentCycle()*(-1));
                }
            }
            else{
                while(payDate.getYear() < year || (payDate.getYear() == year && payDate.getMonth().getValue() < month)){
                    payDate = payDate.plusDays(temp.getPaymentCycle());
                }
                while(payDate.getYear() == year && payDate.getMonth().getValue() == month){
                    dateList.add(payDate.getDayOfMonth());
                    payDate = payDate.plusDays(temp.getPaymentCycle());
                }
            }
        }
        for(GroupEntity temp : group){
            LocalDate payDate = temp.getFirstDayOfPayment();
            if(temp.getPaymentCycle() < 0){
                while(!payDate.isAfter(temp.getLastDayOfPayment()) && (payDate.getYear() < year || (payDate.getYear() == year && payDate.getMonth().getValue() < month))){
                    payDate = payDate.plusMonths(temp.getPaymentCycle()*(-1));
                }
                while(!payDate.isAfter(temp.getLastDayOfPayment()) && (payDate.getYear() == year && payDate.getMonth().getValue() == month)){
                    dateList.add(payDate.getDayOfMonth());
                    payDate = payDate.plusMonths(temp.getPaymentCycle()*(-1));
                }
            }
            else{
                while(!payDate.isAfter(temp.getLastDayOfPayment()) && (payDate.getYear() < year || (payDate.getYear() == year && payDate.getMonth().getValue() < month))){
                    payDate = payDate.plusDays(temp.getPaymentCycle());
                }
                while(!payDate.isAfter(temp.getLastDayOfPayment()) && (payDate.getYear() == year && payDate.getMonth().getValue() == month)){
                    dateList.add(payDate.getDayOfMonth());
                    payDate = payDate.plusDays(temp.getPaymentCycle());
                }
            }
        }
        HashSet<Integer> finalDateList = new HashSet<>(dateList);
        return new TotalRes.calendar(year, month, finalDateList);
    }

    public UserRes.mainInfo getMainInfo(HttpServletRequest request) throws BaseException{
        UserEntity user = this.userService.getSessionUser(request);
        UserRes.userInfo userInfo = new UserRes.userInfo(user.getUserIdx(), user.getId(), user.getProfileImg());
        TotalRes.calendar calendar = getCalendar(request);
        UserRes.randomInfo randomInfo = new UserRes.randomInfo();
        int num = (int)(Math.random()*10);
        if(calendar.getDateList().contains(LocalDate.now().getDayOfMonth())){
            randomInfo.setString1("오늘은");
            randomInfo.setString2("넷플릭스");
            randomInfo.setString3("결제일입니다");
            return new UserRes.mainInfo(randomInfo, userInfo, calendar);
        }
        if(num < 5){
            randomInfo.setString1("다음 결제일은");
            randomInfo.setString2(getNextService(calendar.getDateList()));
            randomInfo.setString3("입니다");
            if(!randomInfo.getString2().equals("")) {
                return new UserRes.mainInfo(randomInfo, userInfo, calendar);
            }
        }
        randomInfo.setString1("요즘 인기있는");
        randomInfo.setString2(getPopularService());
        randomInfo.setString3("추천드려요");
        return new UserRes.mainInfo(randomInfo, userInfo, calendar);
    }

    public String getPopularService(){
        List<PersonalEntity> personalList = this.personalService.getEntityList();
        List<GroupEntity> groupList = this.groupService.getEntityList();
        HashMap<String,Integer> map = new HashMap<String,Integer>();
        int max_num = 0;
        String result = "";
        for(PersonalEntity temp: personalList){
            Integer num = 1;
            if(map.containsKey(temp.getServiceName())){
                num = map.get(temp.getServiceName()) + 1;
            }
            map.put(temp.getServiceName(), num);
            if(max_num < num){
                max_num = num;
                result = temp.getServiceName();
            }
        }
        for(GroupEntity temp: groupList){
            Integer num = 1;
            if(map.containsKey(temp.getServiceName())){
                num = map.get(temp.getServiceName()) + 1;
            }
            map.put(temp.getServiceName(), num);
            if(max_num < num){
                max_num = num;
                result = temp.getServiceName();
            }
        }
        return result;
    }

    public String getNextService(HashSet<Integer> dateList){
        int nearestDate = 32;
        String result = "";
        for(Integer temp : dateList){
            if(temp > LocalDate.now().getDayOfMonth() && nearestDate > temp){
                nearestDate = temp;
                result = temp.toString() + "일 뒤";
            }
        }
        return result;
    }
}