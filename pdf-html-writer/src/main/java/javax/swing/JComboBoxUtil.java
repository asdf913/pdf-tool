package javax.swing;

public interface JComboBoxUtil {

	static void setSelectedItem(final JComboBox<?> instance, final Object selectedItem) {
		if (instance != null) {
			instance.setSelectedItem(selectedItem);
		}
	}

}