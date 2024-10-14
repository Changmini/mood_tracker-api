package kr.co.moodtracker.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.moodtracker.exception.DataNotInsertedException;
import kr.co.moodtracker.exception.SettingDataException;
import kr.co.moodtracker.exception.ZeroDataException;
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
	
}
