package com.kmsystem.document.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class ResDocument {

    private Integer documentId;
    private String documentOri;
    private String documentName;
    private String documentType;
    private String resultType;
    private Integer memberId;
    private String documentResult;
    private Integer categoryId;
    private String categoryName;
    private String categoryType;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastChgDt;
//    @JsonFormat(pattern="HH:mm:sssss")
    private LocalDateTime useTime;

    //파일 정보
    private Integer fileId;
    private String fileNm;
    private String fileTrNm;
    private String fileUrl;
    private long fileSize;

    //ocr이미지경로
    private List<String> ocrPath;

    private List<String> ocrBinary;
}

