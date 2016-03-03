package dev.wolveringer.util.apache;

public class Validate {

	public static void notNull(Object parent, String string) {
		if(parent == null)
			throw new NullPointerException(string);
	}

	public static void isTrue(boolean b, String string) {
		if(b)
			throw new NullPointerException(string);
	}

	public static void notEmpty(String path, String string) {
		if(path == null || "".equalsIgnoreCase(path) || path.isEmpty() || path.length() == 0)
			throw new NullPointerException(string);
	}

}
