package org.apache.pdfbox.pdmodel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Dimension2D;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EventObject;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.AbstractButton;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.ProtectionPolicy;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDFontLike;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.InitializingBean;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

import net.miginfocom.swing.MigLayout;

public class DocumentWriter implements ActionListener, InitializingBean {

	private static final Logger LOG = Logger.getLogger(toString(DocumentWriter.class));

	private static final String WRAP = "wrap";

	private JFrame jFrame = null;

	private JTextComponent tfFontSize, tfMargin, tfText, pfOwner1, pfOwner2, pfUser1, pfUser2, tfFile, tfTitle,
			tfAuthor, tfSubject, tfKeywords, tfCreator = null;

	private AbstractButton btnColor, btnProperties, btnPermission, btnExecute, btnCopy = null;

	private ComboBoxModel<Object> cbPageSize = null;

	private Map<String, PDRectangle> pageSizeMap = null;

	private ComboBoxModel<PDFont> font = null;

	@Retention(value = RetentionPolicy.RUNTIME)
	@Target(value = { ElementType.FIELD })
	private @interface AccessPermissionField {
		String methodName();
	}

	@AccessPermissionField(methodName = "setCanAssembleDocument")
	private ComboBoxModel<Boolean> canAssembleDocument = null;

	@AccessPermissionField(methodName = "setCanExtractContent")
	private ComboBoxModel<Boolean> canExtractContent = null;

	@AccessPermissionField(methodName = "setCanExtractForAccessibility")
	private ComboBoxModel<Boolean> canExtractForAccessibility = null;

	@AccessPermissionField(methodName = "setCanFillInForm")
	private ComboBoxModel<Boolean> canFillInForm = null;

	@AccessPermissionField(methodName = "setCanModify")
	private ComboBoxModel<Boolean> canModify = null;

	@AccessPermissionField(methodName = "setCanModifyAnnotations")
	private ComboBoxModel<Boolean> canModifyAnnotations = null;

	@AccessPermissionField(methodName = "setCanPrint")
	private ComboBoxModel<Boolean> canPrint = null;

	@AccessPermissionField(methodName = "setCanPrintDegraded")
	private ComboBoxModel<Boolean> canPrintDegraded = null;

	private Color color = null;

	private String fontName = null;

	private Integer fontSize = null;

	private Integer margin = null;

	private String pageSize = null;

	private String ownerPassword = null;

	private String userPassword = null;

	private String title = null;

	private String author = null;

	private String subject = null;

	private String keywords = null;

	private String creator = null;

	private String assembleDocument = null;

	private String extractContent = null;

	private String extractForAccessibility = null;

	private String fillInForm = null;

	private String modify = null;

	private String modifyAnnotations = null;

	private String print = null;

	private String printDegraded = null;

	private DocumentWriter() {
	}

	public void setjFrame(final JFrame jFrame) {
		this.jFrame = jFrame;
	}

	public JFrame getjFrame() {
		return jFrame;
	}

	public void setFontName(final String fontName) {
		this.fontName = fontName;
	}

	public void setFontSize(final Integer fontSize) {
		this.fontSize = fontSize;
	}

	public void setMargin(final Integer margin) {
		this.margin = margin;
	}

	public void setPageSize(final String pageSize) {
		this.pageSize = pageSize;
	}

	public void setColor(final Color color) {
		this.color = color;
	}

	public void setOwnerPassword(final String ownerPassword) {
		this.ownerPassword = ownerPassword;
	}

