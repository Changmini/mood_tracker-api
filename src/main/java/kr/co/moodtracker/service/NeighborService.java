package kr.co.moodtracker.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.moodtracker.exception.DataNotInsertedException;
import kr.co.moodtracker.exception.SettingDataException;
import kr.co.moodtracker.exception.ZeroDataException;
import kr.co.moodtracker.handler.DateHandler;
import kr.co.moodtracker.handler.ImageHandler;
import kr.co.moodtracker.mapper.NeighborMapper;
import kr.co.moodtracker.mapper.UsersMapper;
import kr.co.moodtracker.vo.DailySearchVO;
import kr.co.moodtracker.vo.NeighborVO;
import kr.co.moodtracker.vo.SearchNeighborVO;

@Service
public class NeighborService {
	
	@Autowired
	UsersMapper usersMapper;
	
	@Autowired
	NeighborMapper neighborMapper;
	
	@Transactional(readOnly = true)
	public List<NeighborVO> getNeighbors(int userId)  {
		List<NeighborVO> neighbors = neighborMapper.getNeighbors(userId);
		for (NeighborVO n : neighbors) {
			n.setImagePath(
				ImageHandler.pathToBase64(
					n.getImagePath()
				)
			);
		}// path to base64
		return neighbors;
	}
	
	public void postNeighbor(SearchNeighborVO vo) 
			throws DataNotInsertedException {
		if (vo.getNickname().trim().equals(""))
			throw new DataNotInsertedException("요청할 별칭을 입력해주세요");
		int guestProfileId = usersMapper.getUserProfileId(vo.getNickname());
		if (vo.getUserId() == guestProfileId)
			throw new DataNotInsertedException("본인의 별칭으로 요청할 수 없습니다.");
		vo.setGuestProfileId(guestProfileId);
		
		int cnt = neighborMapper.postNeighbor(vo);
		if (cnt < 1) 
			throw new DataNotInsertedException("이웃맺기 요청이 실해했습니다.");
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void synchronize(SearchNeighborVO vo) {
		/* 본인이 요청받은 사람일 경우에만 동기화를 시킬 수 있도록
		 * 조건문을 추가해야 될 듯... */
		int guestProfileId = neighborMapper.getGuestProfileId(vo);
		vo.setGuestProfileId(guestProfileId);
		neighborMapper.setHostSynchronize(vo);
		neighborMapper.setGuestSynchronize(vo);
	}
	
	public void patchNeighbor(SearchNeighborVO vo) {
		neighborMapper.patchNeighbor(vo);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void deleteNeighbor(SearchNeighborVO vo) {
		int guestProfileId = neighborMapper.getGuestProfileId(vo);
		vo.setGuestProfileId(guestProfileId);
		neighborMapper.deleteHostNeighbor(vo);
		neighborMapper.deleteGuestNeighbor(vo);
	}
	
	@Transactional(readOnly = true)
	public DailySearchVO getDailySearchVoOfNeighbor(SearchNeighborVO vo) 
			throws ZeroDataException, SettingDataException {
		String date = vo.getDate();
		if (date == null || date.trim().equals(""))
			throw new SettingDataException("검색에 필요한 정보가 없습니다.");
		Integer neighborUserId = neighborMapper.getNeighborUserId(vo);
		if (neighborUserId == null || neighborUserId == 0)
			throw new ZeroDataException("접근할 수 없는 이웃입니다.");
		DailySearchVO ds = new DailySearchVO();
		ds.setDate(date);
		ds.setUserId(neighborUserId);
		return ds;
	}
	
	/**
	 * <pre> 
	 * 	브라우저에서 Polling을 처리하는 시간 간격(ex 1min)마다 
	 * 	DB 컬럼의 updated_at이 변경되어진 것만 필터링하여 목록을 반환
	 * 	[
	 * 		네트워크 지연, 로직처리, ms 단위의 오차를 고려하여 
	 * 		interval(60s)일 때, 아래 1-1, 1-2가 아닌 2-1, 2-2가 사용된다.
	 * 		1-1) 2025-02-11 00:00:01 ~ 2025-02-11 00:01:00	1-2) 2025-02-11 00:01:01 ~ 2025-02-11 00:02:00
	 * 		2-1) 2025-02-11 00:00:00 ~ 2025-02-11 00:01:00	2-2) 2025-02-11 00:01:00 ~ 2025-02-11 00:02:00
	 * 	]
	 * </pre>
	 * @param vo
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<NeighborVO> shortPolling(SearchNeighborVO vo) {
		if (vo.getUpdatedAt() == null) {
			vo.setUpdatedAt(LocalDateTime.now().format(DateHandler.FORMATTER_TIME));
			// 탐색기준인 날짜가 주어지지 않을 때
		}
		if (vo.getInterval() <= 0) {
			vo.setInterval(60);
			// Polling을(를) 처리하는 시간 간격이 주어지지 않을 때
		}
		List<NeighborVO> neighbors = neighborMapper.alertNeighborsCondition(vo);
		return neighbors == null ? Collections.emptyList() : neighbors;
	}
	
}
