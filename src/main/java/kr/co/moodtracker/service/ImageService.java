package kr.co.moodtracker.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.stereotype.Service;

import kr.co.moodtracker.exception.ImageLoadException;
import kr.co.moodtracker.vo.UserVO;

@Service
public class ImageService {

	public byte[] getImage(String base64, UserVO user) 
			throws ImageLoadException, IOException {
		String imagePath = ImageHandler.base64ToPath(base64);
		String[] pathArr = imagePath.split("\\/");
		if (pathArr.length < 4)
			throw new ImageLoadException("올바르지 않은 파일경로입니다.");
		if (user.getUserId() != Integer.valueOf(pathArr[2]))
			throw new ImageLoadException("접근 불가능한 사용자입니다.");
		try (FileInputStream fis = new FileInputStream(
				new File(FileHandler.rootPath() + imagePath))) {
			return fis.readAllBytes();
		}
	}
	
}
