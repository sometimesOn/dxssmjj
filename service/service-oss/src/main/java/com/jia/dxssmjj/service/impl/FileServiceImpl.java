package com.jia.dxssmjj.service.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.jia.dxssmjj.service.FileService;
import com.jia.dxssmjj.utils.FilePropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public String fileUpload(MultipartFile multipartFile) {
        String endpoint = FilePropertiesUtils.ENDPOINT;
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = FilePropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = FilePropertiesUtils.SECRET;
        // 填写Bucket名称，例如examplebucket。
        String bucketName = FilePropertiesUtils.BUCKET_NAME;
        // 填写Object完整路径，例如exampledir/exampleobject.txt。Object完整路径中不能包含Bucket名称。
        String fileName =  new DateTime().toString("yyyy/MM/dd") + "/" +
                UUID.randomUUID() + multipartFile.getOriginalFilename();

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        InputStream inputStream = null;
        try {
            inputStream = multipartFile.getInputStream();
            ossClient.putObject(bucketName,fileName,inputStream);
            ossClient.shutdown();
            String url = "https://" + bucketName + "." + endpoint + "/" + fileName;
            return url;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
