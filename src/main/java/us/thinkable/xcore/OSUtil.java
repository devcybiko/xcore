package us.thinkable.xcore;

public class OSUtil {

	public static String getName() {
		String os = System.getProperty("os.name");
		return os;
	}

	public static String getArchitecture() {
		String os = System.getProperty("os.arch");
		return os;
	}

	public static String getVersion() {
		String os = System.getProperty("os.version");
		return os;
	}
	
	public static String getPatchLevel() {
		String version = System.getProperty("sun.os.patch.level");
		return version;
	}

	public static boolean isOSX() {
		return "Mac OS X".equals(getName());
	}

	public static boolean isWindows() {
		return getName().startsWith("Windows");
	}
}
