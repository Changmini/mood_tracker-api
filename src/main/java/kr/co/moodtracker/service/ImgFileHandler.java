package kr.co.moodtracker.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import kr.co.moodtracker.vo.DailyInfoVO;
import kr.co.moodtracker.vo.ImageVO;

public class ImgFileHandler {
	private static final String OS = System.getProperty("os.name")
			.toLowerCase().contains("win") ? "C:" : ""; 
	private static final String DIRECTORY = "/data";
	
	public static String rootPath() { return (OS + DIRECTORY); }
	
	public static File makeDirectory(String targetPath) throws FileSystemException{
		File f = null;
		if (targetPath.startsWith("/"))
			f = new File(OS + DIRECTORY + targetPath);			
		else
			f = new File(OS + DIRECTORY + "/" + targetPath);
		if (!f.isDirectory() && !f.mkdirs()) { 
			throw new FileSystemException("부모 디렉토리 생성에 실패했습니다.");
		}
		return f;
	}
	
	public static String saveFile(MultipartFile file, int userId) 
			throws IllegalStateException, IOException {
		String filename = file.getOriginalFilename();
		if (filename.equals("")) 
			return "";
		String targetPath = DateHandler.today() 
				+ "/" + userId
				+ "/" + System.currentTimeMillis()
				+ "_" + filename
				+".png";
		File f = ImgFileHandler.makeDirectory(targetPath);
		file.transferTo(f);
		return f.getAbsolutePath();
	}
	
	public static String pathToBase64(String path) {
		if (path == null) 
			return path;
		return  Base64.getEncoder().encodeToString(path.getBytes());
	}
	
	public static String base64ToPath(String base64) {
		if (base64 == null) 
			return base64;
		byte[] byteArr = Base64.getDecoder().decode(base64);
		return new String(byteArr);
	}
}