	public void setUserPassword(final String userPassword) {
		this.userPassword = userPassword;
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

	public void setAssembleDocument(final String assembleDocument) {
		this.assembleDocument = assembleDocument;
	}

	public void setExtractContent(final String extractContent) {
		this.extractContent = extractContent;
	}

	public void setExtractForAccessibility(final String extractForAccessibility) {
		this.extractForAccessibility = extractForAccessibility;
	}

	public void setFillInForm(final String fillInForm) {
		this.fillInForm = fillInForm;
	}

	public void setModify(final String modify) {
		this.modify = modify;
	}

	public void setModifyAnnotations(final String modifyAnnotations) {
		this.modifyAnnotations = modifyAnnotations;
	}

	public void setPrint(final String print) {
		this.print = print;
	}

	public void setPrintDegraded(final String printDegraded) {
		this.printDegraded = printDegraded;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		//
		init(jFrame);
		//
		createPropertiesDialog();
		createPermissionDialog();
		//
	}

	private void init(final Container container) {
		//
		final String wrap = String.format("span %1$s,%2$s", 3, WRAP);
		//
		final JLabel label = new JLabel("Page Size");
		//
		add(container, label);
		add(container,
				new JComboBox<>(cbPageSize = new DefaultComboBoxModel<>(
						ArrayUtils.insert(0, (pageSizeMap = getPageSizeMap()).keySet().toArray(), (Object) null))),
				wrap);
		if (containsKey(pageSizeMap, pageSize)) {
			cbPageSize.setSelectedItem(pageSize);
		}
		//
		add(container, new JLabel("Font size"));
		add(container, tfFontSize = new JTextField(toString(fontSize)), wrap);
		//
		add(container, new JLabel("Margin"));
		add(container, tfMargin = new JTextField(toString(margin)), wrap);
		//
		add(container, new JLabel("Font"));
		final PDFont[] fonts = getFonts();
		add(container, new JComboBox<>(font = new DefaultComboBoxModel<>(ArrayUtils.insert(0, fonts, (PDFont) null))),
				wrap);
		font.setSelectedItem(Iterables.getFirst(
				collect(filter(stream(fonts), font -> Objects.equals(getName(font), fontName)), Collectors.toList()),
				null));
		//
		add(container, new JLabel("Text"));
		add(container, new JScrollPane(tfText = new JTextArea(10, 100)), wrap);
		//
		add(container, new JLabel(""));
		add(container, btnColor = new JButton("Color"), wrap);
		//
		add(container, new JLabel("Owner Password"));
		add(container, pfOwner1 = new JPasswordField());
		add(container, pfOwner2 = new JPasswordField(), wrap);
		//
		add(container, new JLabel("User Password"));
		add(container, pfUser1 = new JPasswordField());
		add(container, pfUser2 = new JPasswordField(), wrap);
		//
		add(container, new JLabel(""));
		final JPanel panel = new JPanel();
		add(panel, btnProperties = new JButton("Properties"));
		add(panel, btnPermission = new JButton("Permission"));
		add(container, panel, wrap);
		//
		add(container, new JLabel(""));
		add(container, btnExecute = new JButton("Execute"), wrap);
		//
		add(container, new JLabel("File"));
		add(container, tfFile = new JTextField(), "span 2");
		add(container, btnCopy = new JButton("Copy"), "wrap");
		tfFile.setEditable(false);
		//
		addActionListener(this, btnColor, btnProperties, btnPermission, btnExecute, btnCopy);
		//
		final int width = Math.max(250, (int) getWidth(getPreferredSize(tfText), 250));
		setWidth(width - (int) getPreferredSize(btnCopy).getWidth(), tfFile);
		setWidth(width, tfFontSize, tfMargin, tfText);
		setWidth((int) (width - getWidth(getPreferredSize(label), 0) * 3) / 2, pfOwner1, pfOwner2, pfUser1, pfUser2);
		//
		if (color != null) {
			setForeground(tfText, color);
		}
		//
		accept(ownerPassword, Arrays.asList(pfOwner1::setText, pfOwner2::setText));
		accept(userPassword, Arrays.asList(pfUser1::setText, pfUser2::setText));
		//
	}

	private static <T> void accept(final T value, final Iterable<Consumer<T>> consumers) {
		//
		if (consumers != null) {
			//
			for (final Consumer<T> consumer : consumers) {
				//
				if (consumer == null) {
					continue;
				} // skip null
					//
				consumer.accept(value);
				//
			} // for
				//
		} // if
			//
	}

	private static boolean containsKey(final Map<?, ?> instance, final Object key) {
		return instance != null && instance.containsKey(key);
	}

	private static <T> Stream<T> stream(final T[] instance) {
		return instance != null ? Arrays.stream(instance) : null;
	}

	private static <T> Stream<T> stream(final Collection<T> instance) {
		return instance != null ? instance.stream() : null;
	}

	private static <T> Stream<T> filter(final Stream<T> instance, final Predicate<? super T> predicate) {
		return instance != null && predicate != null ? instance.filter(predicate) : null;
	}

	private static <T, R, A> R collect(final Stream<T> instance, final Collector<? super T, A, R> collector) {
		return instance != null && collector != null ? instance.collect(collector) : null;
	}

	private static String getName(final PDFontLike instance) {
		return instance != null ? instance.getName() : null;
	}

	private static String toString(final Object instance) {
		return instance != null ? instance.toString() : null;
	}

	private static Dimension getPreferredSize(final Component instance) {
		return instance != null ? instance.getPreferredSize() : null;
	}

	private static void add(final Container container, final Component component) {
		if (container != null) {
			container.add(component);
		}
	}

	private static void add(final Container container, final Component component, final Object object) {
		if (container != null) {
			container.add(component, object);
		}
	}

	private static double getWidth(final Dimension2D instance, final double defaultValue) {
		return instance != null ? instance.getWidth() : defaultValue;
	}

	private static PDFont[] getFonts() {
		return getFonts(PDType1Font.class.getDeclaredFields());
	}

	private static PDFont[] getFonts(final Field[] fs) {
		//
		List<PDFont> result = null;
		//
		Field f = null;
		PDFont font = null;
		//
		for (int i = 0; fs != null && i < fs.length; i++) {
			//
			if ((f = fs[i]) == null || !Modifier.isStatic(f.getModifiers())) {
				continue;
			} // skip null
				//
			setAccessible(f);
			//
			try {
				//
				if ((font = cast(PDFont.class, f.get(null))) == null) {
					continue;
				}
				//
				if (result == null) {
					result = new ArrayList<>();
				}
				result.add(font);
				//
			} catch (final IllegalAccessException e) {
				LOG.severe(e.getMessage());
			}
			//
		} // for
			//
		return result != null ? result.toArray(new PDFont[result.size()]) : null;
		//
	}

	private static void setAccessible(final AccessibleObject instance) {
		if (instance != null && !instance.isAccessible()) {
			instance.setAccessible(true);
		}
	}

	private static Map<String, PDRectangle> getPageSizeMap() {
		return getPageSizeMap(PDRectangle.class.getDeclaredFields());
	}

	private static Map<String, PDRectangle> getPageSizeMap(final Field[] fs) {
		//
		Map<String, PDRectangle> result = null;
		//
		Field f = null;
		PDRectangle pdRectangle = null;
		//
		for (int i = 0; fs != null && i < fs.length; i++) {
			//
			if ((f = fs[i]) == null || !Modifier.isStatic(f.getModifiers())) {
				continue;
			} // skip null
				//
			setAccessible(f);
			//
			try {
				//
				if ((pdRectangle = cast(PDRectangle.class, f.get(null))) == null) {
					continue;
				}
				//
				if (result == null) {
					result = new LinkedHashMap<>();
				}
				put(result, f.getName(), pdRectangle);
				//
			} catch (final IllegalAccessException e) {
				LOG.severe(e.getMessage());
			}
			//
		} // for
			//
		return result;
		//
	}

	private static <T> T cast(final Class<T> clz, final Object instance) {
		return clz != null && clz.isInstance(instance) ? clz.cast(instance) : null;
	}

	private static void addActionListener(final ActionListener actionListener, final AbstractButton... bs) {
		//
		AbstractButton b = null;
		//
		for (int i = 0; bs != null && i < bs.length; i++) {
			//
			if ((b = bs[i]) == null) {
				continue;
			} // skip null
				//
			b.addActionListener(actionListener);
			//
		} // for
			//
	}

	@Override
	public void actionPerformed(final ActionEvent evt) {
		//
		final Object source = getSource(evt);
		//
		if (Objects.equals(source, btnExecute)) {
			//
			final PDFont font = cast(PDFont.class, getSelectedItem(this.font));
			if (font == null) {
				JOptionPane.showMessageDialog(null, "Please select a font");
				return;
			}
			//
			final Integer fontSize = valueOf(getText(tfFontSize));
			if (fontSize == null) {
				JOptionPane.showMessageDialog(null, "Please enter a vaild font size");
				return;
			}
			//
			final Integer margin = valueOf(getText(tfMargin));
			if (margin == null) {
				JOptionPane.showMessageDialog(null, "Please enter a vaild margin");
				return;
			}
			//
			final PDRectangle pageSize = cast(PDRectangle.class, get(pageSizeMap, getSelectedItem(this.cbPageSize)));
			final PDPage page = pageSize != null ? new PDPage(pageSize) : new PDPage();
			final PDDocument document = new PDDocument();
			//
			try (final PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
				//
				document.setVersion(1.7f);
				document.addPage(page);
				//
				final PDRectangle mediabox = page.getMediaBox();
				float width = mediabox.getWidth() - 2 * margin.intValue();
				final List<String> lines = ObjectUtils.defaultIfNull(toLines(getText(tfText), font, fontSize, width),
						Collections.emptyList());
				//
				float startX = mediabox.getLowerLeftX() + margin.intValue();
				float startY = mediabox.getUpperRightY() - margin.intValue();
				float leading = 1.5f * fontSize;
				//
				contentStream.beginText();
				contentStream.setNonStrokingColor(
						testAndGet(Predicates.notNull(), color, getForeground(tfText), Color.BLACK));
				contentStream.setFont(font, fontSize);
				contentStream.newLineAtOffset(startX, startY);
				//
				for (final String line : lines) {
					contentStream.showText(line);
					contentStream.newLineAtOffset(0, -leading);
				} // for
					//
				contentStream.endText();
				//
				contentStream.addComment("COMMENT 1");
				//
				contentStream.close();
				//
				final PDDocumentInformation documentInformation = document.getDocumentInformation();
				if (documentInformation != null) {
					documentInformation.setTitle(getText(tfTitle));
					documentInformation.setAuthor(getText(tfAuthor));
					documentInformation.setSubject(getText(tfSubject));
					documentInformation.setKeywords(getText(tfKeywords));
					documentInformation.setCreator(getText(tfCreator));
				}
				//
				final File file = new File("test.pdf");
				setText(tfFile, file.getAbsolutePath());
				//
				// https://pdfbox.apache.org/1.8/cookbook/encryption.html
				//
				if (!checkPassword(getText(pfOwner1), getText(pfOwner2))) {
					JOptionPane.showMessageDialog(null, "Owner password not matched");
					return;
				} else if (!checkPassword(getText(pfUser1), getText(pfUser2))) {
					JOptionPane.showMessageDialog(null, "User password not matched");
					return;
				}
				//
				document.protect(createProtectionPolicy(getText(pfOwner1), getText(pfUser1),
						createAccessPermission(this, getClass().getDeclaredFields())));
				//
				document.save(file);
				//
			} catch (final IOException e) {
				e.printStackTrace();
			} finally {
				IOUtils.closeQuietly(document);
			}
			//
		} else if (Objects.equals(source, btnCopy)) {
			//
			final Toolkit toolkit = Toolkit.getDefaultToolkit();
			final Clipboard clipboard = toolkit != null ? toolkit.getSystemClipboard() : null;
			if (clipboard != null) {
				clipboard.setContents(new StringSelection(getText(tfFile)), null);
			}
			//
		} else if (Objects.equals(source, btnColor)) {
			//
			setForeground(tfText, color = JColorChooser.showDialog(null, "Font color", color));
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
		}
		//
	}

	private static void setForeground(final Component instance, final Color color) {
		if (instance != null) {
			instance.setForeground(color);
		}
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

	private JDialog createPropertiesDialog() {
		//
		final JDialog dialog = new JDialog();
		dialog.setTitle("Properties");
		dialog.setLayout(new MigLayout());
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
		setWidth(200, tfTitle, tfAuthor, tfSubject, tfKeywords, tfCreator);
		//
		return dialog;
		//
	}

	private JDialog createPermissionDialog() {
		//
		final JDialog dialog = new JDialog();
		dialog.setTitle("Permission");
		dialog.setLayout(new MigLayout());
		//
		final Predicate<Object> notNull = x -> x != null;
		//
		final Boolean[] booleans = new Boolean[] { null, Boolean.FALSE, Boolean.TRUE };
		//
		final Map<JComboBox<?>, Boolean> initMap = canAssembleDocument == null ? new LinkedHashMap<>() : null;
		JComboBox<Boolean> cb = null;
		//
		add(dialog, new JLabel("Assemble Document"));
		add(dialog, cb = new JComboBox<>(canAssembleDocument = testAndGet(notNull, canAssembleDocument,
				() -> new DefaultComboBoxModel<>(booleans))), WRAP);
		put(initMap, cb, valueOf(assembleDocument, null));
		//
		add(dialog, new JLabel("Extract Content"));
		add(dialog, cb = new JComboBox<>(
				canExtractContent = testAndGet(notNull, canExtractContent, () -> new DefaultComboBoxModel<>(booleans))),
				WRAP);
		put(initMap, cb, valueOf(extractContent, null));
		//
		add(dialog, new JLabel("Extract For Accessibility"));
		add(dialog, cb = new JComboBox<>(canExtractForAccessibility = testAndGet(notNull, canExtractForAccessibility,
				() -> new DefaultComboBoxModel<>(booleans))), WRAP);
		put(initMap, cb, valueOf(extractForAccessibility, null));
		//
		add(dialog, new JLabel("Fill In Form"));
		add(dialog,
				cb = new JComboBox<>(
						canFillInForm = testAndGet(notNull, canFillInForm, () -> new DefaultComboBoxModel<>(booleans))),
				WRAP);
		put(initMap, cb, valueOf(fillInForm, null));
		//
		add(dialog, new JLabel("Modify"));
		add(dialog, cb = new JComboBox<>(
				canModify = testAndGet(notNull, canModify, () -> new DefaultComboBoxModel<>(booleans))), WRAP);
		put(initMap, cb, valueOf(modify, null));
		//
		add(dialog, new JLabel("Modify Annotations"));
		add(dialog, cb = new JComboBox<>(canModifyAnnotations = testAndGet(notNull, canModifyAnnotations,
				() -> new DefaultComboBoxModel<>(booleans))), WRAP);
		put(initMap, cb, valueOf(modifyAnnotations, null));
		//
		add(dialog, new JLabel("Print"));
		add(dialog, cb = new JComboBox<>(
				canPrint = testAndGet(notNull, canPrint, () -> new DefaultComboBoxModel<>(booleans))), WRAP);
		put(initMap, cb, valueOf(print, null));
		//
		add(dialog, new JLabel("Print Degraded"));
		add(dialog, cb = new JComboBox<>(
				canPrintDegraded = testAndGet(notNull, canPrintDegraded, () -> new DefaultComboBoxModel<>(booleans))),
				WRAP);
		put(initMap, cb, valueOf(printDegraded, null));
		//
		setWidth(200, tfTitle, tfAuthor, tfSubject, tfKeywords, tfCreator);
		//
		forEach(stream(entrySet(initMap)), entry -> setSelectedItem(getKey(entry), getValue(entry)));
		//
		return dialog;
		//
	}

	private static <K> K getKey(final Entry<K, ?> instance) {
		return instance != null ? instance.getKey() : null;
	}

	private static <V> V getValue(final Entry<?, V> instance) {
		return instance != null ? instance.getValue() : null;
	}

	private static void setSelectedItem(final JComboBox<?> instance, final Object selectedItem) {
		if (instance != null) {
			instance.setSelectedItem(selectedItem);
		}
	}

	private static <T> void forEach(final Stream<T> instance, final Consumer<? super T> action) {
		if (instance != null && action != null) {
			instance.forEach(action);
		}
	}

	private static <K, V> Set<Map.Entry<K, V>> entrySet(final Map<K, V> instance) {
		return instance != null ? instance.entrySet() : null;
	}

	private static <K, V> void put(final Map<K, V> instance, final K key, final V value) {
		if (instance != null) {
			instance.put(key, value);
		}
	}

	private static Boolean valueOf(final String value, final Boolean defaultValue) {
		return testAndApply(value, StringUtils::isNotEmpty, Boolean::valueOf, defaultValue);
	}

	private static <T, R> R testAndApply(final T value, final Predicate<T> predicate, final Function<T, R> function,
			final R defaultValue) {
		return test(predicate, value) ? apply(function, value, defaultValue) : defaultValue;
	}

	private static <T> boolean test(final Predicate<T> instance, final T value) {
		return instance != null && instance.test(value);
	}

	private static <T, R> R apply(final Function<T, R> instance, final T t, final R defaultValue) {
		return instance != null ? instance.apply(t) : defaultValue;
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

	private static boolean checkPassword(final CharSequence password1, final CharSequence password2) {
		return (StringUtils.isEmpty(password1) && StringUtils.isEmpty(password2))
				|| StringUtils.equals(password1, password2);
	}

	private static List<String> toLines(final String input, final PDFont font, final Integer fontSize,
			final float width) throws IOException {
		//
		List<String> lines = null;
		//
		for (String text : ObjectUtils.defaultIfNull(StringUtils.split(input, "\n"), new String[0])) {
			//
			int lastSpace = -1;
			//
			while (text.length() > 0) {
				//
				int spaceIndex = text.indexOf(' ', lastSpace + 1);
				//
				if (spaceIndex < 0) {
					spaceIndex = text.length();
				}
				//
				String subString = text.substring(0, spaceIndex);
				//
				float size = intValue(fontSize, 0) * (font != null ? font.getStringWidth(subString) : 0) / 1000;
				//
				if (size > width) {
					//
					if (lastSpace < 0) {
						lastSpace = spaceIndex;
					}
					//
					if (lines == null) {
						lines = new ArrayList<>();
					}
					lines.add(subString = text.substring(0, lastSpace));
					text = text.substring(lastSpace).trim();
					lastSpace = -1;
					//
				} else if (spaceIndex == text.length()) {
					//
					if (lines == null) {
						lines = new ArrayList<>();
					}
					lines.add(text);
					text = "";
					//
				} else {
					lastSpace = spaceIndex;
				}
				//
			} // while
				//
		} // for
			//
		return lines;
		//
	}

	private static int intValue(final Number instance, final int defaultValue) {
		return instance != null ? instance.intValue() : defaultValue;
	}

	private static ProtectionPolicy createProtectionPolicy(final String ownerPassword, final String userPassword,
			final AccessPermission accessPermission) {
		//
		final StandardProtectionPolicy result = new StandardProtectionPolicy(ownerPassword, userPassword,
				accessPermission);
		result.setPreferAES(true);
		result.setEncryptionKeyLength(128);
		return result;
		//
	}

	private static AccessPermission createAccessPermission(final Object instance, final Field[] fs) {
		//
		final AccessPermission result = new AccessPermission();
		//
		Field f = null;
		AccessPermissionField apf = null;
		String methodName = null;
		//
		ComboBoxModel<?> model = null;
		Boolean b = null;
		//
		Method method = null;
		//
		for (int i = 0; fs != null && i < fs.length; i++) {
			//
			if ((f = fs[i]) == null || (apf = f.getAnnotation(AccessPermissionField.class)) == null
					|| StringUtils.isEmpty(methodName = apf.methodName())) {
				continue;
			}
			//
			setAccessible(f);
			//
			try {
				//
				if ((model = cast(ComboBoxModel.class, instance != null ? f.get(instance) : null)) != null
						&& (b = cast(Boolean.class, model.getSelectedItem())) != null
						&& (method = AccessPermission.class.getDeclaredMethod(methodName, Boolean.TYPE)) != null) {
					//
					setAccessible(method);
					//
					method.invoke(result, b);
					//
				}
				//
			} catch (final ReflectiveOperationException e) {
				LOG.severe(e.getMessage());
			}
		} // for
			//
		return result;
		//
	}

	private static void setText(final JTextComponent instance, final String text) {
		if (instance != null) {
			instance.setText(text);
		}
	}

	private static <T> T testAndGet(final Predicate<T> predicate, final T... items) {
		//
		T item = null;
		//
		for (int i = 0; items != null && i < items.length && predicate != null; i++) {
			if (test(predicate, item = items[i])) {
				return item;
			}
		} // for
			//
		return item;
		//
	}

	private static Color getForeground(final Component instance) {
		//
		try {
			final Field field = Component.class.getDeclaredField("foreground");
			if (field != null && !field.isAccessible()) {
				field.setAccessible(true);
			}
			return cast(Color.class, field != null && instance != null ? field.get(instance) : null);
		} catch (final NoSuchFieldException | IllegalAccessException e) {
			LOG.severe(e.getMessage());
		}
		//
		return null;
		//
	}

	private static Integer valueOf(final String instance) {
		try {
			return instance != null ? Integer.valueOf(instance) : null;
		} catch (final NumberFormatException e) {
			return null;
		}
	}

	private static <V> V get(final Map<?, V> instance, final Object key) {
		return instance != null ? instance.get(key) : null;
	}

	private static Object getSelectedItem(final ComboBoxModel<?> instance) {
		return instance != null ? instance.getSelectedItem() : null;
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

	private static String getText(final JTextComponent instance) {
		return instance != null ? instance.getText() : null;
	}

	public static void main(final String[] args) {
		//
		final JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new MigLayout());
		frame.setTitle("org.apache.pdfbox.pdmodel.PDDocument Writer");
		//
		final DocumentWriter instance = new DocumentWriter();
		instance.init(frame.getContentPane());
		pack(frame);
		setVisible(frame, true);
		//
	}

}