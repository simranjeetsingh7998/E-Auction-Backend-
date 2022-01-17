package com.auction.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileUpload {
	
	public final static String USERDIRECTORY="user"+File.separator+"documents";
	public final static String AUCTIONDIRECTORY = "auction"+File.separator+"document";
	
	public void uploadMultipartDocument(String filePath, String name, MultipartFile multipartFile) throws IOException { 
		 Path path = Path.of(filePath+File.separator+name);
	     createDirectory(filePath);
		 if(!Files.exists(path)) {
			  Files.createFile(path);
		 }
		 multipartFile.transferTo(path);
	}
	
	public void uploadBase64Document(String filePath, String name, String base64EcodedString) throws IOException { 
		 Path path = Path.of(filePath+File.separator+name);
		 createDirectory(filePath);
		 if(!Files.exists(path)) {
			  Files.createFile(path);
		 }
		byte[] bytes = Base64.getDecoder().decode(base64EcodedString);
		Files.write(path, bytes);
	}
	
	private void createDirectory(String directory) throws IOException {
		if(!Files.exists(Path.of(directory)))
		    Files.createDirectories(Path.of(directory));
	}

}
