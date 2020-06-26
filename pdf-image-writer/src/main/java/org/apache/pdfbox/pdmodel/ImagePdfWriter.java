package org.apache.pdfbox.pdmodel;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.RenderingHints;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextComponentUtil;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.ProtectionPolicy;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import net.miginfocom.swing.MigLayout;

public class ImagePdfWriter implements ActionListener, InitializingBean {

	private static final Logger LOG = LoggerFactory.getLogger(ImagePdfWriter.class);

	private static final String WRAP = "wrap";

	private static final int PREFERRED_WIDTH = 300;

	private static final int PREFERRED_HEIGHT = 300;

	private static final Pattern PATTERN = Pattern.compile("\\.[^.]+$");

	private JFrame jFrame = null;

	private JLabel labelImage = null;

	private JTextComponent pfOwner1, pfOwner2, pfUser1, pfUser2, tfFileInput, tfFileOutput = null;

	private AbstractButton btnFile, btnExecute = null;

	private File file = null;

	private MenuItem showMenuItem, exitMenuItem = null;

	private String toolTip, ownerPassword, userPassword = null;

	private ComboBoxModel<String> pageSize = null;

	private Map<String, PDRectangle> pageSizeMap = null;

	private List<Integer> encryptionKeyLengths = null;

	private ComboBoxModel<Integer> encryptionKeyLength = null;

	private ImagePdfWriter() {
	}

	public void setjFrame(final JFrame jFrame) {
		this.jFrame = jFrame;
	}

	public JFrame getjFrame() {
		return jFrame;
	}

	public void setToolTip(final String toolTip) {
		this.toolTip = toolTip;
	}

	public void setOwnerPassword(final String ownerPassword) {
		this.ownerPassword = ownerPassword;
	}

	public void setUserPassword(final String userPassword) {
		this.userPassword = userPassword;
	}

	public void setEncryptionKeyLengths(final List<Integer> encryptionKeyLengths) {
		this.encryptionKeyLengths = encryptionKeyLengths;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		init(jFrame);
	}

	private void init(final Container container) throws IllegalAccessException {
		//
		final String wrap = String.format("span %1$s,%2$s", 2, WRAP);
		//
		add(container, new JLabel("Page Size"));
		add(container,
				new JComboBox<>(
						pageSize = new DefaultComboBoxModel<>(ArrayUtils.insert(0,
								ObjectUtils.defaultIfNull(toArray(
										keySet(pageSizeMap = getPageSizeMap(PDRectangle.class.getDeclaredFields())),
										new String[0]), new String[0]),
								(String) null))),
				WRAP);
		//
		add(container, new JLabel("Image"));
		add(container, btnFile = new JButton("Select image file"), wrap);
		btnFile.addActionListener(this);
		//
		add(container, new JLabel());
		add(container, tfFileInput = new JTextField(), wrap);
		tfFileInput.setEditable(false);
		tfFileInput.setPreferredSize(new Dimension(PREFERRED_WIDTH, (int) tfFileInput.getPreferredSize().getHeight()));
		//
		add(container, new JLabel());
		final JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
		add(container, panel, wrap);
		panel.add(labelImage = new JLabel());
		//
		add(container, new JLabel("Encryption Key Length"));
		add(container,
				new JComboBox<>(
						encryptionKeyLength = new DefaultComboBoxModel<>(
								ArrayUtils
										.insert(0,
												ObjectUtils.defaultIfNull(
														encryptionKeyLengths != null
																? encryptionKeyLengths.toArray(new Integer[0])
																: new Integer[] { cast(Integer.class,
																		FieldUtils.readDeclaredStaticField(
																				ProtectionPolicy.class,
																				"DEFAULT_KEY_LENGTH", true)) },
														new Integer[0]),
												(Integer) null))),
				WRAP);
		//
		add(container, new JLabel("Owner Password"));
		add(container, pfOwner1 = new JPasswordField());
		add(container, pfOwner2 = new JPasswordField(), WRAP);
		//
		add(container, new JLabel("User Password"));
		add(container, pfUser1 = new JPasswordField());
		add(container, pfUser2 = new JPasswordField(), WRAP);
		//
		add(container, new JLabel());
		add(container, btnExecute = new JButton("Execute"), wrap);
		btnExecute.addActionListener(this);
		//
		add(container, new JLabel("Output"));
		add(container, tfFileOutput = new JTextField(), wrap);
		tfFileOutput.setEditable(false);
		//
		setWidth(PREFERRED_WIDTH, tfFileInput, tfFileOutput);
		setWidth(PREFERRED_WIDTH / 2, pfOwner1, pfOwner2, pfUser1, pfUser2);
		//
		accept(Arrays.asList(pfOwner1::setText, pfOwner2::setText), ownerPassword);
		accept(Arrays.asList(pfUser1::setText, pfUser2::setText), userPassword);
		//
		addWindowListener(jFrame, new InnerWindowAdapter());
		//
		try {
			add(SystemTray.isSupported() ? SystemTray.getSystemTray() : null,
					createTrayIcon(createImage("PDF", "IMG"), toolTip));
		} catch (final AWTException e) {
			LOG.error(e.getMessage(), e);
		}
		//
	}

