package kr.co.moodtracker.handler;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemException;

import org.springframework.web.multipart.MultipartFile;

public class FileHandler {
	public static final boolean IS_WINDOWS = System.getProperty("os.name")
			.toLowerCase().contains("win");
	private static final String DIRECTORY = IS_WINDOWS ? "C:/data" : "/data";
	
	public static String rootPath() { return DIRECTORY; }
	
	public static File makeDirectory(String targetPath) throws FileSystemException{
		File f = null;
		if (targetPath.startsWith("/"))
			f = new File(DIRECTORY + targetPath);			
		else
			f = new File(DIRECTORY + "/" + targetPath);
		if (!f.isDirectory() && !f.mkdirs()) { 
			throw new FileSystemException("부모 디렉토리 생성에 실패했습니다.");
		}
		return f;
	}
	
	public static String saveFile(MultipartFile file, int userId) 
			throws IllegalStateException, IOException {
		return saveFile(file, userId, DateHandler.today());
	}
	public static String saveFile(MultipartFile file, int userId, String basicFolder) 
			throws IllegalStateException, IOException {
		String filename = removeExtension(file.getOriginalFilename());
		if (filename.equals("")) 
			return null;
		String targetPath = basicFolder 
				+ "/" + userId
				+ "/" + System.currentTimeMillis()
				+ "_" + filename
				+".png";
		File f = FileHandler.makeDirectory(targetPath);
		file.transferTo(f);
		return f.getAbsolutePath().replaceAll("\\\\", "/");
	}
	
	public static String removeExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf('.');
        if (lastIndex != -1) {
            fileName = fileName.substring(0, lastIndex);
        }
        return fileName;
    }
	
}
