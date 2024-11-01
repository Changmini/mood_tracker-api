package kr.co.moodtracker;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class MoodtrackerApplicationTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	/*
	 * assertEquals(a, b) : a와 b의 값이 일치한지 확인한다.
	 * assertSame(a, b)   : a와 b가 같은 객체임을 확인한다.
	 * assertTrue(a)      : 조건 a가 참인가를 확인한다.
	 * assertNotNull(a)   : 객체 a가 null이 아님을 확인한다.
	 * assertThat()       : -Equals와 비슷하지만 타입<T>의 안전성, 유연성, 오류 메시지 등 더 나은 결과을 제공한다. 
	 * 
	 * https://spring.io/guides/gs/testing-web
	 */
	
	@Test
	void contextLoads() throws Exception {
		this.mockMvc.perform(get("/"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("Hello Moodtracker")));
	}

}
