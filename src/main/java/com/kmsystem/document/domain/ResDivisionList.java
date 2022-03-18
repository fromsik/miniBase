package com.kmsystem.document.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
@Getter
@Setter
@ToString
public class ResDivisionList {
        private int totalCount;
        private int completeCount;
        private int possibleCount;
        private List<ResCategoryList> resCategoryList;
}
