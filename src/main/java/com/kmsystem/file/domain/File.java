package com.kmsystem.file.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
public class File {

    private Integer fileId;
    private Integer documentId;
    private String fileNm;
    private String fileTrNm;
    private String fileUrl;
    private long fileSize;

}

