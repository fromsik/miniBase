package com.kmsystem.document.mapper;

import com.kmsystem.document.domain.*;

import java.util.List;

public interface DocumentMapper {

    public void createDocument(Document document)throws Exception;

    public List<ResDocumentList> readDocumentList(Integer memberId)throws Exception;

    public List<ResDocumentAllList> readDocumentAllList(Integer memberId)throws Exception;

    public List<Document> readDocConvertList(Integer memberId, String resultType)throws Exception;

    public List<ResDocUploadList> readDocUploadList(Integer memberId, String resultType)throws Exception;

    public void createDocumentResult(DocumentResult documentResult)throws Exception;

    public ResDocument readDocument(Integer documentId)throws Exception;

    public void deleteDocument(Integer documentId)throws Exception;

    public ResDocument readDocumentByFileTrNm(String fileTrNm)throws Exception;
}