	private static <K> Set<K> keySet(final Map<K, ?> instance) {
		return instance != null ? instance.keySet() : null;
	}

	private static <E> E[] toArray(final Collection<E> instance, final E[] array) {
		return instance != null && array != null ? instance.toArray(array) : null;
	}

	private static Map<String, PDRectangle> getPageSizeMap(final Field[] fs) {
		//
		Map<String, PDRectangle> result = null;
		//
		Field f = null;
		Object obj = null;
		//
		for (int i = 0; fs != null && i < fs.length; i++) {
			//
			if ((f = fs[i]) == null || !Objects.equals(f.getType(), PDRectangle.class)
					|| !Modifier.isStatic(f.getModifiers())) {
				continue;
			}
			//
			f.setAccessible(true);
			//
			try {
				//
				if (!((obj = f.get(null)) instanceof PDRectangle)) {
					continue;
				} //
					//
				if (result == null) {
					result = new LinkedHashMap<>();
				}
				result.put(f.getName(), (PDRectangle) obj);
				//
			} catch (final IllegalAccessException e) {
				LOG.error(e.getMessage(), e);
			}
			//
		} // for
			//
		return result;
		//
	}

	private static <T> void accept(final Iterable<Consumer<T>> consumers, final T value) {
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

	private static synchronized void addWindowListener(final Window instance, final WindowListener windowListener) {
		if (instance != null) {
			instance.addWindowListener(windowListener);
		}
	}

	private class InnerWindowAdapter extends WindowAdapter {

		@Override
		public void windowClosed(final WindowEvent e) {
			System.exit(0);
		}

		@Override
		public void windowClosing(final WindowEvent e) {
			setLabel(showMenuItem, "Show");
		}

	}

	private static void addActionListener(final ActionListener actionListener, final MenuItem... mis) {
		//
		MenuItem mi = null;
		//
		for (int i = 0; mis != null && i < mis.length; i++) {
			//
			if ((mi = mis[i]) == null) {
				continue;
			} // skip null
				//
			mi.addActionListener(actionListener);
			//
		} // for
			//
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

	@Override
	public void actionPerformed(final ActionEvent evt) {
		//
		final Object source = evt != null ? evt.getSource() : null;
		//
		if (Objects.deepEquals(source, btnFile)) {
			//
			setIcon(labelImage, new ImageIcon());
			//
			JTextComponentUtil.setText(tfFileInput, null);
			//
			final JFileChooser jfc = new JFileChooser(".");
			//
			if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				try {
					//
					setIcon(labelImage, new ImageIcon(getScaledInstance(ImageIO.read(file = jfc.getSelectedFile()),
							PREFERRED_WIDTH, PREFERRED_HEIGHT, Image.SCALE_DEFAULT)));
					//
					JTextComponentUtil.setText(tfFileInput, getAbsolutePath(file));
					//
				} catch (final IOException | NullPointerException e) {
					LOG.error(e.getMessage(), e);
				}
			}
			//
		} else if (Objects.deepEquals(source, btnExecute)) {
			//
			try {
				//
				if (file == null) {
					//
					final JFileChooser jfc = new JFileChooser(".");
					if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
						file = jfc.getSelectedFile();
					}
					//
				}
				//
				final String owner1 = JTextComponentUtil.getText(pfOwner1),
						owner2 = JTextComponentUtil.getText(pfOwner2);
				if (!Objects.equals(owner1, owner2)) {
					JOptionPane.showMessageDialog(null, "Owner passwords are not matched");
					return;
				}
				//
				final String user1 = JTextComponentUtil.getText(pfUser1), user2 = JTextComponentUtil.getText(pfUser2);
				if (!Objects.equals(user1, user2)) {
					JOptionPane.showMessageDialog(null, "User passwords are not matched");
					return;
				}
				//
				final PDDocument document = toPDDocument(file,
						cast(PDRectangle.class, pageSizeMap.get(getSelectedItem(pageSize))),
						cast(Integer.class, getSelectedItem(encryptionKeyLength)), owner1, user1);
				if (document != null) {
					//
					final File fileOutput = new File(generateFileName(file, "pdf"));
					JTextComponentUtil.setText(tfFileOutput, getAbsolutePath(fileOutput));
					document.save(new File(generateFileName(fileOutput, "pdf")));
					//
				}
				//
			} catch (final IOException e) {
				LOG.error(e.getMessage(), e);
			}
			//
		} else if (Objects.equals(source, showMenuItem)) {
			final Boolean isVisible = isVisible(jFrame);
			if (Objects.equals(Boolean.TRUE, isVisible)) {
				setVisible(jFrame, false);
				setLabel(showMenuItem, "Show");
			} else if (Objects.equals(Boolean.FALSE, isVisible)) {
				setVisible(jFrame, true);
				setLabel(showMenuItem, "Hide");
			}
		} else if (Objects.equals(source, exitMenuItem) && jFrame != null) {
			jFrame.dispose();
		}
		//
	}

	private static String getAbsolutePath(final File instance) {
		return instance != null ? instance.getAbsolutePath() : null;
	}

