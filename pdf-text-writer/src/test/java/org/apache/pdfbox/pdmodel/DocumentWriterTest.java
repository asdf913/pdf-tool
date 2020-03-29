package org.apache.pdfbox.pdmodel;

import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.MenuItem;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.Window;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.awt.geom.Dimension2D;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.EventObject;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

import javax.swing.AbstractButton;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextField;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.ProtectionPolicy;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDFontLike;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.common.base.Predicates;

class DocumentWriterTest {

	private static final String OWNER_PASSWORD = "OWNER_PASSWORD";

	private static Method METHOD_GET_WIDTH, METHOD_GET_FONTS, METHOD_GET_PAGE_SIZE_MAP, METHOD_CAST,
			METHOD_ADD_ACTION_LISTENER1, METHOD_ADD_ACTION_LISTENER2, METHOD_CREATE_PROTECTION_POLICY,
			METHOD_TEST_AND_GET2, METHOD_TEST_AND_GET3, METHOD_GET_FOREGROUND, METHOD_VALUE_OF, METHOD_GET_MAP,
			METHOD_GET_FIELD, METHOD_GET_SELECTED_ITEM, METHOD_SET_WIDTH, METHOD_CREATE_ACCESS_PERMISSION,
			METHOD_SET_ACCESSIBLE, METHOD_TO_LINES, METHOD_CHECK_PASSWORD, METHOD_PACK, METHOD_SET_VISIBLE,
			METHOD_CREATE_PROPERTIES_DIALOG, METHOD_CREATE_PERMISSION_DIALOG, METHOD_STREAM, METHOD_FILTER,
			METHOD_COLLECT, METHOD_GET_NAME, METHOD_CONTAINS_KEY, METHOD_GET_SOURCE, METHOD_SET_FORE_GROUND,
			METHOD_ACCEPT1, METHOD_ACCEPT2, METHOD_TEST_AND_APPLY, METHOD_GET_KEY, METHOD_GET_VALUE,
			METHOD_SET_SELECTED_ITEM, METHOD_FOR_EACH, METHOD_ADD, METHOD_GET_SYSTEM_CLIP_BOARD,
			METHOD_GET_FONT_METRICS, METHOD_ADD_WINDOW_LISTENER = null;

