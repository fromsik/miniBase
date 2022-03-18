package com.kmsystem.file.service;

import com.kmsystem.config.Properties;
import com.kmsystem.file.exception.FileStorageException;
import com.kmsystem.file.exception.MyFileNotFoundException;
import net.bytebuddy.utility.RandomString;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class FileStorageService {


    private final Path fileStorageLocation;


    @Autowired
    public FileStorageService(Properties Properties) {

        this.fileStorageLocation = Paths.get(Properties.getUploadPath()
//                                            , String.valueOf(LocalDate.now().getYear())
//                                            , String.valueOf(LocalDate.now().getMonthValue())
//                                            , String.valueOf(LocalDate.now().getDayOfMonth())
                                             )
                                    .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file) {

        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        fileName = fileName.replaceAll("\\p{Z}","_");

        //fileName 랜덤 생성
        String LocalData = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd"));

        //random 생성
        String randomKey = RandomStringUtils.randomAlphabetic(10);

        String fileTrNm = randomKey+LocalData+"_"+fileName;

        if(fileTrNm.length() > 48){
            String remove = fileTrNm.substring(40,fileTrNm.length()-5);
            fileTrNm = fileTrNm.replace(remove,"-");
        }

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileTrNm);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileTrNm;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fiTrfNm) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fiTrfNm).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fiTrfNm);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fiTrfNm, ex);
        }
    }
}