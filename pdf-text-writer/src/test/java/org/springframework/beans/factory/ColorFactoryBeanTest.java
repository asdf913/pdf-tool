package org.springframework.beans.factory;

import java.awt.Color;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ColorFactoryBeanTest {

	private static Method METHOD_GET_COLOR, METHOD_FILTER, METHOD_COLLECT = null;

	@BeforeAll
	static void beforeAll() throws ReflectiveOperationException {
		//
		final Class<?> clz = ColorFactoryBean.class;
		//
		(METHOD_GET_COLOR = clz.getDeclaredMethod("getColor", Field[].class, String.class)).setAccessible(true);
		//
		(METHOD_FILTER = clz.getDeclaredMethod("filter", Stream.class, Predicate.class)).setAccessible(true);
		//
		(METHOD_COLLECT = clz.getDeclaredMethod("collect", Stream.class, Collector.class)).setAccessible(true);
		//
	}

	private ColorFactoryBean instance = null;

	@BeforeEach
	void beforeEach() {
		instance = new ColorFactoryBean();
	}

	@Test
	void testGetObjectType() {
		Assertions.assertEquals(Color.class, instance.getObjectType());
	}

	@Test
	void tsetGetObject() throws Exception {
		Assertions.assertNull(instance.getObject());
	}

	@Test
	void testGetColor() throws Throwable {
		//
		Assertions.assertNull(getColor(null, null));
		Assertions.assertNull(getColor(new Field[1], null));
		//
		final String black = "black";
		final Field field = Color.class.getDeclaredField(black);
		//
		Assertions.assertSame(Color.black, getColor(new Field[] { field }, black));
		Assertions.assertThrows(IllegalStateException.class, () -> getColor(new Field[] { field, field }, black));
		//
	}

	private static Color getColor(final Field[] fs, final String name) throws Throwable {
		try {
			final Object obj = METHOD_GET_COLOR.invoke(null, fs, name);
			if (obj == null) {
				return null;
			} else if (obj instanceof Color) {
				return (Color) obj;
			}
			throw new Throwable(toString(getClass(obj)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	private static Class<?> getClass(final Object instance) {
		return instance != null ? instance.getClass() : null;
	}

	private static String toString(final Object instance) {
		return instance != null ? instance.toString() : null;
	}

	@Test
	void testFilter() throws Throwable {
		Assertions.assertNull(filter(Stream.of(), null));
	}

	private static <T> Stream<T> filter(final Stream<T> instance, final Predicate<? super T> predicate)
			throws Throwable {
		try {
			final Object obj = METHOD_FILTER.invoke(null, instance, predicate);
			if (obj == null) {
				return null;
			} else if (obj instanceof Stream) {
				return (Stream) obj;
			}
			throw new Throwable(toString(getClass(obj)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testCollect() throws Throwable {
		Assertions.assertNull(collect(Stream.of(), null));
	}

	private static <T, R, A> R collect(final Stream<T> instance, final Collector<? super T, A, R> collector)
			throws Throwable {
		try {
			return (R) METHOD_COLLECT.invoke(null, instance, collector);
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

}