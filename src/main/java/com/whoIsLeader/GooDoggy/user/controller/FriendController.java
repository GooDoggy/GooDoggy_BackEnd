package com.whoIsLeader.GooDoggy.user.controller;

import com.whoIsLeader.GooDoggy.subscription.DTO.PersonalRes;
import com.whoIsLeader.GooDoggy.subscription.DTO.TotalRes;
import com.whoIsLeader.GooDoggy.subscription.service.PersonalService;
import com.whoIsLeader.GooDoggy.subscription.service.TotalService;
import com.whoIsLeader.GooDoggy.user.DTO.FriendRes;
import com.whoIsLeader.GooDoggy.user.repository.FriendRepository;
import com.whoIsLeader.GooDoggy.user.repository.UserRepository;
import com.whoIsLeader.GooDoggy.user.service.FriendService;
import com.whoIsLeader.GooDoggy.user.service.UserService;
import com.whoIsLeader.GooDoggy.util.BaseException;
import com.whoIsLeader.GooDoggy.util.BaseResponse;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/friends")
public class FriendController {

    private FriendService friendService;
    private PersonalService personalService;
    private TotalService totalService;

    public FriendController(FriendService friendService, PersonalService personalService, TotalService totalService){

        this.friendService = friendService;
        this.personalService = personalService;
        this.totalService = totalService;
    }

    @ResponseBody
    @PostMapping("/{id}")
    public BaseResponse<String> requestFriend(@PathVariable String id, HttpServletRequest request){
        try{
            this.friendService.requestFriend(id, request);
            return new BaseResponse<>(id.toString() + "님에게 친구 요청을 전송하였습니다.");
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @PatchMapping("/accept/{friendIdx}")
    public BaseResponse<String> acceptFriend(@PathVariable Long friendIdx, HttpServletRequest request){
        try{
            String name = this.friendService.acceptFriend(friendIdx, request);
            return new BaseResponse<>( name + "님의 친구 요청을 수락하였습니다.");
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @DeleteMapping("/reject/{friendIdx}")
    public BaseResponse<String> rejectFriend(@PathVariable Long friendIdx, HttpServletRequest request){
        try{
            String name = this.friendService.rejectFriend(friendIdx, request);
            return new BaseResponse<>( name + "님의 친구 요청을 거절하였습니다.");
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @DeleteMapping("/{friendIdxList}")
    public BaseResponse<String> deleteFriend(@PathVariable List<Long> friendIdxList, HttpServletRequest request){
        try{
            this.friendService.deleteFriend(friendIdxList, request);
            return new BaseResponse<>( "친구 삭제를 정상적으로 처리하였습니다.");
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/list")
    public BaseResponse<List<FriendRes.FriendInfo>> getFriendList(HttpServletRequest request){
        try{
            List<FriendRes.FriendInfo> friendList = this.friendService.getFriendList(request);
            return new BaseResponse<>(friendList);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/reqList") //유저가 받은
    public BaseResponse<List<FriendRes.FriendInfo>> getReqFriendList(HttpServletRequest request){
        try{
            List<FriendRes.FriendInfo> friendList = this.friendService.getReqFriendList(request);
            return new BaseResponse<>(friendList);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/resList") //유저가 보낸
    public BaseResponse<List<FriendRes.FriendInfo>> getResFriendList(HttpServletRequest request){
        try{
            List<FriendRes.FriendInfo> friendList = this.friendService.getResFriendList(request);
            return new BaseResponse<>(friendList);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/{friendIdx}")
    public BaseResponse<FriendRes.BriefInfo> getBriefInfo(@PathVariable Long friendIdx, HttpServletRequest request){
        try{
            FriendRes.BriefInfo briefInfoList = this.friendService.getBriefSubscription(friendIdx, request);
            return new BaseResponse<>(briefInfoList);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/subscriptions/{friendIdx}")
    public BaseResponse<TotalRes.allSubscription> getSubscriptionList(@PathVariable Long friendIdx, HttpServletRequest request){
        try{
            TotalRes.allSubscription subscriptionList= this.totalService.getOthersSubscriptionList(friendIdx, request);
            return new BaseResponse<>(subscriptionList);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
}
