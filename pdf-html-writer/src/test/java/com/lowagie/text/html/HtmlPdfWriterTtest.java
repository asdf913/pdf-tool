package com.lowagie.text.html;

import java.awt.Component;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.AbstractButton;
import javax.swing.JTextField;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.lowagie.text.ExceptionConverter;

class HtmlPdfWriterTtest {

	private static Method METHOD_WRITE_HTML_FILE_TO_PDF_FILE, METHOD_ADD_ACTION_LISTENER, METHOD_ADD2, METHOD_ADD3,
			METHOD_GET_SYSTEM_CLIP_BOARD, METHOD_SET_CONTENTS = null;

	@BeforeAll
	static void beforeAll() throws ReflectiveOperationException {
		//
		final Class<?> clz = HtmlPdfWriter.class;
		//
		(METHOD_WRITE_HTML_FILE_TO_PDF_FILE = clz.getDeclaredMethod("writeHtmlFileToPdfFile", File.class, File.class))
				.setAccessible(true);
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
	}

	private Clipboard systemClipboard = null;

	@BeforeEach
	void beforeEach() throws Throwable {
		systemClipboard = getSystemClipboard(Toolkit.getDefaultToolkit());
	}

	@Test
	void testAfterPropertiesSet() {
		Assertions.assertDoesNotThrow(() -> new HtmlPdfWriter().afterPropertiesSet());
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
		Assertions.assertThrows(ExceptionConverter.class, () -> writeHtmlFileToPdfFile(null, null));
	}

	private static void writeHtmlFileToPdfFile(final File input, final File output) throws Throwable {
		try {
			METHOD_WRITE_HTML_FILE_TO_PDF_FILE.invoke(null, input, output);
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
			throw new Throwable(obj.getClass() != null ? obj.getClass().toString() : null);
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
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

}