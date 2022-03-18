package com.kmsystem.document.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ResDocResultList {

    private Integer documentId;
    private String documentName;
    private String documentType;
    private Integer categoryId;
    private String categoryName;
    private String categoryType;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastChgDt;
}

