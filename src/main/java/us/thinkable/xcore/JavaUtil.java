package us.thinkable.xcore;

public class JavaUtil {

	public static String getAWTGraphicsEnv() {
		String value = System.getProperty("java.awt.graphicsenv");
		return value;
	}

	public static String getAWTPrinterJob() {
		String value = System.getProperty("java.awt.printerjob");
		return value;
	}

	public static String getAWTToolkit() {
		String value = System.getProperty("awt.toolkit");
		return value;
	}

	public static String getClassPath() {
		String value = System.getProperty("java.class.path");
		return value;
	}

	public static String getClassVersion() {
		String value = System.getProperty("java.class.version");
		return value;
	}

	public static String getCommand() {
		String value = System.getProperty("sun.java.command");
		return value;
	}

	public static String getEndorsedDirs() {
		String value = System.getProperty("java.endorsed.dirs");
		return value;
	}

	public static String getExtDirs() {
		String value = System.getProperty("java.ext.dirs");
		return value;
	}

	public static String getHome() {
		String value = System.getProperty("java.home");
		return value;
	}

	public static String getIOTmpDir() {
		String value = System.getProperty("java.io.tmpdir");
		return value;
	}

	public static String getLauncher() {
		String value = System.getProperty("sun.java.launcher");
		return value;
	}

	public static String getLibraryPath() {
		String value = System.getProperty("java.library.path");
		return value;
	}

	public static String getRuntimeName() {
		String value = System.getProperty("java.runtime.name");
		return value;
	}

	public static String getRuntimeVersion() {
		String value = System.getProperty("java.runtime.version");
		return value;
	}

	public static String getSpecificationName() {
		String value = System.getProperty("java.specification.name");
		return value;
	}

	public static String getSpecificationVendor() {
		String value = System.getProperty("java.specification.vendor");
		return value;
	}

	public static String getSpecificationVersion() {
		String value = System.getProperty("java.specification.version");
		return value;
	}

	public static String getVendor() {
		String value = System.getProperty("java.vendor");
		return value;
	}

	public static String getVendorURL() {
		String value = System.getProperty("java.vendor.url");
		return value;
	}

	public static String getVendorURLBug() {
		String value = System.getProperty("java.vendor.url.bug");
		return value;
	}

	public static String getVersion() {
		String value = System.getProperty("java.version");
		return value;
	}

	public static String getVMInfo() {
		String value = System.getProperty("java.vm.info");
		return value;
	}

	public static String getVMName() {
		String value = System.getProperty("java.vm.name");
		return value;
	}

	public static String getVMSpecification() {
		String value = System.getProperty("java.vm.specification.version");
		return value;
	}

	public static String getVMSpecificationName() {
		String value = System.getProperty("java.vm.specification.name");
		return value;
	}

	public static String getVMSpecificationVendor() {
		String value = System.getProperty("java.vm.specification.vendor");
		return value;
	}

	public static String getVMVendor() {
		String value = System.getProperty("java.vm.vendor");
		return value;
	}

	public static String getVMVersion() {
		String value = System.getProperty("java.vm.version");
		return value;
	}
}
