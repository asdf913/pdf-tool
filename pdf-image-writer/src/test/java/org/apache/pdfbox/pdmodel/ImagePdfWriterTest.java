package org.apache.pdfbox.pdmodel;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ComboBoxModel;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.google.common.reflect.Reflection;

class ImagePdfWriterTest {

	private static Pattern PATTERN = null;

	private static final String A0 = "A0";

	private static Method METHOD_GENERATE_FILE_NAME, METHOD_MATCHER, METHOD_REPLACE_ALL, METHOD_KEY_SET,
			METHOD_TO_ARRAY, METHOD_GET_PAGE_SIZE_MAP, METHOD_CAST, METHOD_GET_SELECTED_ITEM = null;

	@BeforeAll
	static void beforeAll() throws ReflectiveOperationException {
		//
		final Class<?> clz = ImagePdfWriter.class;
		//
		(METHOD_GENERATE_FILE_NAME = clz.getDeclaredMethod("generateFileName", File.class, String.class))
				.setAccessible(true);
		//
		(METHOD_MATCHER = clz.getDeclaredMethod("matcher", Pattern.class, String.class)).setAccessible(true);
		//
		(METHOD_REPLACE_ALL = clz.getDeclaredMethod("replaceAll", Matcher.class, String.class)).setAccessible(true);
		//
		(METHOD_KEY_SET = clz.getDeclaredMethod("keySet", Map.class)).setAccessible(true);
		//
		(METHOD_TO_ARRAY = clz.getDeclaredMethod("toArray", Collection.class, Object[].class)).setAccessible(true);
		//
		(METHOD_GET_PAGE_SIZE_MAP = clz.getDeclaredMethod("getPageSizeMap", Field[].class)).setAccessible(true);
		//
		(METHOD_CAST = clz.getDeclaredMethod("cast", Class.class, Object.class)).setAccessible(true);
		//
		(METHOD_GET_SELECTED_ITEM = clz.getDeclaredMethod("getSelectedItem", ComboBoxModel.class)).setAccessible(true);
		//
		final Object object = FieldUtils.readDeclaredStaticField(clz, "PATTERN", true);
		PATTERN = object instanceof Pattern ? (Pattern) object : null;
		//
	}

	private class IH implements InvocationHandler {

		private Object selectedItem;

		@Override
		public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
			//
			final String methodName = method != null ? method.getName() : null;
			//
			if (proxy instanceof ComboBoxModel) {
				if ("getSelectedItem".equals(methodName)) {
					return selectedItem;
				}
			}
			//
			throw new Throwable(methodName);
			//
		}

	}

	@Test
	void testGenerateFileName() throws Throwable {
		//
		Assertions.assertNull(generateFileName(null, null));
		//
		final String fileName = "pom.xml";
		final File file = new File(fileName);
		//
		Assertions.assertSame(fileName, generateFileName(file, null));
		Assertions.assertSame(fileName, generateFileName(file, ""));
		Assertions.assertSame(fileName, generateFileName(file, " "));
		//
		final String pdf = "pdf";
		Assertions.assertEquals(StringUtils.join(StringUtils.substringBefore(fileName, "."), ".", pdf),
				generateFileName(file, pdf));
		//
	}

	private static String generateFileName(final File file, final String newFileExtension) throws Throwable {
		try {
			final Object obj = METHOD_GENERATE_FILE_NAME.invoke(null, file, newFileExtension);
			if (obj == null) {
				return null;
			} else if (obj instanceof String) {
				return (String) obj;
			}
			throw new Throwable(toString(obj.getClass()));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	private static String toString(final Object instance) {
		return instance != null ? instance.toString() : null;
	}

	@Test
	void testMatcher() throws Throwable {
		//
		Assertions.assertNull(matcher(null, null));
		Assertions.assertNull(matcher(PATTERN, null));
		//
	}

	private static Matcher matcher(final Pattern instance, final String input) throws Throwable {
		try {
			final Object obj = METHOD_MATCHER.invoke(null, instance, input);
			if (obj == null) {
				return null;
			} else if (obj instanceof Matcher) {
				return (Matcher) obj;
			}
			throw new Throwable(toString(obj.getClass()));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testReplaceAll() throws Throwable {
		//
		Assertions.assertNull(replaceAll(null, null));
		//
		final Matcher matcher = matcher(PATTERN, "a.b");
		Assertions.assertNull(replaceAll(matcher, null));
		Assertions.assertEquals("a.pdf", replaceAll(matcher, ".pdf"));
		//
	}

	private static String replaceAll(final Matcher instance, final String replacement) throws Throwable {
		try {
			final Object obj = METHOD_REPLACE_ALL.invoke(null, instance, replacement);
			if (obj == null) {
				return null;
			} else if (obj instanceof String) {
				return (String) obj;
			}
			throw new Throwable(toString(obj.getClass()));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testKeySet() throws Throwable {
		//
		Assertions.assertNull(keySet(null));
		Assertions.assertSame(Collections.emptySet(), keySet(Collections.emptyMap()));
		//
	}

	private static <K> Set<K> keySet(final Map<K, ?> instance) throws Throwable {
		try {
			final Object obj = METHOD_KEY_SET.invoke(null, instance);
			if (obj == null) {
				return null;
			} else if (obj instanceof Set) {
				return (Set) obj;
			}
			throw new Throwable(toString(obj.getClass()));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testToArray() throws Throwable {
		//
		Assertions.assertNull(toArray(null, null));
		Assertions.assertNull(toArray(Collections.emptyList(), null));
		//
		final Object[] objects = new Object[0];
		Assertions.assertSame(objects, toArray(Collections.emptyList(), objects));
		//
	}

	private static <E> E[] toArray(final Collection<E> instance, final E[] array) throws Throwable {
		try {
			return (E[]) METHOD_TO_ARRAY.invoke(null, instance, array);
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testGetPageSizeMap() throws Throwable {
		//
		Assertions.assertNull(getPageSizeMap(null));
		//
		final Field field = PDRectangle.class.getDeclaredField(A0);
		if (field != null) {
			field.setAccessible(true);
		}
		//
		Assertions.assertEquals(Collections.singletonMap(A0, field != null ? field.get(null) : null),
				getPageSizeMap(new Field[] { null, String.class.getDeclaredField("value"), field, field }));
		//
	}

	private static Map<String, PDRectangle> getPageSizeMap(final Field[] fs) throws Throwable {
		try {
			final Object obj = METHOD_GET_PAGE_SIZE_MAP.invoke(null, (Object) fs);
			if (obj == null) {
				return null;
			} else if (obj instanceof Map) {
				return (Map) obj;
			}
			throw new Throwable(toString(obj.getClass()));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testCast() throws Throwable {
		//
		Assertions.assertNull(cast(null, null));
		Assertions.assertNull(cast(String.class, null));
		Assertions.assertSame(A0, cast(String.class, A0));
		//
	}

	private static <T> T cast(final Class<T> clz, final Object instance) throws Throwable {
		try {
			return (T) METHOD_CAST.invoke(null, clz, instance);
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testGetSelectedItem() throws Throwable {
		//
		Assertions.assertNull(getSelectedItem(null));
		Assertions.assertNull(getSelectedItem(Reflection.newProxy(ComboBoxModel.class, new IH())));
		//
	}

	private static Object getSelectedItem(final ComboBoxModel<?> instance) throws Throwable {
		try {
			return METHOD_GET_SELECTED_ITEM.invoke(null, instance);
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

}