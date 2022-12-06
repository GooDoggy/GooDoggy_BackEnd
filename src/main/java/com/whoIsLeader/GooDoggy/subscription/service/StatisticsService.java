package com.whoIsLeader.GooDoggy.subscription.service;

import com.whoIsLeader.GooDoggy.subscription.DTO.StatisticsReq;
import com.whoIsLeader.GooDoggy.subscription.entity.Category;
import com.whoIsLeader.GooDoggy.subscription.entity.GroupEntity;
import com.whoIsLeader.GooDoggy.subscription.entity.PersonalEntity;
import com.whoIsLeader.GooDoggy.subscription.DTO.StatisticsRes;
import com.whoIsLeader.GooDoggy.user.entity.UserEntity;
import com.whoIsLeader.GooDoggy.user.service.UserService;
import com.whoIsLeader.GooDoggy.util.BaseException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.whoIsLeader.GooDoggy.subscription.entity.Category.OTT;

@Service
public class StatisticsService {

    private PersonalService personalService;
    private GroupService groupService;
    private UserService userService;

    public StatisticsService(PersonalService personalService, GroupService groupService, UserService userService) {
        this.personalService = personalService;
        this.groupService = groupService;
        this.userService = userService;
    }

    public List<StatisticsRes.category> getCategoryStatistics(HttpServletRequest request) throws BaseException {
        UserEntity user = this.userService.getSessionUser(request);
        List<PersonalEntity> personalList = this.personalService.getEntityList();
        List<GroupEntity> groupList = this.groupService.getEntityList();
        HashMap<Category,Integer> categoryMap = new HashMap<Category,Integer>();
        int max_num = 0;
        Category result = OTT;
        for(PersonalEntity temp: personalList){
            Integer num = 1;
            if(categoryMap.containsKey(temp.getCategory())){
                num = categoryMap.get(temp.getCategory()) + 1;
            }
            categoryMap.put(temp.getCategory(), num);
            if(max_num < num){
                max_num = num;
                result = temp.getCategory();
            }
        }
        for(GroupEntity temp: groupList){
            Integer num = 1;
            if(categoryMap.containsKey(temp.getCategory())){
                num = categoryMap.get(temp.getCategory()) + (int)(long)temp.getJoinNum();
            }
            categoryMap.put(temp.getCategory(), num);
            if(max_num < num){
                max_num = num;
                result = temp.getCategory();
            }
        }
        int totalUser = 0;
        for (int temp: categoryMap.values()) {
            totalUser += temp;
        }
        List<StatisticsRes.category> categoryList = new ArrayList<>();
        for (Category temp: categoryMap.keySet()) {
            float percent = Float.parseFloat(String.format("%.2f", (float)categoryMap.get(temp) / totalUser * 100));
            categoryList.add(new StatisticsRes.category(temp.toString(), categoryMap.get(temp), percent));
        }
        return categoryList;
    }

    public List<StatisticsRes.briefSub> getBriefList(HttpServletRequest request) throws BaseException{
        UserEntity user = this.userService.getSessionUser(request);
        List<PersonalEntity> personal = this.personalService.getPersonalList(user);
        List<StatisticsRes.briefSub> briefSubList = new ArrayList<>();
        for(PersonalEntity temp : personal){
            briefSubList.add(new StatisticsRes.briefSub(temp.getProfileImg(), temp.getServiceName()));
        }
        return briefSubList;
    }

