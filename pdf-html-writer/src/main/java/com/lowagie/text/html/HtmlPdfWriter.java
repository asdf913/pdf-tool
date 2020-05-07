package com.lowagie.text.html;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Dimension2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.EventObject;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.Vector;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.AbstractButton;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComboBoxUtil;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextComponentUtil;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.text.JTextComponent;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;

import net.miginfocom.swing.MigLayout;

public class HtmlPdfWriter implements ActionListener, InitializingBean, KeyListener {

	private static final Logger LOG = LoggerFactory.getLogger(HtmlPdfWriter.class);

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
		for (int i = 0; i < size(temp); i++) {
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
				LOG.error(e.getMessage(), e);
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
		Field f = null;
		//
		for (int i = 0; i < size(fs); i++) {
			//
			if ((f = fs.get(i)) == null) {
				continue;
			}
			//
			if (result == null) {
				result = new LinkedHashMap<>();
			}
			//
			try {
				result.put(getName(f), f.getInt(null));
			} catch (final IllegalAccessException e) {
				LOG.error(e.getMessage(), e);
			}
			//
		} // for
			//
		return result;
		//
	}

	private JFrame jFrame = null;

	private JTextComponent pfUser, pfOwner, tfOutput, tfTitle, tfAuthor, tfSubject, tfKeywords, tfCreator, tfProducer,
			tfHeaders = null;

	private JComboBox<String> encryptionTypes = null;

	private AbstractButton btnProperties, btnPermission, btnExecute, btnCopy, cbFullCompression = null;

	private String encryptionType = null;

	private String userPassword = null;

	private String ownerPassword = null;

	private String title = null;

	private String author = null;

	private String subject = null;

	private String keywords = null;

	private String creator = null;

	private String producer = null;

	private Boolean addCreationDate = null;

	private AbstractButton btnCreationDate = null;

	private DefaultTableModel headers = null;

	private JComboBox<Boolean> cbAllowAll = null;

	@Retention(value = RetentionPolicy.RUNTIME)
	@Target(value = { ElementType.FIELD })
	private @interface AllowPermissionField {
		int value();
	}

	@AllowPermissionField(PdfWriter.ALLOW_ASSEMBLY)
	private ComboBoxModel<Boolean> allowAssembly = null;

	@AllowPermissionField(PdfWriter.ALLOW_COPY)
	private ComboBoxModel<Boolean> allowCopy = null;

	@AllowPermissionField(PdfWriter.ALLOW_DEGRADED_PRINTING)
	private ComboBoxModel<Boolean> allowDegradePrinting = null;

	@AllowPermissionField(PdfWriter.ALLOW_FILL_IN)
	private ComboBoxModel<Boolean> allowFillin = null;

	@AllowPermissionField(PdfWriter.ALLOW_MODIFY_ANNOTATIONS)
	private ComboBoxModel<Boolean> allowModifyAnnotations = null;

	@AllowPermissionField(PdfWriter.ALLOW_MODIFY_CONTENTS)
	private ComboBoxModel<Boolean> allowModifyContents = null;

	@AllowPermissionField(PdfWriter.ALLOW_PRINTING)
	private ComboBoxModel<Boolean> allowPrinting = null;

	@AllowPermissionField(PdfWriter.ALLOW_SCREENREADERS)
	private ComboBoxModel<Boolean> allowScreenReaders = null;

	private Map<Method, JTextComponent> permissionMethodJTextComponentMap = null;

	public JFrame getjFrame() {
		return jFrame;
	}

	public void setEncryptionType(final String encryptionType) {
		this.encryptionType = encryptionType;
	}

	public void setUserPassword(final String userPassword) {
		this.userPassword = userPassword;
	}

	public void setOwnerPassword(final String ownerPassword) {
		this.ownerPassword = ownerPassword;
	}

