package com.kmsystem.document.controller;

import com.kmsystem.common.security.domain.CustomUser;
import com.kmsystem.config.Properties;
import com.kmsystem.document.domain.*;
import com.kmsystem.document.service.DocumentService;
import com.kmsystem.util.cmd.ShellService;
import com.kmsystem.file.controller.FileController;
import com.kmsystem.file.domain.File;
import com.kmsystem.file.domain.FileUpload;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/document")
public class DocumentController {

    @Autowired
    private FileController fileController;
    @Autowired
    private Properties properties;

    private final DocumentService documentService;
    private final ShellService shellService;

    @ApiOperation("문서수집_문서등록(업로드)")
    @PostMapping()
    public ResponseEntity<Void> createDocument(
            @AuthenticationPrincipal CustomUser customUser,
            MultipartFile[] files) throws Exception {

        for (MultipartFile file : files) {
            String fileExtension = file.getOriginalFilename().replaceAll("^.*\\.(.*)$", "$1");
            Document document = new Document();

            /* file 정보, 서버저장 */
            if (fileExtension.equals("docx") ||
                    fileExtension.equals("hwp") ||
                    fileExtension.equals("xlsx") ||
                    fileExtension.equals("xls")) { //택스트 파일
                document.setResultType("T");
            } else if (fileExtension.equals("jpg") ||
                    fileExtension.equals("jpeg") ||
                    fileExtension.equals("png") ||
                    fileExtension.equals("pdf")) { //이미지 파일
                document.setResultType("I");
            } else {
                document.setResultType("X");
            }
            if (!document.getResultType().equals("X")) {

                FileUpload fileUpload = fileController.uploadFile(file);
                /* 문서생성 */
                document.setMemberId(customUser.getMemberId());
                document.setDocumentOri(null); // html 변환 파일 모듈 추가하기 #####
                document.setDocumentName(fileUpload.getFileNm());
                document.setDocumentType(fileUpload.getFiTy());
                documentService.createDocument(document);

                /* file 정보저장*/
                File fi = File.builder()
                        .documentId(document.getDocumentId())
                        .fileNm(fileUpload.getFileNm())
                        .fileUrl(fileUpload.getFileUrl())
                        .fileTrNm(fileUpload.getFileTrNm())
                        .fileSize(file.getSize())
                        .build();
                fileController.uploadFileInfo(fi);
            }
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation("문서수집_목록")
    @GetMapping("/list")
    public ResponseEntity<List<ResDocumentAllList>> readDocumentAllList(@AuthenticationPrincipal CustomUser customUser) throws Exception {
        List<ResDocumentAllList> resDocumentList = documentService.readDocumentAllList(customUser.getMemberId());
        return new ResponseEntity<>(resDocumentList, HttpStatus.OK);
    }

    @ApiOperation("문서수집_목록")
    @GetMapping()
    @ApiIgnore
    public ResponseEntity<List<ResDocumentList>> readDocumentList(@AuthenticationPrincipal CustomUser customUser) throws Exception {
        List<ResDocumentList> resDocumentList = documentService.readDocumentList(customUser.getMemberId());
        return new ResponseEntity<>(resDocumentList, HttpStatus.OK);
    }

    @ApiOperation("Parsing_목록")
    @GetMapping("/parsing")
    public ResponseEntity<ResDocConvertList> readDocParsingList(@AuthenticationPrincipal CustomUser customUser) throws Exception {
        ResDocConvertList resDocConvertList = documentService.readDocConvertList(customUser.getMemberId(), "T");
        return new ResponseEntity<>(resDocConvertList, HttpStatus.OK);
    }

    @ApiOperation("OCR_목록")
    @GetMapping("/ocr")
    public ResponseEntity<ResDocConvertList> readDocOcrList(@AuthenticationPrincipal CustomUser customUser) throws Exception {
        ResDocConvertList resDocConvertList = documentService.readDocConvertList(customUser.getMemberId(), "I");
        return new ResponseEntity<>(resDocConvertList, HttpStatus.OK);
    }

    @ApiOperation("Parsing업로드_목록")
    @GetMapping("/parsing/upload")
    public ResponseEntity<List<ResDocUploadList>> readDocParsingUploadList(@AuthenticationPrincipal CustomUser customUser) throws Exception {
        List<ResDocUploadList> resDocUploadList = documentService.readDocUploadList(customUser.getMemberId(), "T");
        return new ResponseEntity<>(resDocUploadList, HttpStatus.OK);
    }

    @ApiOperation("OCR업로드_목록")
    @GetMapping("/ocr/upload")
    public ResponseEntity<List<ResDocUploadList>> readDocOcrUploadList(@AuthenticationPrincipal CustomUser customUser) throws Exception {
        List<ResDocUploadList> resDocUploadList = documentService.readDocUploadList(customUser.getMemberId(), "I");
        return new ResponseEntity<>(resDocUploadList, HttpStatus.OK);
    }

    @ApiOperation("문서변환(parsing,ocr)_등록")
    @PostMapping("/upload")
    public ResponseEntity<Void> createDocumentConvertParsing(@RequestBody Map<String, List<Integer>> map,@AuthenticationPrincipal CustomUser customUser) throws Exception {
        for (Integer documentId : map.get("documentIdList")) {
            ResDocument resDocument = documentService.readDocument(documentId);
            String documentPath = properties.getUploadPath() + "/" + resDocument.getFileTrNm();

            //문서변환 모듈
            String text = "";
            if (resDocument.getResultType().equals("T")) { //parsing 모듈(txt)
                List<String> msg = shellService.shellCmd("python3 /home/python_file/parsing.py " + documentPath);
                try {
                    List<String> result = Files.readAllLines(Paths.get(documentPath + "2txt.txt"));
                    for (String line : result) {
                        text += (line + System.getProperty("line.separator")); //개행
                    }

                } catch (Exception e) {
                    //throw new IOException("ERROR : " + msg);
                }
            } else if (resDocument.getResultType().equals("I")) { //OCR 모듈(image) -현재 폴더만 가능 /upload/ocr으로 사용

            }

            if (!text.equals("")) {
                DocumentResult documentResult = new DocumentResult();
                documentResult.setDocumentId(documentId);
                documentResult.setDocumentResult(text);
                documentService.createDocumentResult(documentResult);
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("문서변환(ocr)_등록")
    @GetMapping("/upload/ocr")
    public ResponseEntity<Void> createDocumentConvertOCR(@AuthenticationPrincipal CustomUser customUser) throws Exception {
        // /Users/upload/pdf 폴더 초기화
        java.io.File folder = new java.io.File("/Users/upload/pdf");
        try {
            if(folder.exists()){
                java.io.File[] folderList = folder.listFiles();
                for (int j = 0; j < folderList.length; j++) {
                    folderList[j].delete(); //파일 삭제
                }
            }
        }catch (Exception e){
            e.getStackTrace();
        }
       // OCR 대상 파일 복사
        List<ResDocUploadList> resDocUploadList = documentService.readDocUploadList(customUser.getMemberId(), "I");
        for(ResDocUploadList resDocument : resDocUploadList){
            shellService.shellCmd("cp /Users/upload/"+resDocument.getFileTrNm()+" /Users/upload/pdf/"+resDocument.getFileTrNm());
        }
        //OCR
        shellService.shellCmd("python3 /home/python_file/main_ocr/main/main.py --mode deep-text --target /Users/upload/pdf");

        //결과 얻기
        for(ResDocUploadList resDocument : resDocUploadList) {
            String fileTrNm = resDocument.getFileTrNm();
            String fileName = fileTrNm.substring(0,fileTrNm.lastIndexOf("."));
            System.out.println("fileName :"+fileName);

            //front폴더로 파일 copy
/*            folder = new java.io.File("/predictions/deep-text");
            if(folder.exists()){
                java.io.File[] folderList = folder.listFiles();
                for (int j = 0; j < Objects.requireNonNull(folderList).length; j++) {
                    if(folderList[j].getName().contains(fileName)){
                        shellService.shellCmd("cp "+folderList[j]+" /root/jenkins/workspace/kms-front/src/images/"+folderList[j].getName());
                    }
                }
            }*/

            List<String> result = Files.readAllLines(Paths.get("/predictions/json/" + fileName + ".json"));
            String text = "";
            for (String str : result) {
                if (str.contains("\"texts\": ")) {
                    str = str.substring(22,str.length()-2);
                    text += (str + System.getProperty("line.separator")); //개행
                }
            }
            if (!text.equals("")) {
                DocumentResult documentResult = new DocumentResult();
                documentResult.setDocumentId(resDocument.getDocumentId());
                documentResult.setDocumentResult(text);
                documentService.createDocumentResult(documentResult);
            }
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("문서결과_상세")
    @GetMapping("/{documentId}")
    public ResponseEntity<ResDocument> readDocumentResult(@PathVariable("documentId") Integer documentId) throws Exception {
        ResDocument resDocument = documentService.readDocument(documentId);
        return new ResponseEntity<>(resDocument, HttpStatus.OK);
    }

    @ApiOperation("문서수집_삭제")
    @PostMapping("/delete")
    public ResponseEntity<Void> deleteDocument(@RequestBody Map<String, List<Integer>> map) throws Exception {
        for (Integer documentId : map.get("documentIdList")) {
            documentService.deleteDocument(documentId);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
