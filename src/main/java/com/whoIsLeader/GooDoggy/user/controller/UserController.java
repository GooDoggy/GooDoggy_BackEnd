package com.whoIsLeader.GooDoggy.user.controller;

import com.whoIsLeader.GooDoggy.user.DTO.UserReq;
import com.whoIsLeader.GooDoggy.user.entity.UserEntity;
import com.whoIsLeader.GooDoggy.user.service.UserService;
import com.whoIsLeader.GooDoggy.util.BaseException;
import com.whoIsLeader.GooDoggy.util.BaseResponse;
import com.whoIsLeader.GooDoggy.util.BaseResponseStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @ResponseBody
    @GetMapping("/test")
    public BaseResponse<String> test() {
        try {
            String testString = this.userService.test();
            return new BaseResponse<>(testString);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/signin")
    public BaseResponse<String> signin(@RequestBody UserReq.GetUserInfo userInfo){
        try{
            this.userService.signin(userInfo);
            return new BaseResponse<>("회원가입이 정상적으로 처리되었습니다.");
        } catch(BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/login")
    public BaseResponse<String> login(@RequestBody UserReq.GetUserIdPw userIdPw, HttpServletRequest request){
        try{
            this.userService.login(userIdPw, request);
            return new BaseResponse<>("로그인이 정상적으로 처리되었습니다.");
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/logout")
    public BaseResponse<String> logout(HttpServletRequest request){
        try {
            this.userService.logout(request);
            return new BaseResponse<>("로그아웃이 정상적으로 처리되었습니다.");
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/friends/{id}")
    public BaseResponse<String> requestFriend(@PathVariable String id, HttpServletRequest request){
        try{
            this.userService.requestFriend(id, request);
            return new BaseResponse<>(id.toString() + "님에게 친구 요청을 전송하였습니다.");
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @PatchMapping("/friends/accept/{friendIdx}")
    public BaseResponse<String> acceptFriend(@PathVariable Long friendIdx, HttpServletRequest request){
        try{
            String name = this.userService.acceptFriend(friendIdx, request);
            return new BaseResponse<>( name + "님의 친구 요청을 수락하였습니다.");
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @DeleteMapping("/friends/reject/{friendIdx}")
    public BaseResponse<String> rejectFriend(@PathVariable Long friendIdx, HttpServletRequest request){
        try{
            String name = this.userService.rejectFriend(friendIdx, request);
            return new BaseResponse<>( name + "님의 친구 요청을 거절하였습니다.");
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/id")
    public BaseResponse<String> findId(@RequestBody UserReq.GetUserNameEmail userNameEmail){
        try{
            String id = this.userService.findId(userNameEmail);
            return new BaseResponse<>(id);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/pw")
    public BaseResponse<Long> findPw(@RequestBody UserReq.GetUserNameId userNameId){
        try{
            Long userIdx = this.userService.findPw(userNameId);
            return new BaseResponse<>(userIdx);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @PatchMapping("/pw")
    public BaseResponse<String> changePw(@RequestBody UserReq.GetUserPws userPws){
        try{
            this.userService.changePw(userPws);
            return new BaseResponse<>("비밀번호 변경이 정상적으로 처리되었습니다.");
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @DeleteMapping("/friends/{friendIdxList}")
    public BaseResponse<String> deleteFriend(@PathVariable List<Long> friendIdxList, HttpServletRequest request){
        try{
            this.userService.deleteFriend(friendIdxList, request);
            return new BaseResponse<>( "친구 삭제를 정상적으로 처리하였습니다.");
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/friends/list")
    public BaseResponse<List<UserEntity>> getFriendList(HttpServletRequest request){
        try{
            List<UserEntity> friendList = this.userService.getFriendList(request);
            return new BaseResponse<>(friendList);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/friends/reqlist") //유저가 받은
    public BaseResponse<List<UserEntity>> getReqFriendList(HttpServletRequest request){
        try{
            List<UserEntity> friendList = this.userService.getReqFriendList(request);
            return new BaseResponse<>(friendList);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/friends/reslist") //유저가 보낸
    public BaseResponse<List<UserEntity>> getResFriendList(HttpServletRequest request){
        try{
            List<UserEntity> friendList = this.userService.getResFriendList(request);
            return new BaseResponse<>(friendList);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
}
