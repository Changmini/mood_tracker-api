package kr.co.moodtracker.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.co.moodtracker.exception.DataNotInsertedException;
import kr.co.moodtracker.exception.DataNotUpdatedException;
import kr.co.moodtracker.handler.FileHandler;
import kr.co.moodtracker.handler.ImageHandler;
import kr.co.moodtracker.mapper.UsersMapper;
import kr.co.moodtracker.vo.ProfileVO;
import kr.co.moodtracker.vo.UserVO;

@Service
public class UserService {
	
	@Autowired
	UsersMapper userMapper;
	
	/**
	 * <pre>
	 * 	인자 값은 username과 password를 key로 하고 <br/>
	 * 	해당 key와 의미가 일치하는 적절한 value를 저장하여 넘겨주면 <br/>
	 * 	사용자 정보를 리턴해준다.
	 * </pre>
	 * @param vo
	 * @return custom ValueObject
	 */
	public UserVO getUser(Map<String, String> vo) {
		String username = vo.get("username");
		String password = vo.get("password");
		boolean emptyUsername = !(username != null && !username.equals("")); 
		boolean emptyPassword = !(password != null && !password.equals("")); 
		if (emptyUsername || emptyPassword) 
			return null;
		UserVO user = userMapper.getUser(vo);
		if (!(user != null && user.getUserId() > 0))
			return null;
		return user;
	}
	
	@Transactional(rollbackFor = {Exception.class})
	public void postUser(UserVO vo) throws DataNotInsertedException {
		try {
			userMapper.postUser(vo);
			userMapper.postUserProfile(vo);
		} catch (DuplicateKeyException e) {
			String err = e.getMessage();
			String msg = null;
			if (err.contains("users.username")) {
				msg = "사용할 수 없는 아이디입니다.";
			} else if (err.contains("user_profile.nickname")) {
				msg = "사용할 수 없는 닉네임입니다.";
			}
			throw new DataNotInsertedException(msg);
		} catch (Exception e1) {
			e1.printStackTrace();
			throw new DataNotInsertedException("새 계정 생성에 실패했습니다.");
		}
	}
	
	public ProfileVO getUserProfile(int userId) {
		ProfileVO profile = userMapper.getUserProfile(userId);
		/* 프로필 사진 경로에 대한 전처리 */
		String imagePath = profile.getImagePath();
		if (imagePath != null && !imagePath.equals("")) {
			imagePath = ImageHandler.pathToBase64(
					imagePath.replace(FileHandler.rootPath(), ""));
			profile.setImagePath(imagePath);
		}
		return profile;
	}

	public void patchUserProfile(int userId, UserVO vo) 
			throws DataNotUpdatedException {
		vo.setUserId(userId);
		int cnt = userMapper.patchUserProfile(vo);
		if (cnt == 0) {
			throw new DataNotUpdatedException("프로필 업데이트 실패");
		}
	}
	
	public String putUserProfileImage(MultipartFile file, int userId, UserVO vo) 
			throws DataNotUpdatedException, IllegalStateException, IOException {
		vo.setUserId(userId);
		String base64 = null;
		if (file != null && !file.isEmpty()) {
			String filepath = FileHandler.saveFile(file, userId, "profile");
			vo.setImagePath(filepath);
			base64 = ImageHandler.pathToBase64(filepath);
		}
		int cnt = userMapper.putUserProfileImage(vo);
		if (cnt == 0) {
			throw new DataNotUpdatedException("프로필 사진 업데이트 실패");
		}
		return base64;
	}
	
}
