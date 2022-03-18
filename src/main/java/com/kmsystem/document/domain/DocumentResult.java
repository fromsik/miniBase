package com.kmsystem.document.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class DocumentResult {

    private Integer documentId;
    private String documentResult;
    private Integer categoryId;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastChgDt;
    @JsonFormat(pattern="HH:mm:sssss")
    private LocalDateTime useTime;
}