	@BeforeAll
	static void beforeAll() throws ReflectiveOperationException {
		//
		final Class<?> clz = DocumentWriter.class;
		//
		(METHOD_GET_WIDTH = clz.getDeclaredMethod("getWidth", Dimension2D.class, Double.TYPE)).setAccessible(true);
		//
		(METHOD_GET_FONTS = clz.getDeclaredMethod("getFonts", Field[].class)).setAccessible(true);
		//
		(METHOD_GET_PAGE_SIZE_MAP = clz.getDeclaredMethod("getPageSizeMap", Field[].class)).setAccessible(true);
		//
		(METHOD_CAST = clz.getDeclaredMethod("cast", Class.class, Object.class)).setAccessible(true);
		//
		(METHOD_ADD_ACTION_LISTENER1 = clz.getDeclaredMethod("addActionListener", ActionListener.class,
				AbstractButton[].class)).setAccessible(true);
		//
		(METHOD_ADD_ACTION_LISTENER2 = clz.getDeclaredMethod("addActionListener", ActionListener.class,
				MenuItem[].class)).setAccessible(true);
		//
		(METHOD_CREATE_PROTECTION_POLICY = clz.getDeclaredMethod("createProtectionPolicy", String.class, String.class,
				AccessPermission.class)).setAccessible(true);
		//
		(METHOD_TEST_AND_GET2 = clz.getDeclaredMethod("testAndGet", Predicate.class, Object[].class))
				.setAccessible(true);
		//
		(METHOD_TEST_AND_GET3 = clz.getDeclaredMethod("testAndGet", Predicate.class, Object.class, Supplier.class))
				.setAccessible(true);
		//
		(METHOD_GET_FOREGROUND = clz.getDeclaredMethod("getForeground", Component.class)).setAccessible(true);
		//
		(METHOD_VALUE_OF = clz.getDeclaredMethod("valueOf", String.class)).setAccessible(true);
		//
		(METHOD_GET_MAP = clz.getDeclaredMethod("get", Map.class, Object.class)).setAccessible(true);
		//
		(METHOD_GET_FIELD = clz.getDeclaredMethod("get", Field.class, Object.class)).setAccessible(true);
		//
		(METHOD_GET_SELECTED_ITEM = clz.getDeclaredMethod("getSelectedItem", ComboBoxModel.class)).setAccessible(true);
		//
		(METHOD_SET_WIDTH = clz.getDeclaredMethod("setWidth", Integer.TYPE, Component[].class)).setAccessible(true);
		//
		(METHOD_CREATE_ACCESS_PERMISSION = clz.getDeclaredMethod("createAccessPermission", Object.class, Field[].class))
				.setAccessible(true);
		//
		(METHOD_SET_ACCESSIBLE = clz.getDeclaredMethod("setAccessible", AccessibleObject.class)).setAccessible(true);
		//
		(METHOD_TO_LINES = clz.getDeclaredMethod("toLines", String.class, PDFont.class, Integer.class, Float.TYPE))
				.setAccessible(true);
		//
		(METHOD_CHECK_PASSWORD = clz.getDeclaredMethod("checkPassword", CharSequence.class, CharSequence.class))
				.setAccessible(true);
		//
		(METHOD_PACK = clz.getDeclaredMethod("pack", Window.class)).setAccessible(true);
		//
		(METHOD_SET_VISIBLE = clz.getDeclaredMethod("setVisible", Component.class, Boolean.TYPE)).setAccessible(true);
		//
		(METHOD_CREATE_PROPERTIES_DIALOG = clz.getDeclaredMethod("createPropertiesDialog")).setAccessible(true);
		//
		(METHOD_CREATE_PERMISSION_DIALOG = clz.getDeclaredMethod("createPermissionDialog")).setAccessible(true);
		//
		(METHOD_STREAM = clz.getDeclaredMethod("stream", Object[].class)).setAccessible(true);
		//
		(METHOD_FILTER = clz.getDeclaredMethod("filter", Stream.class, Predicate.class)).setAccessible(true);
		//
		(METHOD_COLLECT = clz.getDeclaredMethod("collect", Stream.class, Collector.class)).setAccessible(true);
		//
		(METHOD_GET_NAME = clz.getDeclaredMethod("getName", PDFontLike.class)).setAccessible(true);
		//
		(METHOD_CONTAINS_KEY = clz.getDeclaredMethod("containsKey", Map.class, Object.class)).setAccessible(true);
		//
		(METHOD_GET_SOURCE = clz.getDeclaredMethod("getSource", EventObject.class)).setAccessible(true);
		//
		(METHOD_SET_FORE_GROUND = clz.getDeclaredMethod("setForeground", Component.class, Color.class))
				.setAccessible(true);
		//
		(METHOD_ACCEPT1 = clz.getDeclaredMethod("accept", Iterable.class, Object.class)).setAccessible(true);
		//
		(METHOD_ACCEPT2 = clz.getDeclaredMethod("accept", Consumer.class, Object.class)).setAccessible(true);
		//
		(METHOD_TEST_AND_APPLY = clz.getDeclaredMethod("testAndApply", Object.class, Predicate.class, Function.class,
				Object.class)).setAccessible(true);
		//
		(METHOD_GET_KEY = clz.getDeclaredMethod("getKey", Entry.class)).setAccessible(true);
		//
		(METHOD_GET_VALUE = clz.getDeclaredMethod("getValue", Entry.class)).setAccessible(true);
		//
		(METHOD_SET_SELECTED_ITEM = clz.getDeclaredMethod("setSelectedItem", JComboBox.class, Object.class))
				.setAccessible(true);
		//
		(METHOD_FOR_EACH = clz.getDeclaredMethod("forEach", Stream.class, Consumer.class)).setAccessible(true);
		//
		(METHOD_ADD = clz.getDeclaredMethod("add", SystemTray.class, TrayIcon.class)).setAccessible(true);
		//
		(METHOD_GET_SYSTEM_CLIP_BOARD = clz.getDeclaredMethod("getSystemClipboard", Toolkit.class)).setAccessible(true);
		//
		(METHOD_GET_FONT_METRICS = clz.getDeclaredMethod("getFontMetrics", Graphics.class)).setAccessible(true);
		//
		(METHOD_ADD_WINDOW_LISTENER = clz.getDeclaredMethod("addWindowListener", Window.class, WindowListener.class))
				.setAccessible(true);
		//
	}

	private DocumentWriter instance = null;

