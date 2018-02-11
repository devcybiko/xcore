package us.thinkable.xcore.reflect;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Custom class loader where you can add a list of jar files and query for
 * classes that implement a particular interface or extend a particular class
 * 
 * @author greg
 * 
 */

public class JarFileLoader extends URLClassLoader {

	/**
	 * Constructor - extends URLClassLoader
	 */
	public JarFileLoader() {
		super(new URL[0]);
	}

	/**
	 * Adds a new jar file by name
	 * 
	 * @param path
	 * @throws MalformedURLException
	 */

	public void addFile(String path) throws MalformedURLException {
		String urlPath = "jar:file://" + path + "!/";
		addURL(new URL(urlPath));
	}

	/**
	 * 
	 * @param jarFile
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public List<String> getClasses(Class clazz) throws Exception {
		List<String> result = new ArrayList<String>();
		for (URL url : this.getURLs()) {
			String fname = url.getPath().replaceAll("^file://", "").replaceAll("!/", "");
			JarFile jar = new JarFile(fname);
			for (Enumeration<JarEntry> e = jar.entries(); e.hasMoreElements();) {
				JarEntry entry = e.nextElement();
				if (entry.isDirectory()) {
					continue;
				}
				if (!entry.getName().endsWith(".class") || entry.getName().contains("$")) {
					continue;
				}
				String className = entry.getName().replaceFirst("\\.class$", "").replace('/', '.');
				try {
					Class<?> newClazz = this.loadClass(className);
					if (clazz.isAssignableFrom(newClazz)) {
						result.add(className);
					}
				} catch (ClassNotFoundException ex) {
					continue; // - its an inner class
				} catch (NoClassDefFoundError ex) {
					continue; // - its an inner class
				}
			}
		}
		return result;
	}

}
