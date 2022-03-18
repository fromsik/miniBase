package com.kmsystem.document.service;

import com.kmsystem.document.domain.*;
import com.kmsystem.document.mapper.DocumentMapper;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class DocumentService {

    private final DocumentMapper documentMapper;

    @Transactional
	public void createDocument(Document document)throws Exception{
		 documentMapper.createDocument(document);
	}

	public List<ResDocumentAllList> readDocumentAllList(Integer memberId)throws Exception{
		return documentMapper.readDocumentAllList(memberId);
	}

	public List<ResDocumentList> readDocumentList(Integer memberId)throws Exception{
		return documentMapper.readDocumentList(memberId);
	}

	public ResDocConvertList readDocConvertList(Integer memberId, String resultType)throws Exception{

		List<Document> documentList = documentMapper.readDocConvertList(memberId, resultType);
		int totalCount = documentList.size();
		int completeCount = (int)documentList.stream().filter(document -> document.getDocumentId() != null).count();
		int possibleCount = totalCount - completeCount;
		documentList = documentList.stream()
				.filter(document -> document.getDocumentId() != null)
				.sorted(Comparator.comparing(Document::getLastChgDt).reversed())
				.collect(Collectors.toList());

		ResDocConvertList resDocConvertList = new ResDocConvertList();
		resDocConvertList.setDocumentList(documentList);
		resDocConvertList.setTotalCount(totalCount);
		resDocConvertList.setCompleteCount(completeCount);
		resDocConvertList.setPossibleCount(possibleCount);
		return resDocConvertList;
	}

	public List<ResDocUploadList> readDocUploadList(Integer memberId, String resultType)throws Exception{
		return documentMapper.readDocUploadList(memberId,resultType);
	}

	public void createDocumentResult(DocumentResult documentResult)throws Exception{
		documentMapper.createDocumentResult(documentResult);
	}

	public ResDocument readDocument(Integer documentId)throws Exception{
		ResDocument resDocument = documentMapper.readDocument(documentId);
		//OCR 이미지 파일 경로 & 이미지 base64변환
		if(resDocument.getResultType().equals("I")){
			List<String> pathList = new ArrayList<>();
			List<String> ocrBinary = new ArrayList<>();
			String fileTrNm = resDocument.getFileTrNm();
			String fileName = fileTrNm.substring(0,fileTrNm.lastIndexOf("."));
			java.io.File folder = new java.io.File("/predictions/deep-text");
			if(folder.exists()){
				java.io.File[] folderList = folder.listFiles();
				for (int j = 0; j < Objects.requireNonNull(folderList).length; j++) {
					if(folderList[j].getName().contains(fileName)){
						pathList.add(folderList[j].getName());
						String binary = fileToBinary(folderList[j]);
						ocrBinary.add(binary);
					}
				}
				pathList.sort(Comparator.naturalOrder());
			}
			resDocument.setOcrPath(pathList);
			resDocument.setOcrBinary(ocrBinary);
		}
		return resDocument;
	}

	public void deleteDocument(Integer documentId)throws Exception{
		documentMapper.deleteDocument(documentId);
	}

	public static String fileToBinary(File file) {
		String out = new String();
		FileInputStream fis = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			System.out.println("Exception position : FileUtil - fileToString(File file)");
		}

		int len = 0;
		byte[] buf = new byte[1024];
		try {
			while ((len = fis.read(buf)) != -1) {
				baos.write(buf, 0, len);
			}

			byte[] fileArray = baos.toByteArray();
			out = new String(base64Enc(fileArray));

			fis.close();
			baos.close();
		} catch (IOException e) {
			System.out.println("Exception position : FileUtil - fileToString(File file)");
		}

		return out;
	}

	public static byte[] base64Enc(byte[] buffer) {
		return Base64.encodeBase64(buffer);
	}
}
