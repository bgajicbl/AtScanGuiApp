package at.mtel.denza.alfresco.scan;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class AppPropertyReader {

	private static ResourceBundle myResources;
	public static String FILENAME = "props";
	static {
		initialize(FILENAME);
	}

	public static void initialize(String propertyFile) throws MissingResourceException {
		try {
			myResources = ResourceBundle.getBundle(FILENAME, Locale.getDefault());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static String getParameter(String parmName) {
		String param = null;
		try {
			param = myResources.getString(parmName);

		} catch (Exception e) {
			param = null;
			e.printStackTrace();
		}
		if (param != null)
			return param.trim();
		else
			return param;
	}

}
