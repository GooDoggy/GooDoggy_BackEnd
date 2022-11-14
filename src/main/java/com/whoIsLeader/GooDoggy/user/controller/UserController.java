package com.whoIsLeader.GooDoggy.user.controller;

import com.whoIsLeader.GooDoggy.user.service.UserService;
import com.whoIsLeader.GooDoggy.util.BaseException;
import com.whoIsLeader.GooDoggy.util.BaseResponse;
import com.whoIsLeader.GooDoggy.util.BaseResponseStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
}
