package org.springframework.context.support;

import java.awt.Component;
import java.awt.Window;

import javax.swing.JFrame;

import org.apache.pdfbox.pdmodel.DocumentWriter;

public class Main {

	private Main() {
	}

	public static void main(final String[] args) {
		//
		try (final AbstractApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml")) {
			//
			final DocumentWriter instance = context.getBean(DocumentWriter.class);
			final JFrame jFrame = instance != null ? instance.getjFrame() : null;
			pack(jFrame);
			setVisible(jFrame, true);
			//
		}
		//
	}

	private static void pack(final Window instance) {
		if (instance != null) {
			instance.pack();
		}
	}

	private static void setVisible(final Component instance, final boolean flag) {
		if (instance != null) {
			instance.setVisible(flag);
		}
	}

}