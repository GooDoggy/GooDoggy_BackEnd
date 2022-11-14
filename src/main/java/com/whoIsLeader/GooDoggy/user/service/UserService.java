package com.whoIsLeader.GooDoggy.user.service;

import com.whoIsLeader.GooDoggy.util.BaseException;
import com.whoIsLeader.GooDoggy.util.BaseResponse;
import com.whoIsLeader.GooDoggy.util.BaseResponseStatus;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public String test() throws BaseException {
        String testString = "hello world";
        if(testString.equals("hello world")) {
            return testString;
        }
        else{
            throw new BaseException(BaseResponseStatus.TEST);
        }
    }
}
