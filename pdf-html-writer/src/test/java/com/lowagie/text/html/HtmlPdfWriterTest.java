package com.lowagie.text.html;

import java.awt.Component;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Dimension2D;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.EventObject;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextField;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.lowagie.text.Document;
import com.lowagie.text.ExceptionConverter;

class HtmlPdfWriterTest {

	private static final int ONE = 1;

	private static Method METHOD_WRITE_HTML_FILE_TO_PDF_FILE, METHOD_ADD_ACTION_LISTENER, METHOD_ADD2, METHOD_ADD3,
			METHOD_GET_SYSTEM_CLIP_BOARD, METHOD_SET_CONTENTS, METHOD_GET_PERMISSIONS, METHOD_KEY_SET, METHOD_TO_ARRAY,
			METHOD_SET_WIDTH, METHOD_GET, METHOD_GET_SELECTED_ITEM, METHOD_OR, METHOD_GET_BYTES, METHOD_LENGTH,
			METHOD_SET_ENCRYPTION, METHOD_SET_META_DATA, METHOD_CREATE_PROPERTIES_DIALOG,
			METHOD_CREATE_PERMISSION_DIALOG, METHOD_GET_SOURCE, METHOD_PACK, METHOD_SET_VISIBLE, METHOD_GET_WIDTH,
			METHOD_TEST_AND_GET, METHOD_CAST, METHOD_IS_SELECTED = null;

	@BeforeAll
	static void beforeAll() throws ReflectiveOperationException {
		//
		final Class<?> clz = HtmlPdfWriter.class;
		//
		(METHOD_WRITE_HTML_FILE_TO_PDF_FILE = clz.getDeclaredMethod("writeHtmlFileToPdfFile", File.class,
				Consumer.class, File.class)).setAccessible(true);
		//
		(METHOD_ADD_ACTION_LISTENER = clz.getDeclaredMethod("addActionListener", ActionListener.class,
				AbstractButton[].class)).setAccessible(true);
		//
		(METHOD_ADD2 = clz.getDeclaredMethod("add", Container.class, Component.class)).setAccessible(true);
		//
		(METHOD_ADD3 = clz.getDeclaredMethod("add", Container.class, Component.class, Object.class))
				.setAccessible(true);
		//
		(METHOD_GET_SYSTEM_CLIP_BOARD = clz.getDeclaredMethod("getSystemClipboard", Toolkit.class)).setAccessible(true);
		//
		(METHOD_SET_CONTENTS = clz.getDeclaredMethod("setContents", Clipboard.class, Transferable.class,
				ClipboardOwner.class)).setAccessible(true);
		//
		(METHOD_GET_PERMISSIONS = clz.getDeclaredMethod("getPermissions", Field[].class)).setAccessible(true);
		//
		(METHOD_KEY_SET = clz.getDeclaredMethod("keySet", Map.class)).setAccessible(true);
		//
		(METHOD_TO_ARRAY = clz.getDeclaredMethod("toArray", Collection.class, Object[].class)).setAccessible(true);
		//
		(METHOD_SET_WIDTH = clz.getDeclaredMethod("setWidth", Integer.TYPE, Component[].class)).setAccessible(true);
		//
		(METHOD_GET = clz.getDeclaredMethod("get", Map.class, Object.class)).setAccessible(true);
		//
		(METHOD_GET_SELECTED_ITEM = clz.getDeclaredMethod("getSelectedItem", JComboBox.class)).setAccessible(true);
		//
		(METHOD_OR = clz.getDeclaredMethod("or", int[].class)).setAccessible(true);
		//
		(METHOD_GET_BYTES = clz.getDeclaredMethod("getBytes", String.class)).setAccessible(true);
		//
		(METHOD_LENGTH = clz.getDeclaredMethod("length", byte[].class, Integer.TYPE)).setAccessible(true);
		//
		(METHOD_SET_ENCRYPTION = clz.getDeclaredMethod("setEncryption", File.class, byte[].class, byte[].class,
				Integer.TYPE, Integer.TYPE)).setAccessible(true);
		//
		(METHOD_SET_META_DATA = clz.getDeclaredMethod("setMetaData", Document.class)).setAccessible(true);
		//
		(METHOD_CREATE_PROPERTIES_DIALOG = clz.getDeclaredMethod("createPropertiesDialog")).setAccessible(true);
		//
		(METHOD_CREATE_PERMISSION_DIALOG = clz.getDeclaredMethod("createPermissionDialog")).setAccessible(true);
		//
		(METHOD_GET_SOURCE = clz.getDeclaredMethod("getSource", EventObject.class)).setAccessible(true);
		//
		(METHOD_PACK = clz.getDeclaredMethod("pack", Window.class)).setAccessible(true);
		//
		(METHOD_SET_VISIBLE = clz.getDeclaredMethod("setVisible", Component.class, Boolean.TYPE)).setAccessible(true);
		//
		(METHOD_GET_WIDTH = clz.getDeclaredMethod("getWidth", Dimension2D.class, Double.TYPE)).setAccessible(true);
		//
		(METHOD_TEST_AND_GET = clz.getDeclaredMethod("testAndGet", Predicate.class, Object.class, Supplier.class))
				.setAccessible(true);
		//
		(METHOD_CAST = clz.getDeclaredMethod("cast", Class.class, Object.class)).setAccessible(true);
		//
		(METHOD_IS_SELECTED = clz.getDeclaredMethod("isSelected", AbstractButton.class)).setAccessible(true);
		//
	}

