package com.keduit.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log
public class FileService{

    public String uploadFiles(String uploadPath, String orignalFileName, byte[] fileData) throws Exception{
        UUID uuid = UUID.randomUUID();
        String extention = orignalFileName.substring(orignalFileName.lastIndexOf("."));
        String  savedFileName = uuid.toString() + extention;
        String filUploadFullUrl = uploadPath + "/" + savedFileName;
        FileOutputStream fos = new FileOutputStream(filUploadFullUrl);
        fos.write(fileData);
        fos.close();
        return savedFileName;
    }

    public void deleteFile(String filePath) throws Exception{
        File deleteFile = new File(filePath);

        if(deleteFile.exists()){
            deleteFile.delete();
            log.info("파일을 삭제 하였습니다.");
        }else {
            log.info("파일이 존재하지 않습니다.");
        }
    }
}
