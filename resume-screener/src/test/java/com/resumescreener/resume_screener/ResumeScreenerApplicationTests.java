package com.resumescreener.resume_screener;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
		"gemini.api.key=test-key",
		"gemini.api.url=https://example.com",
		"gemini.api.model=gemini-3.6-flash"
})
class ResumeScreenerApplicationTests {

	@Test
	void contextLoads() {
	}

}