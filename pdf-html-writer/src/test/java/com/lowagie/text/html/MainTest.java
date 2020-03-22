package com.lowagie.text.html;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MainTest {

	@Test
	void tsetMain() {
		//
		Assertions.assertThrows(IllegalArgumentException.class, () -> Main.main(null));
		Assertions.assertThrows(IllegalArgumentException.class, () -> Main.main(new String[0]));
		Assertions.assertThrows(IllegalArgumentException.class, () -> Main.main(new String[] { null }));
		Assertions.assertDoesNotThrow(() -> Main.main(new String[] { "." }));
		//
	}

}