	private HtmlPdfWriter instance = null;

	private Clipboard systemClipboard = null;

	@BeforeEach
	void beforeEach() throws Throwable {
		//
		instance = new HtmlPdfWriter();
		//
		systemClipboard = getSystemClipboard(Toolkit.getDefaultToolkit());
		//
	}

	@Test
	void testActionPerformed() throws IllegalAccessException {
		//
		Assertions.assertDoesNotThrow(() -> instance.actionPerformed(new ActionEvent("", 0, null)));
		//
		final JComboBox<?> cbAllowAll = new JComboBox<>();
		FieldUtils.writeDeclaredField(instance, "cbAllowAll", cbAllowAll, true);
		Assertions.assertDoesNotThrow(() -> instance.actionPerformed(new ActionEvent(cbAllowAll, 0, null)));
		//
	}

	@Test
	void testAfterPropertiesSet() {
		Assertions.assertDoesNotThrow(() -> instance.afterPropertiesSet());
	}

	@Test
	void testMain() {
		//
		Assertions.assertThrows(IllegalArgumentException.class, () -> HtmlPdfWriter.main(null));
		Assertions.assertThrows(IllegalArgumentException.class, () -> HtmlPdfWriter.main(new String[0]));
		Assertions.assertThrows(IllegalArgumentException.class, () -> HtmlPdfWriter.main(new String[] { null }));
		Assertions.assertDoesNotThrow(() -> HtmlPdfWriter.main(new String[] { "." }));
		//
	}

	@Test
	void testWriteHtmlFileToPdfFile() {
		//
		Assertions.assertThrows(ExceptionConverter.class, () -> writeHtmlFileToPdfFile(null, null, null));
		Assertions.assertThrows(ExceptionConverter.class, () -> writeHtmlFileToPdfFile(null, d -> {
		}, null));
		//
	}

