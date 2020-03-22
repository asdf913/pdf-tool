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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextComponentUtil;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;

public class HtmlPdfWriter implements ActionListener, InitializingBean {

	private static final String WRAP = "wrap";

	private static final int PREFERRED_WIDTH = 300;

	private static int[] PERMISSIONS = null;

	private static Map<String, Integer> ENCRYPTION_TYPES = null;

	static {
		//
		final Field[] fs = PdfWriter.class.getDeclaredFields();
		//
		PERMISSIONS = getPermissions(fs);
		//
		ENCRYPTION_TYPES = getEncryptionTypes(fs);
		//
	}

	private static int[] getPermissions(final Field[] input) {
		//
		final List<Field> temp = collect(
				filter(stream(input),
						f -> StringUtils.startsWith(getName(f), "ALLOW_") && Objects.equals(f.getType(), Integer.TYPE)),
				Collectors.toList());
		//
		Field f = null;
		//
		int[] result = null;
		//
		for (int i = 0; temp != null && i < temp.size(); i++) {
			//
			if ((f = temp.get(i)) == null || !Objects.equals(f.getType(), Integer.TYPE)
					|| !Modifier.isStatic(f.getModifiers())) {
				continue;
			}
			//
			f.setAccessible(true);
			//
			if (result == null) {
				result = new int[0];
			}
			//
			try {
				result = ArrayUtils.add(result, f.getInt(null));
			} catch (final IllegalAccessException e) {
				e.printStackTrace();
			}
			//
		} // for
			//
		return result;
		//
	}

	private static <T> Stream<T> stream(final T[] instance) {
		return instance != null ? Arrays.stream(instance) : null;
	}

	private static <T> Stream<T> filter(final Stream<T> instance, final Predicate<? super T> predicate) {
		return instance != null ? instance.filter(predicate) : null;
	}

	private static <T, R, A> R collect(final Stream<T> instance, final Collector<? super T, A, R> collector) {
		return instance != null ? instance.collect(collector) : null;
	}

	private static Map<String, Integer> getEncryptionTypes(final Field[] input) {
		//
		final Collection<String> encryptionTypeFieldNames = Arrays.asList("STANDARD_ENCRYPTION_40",
				"STANDARD_ENCRYPTION_128", "ENCRYPTION_AES_128");
		//
		final List<Field> fs = collect(
				filter(stream(input), t -> encryptionTypeFieldNames.contains(getName(t))
						&& Objects.equals(t.getType(), Integer.TYPE) && Modifier.isStatic(t.getModifiers())),
				Collectors.toList());
		//
		Map<String, Integer> result = null;
		//
		if (fs != null) {
			//
			Field f = null;
			//
			for (int i = 0; i < fs.size(); i++) {
				//
				if ((f = fs.get(i)) == null) {
					continue;
				}
				//
				if (result == null) {
					result = new LinkedHashMap<>();
				}
				try {
					result.put(getName(f), f.getInt(null));
				} catch (final IllegalAccessException e) {
					e.printStackTrace();
				}

			}
			//
		} // if
			///
		return result;
		//
	}

	private JFrame jFrame = null;

	private JTextComponent pfUser, pfOwner, tfOutput = null;

	private JComboBox<String> encryptionTypes = null;

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
		add(container, new JLabel("User Pasword"));
		add(container, encryptionTypes = new JComboBox<>(
				ArrayUtils.insert(0, toArray(keySet(ENCRYPTION_TYPES), new String[0]), (String) null)), WRAP);
		//
		add(container, new JLabel("User Pasword"));
		add(container, pfUser = new JPasswordField(), WRAP);
		//
		add(container, new JLabel("Owner Pasword"));
		add(container, pfOwner = new JPasswordField(), WRAP);
		//
		add(container, new JLabel());
		add(container, btnExecute = new JButton("Select HTML file"), WRAP);
		//
		add(container, new JLabel("Output"));
		add(container, tfOutput = new JTextField());
		tfOutput.setEditable(false);
		add(container, btnCopy = new JButton("Copy"), WRAP);
		//
		addActionListener(this, btnExecute, btnCopy);
		//
		setWidth(PREFERRED_WIDTH, pfOwner, pfUser, tfOutput);
		//
	}

	private static <K> Set<K> keySet(final Map<K, ?> instance) {
		return instance != null ? instance.keySet() : null;
	}

	private static <T> T[] toArray(final Collection<T> instance, final T[] array) {
		return instance != null && array != null ? instance.toArray(array) : null;
	}

	private static String getName(final Member instance) {
		return instance != null ? instance.getName() : null;
	}

	private static void setWidth(final int width, final Component... cs) {
		//
		Component c = null;
		Dimension preferredSize = null;
		//
		for (int i = 0; cs != null && i < cs.length; i++) {
			//
			if ((preferredSize = getPreferredSize(c = cs[i])) == null) {
				continue;
			} // skip null
				//
			c.setPreferredSize(new Dimension(width, (int) preferredSize.getHeight()));
			//
		} // for
			//
	}

	private static Dimension getPreferredSize(final Component instance) {
		return instance != null ? instance.getPreferredSize() : null;
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
					//
					final byte[] userPassword = getBytes(JTextComponentUtil.getText(pfUser));
					final byte[] ownerPassword = getBytes(JTextComponentUtil.getText(pfOwner));
					//
					final Integer encryptionType = get(ENCRYPTION_TYPES, getSelectedItem(encryptionTypes));
					if (encryptionType != null && (length(userPassword, 0) > 0 || length(ownerPassword, 0) > 0)) {
						setEncryption(fileOutput, userPassword, ownerPassword, or(PERMISSIONS),
								encryptionType.intValue());
					}
					//
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

	private static <V> V get(final Map<?, V> instance, final Object key) {
		return instance != null ? instance.get(key) : null;
	}

	private static Object getSelectedItem(final JComboBox<?> instance) {
		return instance != null ? instance.getSelectedItem() : null;
	}

	private static int or(final int[] instance) {
		//
		if (instance == null) {
			throw new IllegalArgumentException("instance is null");
		}
		//
		int result = instance[0];
		//
		for (int i = 1; i < instance.length; i++) {
			result |= instance[i];
		} // for
			//
		return result;
		//
	}

	private static byte[] getBytes(final String instance) {
		return instance != null ? instance.getBytes() : null;
	}

	private static int length(final byte[] instance, int defaultValue) {
		return instance != null ? instance.length : defaultValue;
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

	private static void setEncryption(final File file, final byte[] userPassword, final byte[] ownerPassword,
			final int permission, final int encryptionType) throws IOException {
		//
		try (final InputStream is = file != null ? new FileInputStream(file) : null;
				final PdfReader pdfReader = is != null ? new PdfReader(is) : null;
				final OutputStream os = file != null ? new FileOutputStream(file) : null) {
			//
			final PdfStamper stamper = pdfReader != null ? new PdfStamper(pdfReader, os) : null;
			if (stamper != null) {
				stamper.setEncryption(userPassword, ownerPassword, permission, encryptionType);
				stamper.close();
			}
			//
		} // try
			//
	}

}