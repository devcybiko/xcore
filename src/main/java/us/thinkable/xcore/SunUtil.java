package us.thinkable.xcore;

public class SunUtil {

	public static String getArchDataModel() {
		String version = System.getProperty("sun.arch.data.model");
		return version;
	}

	public static String getBootClassPath() {
		String version = System.getProperty("sun.boot.class.path");
		return version;
	}

	public static String getBootLibraryPath() {
		String version = System.getProperty("sun.boot.library.path");
		return version;
	}

	public static String getCPUEndian() {
		String version = System.getProperty("sun.cpu.endian");
		return version;
	}

	public static String getCPUIsaList() {
		String version = System.getProperty("sun.cpu.isalist");
		return version;
	}

	public static String getDesktop() {
		String version = System.getProperty("sun.desktop");
		return version;
	}

	public static String getIOUNicodeEncoding() {
		String version = System.getProperty("sun.io.unicode.encoding");
		return version;
	}

	public static String getJavaCommand() {
		String version = System.getProperty("sun.java.command");
		return version;
	}
	public static String getJavaLauncher() {
		String version = System.getProperty("sun.java.launcher");
		return version;
	}
	public static String getJNUEncoding() {
		String version = System.getProperty("sun.jnu.encoding");
		return version;
	}
	public static String getManagemenetCompiler() {
		String version = System.getProperty("sun.management.compiler");
		return version;
	}
	public static String getOSPatchLevel() {
		String version = System.getProperty("sun.os.patch.level");
		return version;
	}
}
