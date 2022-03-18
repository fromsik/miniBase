package com.kmsystem.document.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class Document {

    private Integer documentId;
    private String documentOri;
    private String documentName;
    private String documentType;
    private String resultType;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastChgDt;
    private Integer memberId;
}
