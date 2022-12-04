package com.whoIsLeader.GooDoggy.subscription.service;

import com.whoIsLeader.GooDoggy.subscription.entity.Category;
import com.whoIsLeader.GooDoggy.subscription.entity.GroupEntity;
import com.whoIsLeader.GooDoggy.subscription.entity.PersonalEntity;
import com.whoIsLeader.GooDoggy.user.DTO.StatisticsRes;
import com.whoIsLeader.GooDoggy.user.entity.UserEntity;
import com.whoIsLeader.GooDoggy.user.service.UserService;
import com.whoIsLeader.GooDoggy.util.BaseException;
import com.whoIsLeader.GooDoggy.util.BaseResponseStatus;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.whoIsLeader.GooDoggy.subscription.entity.Category.FOOD;
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
            categoryList.add(new StatisticsRes.category(temp, categoryMap.get(temp), percent));
        }
        return categoryList;
    }
}