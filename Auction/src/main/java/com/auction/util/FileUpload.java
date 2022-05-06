package com.auction.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileUpload {
	
	public static final  String USERDIRECTORY = "images"+File.separator+"user"+File.separator+"documents";
	public static final String AUCTIONDIRECTORY = "images"+File.separator+"auction"+File.separator+"document";

	@Value(value = "${files.directory}")
	private String fileDirectory;
	
	private static Map<String, String> fileExtensionMap = new HashMap<>();
	
	public FileUpload() {
		 if(fileExtensionMap.isEmpty()) {
			  FileUpload.fileExtensionMap.put("pdf", "data:application/pdf;base64,");
			  FileUpload.fileExtensionMap.put("jpg", "data:image/jpg;base64,");
			  FileUpload.fileExtensionMap.put("png", "data:image/png;base64,");
			  FileUpload.fileExtensionMap.put("jpeg", "data:image/jpeg;base64,");
			  FileUpload.fileExtensionMap.put("JPG", "data:image/jpg;base64,");
			  
		 }
			 
	}
	
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
	
	public StringBuilder getDirectory(String fileLocation, String innerFolder, String fileType) {
		StringBuilder directory = new StringBuilder();
		directory.append(fileLocation);
		directory.append(File.separator);
		directory.append(innerFolder);
		directory.append(File.separator);
		directory.append(fileType);
	  return directory;	
	}
	
	public  Map<String, Object> encodeFileToBase64(String filePath) {
		File file = null;
	    try {
//	        URL url = new URL(filePath);
//	        URLConnection connection = url.openConnection();
//	        InputStream inputStream = connection.getInputStream();
	    	System.out.println(this.fileDirectory);
	        file = new File(this.fileDirectory+filePath);
	        byte[] fileContent = // inputStream.readAllBytes(); 
	        		Files.readAllBytes(file.toPath());
	        String base64 = Base64.getEncoder().encodeToString(fileContent);
	        String extension = this.getFileExtension(filePath);
	        Map<String, Object> map = new HashMap<>();
	        map.put("file", base64);
	        map.put("prefix", FileUpload.fileExtensionMap.get(extension));
	        return map;
	    } catch (IOException e) {
	        throw new IllegalStateException("could not read file " + file, e);
	    }
	}
	
	public String getFileExtension(String file) {
    	String extension = file.substring(file.lastIndexOf(".")+1);
    	System.out.println(extension);
    	return extension;
	}

}
