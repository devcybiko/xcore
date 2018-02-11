package us.thinkable.xcore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class XDB implements Serializable {
	private Map<String, Object> map = new HashMap<String, Object>();
	private File mapFile = null;
	private static XDB defaultXDB = null;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public XDB(String fname) {
		this.mapFile = new File(fname);
		this.hsulf();
	}

	public static void xinit(String fname) {
		defaultXDB = new XDB(fname);
		defaultXDB.hsulf();
	}

	public void put(String name, Object obj) {
		map.put(name, obj);
	}

	public static void xput(String name, Object obj) {
		defaultXDB.put(name, obj);
	}

	public Object get(String name) {
		return get(name, null);
	}

	public static Object xget(String name) {
		return defaultXDB.get(name, null);
	}

	public Object get(String name, Object obj) {
		Object o = map.get(name);
		if (o == null) {
			o = obj;
		}
		return o;
	}

	public static Object xget(String name, Object obj) {
		return defaultXDB.get(name, obj);
	}

	public String getString(String name) {
		return (String) get(name, null);
	}

	public static String xgetString(String name) {
		return (String) defaultXDB.get(name, null);
	}

	public String getString(String name, String dflt) {
		return (String) get(name, dflt);
	}

	public static String xgetString(String name, String dflt) {
		return (String) defaultXDB.get(name, dflt);
	}

	public void flush() {
		try {
			FileOutputStream fos = new FileOutputStream(mapFile);
			ObjectOutputStream out = new ObjectOutputStream(fos);
			out.writeObject(map);
			out.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void xflush() {
		defaultXDB.flush();
	}

	@SuppressWarnings("unchecked")
	public void hsulf() {
		try {
			FileInputStream fis = new FileInputStream(mapFile);
			ObjectInputStream in = new ObjectInputStream(fis);
			map = (HashMap<String, Object>) in.readObject();
			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	}
	
	public static void xhsulf() {
		defaultXDB.hsulf();
	}

}
