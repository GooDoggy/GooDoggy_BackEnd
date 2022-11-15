package com.whoIsLeader.GooDoggy.user.controller;

import com.whoIsLeader.GooDoggy.user.DTO.UserReq;
import com.whoIsLeader.GooDoggy.user.service.UserService;
import com.whoIsLeader.GooDoggy.util.BaseException;
import com.whoIsLeader.GooDoggy.util.BaseResponse;
import com.whoIsLeader.GooDoggy.util.BaseResponseStatus;
import org.springframework.web.bind.annotation.*;

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
}
