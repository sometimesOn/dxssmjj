package com.jia.dxssmjj.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String fileUpload(MultipartFile multipartFile);
}
