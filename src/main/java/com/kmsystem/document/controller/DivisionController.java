package com.kmsystem.document.controller;

import com.kmsystem.common.security.domain.CustomUser;
import com.kmsystem.config.Properties;
import com.kmsystem.document.domain.*;
import com.kmsystem.document.mapper.DocumentMapper;
import com.kmsystem.document.service.CategoryService;
import com.kmsystem.document.service.DivisionService;
import com.kmsystem.document.service.DocumentService;
import com.kmsystem.file.controller.FileController;
import com.kmsystem.file.domain.File;
import com.kmsystem.file.domain.FileUpload;
import com.kmsystem.util.cmd.ShellService;
import com.kmsystem.util.csv.CsvService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/division")
public class DivisionController {

    @Autowired
    private FileController fileController;

    @Autowired
    private Properties properties;

    private final DivisionService divisionService;

    private final CategoryService categoryService;

    private final DocumentMapper documentMapper;

    private final CsvService csvService;

    private final ShellService shellService;

    @ApiOperation("문서분류관리_목록")
    @GetMapping()
    public ResponseEntity<ResDivisionList> readDocDivisionList(@AuthenticationPrincipal CustomUser customUser) throws Exception {
        ResDivisionList resDivisionList = divisionService.readDivisionList(customUser.getMemberId());
        return new ResponseEntity<>(resDivisionList, HttpStatus.OK);
    }

    @ApiOperation("문서분류실행")
    @GetMapping("/type")
    public ResponseEntity<Map<String,Object>> docDivision(@AuthenticationPrincipal CustomUser customUser) throws Exception {

        Map<String,Object> resultMap = new HashMap<>();
        //분류가능 목록
        List<ResDocument> resDocumentList = divisionService.readDivisionPossibleList(customUser.getMemberId());

        if(!resDocumentList.isEmpty()) {
            //분류를 위한 csv생성
            new FileOutputStream(properties.getUploadPath() + "/csv/send.csv").close(); //csv초기화
            List<String[]> csvList = new ArrayList<>();
            csvList.add(new String[]{"fileName", "text"});
            for (ResDocument resDocument : resDocumentList) {
                String documentResult = resDocument.getDocumentResult();
                documentResult = documentResult.replace(System.getProperty("line.separator").toString(), " ");
                documentResult = documentResult.replaceAll("\"", " ");
                csvList.add(new String[]{resDocument.getFileTrNm(), documentResult});
            }
            csvService.writeCSV(csvList);

            List<ResDocument> documentList = new ArrayList<>();
            //모듈 실행
            List<String> cmdList = shellService.shellCmd("python3 /home/python_file/main/main.py -m predict");

            for (int i = 0; i < cmdList.size(); i++) {
                String[] array = cmdList.get(i).split(" ");
                if (i != (cmdList.size() - 1) &&
                         resDocumentList.stream().anyMatch(resDocument -> resDocument.getFileTrNm().equals(array[0]))) {
                         ResDocument resDocument = documentMapper.readDocumentByFileTrNm(array[0]);
                         divisionService.updateDocumentCategory(resDocument.getDocumentId(), array[1], null);
                         resDocument.setDocumentType(array[1]);
                         resDocument.setCategoryType(array[1]);
                         resDocument.setCategoryName(categoryService.readCategoryByCategoryType(array[1]).getCategoryName());
                         documentList.add(resDocument);
                } else if(i == (cmdList.size() - 1)){//total time
                    try {
                        double checkDouble = Double.parseDouble(array[3]);
                        resultMap.put("totalTime", array[3]);
                    }catch (Exception e){
                        resultMap.put("totalTime", null);
                    }
                }
            }
            resultMap.put("documentList", documentList);
        }

        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    @ApiOperation("학습관리_목록")
    @GetMapping("/result")
    public ResponseEntity<List<ResDocResultList>> readDocResultList(@AuthenticationPrincipal CustomUser customUser) throws Exception {

        List<ResDocResultList> resDocResultList = divisionService.readDocResultList(customUser.getMemberId());
        System.out.println(resDocResultList);
        return new ResponseEntity<>(resDocResultList, HttpStatus.CREATED);
    }

    @ApiOperation("학습관리_분류수정")
    @GetMapping("/result/{documentId}/{categoryId}")
    public ResponseEntity<Void> updateDocumentCategoryId(@PathVariable("documentId") Integer documentId,
                                                          @PathVariable("categoryId") Integer categoryId) throws Exception {

        divisionService.updateDocumentCategoryId(documentId, categoryId);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


}
