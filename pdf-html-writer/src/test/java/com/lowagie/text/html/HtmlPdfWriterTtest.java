package com.lowagie.text.html;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HtmlPdfWriterTtest {

	@Test
	void tsetMain() {
		//
		Assertions.assertThrows(IllegalArgumentException.class, () -> HtmlPdfWriter.main(null));
		Assertions.assertThrows(IllegalArgumentException.class, () -> HtmlPdfWriter.main(new String[0]));
		Assertions.assertThrows(IllegalArgumentException.class, () -> HtmlPdfWriter.main(new String[] { null }));
		Assertions.assertDoesNotThrow(() -> HtmlPdfWriter.main(new String[] { "." }));
		//
	}

}