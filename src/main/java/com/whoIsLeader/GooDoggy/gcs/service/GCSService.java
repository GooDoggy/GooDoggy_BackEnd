package com.whoIsLeader.GooDoggy.gcs.service;

import com.google.cloud.storage.*;
import com.whoIsLeader.GooDoggy.gcs.util.DataBucketUtil;
import com.whoIsLeader.GooDoggy.user.DTO.UserReq;
import com.whoIsLeader.GooDoggy.user.entity.UserEntity;
import com.whoIsLeader.GooDoggy.user.repository.UserRepository;
import com.whoIsLeader.GooDoggy.user.service.UserService;
import com.whoIsLeader.GooDoggy.util.BaseException;
import com.whoIsLeader.GooDoggy.util.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class GCSService {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    private final DataBucketUtil dataBucketUtil;

    @SuppressWarnings("deprecation")
    public UserReq.GetProfileImg uploadFileToGCS(MultipartFile imgfile, HttpServletRequest request) throws IOException, BaseException {
        UserEntity user = this.userService.getSessionUser(request);
        UserReq.GetProfileImg getProfileImg;
        String originalFileName = imgfile.getOriginalFilename();
        if(originalFileName == null){
            throw new BaseException(BaseResponseStatus.FILENAME_NULL);
        }
        Path path = new File(originalFileName).toPath();
        try{
            String contentType = Files.probeContentType(path);
            getProfileImg = dataBucketUtil.uploadFile(imgfile, originalFileName, contentType);
        }catch(Exception e){
            throw new BaseException(BaseResponseStatus.ERROR_STORING_TO_GCS);
        }
        try{
            user.changeProfileImg(getProfileImg.getProfileImg());
            this.userRepository.save(user);
        } catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_INSERT_ERROR);
        }
        return getProfileImg;
    }

    public String getServiceImg(String serviceName){
        String returnImg = "https://storage.googleapis.com/goodoggy_bucket/goodoggy.jpg";

        if(serviceName.equals("쿠팡") || serviceName.equals("로켓배송") || serviceName.equals("쿠팡 로켓배송")) {
            returnImg = "https://storage.googleapis.com/goodoggy_bucket/platform/coupang.png";
        }
        if(serviceName.equals("플로")) {
            returnImg = "https://storage.googleapis.com/goodoggy_bucket/platform/flo.png";
        }
        if(serviceName.equals("프레딧")) {
            returnImg = "https://storage.googleapis.com/goodoggy_bucket/platform/fredit.png";
        }
        if(serviceName.equals("중앙일보")) {
            returnImg = "https://storage.googleapis.com/goodoggy_bucket/platform/joongang.png";
        }
        if(serviceName.equals("밀리의 서재") || serviceName.equals("밀리의서재")) {
            returnImg = "https://storage.googleapis.com/goodoggy_bucket/platform/millie.png";
        }
        if(serviceName.equals("멜론")) {
            returnImg = "https://storage.googleapis.com/goodoggy_bucket/platform/melon.png";
        }
        if(serviceName.equals("라프텔")) {
            returnImg = "https://storage.googleapis.com/goodoggy_bucket/platform/laftel.png";
        }
        if(serviceName.equals("넷플릭스")) {
            returnImg = "https://storage.googleapis.com/goodoggy_bucket/platform/netflix.png";
        }
        if(serviceName.equals("왓챠")) {
            returnImg = "https://storage.googleapis.com/goodoggy_bucket/platform/watcha.png";
        }
        if(serviceName.equals("유튜브 프리미엄")) {
            returnImg = "https://storage.googleapis.com/goodoggy_bucket/platform/youtube.png";
        }
        return returnImg;
    }
}
