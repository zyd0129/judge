package com.ps.judge.web.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectResult;
import com.ps.judge.web.config.JudgeWebProperties;
import com.ps.judge.web.models.StoragePath;
import com.ps.judge.web.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
public class OssStorageServiceImpl implements StorageService {
    @Autowired
    private JudgeWebProperties properties;
    @Autowired
    private OSS client;

    @Override
    public StoragePath upload(InputStream inputStream, long size, String extName, Map<String, ?> metadata) {
        this.checkBucket();
        String key = this.getKey(extName);
        PutObjectResult result = this.client.putObject(this.properties.getBucketName(), key, inputStream);
        return this.getStoragePath(result, key);
    }

    @Override
    public StoragePath upload(File file, Map<String, ?> metadata) throws IOException {
        if (Objects.isNull(file) || !file.exists() || !file.isFile()) {
            throw new FileNotFoundException("storage file not found or is a invalid file.");
        }
        String extName = "";
        String[] array = file.getName().split("\\.");
        if (array.length > 1) {
            extName = array[array.length - 1];
        }
        this.checkBucket();
        String key = this.getKey(extName);
        PutObjectResult result = this.client.putObject(this.properties.getBucketName(), key, file);
        return this.getStoragePath(result, key);
    }

    @Override
    public StoragePath upload(String base64, String extName, Map<String, ?> metadata) throws IOException {
        if (!this.isBase64(base64)) {
            throw new RuntimeException("storage content is not base64 format.");
        }
        byte[] content = Base64.getDecoder().decode(base64.getBytes(UTF_8));
        try (InputStream inputStream = new ByteArrayInputStream(content)) {
            this.checkBucket();
            String key = this.getKey(extName);
            PutObjectResult result = this.client.putObject(this.properties.getBucketName(), key, inputStream);
            return this.getStoragePath(result, key);
        }
    }

    @Override
    public InputStream download(String path) {
        if (Objects.isNull(path)) {
            throw new RuntimeException("file path must be not empty.");
        }

        GetObjectRequest request = new GetObjectRequest(this.properties.getBucketName(), path);
        if (isFullPath(path)) {
            try {
                request = new GetObjectRequest(new URL(path), null);
            } catch (MalformedURLException e) {
                log.error("download file[{}] error:", path, e);
                throw new RuntimeException("download file[" + path + "] error.", e);
            }
        }

        OSSObject ossObject = this.client.getObject(request);
        if (Objects.isNull(ossObject) || Objects.isNull(ossObject.getObjectContent())) {
            throw new RuntimeException("file[" + path + "] not found.");
        }
        return ossObject.getObjectContent();
    }

    @Override
    public void delete(String path) {
        this.client.deleteObject(this.properties.getBucketName(), path);
    }

    @Override
    public String getBaseUrl() {
        return this.properties.getBaseUrl();
    }

    private void checkBucket() {
        if (!this.client.doesBucketExist(this.properties.getBucketName())) {
            this.client.createBucket(this.properties.getBucketName());
            this.client.setBucketAcl(this.properties.getBucketName(), CannedAccessControlList.PublicReadWrite);
        }
    }

    private String getKey(String extName) {
        if (Objects.isNull(extName)) {
            extName = "";
        }
        if (!extName.startsWith(".")) {
            extName = "." + extName;
        }
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return date + "/" + System.currentTimeMillis() + extName;
    }

    private StoragePath getStoragePath(PutObjectResult result, String key) {
        if (Objects.isNull(result)) {
            throw new RuntimeException("upload to alicloud oss storage failed.");
        }
        URL url = this.client.generatePresignedUrl(this.properties.getBucketName(), key,
                new Date(System.currentTimeMillis() + this.properties.getExpiration()));
        StoragePath storagePath = new StoragePath();
        storagePath.setUrl(url.toString());
        storagePath.setHost(url.getHost());
        storagePath.setFile(url.getFile());
        storagePath.setPath(key);
//        if(url.getPath().startsWith("/")) {
//            storagePath.setPath(url.getPath().substring(1));
//        } else {
//            storagePath.setPath(url.getPath());
//        }
        return storagePath;
    }
}
