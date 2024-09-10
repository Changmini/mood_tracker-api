package kr.co.moodtracker.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import kr.co.moodtracker.exception.SessionNotFoundException;
import kr.co.moodtracker.service.GraphService;
import kr.co.moodtracker.vo.UserVO;

@RequestMapping("/graph")
@RestController
public class GraphController extends CommonController {
	
	@Autowired
	GraphService graphService;
	
	@GetMapping("/datasets")
	public ResponseEntity<?> graphData(HttpSession sess) throws SessionNotFoundException {
		Map<String, Object> result = new HashMap<>();
		try {
			UserVO user = setUserInfo(sess);
			Map<?,?> datasets = graphService.getGraphData(user.getUserId());
            return ResponseEntity.ok().body(datasets);
		} catch(Exception e) {
			
		}
		return ResponseEntity.ok().body(result);
	}
}
