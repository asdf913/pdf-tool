package org.springframework.beans.factory.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CustomBeanPostProcessorTest {

	@Test
	void testPostProcessBeforeInitialization() {
		Assertions.assertNull(new CustomBeanPostProcessor().postProcessBeforeInitialization(null, null));
	}

}