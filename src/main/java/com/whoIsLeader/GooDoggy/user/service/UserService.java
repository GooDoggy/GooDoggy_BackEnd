package com.whoIsLeader.GooDoggy.user.service;

import com.whoIsLeader.GooDoggy.subscription.DTO.TotalRes;
import com.whoIsLeader.GooDoggy.subscription.entity.PersonalEntity;
import com.whoIsLeader.GooDoggy.subscription.service.TotalService;
import com.whoIsLeader.GooDoggy.user.DTO.FriendRes;
import com.whoIsLeader.GooDoggy.user.DTO.UserReq;
import com.whoIsLeader.GooDoggy.user.DTO.UserRes;
import com.whoIsLeader.GooDoggy.user.entity.FriendEntity;
import com.whoIsLeader.GooDoggy.user.entity.UserEntity;
import com.whoIsLeader.GooDoggy.user.repository.FriendRepository;
import com.whoIsLeader.GooDoggy.user.repository.UserRepository;
import com.whoIsLeader.GooDoggy.util.BaseException;
import com.whoIsLeader.GooDoggy.util.BaseResponse;
import com.whoIsLeader.GooDoggy.util.BaseResponseStatus;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {

    private UserRepository userRepository;
    private FriendRepository friendRepository;

    public UserService(UserRepository userRepository, FriendRepository friendRepository){
        this.userRepository = userRepository;
        this.friendRepository = friendRepository;
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

    public UserEntity getSessionUser(HttpServletRequest request) throws BaseException{
        HttpSession session = request.getSession(false);
        if(session == null){
            throw new BaseException(BaseResponseStatus.NON_EXIST_SESSION);
        }
        Long userIdx = (Long)session.getAttribute("LOGIN_USER");
        Optional<UserEntity> optional = this.userRepository.findByUserIdx(userIdx);
        if(optional.isEmpty()){
            throw new BaseException(BaseResponseStatus.INVALID_SESSION_INFORMATION);
        }
        if(optional.get().getStatus().equals("inactive")){
            throw new BaseException(BaseResponseStatus.INACTIVE_USER);
        }
        return optional.get();
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
        if(optional.get().getStatus().equals("inactive")){
            throw new BaseException(BaseResponseStatus.INACTIVE_USER);
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

    public String findId(UserReq.GetUserNameEmail userNameEmail) throws BaseException{
        Optional<UserEntity> optional = this.userRepository.findByNameAndEmail(userNameEmail.getName(), userNameEmail.getEmail());
        if(optional.isEmpty()){
            throw new BaseException(BaseResponseStatus.INVALID_NAME_EMAIL);
        }
        if(optional.get().getStatus().equals("inactive")){
            throw new BaseException(BaseResponseStatus.INACTIVE_USER);
        }
        return optional.get().getId();
    }

    public Long findPw(UserReq.GetUserNameId userNameId) throws BaseException{
        Optional<UserEntity> optional = this.userRepository.findByNameAndId(userNameId.getName(), userNameId.getId());
        if(optional.isEmpty()){
            throw new BaseException(BaseResponseStatus.INVALID_NAME_ID);
        }
        if(optional.get().getStatus().equals("inactive")){
            throw new BaseException(BaseResponseStatus.INACTIVE_USER);
        }
        return optional.get().getUserIdx();
    }

    public void changePw(UserReq.GetUserPws userPws) throws BaseException{
        Optional<UserEntity> optional = this.userRepository.findByUserIdx(userPws.getUserIdx());
        if(optional.isEmpty()){
            throw new BaseException(BaseResponseStatus.NON_EXIST_USERIDX);
        }
        if(optional.get().getStatus().equals("inactive")){
            throw new BaseException(BaseResponseStatus.INACTIVE_USER);
        }
        if(!userPws.getNewPassword().equals(userPws.getNewPassword_check())){
            throw new BaseException(BaseResponseStatus.DISMATCH_PASSWORD);
        }
        try{
            optional.get().changePw(userPws.getNewPassword());
            this.userRepository.save(optional.get());
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_PATCH_ERROR);
        }
    }

    public String getProfileimg(HttpServletRequest request) throws BaseException{
        UserEntity user = this.getSessionUser(request);
        return user.getProfileimg();
    }
}
