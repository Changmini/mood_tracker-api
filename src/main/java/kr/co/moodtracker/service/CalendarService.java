package kr.co.moodtracker.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.co.moodtracker.enums.MethodType;
import kr.co.moodtracker.exception.DataMissingException;
import kr.co.moodtracker.exception.DataNotDeletedException;
import kr.co.moodtracker.exception.DataNotInsertedException;
import kr.co.moodtracker.exception.DuplicateDataException;
import kr.co.moodtracker.handler.DateHandler;
import kr.co.moodtracker.handler.FileHandler;
import kr.co.moodtracker.handler.ImageHandler;
import kr.co.moodtracker.mapper.DailiesMapper;
import kr.co.moodtracker.mapper.ImagesMapper;
import kr.co.moodtracker.mapper.MoodsMapper;
import kr.co.moodtracker.mapper.NotesMapper;
import kr.co.moodtracker.vo.DailyInfoVO;
import kr.co.moodtracker.vo.SearchDailyInfoVO;
import kr.co.moodtracker.vo.ImageVO;
import kr.co.moodtracker.vo.ReturnDailyInfoVO;

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
	
	@Transactional(readOnly = true)
	public List<ReturnDailyInfoVO> getDailyInfoOfTheMonth(SearchDailyInfoVO vo) throws DataMissingException {
		DateHandler.determineDateRange(vo);
		List<DailyInfoVO> dailies = dailiesMapper.getDailyInfoOfTheMonth(vo);
		List<DailyInfoVO> images =  imagesMapper.getImageInfoList(vo);
		ImageHandler.insertImageDataIntoDailyInfo(images, dailies, true);
		List<ReturnDailyInfoVO> dailyInfoList = DateHandler.makeDateListInCalendar(vo, dailies);
		// DateHandler.makeDateListInCalendar에서 DailyInfoVO => ReturnDailyInfoVO 변환 작업도 같이 실행
		if (dailyInfoList.size() < 1) return Collections.emptyList();
		return dailyInfoList;
	}
	
	@Transactional(readOnly = true)
	public List<DailyInfoVO> getDailyInfoList(SearchDailyInfoVO vo) throws DataMissingException {
		if (vo.getLimit() > 30) throw new DataMissingException("한번에 너무 많은 데이터를 요청할 수 없습니다.");
		List<DailyInfoVO> dailies = dailiesMapper.getDailyInfoList(vo);
		List<Integer> ids = new ArrayList<>();// daily_id 수집
		for (DailyInfoVO d : dailies) {
			ids.add(d.getDailyId());
		}
		vo.setIds(ids);// 이미지를 daily_id 목록으로 검색
		List<DailyInfoVO> images =  imagesMapper.getImageInfoList(vo);
		ImageHandler.insertImageDataIntoDailyInfo(images, dailies, false);
		if (dailies.size() < 1) return Collections.emptyList();
		return dailies;
	}
	
	@Transactional(readOnly = true)
	public ReturnDailyInfoVO getDailyInfo(SearchDailyInfoVO vo) throws DataMissingException {
		DailyInfoVO daily = dailiesMapper.getDailyInfo(vo);
		List<Integer> ids = new ArrayList<>();// daily_id 수집
		ids.add(daily.getDailyId());
		vo.setIds(ids);
		// 이미지를 daily_id 목록으로 검색
		List<DailyInfoVO> images =  imagesMapper.getImageInfoList(vo);
		List<ImageVO> image = images.get(0).getImageList();
		for (ImageVO i : image) {
			i.setImagePath(
					ImageHandler.pathToBase64(i.getImagePath())
			);
		}
		daily.setImageList(image);
		return daily.returnDailyInfoVO();
	}
	
	@Transactional(rollbackFor = { Exception.class })
	public void postDailyInfo(
			SearchDailyInfoVO vo
			, List<MultipartFile> files
	) throws DataNotInsertedException, IllegalStateException, IOException, DuplicateDataException {
		this.savedImageFileList(MethodType.INSERT, files, null, vo);

		int cnt = dailiesMapper.checkDailyInfo(vo);
		if (cnt > 0) throw new DuplicateDataException("해당 날짜에 데이터가 존재합니다. 수정 작업을 수행하여 데이터를 갱신해주세요.");
		
		cnt = notesMapper.postNote(vo);
		if (cnt == 0) throw new DataNotInsertedException("입력된 텍스트 정보를 확인해주세요.");
		cnt = moodsMapper.postMood(vo);
		if (cnt == 0) throw new DataNotInsertedException("분위기 정보를 등록 실패했습니다.");
		cnt = dailiesMapper.postDaily(vo);
		if (cnt == 0) throw new DataNotInsertedException("날짜 정보를 등록 실패했습니다.");
		cnt = imagesMapper.postImage(vo);
		if (cnt == 0) throw new DataNotInsertedException("이미지 등록에 실패했습니다.");
		
	}
	
	@Transactional(rollbackFor = { Exception.class })
	public void putDailyInfoWithImageJobExclusion(
			SearchDailyInfoVO vo
	) throws DataNotInsertedException, IllegalStateException, IOException {
		this.instantInjection(dailiesMapper.getDailyRelatedIds(vo), vo);
		// 날짜로 dailyId, noteId, moodId 세팅
		notesMapper.putNote(vo);
		moodsMapper.putMood(vo);
	}
	
	@Transactional(rollbackFor = { Exception.class })
	public void patchDailyInfo(
			SearchDailyInfoVO vo
			, List<MultipartFile> files
			, List<Integer> preImageId
	) throws DataNotInsertedException, IllegalStateException, IOException {
		int numberOfImagesInserted = this.savedImageFileList(MethodType.UPDATE, files, preImageId, vo);
		
		this.instantInjection(dailiesMapper.getDailyRelatedIds(vo), vo);
		// 날짜로 dailyId, noteId, moodId 세팅
		if (vo.getNoteTitle() != null || vo.getNoteContent() != null)
			notesMapper.patchNote(vo);
		if (vo.getMoodLevel() != null) 
			moodsMapper.putMood(vo);
		if (numberOfImagesInserted > 0) {
			imagesMapper.putImage(vo);
		}
	}

	@Transactional(rollbackFor = { Exception.class })
	public void deleteDailyInfo(SearchDailyInfoVO vo) throws DataNotDeletedException {
		this.instantInjection(dailiesMapper.getDailyRelatedIds(vo), vo);
		// 날짜로 dailyId, noteId, moodId 세팅
		
		int cnt = notesMapper.deleteNote(vo);
		if (cnt == 0) throw new DataNotDeletedException("삭제 데이터의 noteId 정보가 일치한지 확인이 필요합니다.");
		cnt = moodsMapper.deleteMood(vo);
		if (cnt == 0) throw new DataNotDeletedException("삭제 데이터의 moodId 정보가 일치한지 확인이 필요합니다.");
		cnt = dailiesMapper.deleteDaily(vo);
		if (cnt == 0) throw new DataNotDeletedException("삭제 데이터의 dailyId 정보가 일치한지 확인이 필요합니다.");
		cnt = imagesMapper.deleteAllImage(vo);
		if (cnt == 0) throw new DataNotDeletedException("삭제 데이터의 imageId 정보가 일치한지 확인이 필요합니다.");
	}
	
	/**
	 * 인자 값으로 전달된 객체에 저장된 이미지의 경로를 세팅해준다.  
	 * @return 갱신된 이미지의 수
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@SuppressWarnings("unused")
	private int savedImageFileList(
			MethodType m
			, List<MultipartFile> files
			, List<Integer> preImageId
			, SearchDailyInfoVO vo
	) throws IllegalStateException, IOException {
		/**
		 * 이미지 갱신에 사용되는 로직 (PUT, PATCH)
		 */
		if (m == MethodType.UPDATE && files != null && preImageId != null) {
			int filesSize = files.size();
			int imageIdSize = preImageId.size();
			List<ImageVO> imageList = new ArrayList<>();
			for (int i = 0; i < filesSize && i < imageIdSize; i++) {
				String path = FileHandler.saveFile(files.get(i), vo.getUserId());
				if (path == null) continue;
				ImageVO image = new ImageVO();
				image.setImagePath(path);
				image.setImageId(preImageId.get(i));
				imageList.add(image);
			}
			vo.setImageList(imageList);
			return imageList.size();
		}
		/**
		 * 첫 이미지 저장에 사용되는 로직(POST)
		 * 무조건 DB의 images.imagePath를 3개 만들기 위한 작업을 수행
		 */
		if (m == MethodType.INSERT) {
			List<ImageVO> imageList = new ArrayList<>();
			if (files != null && files.size() >= 3) {
				for (MultipartFile f : files) {
					String path = FileHandler.saveFile(f, vo.getUserId());
					ImageVO ivo = new ImageVO();
					ivo.setImagePath(path);
					imageList.add(ivo);
				}				
			} else {
				for (int i = 0; i < 3; i++) {
					imageList.add(new ImageVO());
				}				
			}
			vo.setImageList(imageList);
		} 
		return 0;
	}
	
	private void instantInjection(DailyInfoVO DailyRelatedIds, SearchDailyInfoVO vo) {
		vo.setDailyId(DailyRelatedIds.getDailyId());
		vo.setNoteId(DailyRelatedIds.getNoteId());
		vo.setMoodId(DailyRelatedIds.getMoodId());
	}
}// class
