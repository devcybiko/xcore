package us.thinkable.xcore;

import java.util.HashMap;
import java.util.Map;

public class Context {
	private Map<String, String> map = new HashMap<String, String>();

	public Context() {
		//
	}

	public Context(Map<String, String> map) {
		this.map.putAll(map);
	}

	public void put(String key, String value) {
		map.put(key, value);
	}

	public String get(String key, String dflt) {
		return getString(key, dflt);
	}

	public Integer get(String key, Integer dflt) {
		return getInteger(key, dflt);
	}

	public String getString(String key, String dflt) {
		String result = map.get(key);
		if (result == null) {
			return dflt;
		}
		return result;
	}

	public Integer getInteger(String key, Integer dflt) {
		String result = map.get(key);
		if (result == null) {
			return dflt;
		}
		try {
			Integer iResult = Integer.parseInt(result);
			return iResult;
		} catch (NumberFormatException ex) {
			return dflt;
		}
	}
}
