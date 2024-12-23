package kr.co.moodtracker.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import kr.co.moodtracker.exception.DataMissingException;
import kr.co.moodtracker.exception.DataNotInsertedException;
import kr.co.moodtracker.exception.SessionNotFoundException;
import kr.co.moodtracker.exception.SettingDataException;
import kr.co.moodtracker.exception.ZeroDataException;
import kr.co.moodtracker.service.CalendarService;
import kr.co.moodtracker.service.NeighborService;
import kr.co.moodtracker.vo.DailyInfoVO;
import kr.co.moodtracker.vo.DailySearchVO;
import kr.co.moodtracker.vo.NeighborVO;
import kr.co.moodtracker.vo.SearchNeighborVO;
import kr.co.moodtracker.vo.UserVO;


@RestController
public class NeighborController extends CommonController {
	
	@Autowired
	NeighborService neighborService;
	
	@Autowired
	CalendarService calendarService;
	
	@GetMapping("/neighbors")
	public ResponseEntity<?> getNeighbors(
			HttpSession sess
	) throws SessionNotFoundException 
	{
		UserVO user = getUserInfo(sess);
		Map<String, Object> res = new HashMap<>();
		List<NeighborVO> neighbors =  neighborService.getNeighbors(user.getUserId());
		res.put("neighbors", neighbors);
		return ResponseEntity.ok().body(res);
	}
	
	@PostMapping("/neighbor/{nickname}")
	public ResponseEntity<?> postNeighbor(
			HttpSession sess
			, SearchNeighborVO vo
	) throws SessionNotFoundException, DataNotInsertedException 
	{
		setUserId(sess, vo);
		Map<String, Object> res = new HashMap<>();
		neighborService.postNeighbor(vo);
		res.put("success", true);
		return ResponseEntity.ok().body(res);
	}
	
	@PatchMapping("/neighbor")
	public ResponseEntity<?> patchNeighbor(
			HttpSession sess
			, SearchNeighborVO vo
	) throws SessionNotFoundException 
	{
		setUserId(sess, vo);
		Map<String, Object> res = new HashMap<>();
		neighborService.patchNeighbor(vo);
		res.put("success", true);
		return ResponseEntity.ok().body(res);
	}
	
	@PatchMapping("/neighbor/synchronize")
	public ResponseEntity<?> setSynchronize(
			HttpSession sess
			, SearchNeighborVO vo
	) throws SessionNotFoundException 
	{
		setUserId(sess, vo);
		Map<String, Object> res = new HashMap<>();
		neighborService.synchronize(vo);
		res.put("success", true);
		return ResponseEntity.ok().body(res);
	}
	
	@DeleteMapping("/neighbor")
	public ResponseEntity<?> deleteNeighbor(
			HttpSession sess
			, SearchNeighborVO vo
	) throws SessionNotFoundException 
	{
		setUserId(sess, vo);
		Map<String, Object> res = new HashMap<>();
		neighborService.deleteNeighbor(vo);
		res.put("success", true);
		return ResponseEntity.ok().body(res);
	}
	
	@GetMapping("/neighbor/{neighborId}/calendar/{date}")
	public ResponseEntity<?> getNeighborCalendar(
			HttpSession sess
			, SearchNeighborVO vo
	) throws SessionNotFoundException, DataMissingException, ZeroDataException, SettingDataException 
	{
		setUserId(sess, vo);
		Map<String, Object> res = new HashMap<>();
		DailySearchVO ds = neighborService.getDailySearchVoOfNeighbor(vo);
		List<DailyInfoVO> list = calendarService.getDailyInfoOfTheMonth(ds);
		res.put("dailyInfoList", list);
		res.put("success", true);
		return ResponseEntity.ok().body(res);
	}
}
