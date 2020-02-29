package javax.swing;

import javax.swing.text.JTextComponent;

public interface JTextComponentUtil {

	static String getText(final JTextComponent instance) {
		return instance != null ? instance.getText() : null;
	}

	static void setText(final JTextComponent instance, final String text) {
		if (instance != null) {
			instance.setText(text);
		}
	}

}