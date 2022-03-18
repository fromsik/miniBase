package com.kmsystem.file.mapper;


import com.kmsystem.file.domain.File;

public interface FileMapper {
	
	public void insertFileInfo(File file) throws Exception;
	
	public void updateFileInfo(File file) throws Exception;

	public void deleteFile(Integer fileId) throws Exception;

	public File readFileOne(Integer fileId) throws Exception;

	public File readFileOneByFileTrNm(String fileTrNm) throws Exception;
}
