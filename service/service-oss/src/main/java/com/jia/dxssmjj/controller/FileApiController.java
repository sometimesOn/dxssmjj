package com.jia.dxssmjj.controller;

import com.jia.dxssmjj.common.result.Result;
import com.jia.dxssmjj.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
@Slf4j
@Api(value = "文件接口",tags = "文件接口")
@RequestMapping("/oss/api")
public class FileApiController {

    @Autowired
    private FileService fileService;

    @PostMapping("fileUpload")
    @ApiOperation("上传文件")
    public Result fileUpload(@RequestParam("file") MultipartFile file){
        //获取上传文件
        String url = fileService.fileUpload(file);
        return Result.ok(url);
    }
}
