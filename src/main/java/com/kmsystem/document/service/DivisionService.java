package com.kmsystem.document.service;

import com.kmsystem.document.domain.*;
import com.kmsystem.document.mapper.DivisionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@RequiredArgsConstructor
@Service
public class DivisionService {

    private final DivisionMapper divisionMapper;

    public ResDivisionList readDivisionList(Integer memberId)throws Exception{
        ResDivisionList resDivisionList = new ResDivisionList();
        List<ResCategoryList> resCategoryList = divisionMapper.readDivisionList(memberId);
        int totalCount = divisionMapper.countTotalCount(memberId);
        int completeCount = 0;
        for (ResCategoryList resCategory : resCategoryList){
            completeCount += resCategory.getDocCount();
        }

        resDivisionList.setTotalCount(totalCount);
        resDivisionList.setCompleteCount(completeCount);
        resDivisionList.setPossibleCount(totalCount - completeCount);
        resCategoryList.removeIf(resCategoryList1 -> resCategoryList1.getDocCount() == 0);
        resDivisionList.setResCategoryList(resCategoryList);
        return resDivisionList;
    }

    public List<ResDocument> readDivisionPossibleList(Integer memberId)throws Exception{
        return divisionMapper.readDivisionPossibleList(memberId);
    }

    public void updateDocumentCategory(Integer documentId, String categoryType, LocalDateTime useTime)throws Exception{
        divisionMapper.updateDocumentCategory(documentId,categoryType,useTime);
    }

    public List<ResDocResultList> readDocResultList(Integer memberId)throws Exception{
        return divisionMapper.readDocResultList(memberId);
    }

    public void updateDocumentCategoryId(Integer documentId, Integer categoryId) throws Exception{
        divisionMapper.updateDocumentCategoryId(documentId,categoryId);
    }
}
