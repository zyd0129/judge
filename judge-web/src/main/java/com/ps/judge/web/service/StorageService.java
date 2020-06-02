package com.ps.judge.web.service;

import com.ps.judge.web.models.StoragePath;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public interface StorageService {
    String BASE64_PATTERN = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
    StoragePath upload(InputStream inputStream, long size, String extName, Map<String, ?> metadata);
    StoragePath upload(File file, Map<String, ?> metadata) throws IOException;
    StoragePath upload(String base64, String extName, Map<String, ?> metadata) throws IOException;
    InputStream download(String path);
    void delete(String path);
    String getBaseUrl();
    default boolean isBase64(String base64String) {
        return Pattern.matches(BASE64_PATTERN, base64String);
    }

    default boolean isFullPath(String path) {
        if(Objects.isNull(path)) {
            return false;
        }
        return path.toLowerCase().startsWith("http");
    }
}
