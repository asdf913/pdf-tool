package com.lowagie.text.html;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Objects;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextComponentUtil;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfWriter;

public class HtmlPdfWriter implements ActionListener, InitializingBean {

	private static final String WRAP = "wrap";

	private static final int PREFERRED_WIDTH = 300;

	private JFrame jFrame = null;

	private JTextComponent tfOutput = null;

	private AbstractButton btnExecute, btnCopy = null;

	public JFrame getjFrame() {
		return jFrame;
	}

	public void setjFrame(final JFrame jFrame) {
		this.jFrame = jFrame;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		init(jFrame);
	}

	private void init(final Container container) {
		//
		add(container, new JLabel());
		add(container, btnExecute = new JButton("Select HTML file"), WRAP);
		//
		add(container, new JLabel("Output"));
		add(container, tfOutput = new JTextField());
		tfOutput.setEditable(false);
		tfOutput.setPreferredSize(new Dimension(PREFERRED_WIDTH, (int) tfOutput.getPreferredSize().getHeight()));
		add(container, btnCopy = new JButton("Copy"), WRAP);
		//
		addActionListener(this, btnExecute, btnCopy);
		//
	}

	private static void addActionListener(final ActionListener actionListener, final AbstractButton... buttons) {
		//
		AbstractButton button = null;
		//
		for (int i = 0; buttons != null && i < buttons.length; i++) {
			//
			if ((button = buttons[i]) == null) {
				continue;
			}
			//
			button.addActionListener(actionListener);
			//
		} // for
			//
	}

	private static void add(final Container container, final Component component) {
		if (container != null && component != null) {
			container.add(component);
		}
	}

	private static void add(final Container container, final Component component, final Object object) {
		if (container != null && component != null) {
			container.add(component, object);
		}
	}

	@Override
	public void actionPerformed(final ActionEvent evt) {
		//
		final Object source = evt != null ? evt.getSource() : null;
		//
		if (Objects.deepEquals(source, btnExecute)) {
			//
			final JFileChooser jfc = new JFileChooser(".");
			//
			if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				//
				final File file = jfc.getSelectedFile();
				final String fileName = file != null ? file.getName() : null;
				//
				File fileOutput = null;
				if (StringUtils.lastIndexOf(fileName, '.') > 0) {
					fileOutput = new File(StringUtils.substringBeforeLast(fileName, ".") + ".pdf");
				} else {
					fileOutput = new File(fileName + ".pdf");
				}
				//
				try {
					//
					JTextComponentUtil.setText(tfOutput, null);
					writeHtmlFileToPdfFile(jfc.getSelectedFile(), fileOutput);
					JTextComponentUtil.setText(tfOutput, fileOutput.getAbsolutePath());
					//
				} catch (final IOException e) {
					e.printStackTrace();
				}
				//
			} // if
				//
		} else if (Objects.deepEquals(source, btnCopy)) {
			//
			setContents(getSystemClipboard(Toolkit.getDefaultToolkit()),
					new StringSelection(JTextComponentUtil.getText(tfOutput)), null);
			//
		}
		//
	}

	private static Clipboard getSystemClipboard(final Toolkit instance) {
		return instance != null ? instance.getSystemClipboard() : null;
	}

	private static void setContents(final Clipboard instance, final Transferable contents, final ClipboardOwner owner) {
		if (instance != null && contents != null) {
			instance.setContents(contents, owner);
		}
	}

	public static void main(final String[] args) {
		//
		if (args == null) {
			throw new IllegalArgumentException("args is null");
		} else if (args.length == 0) {
			throw new IllegalArgumentException("args is empty");
		}
		//
		final String arg = args[0];
		//
		final File file = arg != null ? new File(arg) : null;
		if (file == null || !file.exists()) {
			throw new IllegalArgumentException("File not found,file=" + arg);
		}
		//
		File fileOutput = null;
		if (StringUtils.lastIndexOf(arg, '.') > 0) {
			fileOutput = new File(StringUtils.substringBeforeLast(arg, ".") + ".pdf");
		} else {
			fileOutput = new File(arg + ".pdf");
		}
		//
		try {
			writeHtmlFileToPdfFile(file, fileOutput);
		} catch (final DocumentException | IOException e) {
			e.printStackTrace();
		}
		//
	}

	private static void writeHtmlFileToPdfFile(final File input, final File output) throws IOException {
		//
		try (final Document document = new Document();
				final Reader reader = input != null ? new FileReader(input) : null;
				final OutputStream os = output != null ? new FileOutputStream(output) : null) {
			//
			PdfWriter.getInstance(document, os);
			//
			document.open();
			//
			HtmlParser.parse(document, reader);
			//
		} // try
			//
	}

}