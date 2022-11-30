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
            user.changeProfileimg(getProfileImg.getProfileimg());
            this.userRepository.save(user);
        } catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_INSERT_ERROR);
        }
        return getProfileImg;
    }
}
