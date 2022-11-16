package com.whoIsLeader.GooDoggy.user.service;

import com.whoIsLeader.GooDoggy.user.DTO.UserReq;
import com.whoIsLeader.GooDoggy.user.entity.UserEntity;
import com.whoIsLeader.GooDoggy.user.repository.UserRepository;
import com.whoIsLeader.GooDoggy.util.BaseException;
import com.whoIsLeader.GooDoggy.util.BaseResponse;
import com.whoIsLeader.GooDoggy.util.BaseResponseStatus;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public String test() throws BaseException {
        String testString = "hello world";
        if(testString.equals("hello world")) {
            return testString;
        }
        else{
            throw new BaseException(BaseResponseStatus.TEST);
        }
    }

    public void signin(UserReq.GetUserInfo userInfo) throws BaseException{
        Optional<UserEntity> optional = this.userRepository.findById(userInfo.getId());
        if(!optional.isEmpty()){
            throw new BaseException(BaseResponseStatus.DUPLICATE_ID);
        }
        if(!userInfo.getPassword().equals(userInfo.getPassword_check())){
            throw new BaseException(BaseResponseStatus.DISMATCH_PASSWORD);
        }
        String regex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(userInfo.getEmail());
        if(!matcher.find()){
            throw new BaseException(BaseResponseStatus.INVALID_EMAIL);
        }
        UserEntity userEntity = UserEntity.builder()
                .name(userInfo.getName())
                .id(userInfo.getId())
                .email(userInfo.getEmail())
                .password(userInfo.getPassword())
                .build();
        try{
            this.userRepository.save(userEntity);
        } catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_INSERT_ERROR);
        }
    }

    public void login(UserReq.GetUserIdPw userIdPw, HttpServletRequest request) throws BaseException{
        Optional<UserEntity> optional = this.userRepository.findById(userIdPw.getId());
        if(optional.isEmpty()){
            throw new BaseException(BaseResponseStatus.NON_EXIST_ID);
        }
        if(!optional.get().getPassword().equals(userIdPw.getPassword())){
            throw new BaseException(BaseResponseStatus.DISMATCH_PASSWORD);
        }
        HttpSession session = request.getSession();
        session.setAttribute("LOGIN_USER", optional.get().getUserIdx());
    }

    public void logout(HttpServletRequest request) throws BaseException{
        HttpSession session = request.getSession(false);
        if(session == null){
            throw new BaseException(BaseResponseStatus.NON_EXIST_SESSION);
        }
        session.invalidate();
    }
}
