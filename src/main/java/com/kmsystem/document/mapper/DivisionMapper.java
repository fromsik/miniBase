package com.kmsystem.document.mapper;

import com.kmsystem.document.domain.*;

import java.time.LocalDateTime;
import java.util.List;

public interface DivisionMapper {
    public List<ResCategoryList> readDivisionList(Integer memberId)throws Exception;

    public int countTotalCount(Integer memberId)throws Exception;

    public List<ResDocument> readDivisionPossibleList(Integer memberId)throws Exception;

    public void updateDocumentCategory(Integer documentId, String categoryType, LocalDateTime useTime) throws Exception;

    public List<ResDocResultList> readDocResultList(Integer memberId)throws Exception;

    public void updateDocumentCategoryId(Integer documentId, Integer categoryId) throws Exception;
}
