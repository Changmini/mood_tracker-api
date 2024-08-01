package kr.co.moodtracker.mapper;

import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface UsersMapper {

	public Map<String, Object> getUser(Map<String, String> vo);

}
