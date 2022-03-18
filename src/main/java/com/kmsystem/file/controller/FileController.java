package com.kmsystem.file.controller;

import com.kmsystem.file.domain.File;
import com.kmsystem.file.domain.FileUpload;
import com.kmsystem.file.service.FileService;
import com.kmsystem.file.service.FileStorageService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/file")

public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private FileService fileService;

    @ApiOperation("파일 업로드")
    @PostMapping("/uploadfile")
    public FileUpload uploadFile(@RequestParam("file") MultipartFile file) {

        String fileName = file.getOriginalFilename();
        fileName = fileName.replaceAll("\\p{Z}","_");
        String fileTrNm = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/file/downloadfile/")
                .path(fileTrNm)
                .toUriString();
        fileDownloadUri = URLDecoder.decode(fileDownloadUri);

        logger.info(fileDownloadUri);
        logger.info(file.toString());

        return new FileUpload(fileTrNm.replaceAll("^.*\\.(.*)$", "$1"), fileName, fileDownloadUri, fileTrNm);
    }

    @ApiOperation("파일 정보_insert")
    @PostMapping("/uploadfileinfo")
    public ResponseEntity<Void> uploadFileInfo(@RequestBody File file) throws Exception {

        logger.info(file.toString());
        fileService.insertFileInfo(file);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/uploadmultiplefiles")
    public List<FileUpload> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file))
                .collect(Collectors.toList());
    }

    @ApiOperation("파일 다운로드")
    @GetMapping("/downloadfile/{fileTrNm:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileTrNm, HttpServletRequest request) throws Exception {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileTrNm);
        System.out.println(resource);
        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        File file = fileService.readFileOneByFiTrfNm(URLDecoder.decode(resource.getFilename()));
        String fileNm = URLEncoder.encode(file.getFileNm(), "UTF-8");

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileNm + "\"")
                .body(resource);
    }

    @ApiOperation("파일 수정")
    @PostMapping("/updatefile")
    public ResponseEntity<File> updateFile(@RequestBody File file) throws Exception {

        logger.info(file.toString());
        file = fileService.updateFileInfo(file);

        return new ResponseEntity<>(file,HttpStatus.OK);
    }

    @ApiOperation("파일 삭제")
    @PostMapping("/deletefile/{afId}")
    public ResponseEntity<Void> deleteFile(@PathVariable Integer afId) throws Exception {

        fileService.deleteFile(afId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}