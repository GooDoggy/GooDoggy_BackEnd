package com.whoIsLeader.GooDoggy.gcs.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UploadReq {
    private String bucketName = "goodoggy_bucket";
    private String uploadFileName; // 버킷(구글 클라우드 스토리지)에 업로드할 파일 이름
    private String localFileLocation; // 로컬에서 업로드 할 파일이름
}