	private static Object getSelectedItem(final ComboBoxModel<?> instance) {
		return instance != null ? instance.getSelectedItem() : null;
	}

	private static <T> T cast(final Class<T> clz, final Object instance) {
		return clz != null && clz.isInstance(instance) ? clz.cast(instance) : null;
	}

	private static String generateFileName(final File file, final String newFileExtension) {
		//
		final String name = getName(file);
		if (name == null || StringUtils.isBlank(newFileExtension)) {
			return name;
		}
		//
		return replaceAll(matcher(PATTERN, name), "." + newFileExtension);
		//
	}

	private static String getName(final File instance) {
		return instance != null ? instance.getName() : null;
	}

	private static Matcher matcher(final Pattern instance, final String input) {
		return instance != null && input != null ? instance.matcher(input) : null;
	}

	private static String replaceAll(final Matcher instance, final String replacement) {
		return instance != null && replacement != null ? instance.replaceAll(replacement) : null;
	}

	private static Boolean isVisible(final Component instance) {
		return instance != null ? Boolean.valueOf(instance.isVisible()) : null;
	}

	private static synchronized void setLabel(final MenuItem instance, final String label) {
		if (instance != null) {
			instance.setLabel(label);
		}
	}

	private static void setIcon(final JLabel instance, final Icon icon) {
		if (instance != null) {
			instance.setIcon(icon);
		}
	}

	private static Image getScaledInstance(final Image instance, final int width, final int height, final int hints) {
		return instance != null ? instance.getScaledInstance(width, height, hints) : null;
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

	public static void main(final String[] args) throws IllegalAccessException {
		//
		final JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new MigLayout());
		frame.setTitle("org.apache.pdfbox.pdmodel.PDDocument Writer");
		//
		final ImagePdfWriter instance = new ImagePdfWriter();
		instance.init(frame.getContentPane());
		pack(frame);
		setVisible(frame, true);
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

	private static PDDocument toPDDocument(final File file, final PDRectangle pageSize,
			final Integer encryptionKeyLength, final String ownerPassword, final String userPassword)
			throws IOException {
		//
		final PDDocument doc = new PDDocument();
		//
		final PDPage page = new PDPage(pageSize);
		doc.addPage(page);
		//
		final PDImageXObject pdImage = PDImageXObject.createFromFile(getAbsolutePath(file), doc);
		final PDPageContentStream contents = new PDPageContentStream(doc, page);
		final PDRectangle mediaBox = page.getMediaBox();
		//
		final float startX = (mediaBox.getWidth() - pdImage.getWidth()) / 2;
		final float startY = (mediaBox.getHeight() - pdImage.getHeight()) / 2;
		contents.drawImage(pdImage, startX, startY);
		contents.close();
		//
		final StandardProtectionPolicy policy = new StandardProtectionPolicy(ownerPassword, userPassword,
				new AccessPermission());
		policy.setPreferAES(true);
		if (encryptionKeyLength != null) {
			policy.setEncryptionKeyLength(encryptionKeyLength.intValue());
		}
		//
		doc.protect(policy);
		//
		return doc;
		//
	}

	private TrayIcon createTrayIcon(final Image image, final String toolTip) {
		//
		final PopupMenu popup = new PopupMenu();
		popup.add(new MenuItem("PDF Image Writer"));
		popup.add(showMenuItem = new MenuItem("Hide"));
		popup.add(exitMenuItem = new MenuItem("Exit"));
		//
		addActionListener(this, showMenuItem, exitMenuItem);
		//
		final TrayIcon trayIcon = new TrayIcon(image, toolTip);
		trayIcon.setPopupMenu(popup);
		//
		return trayIcon;
		//
	}

	private static BufferedImage createImage(final String line1, final String line2) {
		//
		final int sixteen = 16;
		BufferedImage img = new BufferedImage(sixteen, sixteen, BufferedImage.TYPE_INT_ARGB);
		//
		Graphics2D g2d = img.createGraphics();
		if (g2d != null) {
			//
			final int eight = 8;
			final String arial = "Arial";
			//
			final Font font1 = new Font(arial, Font.BOLD, eight);
			g2d.setFont(font1);
			final FontMetrics fm = getFontMetrics(g2d);
			g2d.dispose();
			//
			if ((g2d = (img = new BufferedImage(sixteen, sixteen, BufferedImage.TYPE_INT_ARGB))
					.createGraphics()) != null) {
				//
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				//
				g2d.setColor(Color.BLACK);
				//
				g2d.setFont(font1);
				g2d.drawString(line1, 0, 0 + eight);
				g2d.drawString(line2, 0, fm.getAscent() + eight);
				//
				g2d.dispose();
				//
			} // if
				//
		} // if
			//
		return img;
		//
	}

	private static FontMetrics getFontMetrics(final Graphics instance) {
		return instance != null ? instance.getFontMetrics() : null;
	}

	private static void add(final SystemTray instance, final TrayIcon trayIcon) throws AWTException {
		if (instance != null && trayIcon != null) {
			instance.add(trayIcon);
		}
	}

}