package com.kmsystem.file.service;

import com.kmsystem.file.domain.File;
import com.kmsystem.file.mapper.FileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FileService  {
	
	private final FileMapper fileMapper;

	public void insertFileInfo(File file)throws Exception{
		fileMapper.insertFileInfo(file);
	}

	public File updateFileInfo(File file) throws Exception {
			fileMapper.updateFileInfo(file);
			return fileMapper.readFileOne(file.getFileId());
	}

	public void deleteFile(Integer fileId) throws Exception {
		fileMapper.deleteFile(fileId);
	}

	public File readFileOne(Integer fileId) throws Exception {
		return fileMapper.readFileOne(fileId);
	}

	public File readFileOneByFiTrfNm(String fileTrNm) throws Exception {
		return fileMapper.readFileOneByFileTrNm(fileTrNm);
	}


}
