package kr.co.moodtracker.mapper;

import java.util.List;
import java.util.Map;

import kr.co.moodtracker.vo.NeighborVO;
import kr.co.moodtracker.vo.SearchNeighborVO;

public interface NeighborMapper {

	public List<NeighborVO> getNeighbors(int userId);
	
	public int postNeighbor(SearchNeighborVO vo);
	
	public int getGuestProfileId(SearchNeighborVO vo);
	
	public int setHostSynchronize(SearchNeighborVO vo);
	public int setGuestSynchronize(SearchNeighborVO vo);
	
	public int patchNeighbor(SearchNeighborVO vo);
	
	public int deleteHostNeighbor(SearchNeighborVO vo);
	public int deleteGuestNeighbor(SearchNeighborVO vo);
	
	public Integer getNeighborUserId(SearchNeighborVO vo);
	
	public List<Map<String,Integer>> getGroupProfileId(Long neighborId);
	
	public int notifyNeighborsChatroomActive(Long neighborId);
	public int leaveChatroom(Long neighborId);

}
