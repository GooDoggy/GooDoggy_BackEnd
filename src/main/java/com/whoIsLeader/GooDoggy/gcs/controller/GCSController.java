package com.whoIsLeader.GooDoggy.gcs.controller;


import com.whoIsLeader.GooDoggy.gcs.service.GCSService;
import com.whoIsLeader.GooDoggy.user.DTO.UserReq;
import com.whoIsLeader.GooDoggy.user.service.UserService;
import com.whoIsLeader.GooDoggy.util.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/profile/upload")
public class GCSController {
    private final GCSService gcsService;
    private final UserService userService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public UserReq.GetProfileImg localUploadToStorage(@RequestParam("file") MultipartFile imgfile, HttpServletRequest request) throws IOException, BaseException {
        return gcsService.uploadFileToGCS(imgfile, request);
    }

    @GetMapping("/get")
    public String getProfileImg(HttpServletRequest request) throws BaseException{
        String profileImg = this.userService.getProfileImg(request);
        return profileImg;
    }
}