	private static void writeHtmlFileToPdfFile(final File input, final Consumer<Document> consumer, final File output)
			throws Throwable {
		try {
			METHOD_WRITE_HTML_FILE_TO_PDF_FILE.invoke(null, input, consumer, output);
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testAddActionListener() {
		//
		Assertions.assertDoesNotThrow(() -> addActionListener(null, (AbstractButton[]) null));
		Assertions.assertDoesNotThrow(() -> addActionListener(null, (AbstractButton) null));
		//
	}

	private void addActionListener(final ActionListener actionListener, final AbstractButton... buttons)
			throws Throwable {
		try {
			METHOD_ADD_ACTION_LISTENER.invoke(null, actionListener, buttons);
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testAdd() {
		//
		final Container container = new Container();
		Assertions.assertDoesNotThrow(() -> add(container, null));
		Assertions.assertDoesNotThrow(() -> add(container, null, null));
		//
		final Component component = new JTextField();
		Assertions.assertDoesNotThrow(() -> add(container, component));
		Assertions.assertDoesNotThrow(() -> add(container, component, null));
		//
	}

	private static void add(final Container container, final Component component) throws Throwable {
		try {
			METHOD_ADD2.invoke(null, container, component);
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	private static void add(final Container container, final Component component, final Object object)
			throws Throwable {
		try {
			METHOD_ADD3.invoke(null, container, component, object);
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

	private static String toString(final Object instance) {
		return instance != null ? instance.toString() : null;
	}

	@Test
	void testSetContents() {
		//
		Assertions.assertDoesNotThrow(() -> setContents(null, null, null));
		//
		Assertions.assertDoesNotThrow(() -> setContents(systemClipboard, null, null));
		Assertions.assertDoesNotThrow(() -> setContents(systemClipboard, new StringSelection(null), null));
		//
	}

	private static void setContents(final Clipboard instance, final Transferable contents, final ClipboardOwner owner)
			throws Throwable {
		try {
			METHOD_SET_CONTENTS.invoke(null, instance, contents, owner);
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testGetPermissions() throws Throwable {
		//
		Assertions.assertNull(getPermissions(null));
		Assertions.assertNull(getPermissions(new Field[] { null }));
		//
	}

	private static int[] getPermissions(final Field[] input) throws Throwable {
		try {
			final Object obj = METHOD_GET_PERMISSIONS.invoke(null, (Object) input);
			if (obj == null) {
				return null;
			} else if (obj instanceof int[]) {
				return (int[]) obj;
			}
			throw new Throwable(toString(getClass(obj)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testKeySet() throws Throwable {
		Assertions.assertNull(keySet(null));
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
	}

	private static <T> T[] toArray(final Collection<T> instance, final T[] array) throws Throwable {
		try {
			return (T[]) METHOD_TO_ARRAY.invoke(null, instance, array);
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testSetWidth() {
		//
		Assertions.assertDoesNotThrow(() -> setWidth(1, (Component[]) null));
		Assertions.assertDoesNotThrow(() -> setWidth(1, (Component) null));
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
	void testGet() throws Throwable {
		//
		Assertions.assertNull(get(null, null));
		//
		final Object key = new Object(), value = new Object();
		final Map<Object, Object> map = Collections.singletonMap(key, value);
		Assertions.assertSame(value, get(map, key));
		//
	}

	private static <V> V get(final Map<?, V> instance, final Object key) throws Throwable {
		try {
			return (V) METHOD_GET.invoke(null, instance, key);
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testGetSelectedItem() throws Throwable {
		//
		Assertions.assertNull(getSelectedItem(null));
		Assertions.assertNull(getSelectedItem(new JComboBox<>()));
		//
	}

	private static Object getSelectedItem(final JComboBox<?> instance) throws Throwable {
		try {
			return METHOD_GET_SELECTED_ITEM.invoke(null, instance);
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testOr() throws Throwable {
		//
		Assertions.assertThrows(IllegalArgumentException.class, () -> or(null));
		//
		Assertions.assertEquals(ONE, or(new int[] { ONE }));
		//
		final int two = 2;
		Assertions.assertEquals(ONE + two, or(new int[] { ONE, two }));
		//
	}

	private static int or(final int[] instance) throws Throwable {
		try {
			final Object obj = METHOD_OR.invoke(null, instance);
			if (obj instanceof Integer) {
				return ((Integer) obj).intValue();
			}
			throw new Throwable(toString(getClass(obj)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	private static Class<?> getClass(final Object instance) {
		return instance != null ? instance.getClass() : null;
	}

	@Test
	void testGetBytes() throws Throwable {
		//
		Assertions.assertNull(getBytes(null));
		Assertions.assertArrayEquals(new byte[0], getBytes(""));
		//
	}

	private static byte[] getBytes(final String instance) throws Throwable {
		try {
			final Object obj = METHOD_GET_BYTES.invoke(null, instance);
			if (obj == null) {
				return null;
			} else if (obj instanceof byte[]) {
				return (byte[]) obj;
			}
			throw new Throwable(toString(getClass(obj)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testLength() throws Throwable {
		//
		Assertions.assertEquals(ONE, length(null, ONE));
		Assertions.assertEquals(0, length(new byte[0], ONE));
		//
	}

	private static int length(final byte[] instance, int defaultValue) throws Throwable {
		try {
			final Object obj = METHOD_LENGTH.invoke(null, instance, defaultValue);
			if (obj instanceof Integer) {
				return ((Integer) obj).intValue();
			}
			throw new Throwable(toString(getClass(obj)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testSetEncryption() {
		Assertions.assertDoesNotThrow(() -> setEncryption(null, null, null, 0, 0));
	}

	private static void setEncryption(final File file, final byte[] userPassword, final byte[] ownerPassword,
			final int permission, final int encryptionType) throws Throwable {
		try {
			METHOD_SET_ENCRYPTION.invoke(null, file, userPassword, ownerPassword, permission, encryptionType);
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testSetMetaData() {
		//
		Assertions.assertDoesNotThrow(() -> setMetaData(null));
		//
		try (final Document document = new Document()) {
			Assertions.assertDoesNotThrow(() -> setMetaData(document));
		} // try
			//
	}

	private void setMetaData(final Document document) throws Throwable {
		try {
			METHOD_SET_META_DATA.invoke(instance, document);
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testCreatePropertiesDialog() throws Throwable {
		Assertions.assertNotNull(createPropertiesDialog());
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
		Assertions.assertNotNull(createPermissionDialog());
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
		Assertions.assertDoesNotThrow(() -> setVisible(null, false));
		Assertions.assertDoesNotThrow(() -> setVisible(new JTextField(), false));
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
	void testGetWidth() throws Throwable {
		//
		final double defaultValue = 0.12;
		Assertions.assertEquals(defaultValue, getWidth(null, defaultValue));
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

	@Test
	void testTestAndGet() throws Throwable {
		//
		Assertions.assertNull(testAndGet(null, null, null));
		Assertions.assertNull(testAndGet(x -> true, null, null));
		Assertions.assertNull(testAndGet(x -> false, null, null));
		//
	}

	private static <T> T testAndGet(final Predicate<Object> predicate, final T value, final Supplier<T> supplier)
			throws Throwable {
		try {
			return (T) METHOD_TEST_AND_GET.invoke(null, predicate, value, supplier);
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testCast() throws Throwable {
		//
		Assertions.assertNull(cast(null, null));
		//
		final String string = "string";
		Assertions.assertSame(string, cast(String.class, string));
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
	void testIsSelected() throws Throwable {
		//
		final AbstractButton instance = new JCheckBox();
		Assertions.assertFalse(isSelected(instance));
		//
		instance.setSelected(true);
		Assertions.assertTrue(isSelected(instance));
		//
	}

	private static boolean isSelected(final AbstractButton instance) throws Throwable {
		try {
			final Object obj = METHOD_IS_SELECTED.invoke(null, instance);
			if (obj instanceof Boolean) {
				return ((Boolean) obj).booleanValue();
			}
			throw new Throwable(toString(getClass(obj)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

}