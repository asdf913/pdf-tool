package com.lowagie.text.html;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import javax.swing.JFrame;

import org.apache.commons.lang3.StringUtils;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfWriter;

public class HtmlPdfWriter {

	private JFrame jFrame = null;

	public JFrame getjFrame() {
		return jFrame;
	}

	public void setjFrame(final JFrame jFrame) {
		this.jFrame = jFrame;
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
		try (final Document document = new Document(); final Reader reader = new FileReader(file)) {
			PdfWriter.getInstance(document, new FileOutputStream(fileOutput));
			// step 2: we open the document
			document.open();
			// step 3: parsing the HTML document to convert it in PDF
			HtmlParser.parse(document, reader);
		} catch (final DocumentException | IOException de) {
			System.err.println(de.getMessage());
		}
	}

}