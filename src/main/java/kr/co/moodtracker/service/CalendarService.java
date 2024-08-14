package kr.co.moodtracker.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.co.moodtracker.exception.DataMissingException;
import kr.co.moodtracker.exception.DataNotInsertedException;
import kr.co.moodtracker.mapper.DailiesMapper;
import kr.co.moodtracker.mapper.ImagesMapper;
import kr.co.moodtracker.mapper.MoodsMapper;
import kr.co.moodtracker.mapper.NotesMapper;
import kr.co.moodtracker.vo.DailyInfoVO;
import kr.co.moodtracker.vo.DailySearchVO;
import kr.co.moodtracker.vo.ImageVO;

@Service
public class CalendarService {
	
	@Autowired
	DailiesMapper dailiesMapper;
	@Autowired
	NotesMapper notesMapper;
	@Autowired
	MoodsMapper moodsMapper;
	@Autowired
	ImagesMapper imagesMapper;
	
	static final int MAX_LIST_COUNT = 10;
	
	public List<DailyInfoVO> getDailyInfoOfTheMonth(DailySearchVO vo) throws DataMissingException {
		DateHandler.determineDateRange(vo);
		List<DailyInfoVO> dailies = dailiesMapper.getDailyInfoOfTheMonth(vo);
		vo.setStartAtPosition(ImgFileHandler.rootPath().length());// 이미지 파일의 루트경로 길이 파악 
		List<DailyInfoVO> images =  imagesMapper.getImageInfoList(vo);
		/* 여기서 dailies와 images를 합치고 넘겨주자. */
		List<DailyInfoVO> dailyInfoList = DateHandler.makeDateList(vo, dailies, images);
		if (dailyInfoList.size() < 1) return Collections.emptyList();
		return dailyInfoList;
	}
	
	public List<DailyInfoVO> getDailyInfoList(DailySearchVO vo) throws DataMissingException {
		if (vo.getLimit() > 30) throw new DataMissingException("한번에 너무 많은 데이터를 요청할 수 없습니다.");
		List<DailyInfoVO> dailies = dailiesMapper.getDailyInfoList(vo);
		if (dailies.size() < 1) return Collections.emptyList();
		return dailies;
	}
	
	@Transactional(rollbackFor = { Exception.class })
	public void postDailyInfo(DailyInfoVO vo, List<MultipartFile> files) 
			throws DataNotInsertedException, IllegalStateException, IOException {
		if (files != null && files.size() > 0) {
			List<ImageVO> imageList = new ArrayList<>();
			for (MultipartFile f : files) {
				String abs = ImgFileHandler.saveFile(f, vo.getUserId());
				ImageVO ivo = new ImageVO();
				ivo.setImagePath(abs);
				imageList.add(ivo);
			}
			vo.setImageList(imageList);
		}
		/* 
		 * 이미 데이터가 존재하는지 확인하는 로직을 추가해야 한다.
		 * */
		int cnt = notesMapper.postNote(vo);
		if (cnt == 0) throw new DataNotInsertedException("입력된 텍스트 정보를 확인해주세요.");
		cnt = moodsMapper.postMood(vo);
		if (cnt == 0) throw new DataNotInsertedException("분위기 정보를 등록 실패했습니다.");
		cnt = dailiesMapper.postDaily(vo);
		if (cnt == 0) throw new DataNotInsertedException("날짜 정보를 등록 실패했습니다.");
		cnt = imagesMapper.postImage(vo);
		if (cnt == 0) throw new DataNotInsertedException("이미지 등록에 실패했습니다.");
		
	}
	
	public void patchDailyInfo(DailyInfoVO vo, List<MultipartFile> files) 
			throws DataNotInsertedException, IllegalStateException, IOException {
		if (files != null && files.size() > 0) {
			List<ImageVO> imageList = new ArrayList<>();
			for (MultipartFile f : files) {
				String abs = ImgFileHandler.saveFile(f, vo.getUserId());
				ImageVO ivo = new ImageVO();
				ivo.setAftImagePath(abs);
				imageList.add(ivo);
			}
			vo.setImageList(imageList);
		}
		notesMapper.patchNote(vo);
		moodsMapper.patchMood(vo);
		imagesMapper.patchImage(vo);
	}
	
	@Transactional(rollbackFor = { Exception.class })
	public void deleteDailyInfo(DailyInfoVO vo) throws DataNotInsertedException {
		int cnt = notesMapper.deleteNote(vo);
		if (cnt == 0) throw new DataNotInsertedException("삭제 데이터의 noteId 정보가 일치한지 확인이 필요합니다.");
		cnt = moodsMapper.deleteMood(vo);
		if (cnt == 0) throw new DataNotInsertedException("삭제 데이터의 moodId 정보가 일치한지 확인이 필요합니다.");
		cnt = dailiesMapper.deleteDaily(vo);
		if (cnt == 0) throw new DataNotInsertedException("삭제 데이터의 dailyId 정보가 일치한지 확인이 필요합니다.");
		cnt = imagesMapper.deleteImage(vo);
		if (cnt == 0) throw new DataNotInsertedException("삭제 데이터의 imageId 정보가 일치한지 확인이 필요합니다.");
	}
	
}// class
