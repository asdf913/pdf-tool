package org.apache.pdfbox.pdmodel;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
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
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.google.common.reflect.Reflection;

class ImagePdfWriterTest {

	private static Pattern PATTERN = null;

	private static final String A0 = "A0";

	private static final int ONE = 1;

	private static Method METHOD_GENERATE_FILE_NAME, METHOD_MATCHER, METHOD_REPLACE_ALL, METHOD_KEY_SET,
			METHOD_TO_ARRAY, METHOD_GET_PAGE_SIZE_MAP, METHOD_CAST, METHOD_GET_SELECTED_ITEM, METHOD_GET_ABSOLUTE_PATH,
			METHOD_RESIZE_IMAGE, METHOD_DRAW_IMAGE, METHOD_VALUE_OF, METHOD_INT_VALUE, METHOD_ADD_WATER_MARK_TEXT,
			METHOD_GET_PD_FONTS0, METHOD_GET_PD_FONTS1 = null;

	@BeforeAll
	private static void beforeAll() throws ReflectiveOperationException {
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
		(METHOD_GET_ABSOLUTE_PATH = clz.getDeclaredMethod("getAbsolutePath", File.class)).setAccessible(true);
		//
		(METHOD_RESIZE_IMAGE = clz.getDeclaredMethod("resizeImage", BufferedImage.class, Integer.TYPE, Integer.TYPE))
				.setAccessible(true);
		//
		(METHOD_DRAW_IMAGE = clz.getDeclaredMethod("drawImage", Graphics.class, Image.class, Integer.TYPE, Integer.TYPE,
				ImageObserver.class)).setAccessible(true);
		//
		(METHOD_VALUE_OF = clz.getDeclaredMethod("valueOf", String.class)).setAccessible(true);
		//
		(METHOD_INT_VALUE = clz.getDeclaredMethod("intValue", Number.class, Integer.TYPE)).setAccessible(true);
		//
		(METHOD_ADD_WATER_MARK_TEXT = clz.getDeclaredMethod("addWatermarkText", PDDocument.class, PDPage.class,
				PDFont.class, String.class, Integer.TYPE)).setAccessible(true);
		//
		(METHOD_GET_PD_FONTS0 = clz.getDeclaredMethod("getPDFonts")).setAccessible(true);
		//
		(METHOD_GET_PD_FONTS1 = clz.getDeclaredMethod("getPDFonts", Field[].class)).setAccessible(true);
		//
		// java.lang.reflect.Field
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
			throw new Throwable(toString(getClass(obj)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	private static String toString(final Object instance) {
		return instance != null ? instance.toString() : null;
	}

	private static Class<?> getClass(final Object instance) {
		return instance != null ? instance.getClass() : null;
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
			throw new Throwable(toString(getClass(obj)));
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
			throw new Throwable(toString(getClass(obj)));
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
			throw new Throwable(toString(getClass(obj)));
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
			throw new Throwable(toString(getClass(obj)));
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

	@Test
	void testGetAbsolutePath() throws Throwable {
		//
		Assertions.assertNull(getAbsolutePath(null));
		Assertions.assertNotNull(getAbsolutePath(new File("")));
		//
	}

	private static String getAbsolutePath(final File instance) throws Throwable {
		try {
			final Object obj = METHOD_GET_ABSOLUTE_PATH.invoke(null, instance);
			if (obj == null) {
				return null;
			} else if (obj instanceof String) {
				return (String) obj;
			}
			throw new Throwable(toString(getClass(obj)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testResizeImage() throws Throwable {
		//
		Assertions.assertNotNull(resizeImage(null, 1, 1));
		Assertions.assertNotNull(resizeImage(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB), 1, 1));
		//
	}

	private static BufferedImage resizeImage(final BufferedImage originalImage, final int targetWidth,
			final int targetHeight) throws Throwable {
		try {
			final Object obj = METHOD_RESIZE_IMAGE.invoke(null, originalImage, targetWidth, targetHeight);
			if (obj == null) {
				return null;
			} else if (obj instanceof BufferedImage) {
				return (BufferedImage) obj;
			}
			throw new Throwable(toString(getClass(obj)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testDrawImage() {
		Assertions.assertDoesNotThrow(() -> drawImage(null, null, 0, 0, null));
	}

	private static void drawImage(final Graphics instance, final Image img, final int x, final int y,
			final ImageObserver observer) throws Throwable {
		try {
			METHOD_DRAW_IMAGE.invoke(null, instance, img, x, y, observer);
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testValueOf() throws Throwable {
		//
		Assertions.assertNull(valueOf(null));
		Assertions.assertNull(valueOf(""));
		Assertions.assertNull(valueOf(" "));
		Assertions.assertNull(valueOf("A"));
		//
		final Integer one = Integer.valueOf(ONE);
		Assertions.assertSame(ONE, valueOf(Integer.toString(ONE)));
		//
	}

	private static Integer valueOf(final String instance) throws Throwable {
		try {
			final Object obj = METHOD_VALUE_OF.invoke(null, instance);
			if (obj == null) {
				return null;
			} else if (obj instanceof Integer) {
				return (Integer) obj;
			}
			throw new Throwable(toString(getClass(obj)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testIntValue() throws Throwable {
		//
		Assertions.assertEquals(0, intValue(null, 0));
		Assertions.assertEquals(Integer.valueOf(ONE), intValue(ONE, 0));
		//
	}

	private static int intValue(final Number instance, final int defaultValue) throws Throwable {
		try {
			final Object obj = METHOD_INT_VALUE.invoke(null, instance, defaultValue);
			if (obj instanceof Integer) {
				return ((Integer) obj).intValue();
			}
			throw new Throwable(toString(getClass(obj)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testAddWatermarkText() {
		//
		Assertions.assertDoesNotThrow(() -> addWatermarkText(null, null, null, null, 0));
		//
		final PDPage pdPage = new PDPage();
		Assertions.assertDoesNotThrow(() -> addWatermarkText(null, pdPage, null, "", 0));
		Assertions.assertDoesNotThrow(() -> addWatermarkText(new PDDocument(), pdPage, null, "", 0));
		Assertions.assertDoesNotThrow(() -> addWatermarkText(new PDDocument(), pdPage, PDType1Font.COURIER, "", 0));
		//
	}

	private static void addWatermarkText(final PDDocument doc, final PDPage page, final PDFont font, final String text,
			final int fontSize) throws Throwable {
		try {
			METHOD_ADD_WATER_MARK_TEXT.invoke(null, doc, page, font, text, fontSize);
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testGetPDFonts() throws Throwable {
		//
		Assertions.assertNotNull(getPDFonts());
		//
		Assertions.assertNull(getPDFonts(null));
		Assertions.assertNull(getPDFonts(new Field[] { null }));
		//
	}

	private static PDFont[] getPDFonts() throws Throwable {
		try {
			final Object obj = METHOD_GET_PD_FONTS0.invoke(null);
			if (obj == null) {
				return null;
			} else if (obj instanceof PDFont[]) {
				return (PDFont[]) obj;
			}
			throw new Throwable(toString(getClass(obj)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	private static PDFont[] getPDFonts(final Field[] fs) throws Throwable {
		try {
			final Object obj = METHOD_GET_PD_FONTS1.invoke(null, (Object) fs);
			if (obj == null) {
				return null;
			} else if (obj instanceof PDFont[]) {
				return (PDFont[]) obj;
			}
			throw new Throwable(toString(getClass(obj)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

}