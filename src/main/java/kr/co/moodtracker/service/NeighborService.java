package kr.co.moodtracker.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.moodtracker.exception.DataNotInsertedException;
import kr.co.moodtracker.handler.ImageHandler;
import kr.co.moodtracker.mapper.NeighborMapper;
import kr.co.moodtracker.mapper.UsersMapper;
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
	
	public void postNeighbor(int userId, SearchNeighborVO vo) 
			throws DataNotInsertedException {
		if (vo.getNickname().trim().equals(""))
			throw new DataNotInsertedException("요청할 별칭을 입력해주세요");
		int guestProfileId = usersMapper.getUserProfileId(vo.getNickname());
		if (userId == guestProfileId)
			throw new DataNotInsertedException("본인의 별칭으로 요청할 수 없습니다.");
		vo.setUserId(userId);
		vo.setGuestProfileId(guestProfileId);
		
		int cnt = neighborMapper.postNeighbor(vo);
		if (cnt < 1) 
			throw new DataNotInsertedException("이웃맺기 요청이 실해했습니다.");
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void synchronize(int userId, SearchNeighborVO vo) {
		vo.setUserId(userId);
		int guestProfileId = neighborMapper.getGuestProfileId(vo);
		vo.setGuestProfileId(guestProfileId);
		neighborMapper.setHostSynchronize(vo);
		neighborMapper.setHostSynchronize(vo);
	}
	
	public void patchNeighbor(int userId, SearchNeighborVO vo) {
		vo.setUserId(userId);
		neighborMapper.patchNeighbor(vo);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void deleteNeighbor(int userId, SearchNeighborVO vo) {
		vo.setUserId(userId);
		int guestProfileId = neighborMapper.getGuestProfileId(vo);
		vo.setGuestProfileId(guestProfileId);
		neighborMapper.deleteHostNeighbor(vo);
		neighborMapper.deleteGuestNeighbor(vo);
	}
	
}
