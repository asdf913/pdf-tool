package org.springframework.context.support;

import java.awt.Component;
import java.awt.Window;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JFrame;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MainTest {

	private static Method METHOD_PACK, METHOD_SET_VISIBLE = null;

	@BeforeAll
	static void beforeAll() throws ReflectiveOperationException {
		//
		final Class<?> clz = Main.class;
		//
		(METHOD_PACK = clz.getDeclaredMethod("pack", Window.class)).setAccessible(true);
		//
		(METHOD_SET_VISIBLE = clz.getDeclaredMethod("setVisible", Component.class, Boolean.TYPE)).setAccessible(true);
		//
	}

	private Window window = null;

	@BeforeEach
	void beforeEach() {
		window = new JFrame();
	}

	@Test
	void testPack() {
		//
		Assertions.assertDoesNotThrow(() -> pack(null));
		Assertions.assertDoesNotThrow(() -> pack(window));
		//
	}

	private static void pack(final Window instance) throws Throwable {
		try {
			METHOD_PACK.invoke(null, instance);
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testSetVisible() {
		//
		Assertions.assertDoesNotThrow(() -> setVisible(null, false));
		Assertions.assertDoesNotThrow(() -> setVisible(window, false));
		//
	}

	private static void setVisible(final Component instance, final boolean flag) throws Throwable {
		try {
			METHOD_SET_VISIBLE.invoke(null, instance, flag);
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

}