    public List<StatisticsRes.personal> getPersonalStatistics(HttpServletRequest request) throws BaseException {
        UserEntity user = this.userService.getSessionUser(request);
        List<PersonalEntity> personal = this.personalService.getEntityList();
        HashMap<String,Integer> personalMap = new HashMap<String,Integer>();
        for(PersonalEntity temp: personal){
            Integer num = 1;
            if(personalMap.containsKey(temp.getServiceName())){
                num = personalMap.get(temp.getServiceName()) + 1;
            }
            personalMap.put(temp.getServiceName(), num);
        }
        int totalUser = 0;
        for (int temp: personalMap.values()) {
            totalUser += temp;
        }
        List<Map.Entry<String, Integer>> entryList = new ArrayList<Map.Entry<String, Integer>>(personalMap.entrySet());
        Collections.sort(entryList, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        List<StatisticsRes.personal> personalList = new ArrayList<>();
        int count = 1;
        float totalPercent = 100;
        int userNum = totalUser;
        for (Map.Entry<String, Integer> temp: entryList) {
            if(count > 5) {
                personalList.add(new StatisticsRes.personal("기타", userNum, totalPercent));
                break;
            }
            float percent = Float.parseFloat(String.format("%.2f", (float)temp.getValue() / totalUser * 100));
            personalList.add(new StatisticsRes.personal(temp.getKey(), temp.getValue(), percent));
            totalPercent -= percent;
            userNum -= temp.getValue();
            count++;
        }
        return personalList;
    }

    public List<StatisticsRes.group> getGroupStatistics(HttpServletRequest request) throws BaseException {
        UserEntity user = this.userService.getSessionUser(request);
        List<GroupEntity> group = this.groupService.getEntityList();
        HashMap<String,Integer> groupMap = new HashMap<String,Integer>();
        for(GroupEntity temp: group){
            Integer num = 1;
            if(groupMap.containsKey(temp.getCategory())) {
                num = groupMap.get(temp.getCategory()) + (int) (long) temp.getJoinNum();
            }
            groupMap.put(temp.getServiceName(), num);
        }
        int totalUser = 0;
        for (int temp: groupMap.values()) {
            totalUser += temp;
        }
        List<Map.Entry<String, Integer>> entryList = new ArrayList<Map.Entry<String, Integer>>(groupMap.entrySet());
        Collections.sort(entryList, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        List<StatisticsRes.group> groupList = new ArrayList<>();
        int count = 1;
        float totalPercent = 100;
        int userNum = totalUser;
        for (Map.Entry<String, Integer> temp: entryList) {
            if(count > 5) {
                groupList.add(new StatisticsRes.group("기타", userNum, totalPercent));
                break;
            }
            float percent = Float.parseFloat(String.format("%.2f", (float)temp.getValue() / totalUser * 100));
            groupList.add(new StatisticsRes.group(temp.getKey(), temp.getValue(), percent));
            totalPercent -= percent;
            userNum -= temp.getValue();
            count++;
        }
        return groupList;
    }

    public List<StatisticsRes.dayReport> getWeekReport(List<StatisticsReq.appUsageList> appUsage, HttpServletRequest request) throws BaseException {
        UserEntity user = this.userService.getSessionUser(request);
        List<PersonalEntity> personal = this.personalService.getPersonalList(user);
        List<GroupEntity> group = this.groupService.getGroupList(user);

        HashSet<String> serviceNameSet = new HashSet<>();
        for(PersonalEntity temp : personal){
            serviceNameSet.add(temp.getServiceName());
        }
        for(GroupEntity temp : group){
            serviceNameSet.add(temp.getServiceName());
        }

        List<StatisticsRes.dayReport> dayReportList = new ArrayList<>();
        for(StatisticsReq.appUsageList temp1 : appUsage){ // 요일
            List<StatisticsRes.subInfo> subInfoList = new ArrayList<>();
            int totalTime = 0;
            for(StatisticsReq.appUsage temp2 : temp1.getAppUsageList()){ // 서비스
                if(serviceNameSet.contains(temp2.getServiceName())){
                    subInfoList.add(new StatisticsRes.subInfo(temp2.getServiceName(), temp2.getUsingTime()));
                    totalTime += temp2.getUsingTime();
                }
            }
            dayReportList.add(new StatisticsRes.dayReport(totalTime, subInfoList));
        }

        return dayReportList;
    }
}