package org.apache.pdfbox.pdmodel;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ImagePdfWriterTest {

	private static Pattern PATTERN = null;

	private static Method METHOD_GENERATE_FILE_NAME, METHOD_MATCHER, METHOD_REPLACE_ALL = null;

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
		final Object object = FieldUtils.readDeclaredStaticField(clz, "PATTERN", true);
		PATTERN = object instanceof Pattern ? (Pattern) object : null;
		//
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

}