	@BeforeEach
	void beforeEach() throws ReflectiveOperationException {
		//
		final Constructor<DocumentWriter> constructor = DocumentWriter.class.getDeclaredConstructor();
		if (constructor != null && !constructor.isAccessible()) {
			constructor.setAccessible(true);
		}
		instance = constructor != null ? constructor.newInstance() : null;
		//
	}

	@Test
	void testAfterPropertiesSet() throws Exception {
		Assertions.assertDoesNotThrow(() -> instance.afterPropertiesSet());
	}

	@Test
	void testActionPerformed() throws IllegalAccessException {
		//
		Assertions.assertDoesNotThrow(() -> instance.actionPerformed(new ActionEvent("", 1, null)));
		//
		final AbstractButton btnCopy = new JButton();
		FieldUtils.writeDeclaredField(instance, "btnCopy", btnCopy, true);
		Assertions.assertDoesNotThrow(() -> instance.actionPerformed(new ActionEvent(btnCopy, 1, null)));
		//
		final MenuItem showMenuItem = new MenuItem();
		FieldUtils.writeDeclaredField(instance, "showMenuItem", showMenuItem, true);
		final ActionEvent actionEvent = new ActionEvent(showMenuItem, 1, null);
		Assertions.assertDoesNotThrow(() -> instance.actionPerformed(actionEvent));
		//
		final MenuItem exitMenuItem = new MenuItem();
		FieldUtils.writeDeclaredField(instance, "exitMenuItem", exitMenuItem, true);
		Assertions.assertDoesNotThrow(() -> instance.actionPerformed(new ActionEvent(exitMenuItem, 1, null)));
		//
		final JFrame jFrame = new JFrame();
		instance.setjFrame(jFrame);
		Assertions.assertDoesNotThrow(() -> instance.actionPerformed(actionEvent));
		//
	}

	@Test
	void testGetWidth() throws Throwable {
		//
		final int width = 100;
		Assertions.assertEquals(width, getWidth(null, width));
		//
	}

