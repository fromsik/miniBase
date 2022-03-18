package com.kmsystem.document.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ResCategoryList {
        private Integer categoryId;
        private String categoryType;
        private String categoryName;
        private int docCount;
}
