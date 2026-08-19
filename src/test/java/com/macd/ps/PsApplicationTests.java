package com.macd.ps;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class PsApplicationTests {

	@Test
	void applicationMainClassIsAvailable() {
		assertDoesNotThrow(() -> Class.forName(PsApplication.class.getName()));
	}

}
