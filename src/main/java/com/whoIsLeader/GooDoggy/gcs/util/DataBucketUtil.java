package com.whoIsLeader.GooDoggy.gcs.util;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.whoIsLeader.GooDoggy.user.DTO.UserReq;
import com.whoIsLeader.GooDoggy.util.BaseException;
import com.whoIsLeader.GooDoggy.util.BaseResponseStatus;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

@Component
public class DataBucketUtil {
    @Value("${gcp.config.file}")
    private String gcpConfigFile;

    @Value("${gcp.project.id}")
    private String gcpProjectId;

    @Value("${gcp.bucket.id}")
    private String gcpBucketId;


    public UserReq.GetProfileImg uploadFile(MultipartFile multipartFile, String fileName, String contentType) throws BaseException {
        try{
            byte[] fileData = FileUtils.readFileToByteArray((convertFile(multipartFile)));

            InputStream inputStream = new ClassPathResource(gcpConfigFile).getInputStream();

            StorageOptions options = StorageOptions.newBuilder().setProjectId(gcpProjectId)
                    .setCredentials(GoogleCredentials.fromStream(inputStream)).build();

            Storage storage = options.getService();
            Bucket bucket = storage.get(gcpBucketId,Storage.BucketGetOption.fields());
            int check = checkFileExtension(fileName);
            if(check == 1){
                Blob blob = bucket.create(fileName, fileData, contentType);
                if(blob != null){
                    String imgurl = "https://storage.googleapis.com/goodoggy_bucket/"+fileName;
                    return new UserReq.GetProfileImg(imgurl);
                }
            }else if(check == 0){
                throw new BaseException(BaseResponseStatus.INVALID_FILE_EXTENSION);
            }
        } catch (Exception e){
            throw new BaseException(BaseResponseStatus.ERROR_STORING_TO_GCS);
        }
        throw new BaseException(BaseResponseStatus.ERROR_STORING_TO_GCS);
    }

    public File convertFile(MultipartFile file) throws BaseException {
        try{
            if(file.getOriginalFilename() == null){
                throw new BaseException(BaseResponseStatus.FILENAME_NULL);
            }
            File convertedFile = new File(file.getOriginalFilename());
            FileOutputStream outputStream = new FileOutputStream(convertedFile);
            outputStream.write(file.getBytes());
            outputStream.close();
            return convertedFile;
        }catch(Exception e){
            throw new BaseException(BaseResponseStatus.FAILED_CONVERT);
        }
    }

    public int checkFileExtension(String fileName){
        if(fileName != null && fileName.contains(".")){
            String[] extensionList = {".png",".jpeg",".jpg"};

            for(String extension: extensionList){
                if(fileName.endsWith(extension)){
                    return 1;
                }
            }
        }
        return 0;
    }
}
