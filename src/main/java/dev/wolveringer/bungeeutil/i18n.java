package dev.wolveringer.bungeeutil;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class i18n {
	private static final String BUNDLE_NAME = "Messages";

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	public static String tr(String key, Object... args){
		return String.format(getString(key), args);
	}
	
	public static String getString(String key) {
		try{
			return RESOURCE_BUNDLE.getString(key);
		}catch (MissingResourceException e){
			return '!' + key + '!';
		}
	}

	private i18n() {}
}
