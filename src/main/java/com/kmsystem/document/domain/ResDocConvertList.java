package com.kmsystem.document.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class ResDocConvertList {

    private int totalCount;
    private int completeCount;
    private int possibleCount;
    private List<Document> documentList;
}