	public void setjFrame(final JFrame jFrame) {
		this.jFrame = jFrame;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public void setAuthor(final String author) {
		this.author = author;
	}

	public void setSubject(final String subject) {
		this.subject = subject;
	}

	public void setKeywords(final String keywords) {
		this.keywords = keywords;
	}

	public void setCreator(final String creator) {
		this.creator = creator;
	}

	public void setProducer(final String producer) {
		this.producer = producer;
	}

	public void setAddCreationDate(final Boolean addCreationDate) {
		this.addCreationDate = addCreationDate;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		init(jFrame);
	}

	private void init(final Container container) {
		//
		final String wrap = String.format("span %1$s,%2$s", 2, WRAP);
		//
		add(container, new JLabel("Full Compression"));
		add(container, cbFullCompression = new JCheckBox(), wrap);
		//
		add(container, new JLabel("Encryption"));
		add(container, encryptionTypes = new JComboBox<>(
				ArrayUtils.insert(0, toArray(keySet(ENCRYPTION_TYPES), new String[0]), (String) null)), wrap);
		JComboBoxUtil.setSelectedItem(encryptionTypes, encryptionType);
		//
		add(container, new JLabel("User Pasword"));
		add(container, pfUser = new JPasswordField(userPassword), wrap);
		//
		add(container, new JLabel("Owner Pasword"));
		add(container, pfOwner = new JPasswordField(ownerPassword), wrap);
		//
		add(container, new JLabel(""));
		final JPanel panel = new JPanel();
		add(panel, btnProperties = new JButton("Properties"));
		add(panel, btnPermission = new JButton("Permission"));
		add(container, panel, wrap);
		//
		add(container, new JLabel());
		add(container, btnExecute = new JButton("Select HTML file"), WRAP);
		//
		add(container, new JLabel("Output"));
		add(container, tfOutput = new JTextField());
		tfOutput.setEditable(false);
		add(container, btnCopy = new JButton("Copy"), WRAP);
		//
		addActionListener(this, btnProperties, btnPermission, btnExecute, btnCopy);
		//
		setWidth(PREFERRED_WIDTH, pfOwner, pfUser);
		setWidth(PREFERRED_WIDTH - (int) getWidth(getPreferredSize(btnCopy), 0), tfOutput);
		//
	}

	private static double getWidth(final Dimension2D instance, final double defaultValue) {
		return instance != null ? instance.getWidth() : defaultValue;
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
		final Object source = getSource(evt);
		//
		if (Objects.equals(source, btnExecute)) {
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
					//
					writeHtmlFileToPdfFile(jfc.getSelectedFile(), this::setMetaData, fileOutput);
					//
					if (isSelected(cbFullCompression)) {
						setFullCompression(fileOutput);
					}
					//
					final byte[] userPassword = getBytes(JTextComponentUtil.getText(pfUser));
					final byte[] ownerPassword = getBytes(JTextComponentUtil.getText(pfOwner));
					final Integer encryptionType = get(ENCRYPTION_TYPES, getSelectedItem(encryptionTypes));
					//
					final boolean passwordSet = length(userPassword, 0) > 0 || length(ownerPassword, 0) > 0;
					if (passwordSet && encryptionType == null) {
						JOptionPane.showMessageDialog(null, "Please select an encryption type");
						return;
					}
					//
					if (encryptionType != null && (length(userPassword, 0) > 0 || length(ownerPassword, 0) > 0)) {
						//
						final List<Field> fs = getAllowPermissionFields(getClass().getDeclaredFields());
						Field f = null;
						ComboBoxModel<?> cb = null;
						//
						AllowPermissionField allowPermissionField = null;
						int[] permission = null;
						Object selectedItem = null;
						//
						for (int i = 0; i < size(fs); i++) {
							//
							if ((f = fs.get(i)) == null || !f.isAnnotationPresent(AllowPermissionField.class)
									|| (allowPermissionField = f.getAnnotation(AllowPermissionField.class)) == null) {
								continue;
							}
							//
							f.setAccessible(true);
							//
							if ((cb = cast(ComboBoxModel.class, f.get(this))) == null) {
								continue;
							}
							//
							if (Objects.equals(Boolean.TRUE, selectedItem = cb.getSelectedItem())) {
								if (permission == null) {
									permission = new int[0];
								}
								permission = ArrayUtils.add(permission, allowPermissionField.value());
							} else if (Objects.equals(Boolean.FALSE, selectedItem)) {
								if (permission == null) {
									permission = new int[0];
								}
								permission = ArrayUtils.add(permission, 0);
							}
							//
						}
						//
						setEncryption(fileOutput, userPassword, ownerPassword,
								or(ObjectUtils.defaultIfNull(permission, PERMISSIONS)), encryptionType.intValue());
						//
					}
					//
					JTextComponentUtil.setText(tfOutput, fileOutput.getAbsolutePath());
					//
				} catch (final IOException | IllegalAccessException e) {
					LOG.error(e.getMessage(), e);
				}
				//
			} // if
				//
		} else if (Objects.equals(source, btnCopy)) {
			//
			setContents(getSystemClipboard(Toolkit.getDefaultToolkit()),
					new StringSelection(JTextComponentUtil.getText(tfOutput)), null);
			//
		} else if (Objects.equals(source, btnProperties)) {
			//
			final JDialog dialog = createPropertiesDialog();
			//
			pack(dialog);
			setVisible(dialog, true);
			//
		} else if (Objects.equals(source, btnPermission)) {
			//
			final JDialog dialog = createPermissionDialog();
			//
			pack(dialog);
			setVisible(dialog, true);
			//
		} else if (Objects.equals(source, cbAllowAll)) {
			//
			final Object selected = getSelectedItem(cbAllowAll);
			//
			final List<Field> fs = getAllowPermissionFields(getClass().getDeclaredFields());
			//
			Field f = null;
			ComboBoxModel<?> cb = null;
			//
			for (int i = 0; i < size(fs); i++) {
				//
				if ((f = fs.get(i)) == null) {
					continue;
				} // skip null
					//
				f.setAccessible(true);
				//
				try {
					if ((cb = cast(ComboBoxModel.class, f.get(this))) != null) {
						cb.setSelectedItem(selected);
					}
				} catch (final IllegalAccessException e) {
					LOG.error(e.getMessage(), e);
				}
				//
			} // for
				//
		}
		//
	}

	@Override
	public void keyPressed(final KeyEvent e) {
	}

	@Override
	public void keyReleased(final KeyEvent evt) {
		//
		if (Objects.equals(getSource(evt), tfHeaders)) {
			//
			for (int i = getRowCount(headers) - 1; i >= 0; i--) {
				headers.removeRow(i);
			}
			//
			try {
				//
				final String string = JTextComponentUtil.getText(tfHeaders);
				//
				forEach(entrySet(
						StringUtils.isNotBlank(string) ? readValue(new ObjectMapper().readerFor(Map.class), string)
								: null),
						entry -> {
							if (entry != null) {
								addRow(headers, new Object[] { entry.getKey(), entry.getValue() });
							}
						});
				//
			} catch (final JsonProcessingException e) {
				LOG.error(e.getMessage(), e);
			}
			//
		}
		//
	}

	private static <T> T readValue(final ObjectReader instance, final String input) throws JsonProcessingException {
		return instance != null ? instance.readValue(input) : null;
	}

	private static <K, V> Set<Entry<K, V>> entrySet(final Map<K, V> instance) {
		return instance != null ? instance.entrySet() : null;
	}

	private static <T> void forEach(final Iterable<T> instance, final Consumer<T> action) {
		if (instance != null) {
			instance.forEach(action);
		}
	}

	private static int getRowCount(final TableModel instance) {
		return instance != null ? instance.getRowCount() : 0;
	}

	private static void addRow(final DefaultTableModel instance, final Object... row) {
		if (instance != null) {
			instance.addRow(row);
		}
	}

	private static Vector<?> getDataVector(final DefaultTableModel instance) {
		return instance != null ? instance.getDataVector() : null;
	}

	@Override
	public void keyTyped(final KeyEvent evt) {
	}

	private static List<Field> getAllowPermissionFields(final Field[] fs) {
		return collect(filter(stream(fs), f -> f != null && Objects.equals(f.getType(), ComboBoxModel.class)
				&& f.isAnnotationPresent(AllowPermissionField.class)), Collectors.toList());
	}

	private static <T> T cast(final Class<T> clz, final Object instance) {
		return clz != null && clz.isInstance(instance) ? clz.cast(instance) : null;
	}

	private static Object getSource(final EventObject instance) {
		return instance != null ? instance.getSource() : null;
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

	private Map<Method, JTextComponent> getPermissionMethodJTextComponentMap() {
		//
		if (permissionMethodJTextComponentMap == null) {
			//
			final Class<?> clz = Document.class;
			final Class<?>[] stringClassOnly = new Class<?>[] { String.class };
			//
			try {
				//
				(permissionMethodJTextComponentMap = new LinkedHashMap<>())
						.put(clz.getDeclaredMethod("addAuthor", stringClassOnly), tfAuthor);
				permissionMethodJTextComponentMap.put(clz.getDeclaredMethod("addCreator", stringClassOnly), tfCreator);
				permissionMethodJTextComponentMap.put(clz.getDeclaredMethod("addKeywords", stringClassOnly),
						tfKeywords);
				permissionMethodJTextComponentMap.put(clz.getDeclaredMethod("addProducer", stringClassOnly),
						tfProducer);
				permissionMethodJTextComponentMap.put(clz.getDeclaredMethod("addSubject", stringClassOnly), tfSubject);
				permissionMethodJTextComponentMap.put(clz.getDeclaredMethod("addTitle", stringClassOnly), tfTitle);
				//
			} catch (final NoSuchMethodException e) {
				LOG.error(e.getMessage(), e);
			}
			//
		}
		//
		return permissionMethodJTextComponentMap;
		//
	}

	private void setMetaData(final Document document) {
		//
		final Map<Method, JTextComponent> map = getPermissionMethodJTextComponentMap();
		//
		if (map != null) {
			//
			Method m = null;
			String input = null;
			//
			for (final Entry<Method, JTextComponent> entry : map.entrySet()) {
				//
				if (entry == null || (m = entry.getKey()) == null) {
					continue;
				} // skip null
					//
				m.setAccessible(true);
				//
				try {
					//
					input = StringUtils.defaultString(JTextComponentUtil.getText(entry.getValue()), "");
					//
					if (Modifier.isStatic(m.getModifiers())) {
						m.invoke(null, input);
					} else if (document != null) {
						m.invoke(document, input);
					}
					//
				} catch (final IllegalAccessException e) {
					LOG.error(e.getMessage(), e);
				} catch (final InvocationTargetException e) {
					//
					final Throwable targetException = (ObjectUtils.defaultIfNull(e.getTargetException(), e));
					if (targetException instanceof RuntimeException) {
						throw (RuntimeException) targetException;
					}
					LOG.error(targetException.getMessage(), targetException);
					//
				}
				//
			} // for
				//
		} // if
			//
		if (isSelected(btnCreationDate)) {
			document.addCreationDate();
		}
		//
		forEach(getDataVector(headers), row -> {
			final List<?> list = cast(List.class, row);
			if (size(list) > 1) {
				document.addHeader(toString(list.get(0)), toString(list.get(1)));
			}
		});
		//
	}

	private static String toString(final Object instance) {
		if (instance instanceof String) {
			return (String) instance;
		}
		return instance != null ? instance.toString() : null;
	}

	private static int size(final Collection<?> instance) {
		return instance != null ? instance.size() : 0;
	}

	private static boolean isSelected(final AbstractButton instance) {
		return instance != null && instance.isSelected();
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
		return instance != null && !GraphicsEnvironment.isHeadless() ? instance.getSystemClipboard() : null;
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
			writeHtmlFileToPdfFile(file, null, fileOutput);
		} catch (final DocumentException | IOException e) {
			LOG.error(e.getMessage(), e);
		}
		//
	}

	private static void writeHtmlFileToPdfFile(final File input, final Consumer<Document> consumer, final File output)
			throws IOException {
		//
		try (final Document document = new Document();
				final Reader reader = input != null ? new FileReader(input) : null;
				final OutputStream os = output != null ? new FileOutputStream(output) : null) {
			//
			PdfWriter.getInstance(document, os);
			//
			document.open();
			if (consumer != null) {
				consumer.accept(document);
			}
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

	private static void setFullCompression(final File file) throws IOException {
		//
		try (final InputStream is = file != null ? new FileInputStream(file) : null;
				final PdfReader pdfReader = is != null ? new PdfReader(is) : null;
				final OutputStream os = file != null ? new FileOutputStream(file) : null) {
			//
			final PdfStamper stamper = pdfReader != null ? new PdfStamper(pdfReader, os) : null;
			if (stamper != null) {
				stamper.setFullCompression();
				stamper.close();
			}
			//
		} // try
			//
	}

	private JDialog createPropertiesDialog() {
		//
		final JDialog dialog = createJDialog();
		setTitle(dialog, "Properties");
		setLayout(dialog, new MigLayout());
		//
		add(dialog, new JLabel("Title"));
		add(dialog, tfTitle = ObjectUtils.defaultIfNull(tfTitle, new JTextField(title)), WRAP);
		//
		add(dialog, new JLabel("Author"));
		add(dialog, tfAuthor = ObjectUtils.defaultIfNull(tfAuthor, new JTextField(author)), WRAP);
		//
		add(dialog, new JLabel("Subject"));
		add(dialog, tfSubject = ObjectUtils.defaultIfNull(tfSubject, new JTextField(subject)), WRAP);
		//
		add(dialog, new JLabel("Keywords"));
		add(dialog, tfKeywords = ObjectUtils.defaultIfNull(tfKeywords, new JTextField(keywords)), WRAP);
		//
		add(dialog, new JLabel("Creator"));
		add(dialog, tfCreator = ObjectUtils.defaultIfNull(tfCreator, new JTextField(creator)), WRAP);
		//
		add(dialog, new JLabel("Producer"));
		add(dialog, tfProducer = ObjectUtils.defaultIfNull(tfProducer, new JTextField(producer)), WRAP);
		//
		add(dialog, new JLabel("Create Date"));
		add(dialog, btnCreationDate = new JCheckBox(), WRAP);
		if (addCreationDate != null) {
			btnCreationDate.setSelected(addCreationDate.booleanValue());
		}
		//
		// header(s)
		//
		add(dialog, new JLabel("Header(s)"));
		if (tfHeaders == null) {
			(tfHeaders = new JTextField()).addKeyListener(this);
		}
		add(dialog, tfHeaders, WRAP);
		//
		add(dialog, new JLabel());
		final Component component = new JScrollPane(new JTable(headers = ObjectUtils.defaultIfNull(headers,
				new DefaultTableModel(new Object[] { "Name", "Value" }, 0))));
		add(dialog, component, WRAP);
		//
		setWidth(Math.max((int) getWidth(getPreferredSize(component), 0), 200), tfTitle, tfAuthor, tfSubject,
				tfKeywords, tfCreator, tfProducer, tfHeaders);
		//
		return dialog;
		//
	}

	private static JDialog createJDialog() {
		return !GraphicsEnvironment.isHeadless() ? new JDialog() : null;
	}

	private static void setTitle(final JDialog instance, final String title) {
		if (instance != null) {
			instance.setTitle(title);
		}
	}

	private static void setLayout(final Container instance, final LayoutManager layoutManager) {
		if (instance != null) {
			instance.setLayout(layoutManager);
		}
	}

	private JDialog createPermissionDialog() {
		//
		final JDialog dialog = createJDialog();
		setTitle(dialog, "Permission");
		setLayout(dialog, new MigLayout());
		//
		final Predicate<Object> notNull = x -> x != null;
		//
		final Boolean[] booleans = new Boolean[] { null, Boolean.FALSE, Boolean.TRUE };
		//
		// all
		//
		add(dialog, new JLabel("All"));
		if (cbAllowAll == null) {
			cbAllowAll = new JComboBox<>(new DefaultComboBoxModel<>(booleans));
		}
		add(dialog, cbAllowAll, WRAP);
		cbAllowAll.addActionListener(this);
		//
		// assembly
		//
		add(dialog, new JLabel("Assembly"));
		add(dialog,
				new JComboBox<>(
						allowAssembly = testAndGet(notNull, allowAssembly, () -> new DefaultComboBoxModel<>(booleans))),
				WRAP);
		//
		// copy
		//
		add(dialog, new JLabel("Copy"));
		add(dialog,
				new JComboBox<>(allowCopy = testAndGet(notNull, allowCopy, () -> new DefaultComboBoxModel<>(booleans))),
				WRAP);
		//
		// degrade printing
		//
		add(dialog, new JLabel("Degrade Printing"));
		add(dialog, new JComboBox<>(allowDegradePrinting = testAndGet(notNull, allowDegradePrinting,
				() -> new DefaultComboBoxModel<>(booleans))), WRAP);
		//
		// fill in
		//
		add(dialog, new JLabel("Fill In"));
		add(dialog,
				new JComboBox<>(
						allowFillin = testAndGet(notNull, allowFillin, () -> new DefaultComboBoxModel<>(booleans))),
				WRAP);
		//
		// modify annotations
		//
		add(dialog, new JLabel("Modify Annotations"));
		add(dialog, new JComboBox<>(allowModifyAnnotations = testAndGet(notNull, allowModifyAnnotations,
				() -> new DefaultComboBoxModel<>(booleans))), WRAP);
		//
		// modify contents
		//
		add(dialog, new JLabel("Modify Contents"));
		add(dialog, new JComboBox<>(allowModifyContents = testAndGet(notNull, allowModifyContents,
				() -> new DefaultComboBoxModel<>(booleans))), WRAP);
		//
		// printing
		//
		add(dialog, new JLabel("Printing"));
		add(dialog,
				new JComboBox<>(
						allowPrinting = testAndGet(notNull, allowPrinting, () -> new DefaultComboBoxModel<>(booleans))),
				WRAP);
		//
		// screen reader
		//
		add(dialog, new JLabel("Screen Readers"));
		add(dialog, new JComboBox<>(allowScreenReaders = testAndGet(notNull, allowScreenReaders,
				() -> new DefaultComboBoxModel<>(booleans))), WRAP);
		//
		return dialog;
		//
	}

	private static <T> T testAndGet(final Predicate<Object> predicate, final T value, final Supplier<T> supplier) {
		//
		if (predicate == null || predicate.test(value)) {
			return value;
		}
		//
		return supplier != null ? supplier.get() : value;
		//
	}

}