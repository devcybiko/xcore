package us.thinkable.xcore;

public class UserUtil {

	public static String getCountry() {
		String version = System.getProperty("user.country");
		return version;
	}

	public static String getDir() {
		String version = System.getProperty("user.dir");
		return version;
	}

	public static String getHome() {
		String version = System.getProperty("user.home");
		return version;
	}

	public static String getLanguage() {
		String version = System.getProperty("user.language");
		return version;
	}

	public static String getName() {
		String version = System.getProperty("user.name");
		return version;
	}

	public static String getScript() {
		String version = System.getProperty("user.script");
		return version;
	}

	public static String getTimeZone() {
		String version = System.getProperty("user.timezone");
		return version;
	}

	public static String getVarient() {
		String version = System.getProperty("user.variant");
		return version;
	}
}
