package com.kmsystem.file.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FileUpload {
    private String fiTy; // 확장자
    private String fileNm;  // 이름
    private String fileUrl;  // 경로
    private String fileTrNm; //파일변경이름

    public FileUpload(String fiTy, String fileNm, String fileUrl, String fileTrNm) {
        this.fiTy = fiTy;
        this.fileNm = fileNm;
        this.fileUrl = fileUrl;
        this.fileTrNm = fileTrNm;
    }
}
