package us.thinkable.xcore.reflect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReflectionUtil {

	public static Method getMethod(Object obj, String name, Class... classes) {
		Method result = null;
		try {
			result = obj.getClass().getMethod(name, classes);
		} catch (Exception e) {
			//
		}
		return result;
	}

	public static Object invoke(Method method, Object obj, Object... args) {
		Object result = null;
		try {
			result = method.invoke(obj, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static List<Method> getGetters(Object obj) {
		List<Method> getters = new ArrayList<Method>();
		Class clazz = obj.getClass();
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			if (method.getName().startsWith("get")) {
				if (method.getParameterCount() == 0) {
					getters.add(method);
				}
			}
		}
		return getters;
	}

	public static Map<String, Object> toMap(Object obj) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Method> getters = getGetters(obj);
		for (Method getter : getters) {
			Object value = invoke(getter, obj);
			String name = getter.getName().substring(3);
			map.put(name, value);
		}
		return map;
	}

	public static String getField(Method accessor) {
		String name = accessor.getName();
		if (name.startsWith("get") || name.startsWith("set")) {
			name = name.substring(3, 4).toLowerCase() + name.substring(4);
		}
		return name;
	}
}