	private static double getWidth(final Dimension2D instance, final double defaultValue) throws Throwable {
		try {
			final Object obj = METHOD_GET_WIDTH.invoke(null, instance, defaultValue);
			if (obj instanceof Double) {
				return ((Double) obj).doubleValue();
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
	void testGetFonts() throws Throwable {
		//
		Assertions.assertNull(getFonts(null));
		Assertions.assertNull(getFonts(new Field[] { null }));
		//
	}

	private static PDFont[] getFonts(final Field[] fs) throws Throwable {
		try {
			final Object obj = METHOD_GET_FONTS.invoke(null, (Object) fs);
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

	@Test
	void testGetPageSizeMap() throws Throwable {
		//
		Assertions.assertNull(getPageSizeMap(null));
		Assertions.assertNull(getPageSizeMap(new Field[] { null }));
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
		Assertions.assertNull(cast(null, null));
	}

	private static <T> T cast(final Class<T> clz, final Object instance) throws Throwable {
		try {
			return (T) METHOD_CAST.invoke(null, clz, instance);
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testGddActionListener() {
		//
		Assertions.assertDoesNotThrow(() -> addActionListener(null, (AbstractButton[]) null));
		Assertions.assertDoesNotThrow(() -> addActionListener(null, (AbstractButton) null));
		//
		Assertions.assertDoesNotThrow(() -> addActionListener(null, (MenuItem[]) null));
		Assertions.assertDoesNotThrow(() -> addActionListener(null, (MenuItem) null));
		//
	}

	private static void addActionListener(final ActionListener actionListener, final AbstractButton... bs)
			throws Throwable {
		try {
			METHOD_ADD_ACTION_LISTENER1.invoke(null, actionListener, bs);
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	private static void addActionListener(final ActionListener actionListener, final MenuItem... bs) throws Throwable {
		try {
			METHOD_ADD_ACTION_LISTENER2.invoke(null, actionListener, bs);
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testCreateProtectionPolicy() throws Throwable {
		//
		Assertions.assertEquals(
				"StandardProtectionPolicy[ownerPassword=<null>,permissions=<null>,preferAES=true,userPassword=<null>,encryptionKeyLength=128]",
				ToStringBuilder.reflectionToString(createProtectionPolicy(null, null, null),
						ToStringStyle.SHORT_PREFIX_STYLE));
		//
		final String userPassword = "userPassword";
		final AccessPermission accessPermission = new AccessPermission();
		//
		Assertions.assertEquals(String.format(
				"StandardProtectionPolicy[ownerPassword=%1$s,permissions=%2$s,preferAES=true,userPassword=%3$s,encryptionKeyLength=128]",
				OWNER_PASSWORD, accessPermission, userPassword),
				ToStringBuilder.reflectionToString(
						createProtectionPolicy(OWNER_PASSWORD, userPassword, accessPermission),
						ToStringStyle.SHORT_PREFIX_STYLE));
		//
	}

	private static ProtectionPolicy createProtectionPolicy(final String ownerPassword, final String userPassword,
			final AccessPermission accessPermission) throws Throwable {
		try {
			final Object obj = METHOD_CREATE_PROTECTION_POLICY.invoke(null, ownerPassword, userPassword,
					accessPermission);
			if (obj == null) {
				return null;
			} else if (obj instanceof ProtectionPolicy) {
				return (ProtectionPolicy) obj;
			}
			throw new Throwable(toString(getClass(obj)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testTestAndGet() throws Throwable {
		//
		Assertions.assertNull(testAndGet(null, (Object[]) null));
		Assertions.assertNull(testAndGet(null, (Object) null));
		Assertions.assertNull(testAndGet(Predicates.alwaysFalse(), (Object) null));
		//
		Assertions.assertSame(OWNER_PASSWORD, testAndGet(Predicates.alwaysTrue(), OWNER_PASSWORD));
		//
		Assertions.assertNull(testAndGet(null, null, null));
		Assertions.assertNull(testAndGet(x -> x != null, null, null));
		//
	}

	private static <T> T testAndGet(final Predicate<T> predicate, final T... items) throws Throwable {
		try {
			return (T) METHOD_TEST_AND_GET2.invoke(null, predicate, items);
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	private static <T> T testAndGet(final Predicate<Object> predicate, final T value, final Supplier<T> supplier)
			throws Throwable {
		try {
			return (T) METHOD_TEST_AND_GET3.invoke(null, predicate, value, supplier);
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testGetForeground() throws Throwable {
		Assertions.assertNull(getForeground(null));
	}

	private static Color getForeground(final Component instance) throws Throwable {
		try {
			final Object obj = METHOD_GET_FOREGROUND.invoke(null, instance);
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

	@Test
	void testValueOf() throws Throwable {
		//
		Assertions.assertNull(valueOf(null));
		Assertions.assertNull(valueOf(""));
		//
		final int one = 1;
		Assertions.assertSame(Integer.valueOf(one), valueOf(Integer.toString(one)));
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
	void testGet() throws Throwable {
		//
		Assertions.assertNull(get((Map<?, ?>) null, null));
		Assertions.assertSame(OWNER_PASSWORD, get(Collections.singletonMap(null, OWNER_PASSWORD), null));
		//
		Assertions.assertNull(get((Field) null, null));
		//
	}

	private static <V> V get(final Map<?, V> instance, final Object key) throws Throwable {
		try {
			return (V) METHOD_GET_MAP.invoke(null, instance, key);
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	private static Object get(final Field field, final Object instance) throws Throwable {
		try {
			return METHOD_GET_FIELD.invoke(null, field, instance);
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testGetSelectedItem() throws Throwable {
		//
		Assertions.assertNull(getSelectedItem(null));
		//
		Assertions.assertSame(OWNER_PASSWORD,
				getSelectedItem(new DefaultComboBoxModel<>(new Object[] { OWNER_PASSWORD })));
		//
	}

	private static Object getSelectedItem(final ComboBoxModel<?> instance) throws Throwable {
		try {
			return (Object) METHOD_GET_SELECTED_ITEM.invoke(null, instance);
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testSetWidth() {
		//
		Assertions.assertDoesNotThrow(() -> setWidth(0, (Component[]) null));
		Assertions.assertDoesNotThrow(() -> setWidth(0, (Component) null));
		//
	}

	private static void setWidth(final int width, final Component... cs) throws Throwable {
		try {
			METHOD_SET_WIDTH.invoke(null, width, cs);
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testCreateAccessPermission() throws Throwable {
		//
		final Function<Object, String> toString = item -> ToStringBuilder.reflectionToString(item,
				ToStringStyle.SHORT_PREFIX_STYLE);
		//
		Assertions.assertEquals("AccessPermission[bytes=-4,readOnly=false]",
				toString.apply(createAccessPermission(null, null)));
		//
		final Field field = DocumentWriter.class.getDeclaredField("canAssembleDocument");
		final Field[] fields = new Field[] { null, String.class.getDeclaredField("value"), field };
		//
		Assertions.assertEquals("AccessPermission[bytes=-4,readOnly=false]",
				toString.apply(createAccessPermission(null, fields)));
		Assertions.assertEquals("AccessPermission[bytes=-4,readOnly=false]",
				toString.apply(createAccessPermission(instance, fields)));
		//
		setAccessible(field);
		//
		final ComboBoxModel<?> canAssembleDocument = new DefaultComboBoxModel<>(
				new Boolean[] { null, Boolean.FALSE, Boolean.TRUE });
		field.set(instance, canAssembleDocument);
		//
		Assertions.assertEquals("AccessPermission[bytes=-4,readOnly=false]",
				toString.apply(createAccessPermission(null, fields)));
		Assertions.assertEquals("AccessPermission[bytes=-4,readOnly=false]",
				toString.apply(createAccessPermission(instance, fields)));
		//
		canAssembleDocument.setSelectedItem(Boolean.FALSE);
		Assertions.assertEquals("AccessPermission[bytes=-4,readOnly=false]",
				toString.apply(createAccessPermission(null, fields)));
		Assertions.assertEquals("AccessPermission[bytes=-1028,readOnly=false]",
				toString.apply(createAccessPermission(instance, fields)));
		//
	}

	private static AccessPermission createAccessPermission(final Object instance, final Field[] fs) throws Throwable {
		try {
			final Object obj = METHOD_CREATE_ACCESS_PERMISSION.invoke(null, instance, fs);
			if (obj == null) {
				return null;
			} else if (obj instanceof AccessPermission) {
				return (AccessPermission) obj;
			}
			throw new Throwable(toString(getClass(obj)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testSetAccessible() {
		Assertions.assertDoesNotThrow(() -> setAccessible(null));
	}

	private static void setAccessible(final AccessibleObject instance) throws Throwable {
		try {
			METHOD_SET_ACCESSIBLE.invoke(null, instance);
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testToLines() throws Throwable {
		//
		Assertions.assertNull(toLines(null, null, null, 0));
		Assertions.assertNull(toLines("", null, null, 0));
		//
		Assertions.assertEquals(Arrays.asList(OWNER_PASSWORD), toLines(OWNER_PASSWORD, null, null, 0));
		Assertions.assertEquals(Arrays.asList(OWNER_PASSWORD), toLines(OWNER_PASSWORD, null, 1, 0));
		//
		final String line2 = "line 2";
		Assertions.assertEquals(Arrays.asList(OWNER_PASSWORD, line2),
				toLines(OWNER_PASSWORD + "\n" + line2, null, 1, 0));
		//
	}

	private static List<String> toLines(final String input, final PDFont font, final Integer fontSize,
			final float width) throws Throwable {
		try {
			final Object obj = METHOD_TO_LINES.invoke(null, input, font, fontSize, width);
			if (obj == null) {
				return null;
			} else if (obj instanceof List) {
				return (List) obj;
			}
			throw new Throwable(toString(getClass(obj)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testCheckPassword() throws Throwable {
		//
		Assertions.assertTrue(checkPassword(null, null));
		Assertions.assertTrue(checkPassword(null, ""));
		Assertions.assertTrue(checkPassword("", null));
		Assertions.assertTrue(checkPassword("", ""));
		//
		Assertions.assertFalse(checkPassword(" ", null));
		Assertions.assertFalse(checkPassword(null, " "));
		Assertions.assertTrue(checkPassword(" ", " "));
		//
	}

	private static boolean checkPassword(final CharSequence password1, final CharSequence password2) throws Throwable {
		try {
			final Object obj = METHOD_CHECK_PASSWORD.invoke(null, password1, password2);
			if (obj instanceof Boolean) {
				return ((Boolean) obj).booleanValue();
			}
			throw new Throwable(toString(getClass(obj)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testPack() {
		//
		Assertions.assertDoesNotThrow(() -> pack(null));
		Assertions.assertDoesNotThrow(() -> pack(new JFrame()));
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
		Assertions.assertDoesNotThrow(() -> setVisible(null, true));
		//
		if (!GraphicsEnvironment.isHeadless()) {
			Assertions.assertDoesNotThrow(() -> setVisible(new JFrame(), true));
		}
		//
	}

	private static void setVisible(final Component instance, final boolean flag) throws Throwable {
		try {
			METHOD_SET_VISIBLE.invoke(null, instance, flag);
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testCreatePropertiesDialog() throws Throwable {
		//
		final JDialog instance1 = createPropertiesDialog();
		Assertions.assertNotNull(instance1);
		Assertions.assertNotSame(instance1, createPropertiesDialog());
		//
	}

	private JDialog createPropertiesDialog() throws Throwable {
		try {
			final Object obj = METHOD_CREATE_PROPERTIES_DIALOG.invoke(instance);
			if (obj == null) {
				return null;
			} else if (obj instanceof JDialog) {
				return (JDialog) obj;
			}
			throw new Throwable(toString(getClass(obj)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testCreatePermissionDialog() throws Throwable {
		//
		if (!GraphicsEnvironment.isHeadless()) {
			//
			final JDialog instance1 = createPermissionDialog();
			Assertions.assertNotNull(instance1);
			Assertions.assertNotSame(instance1, createPermissionDialog());
			//
		}
		//
	}

	private JDialog createPermissionDialog() throws Throwable {
		try {
			final Object obj = METHOD_CREATE_PERMISSION_DIALOG.invoke(instance);
			if (obj == null) {
				return null;
			} else if (obj instanceof JDialog) {
				return (JDialog) obj;
			}
			throw new Throwable(toString(getClass(obj)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testStream() throws Throwable {
		Assertions.assertNull(stream(null));
	}

	private static <T> Stream<T> stream(final T[] instance) throws Throwable {
		try {
			final Object obj = METHOD_STREAM.invoke(null, (Object) instance);
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
	void testFilter() throws Throwable {
		//
		Assertions.assertNull(filter(null, null));
		Assertions.assertNull(filter(Stream.of(), null));
		//
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
		//
		Assertions.assertNull(collect(null, null));
		Assertions.assertNull(collect(Stream.of(), null));
		//
	}

	private static <T, R, A> R collect(final Stream<T> instance, final Collector<? super T, A, R> collector)
			throws Throwable {
		try {
			return (R) METHOD_COLLECT.invoke(null, instance, collector);
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testGetName() throws Throwable {
		Assertions.assertNull(getName(null));
	}

	private static String getName(final PDFontLike instance) throws Throwable {
		try {
			final Object obj = METHOD_GET_NAME.invoke(null, instance);
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
	void testContainsKey() throws Throwable {
		//
		Assertions.assertFalse(containsKey(null, null));
		Assertions.assertTrue(containsKey(Collections.singletonMap(OWNER_PASSWORD, null), OWNER_PASSWORD));
		//
	}

	private static boolean containsKey(final Map<?, ?> instance, final Object key) throws Throwable {
		try {
			final Object obj = METHOD_CONTAINS_KEY.invoke(null, instance, key);
			if (obj instanceof Boolean) {
				return ((Boolean) obj).booleanValue();
			}
			throw new Throwable(toString(getClass(obj)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testGetSource() throws Throwable {
		Assertions.assertNull(getSource(null));
	}

	private static Object getSource(final EventObject instance) throws Throwable {
		try {
			return METHOD_GET_SOURCE.invoke(null, instance);
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testSetForeground() {
		//
		Assertions.assertDoesNotThrow(() -> setForeground(null, null));
		Assertions.assertDoesNotThrow(() -> setForeground(new JTextField(), null));
		//
	}

	private static void setForeground(final Component instance, final Color color) throws Throwable {
		try {
			METHOD_SET_FORE_GROUND.invoke(null, instance, color);
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testAccept() {
		//
		Assertions.assertDoesNotThrow(() -> accept((Iterable<Consumer<Object>>) null, null));
		Assertions.assertDoesNotThrow(() -> accept((Consumer<Object>) null, null));
		Assertions.assertDoesNotThrow(() -> accept(i -> {
		}, null));
		Assertions.assertDoesNotThrow(() -> accept(Collections.singleton(null), null));
		//
	}

	private static <T> void accept(final Iterable<Consumer<T>> consumers, final T value) throws Throwable {
		try {
			METHOD_ACCEPT1.invoke(null, consumers, value);
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	private static <T> void accept(final Consumer<T> instance, final T value) throws Throwable {
		try {
			METHOD_ACCEPT2.invoke(null, instance, value);
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testTestAndApply() throws Throwable {
		//
		Assertions.assertNull(testAndApply(null, null, null, null));
		Assertions.assertSame(OWNER_PASSWORD, testAndApply(null, null, null, OWNER_PASSWORD));
		Assertions.assertSame(OWNER_PASSWORD, testAndApply(null, Predicates.alwaysTrue(), null, OWNER_PASSWORD));
		Assertions.assertNull(testAndApply(null, Predicates.alwaysTrue(), a -> null, OWNER_PASSWORD));
		//
	}

	private static <T, R> R testAndApply(final T value, final Predicate<T> predicate, final Function<T, R> function,
			final R defaultValue) throws Throwable {
		try {
			return (R) METHOD_TEST_AND_APPLY.invoke(null, value, predicate, function, defaultValue);
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testGetKey() throws Throwable {
		Assertions.assertNull(getKey(null));
	}

	private static <K> K getKey(final Entry<K, ?> instance) throws Throwable {
		try {
			return (K) METHOD_GET_KEY.invoke(null, instance);
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testGetValue() throws Throwable {
		Assertions.assertNull(getValue(null));
	}

	private static <V> V getValue(final Entry<?, V> instance) throws Throwable {
		try {
			return (V) METHOD_GET_VALUE.invoke(null, instance);
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testSetSelectedItem() {
		Assertions.assertDoesNotThrow(() -> setSelectedItem(null, null));
	}

	private static void setSelectedItem(final JComboBox<?> instance, final Object selectedItem) throws Throwable {
		try {
			METHOD_SET_SELECTED_ITEM.invoke(null, instance, selectedItem);
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testForEach() {
		Assertions.assertDoesNotThrow(() -> forEach(Stream.of(), null));
	}

	private static <T> void forEach(final Stream<T> instance, Consumer<? super T> action) throws Throwable {
		try {
			METHOD_FOR_EACH.invoke(null, instance, action);
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testAdd() {
		//
		Assertions.assertDoesNotThrow(() -> add(null, null));
		//
		if (SystemTray.isSupported()) {
			Assertions.assertDoesNotThrow(() -> add(SystemTray.getSystemTray(), null));
		}
		//
	}

	private static void add(final SystemTray instance, final TrayIcon trayIcon) throws Throwable {
		try {
			METHOD_ADD.invoke(null, instance, trayIcon);
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testGetSystemClipboard() throws Throwable {
		Assertions.assertNull(getSystemClipboard(null));
	}

	private static Clipboard getSystemClipboard(final Toolkit instance) throws Throwable {
		try {
			final Object obj = METHOD_GET_SYSTEM_CLIP_BOARD.invoke(null, instance);
			if (obj == null) {
				return null;
			} else if (obj instanceof Clipboard) {
				return (Clipboard) obj;
			}
			throw new Throwable(toString(getClass(obj)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testGetFontMetrics() throws Throwable {
		Assertions.assertNull(getFontMetrics(null));
	}

	private static FontMetrics getFontMetrics(final Graphics instance) throws Throwable {
		try {
			final Object obj = METHOD_GET_FONT_METRICS.invoke(null, instance);
			if (obj == null) {
				return null;
			} else if (obj instanceof FontMetrics) {
				return (FontMetrics) obj;
			}
			throw new Throwable(toString(getClass(obj)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testInnerWindowAdapter() throws Throwable {
		//
		final Class<?> clz = Class.forName("org.apache.pdfbox.pdmodel.DocumentWriter$InnerWindowAdapter");
		final Constructor<?> constructor = clz != null ? clz.getDeclaredConstructor(DocumentWriter.class) : null;
		setAccessible(constructor);
		final WindowListener windowListener = cast(WindowListener.class,
				constructor != null ? constructor.newInstance(instance) : null);
		//
		Assertions.assertDoesNotThrow(() -> {
			if (windowListener != null) {
				windowListener.windowClosing(null);
			}
		});
		//
	}

	@Test
	void testAddWindowListener() {
		Assertions.assertDoesNotThrow(() -> addWindowListener(new Window(null), null));
	}

	private static synchronized void addWindowListener(final Window instance, final WindowListener windowListener)
			throws Throwable {
		try {
			METHOD_ADD_WINDOW_LISTENER.invoke(null, instance, windowListener);
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

}