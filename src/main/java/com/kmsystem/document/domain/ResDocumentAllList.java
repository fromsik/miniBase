package com.kmsystem.document.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ResDocumentAllList {

    private Integer documentId;
    private String documentName;
    private String documentType;
    private String resultType;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastChgDt;
    private String memName;

    private String fileNm;
    private String fileUrl;
    private long fileSize;

}
