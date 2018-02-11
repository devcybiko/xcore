package us.thinkable.xcore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import us.thinkable.xcore.reflect.ReflectionUtil;

public class Template {
	private String template;
	private List<Fragment> fragments;

	public Template(String templateFile) {
		if (templateFile.startsWith("/")) {
			String tmp = "Error reading " + templateFile;
				tmp = FileUtil.fileRead(templateFile);
			this.template = tmp;
			compile();
		} else {
			this.template = templateFile;
			compile();
		}
	}

	public String expand(Map<String, Object> context) {
		String result = "";
		for (Fragment fragment : fragments) {
			result += fragment.expand(context);
		}
		return result;
	}

	public static String page(String templatePath, Object header, List<Object> body, Object footer,
			Map<String, Object> context) {
		Template headerTemplate = new Template(templatePath + ".header");
		Template bodyTemplate = new Template(templatePath + ".body");
		Template footerTemplate = new Template(templatePath + ".footer");

		Map<String, Object> map = new HashMap<String, Object>();
		if (context != null) {
			map.putAll(context);
		}

		map.put("header", header);
		map.put("footer", footer);
		String result = headerTemplate.expand(map);

		for (Object obj : body) {
			map.put("body", obj);
			result += bodyTemplate.expand(map);
		}
		map.remove("body");

		result += footerTemplate.expand(map);
		return result;
	}

	private void compile() {
		fragments = new ArrayList<Fragment>();
		Pattern p = Pattern.compile("\\$\\{.*?\\}");
		Matcher m = p.matcher(template);
		int i = 0;
		while (m.find()) {
			String left = template.substring(i, m.start());
			String format = null;
			String nullFormat = null;
			String name = m.group().substring(2);
			name = name.substring(0, name.length() - 1);
			if (name.contains("|")) {
				String[] words = name.split("[|]");
				name = words[0];
				format = words[1];
				if (words.length > 2) {
					nullFormat = words[2];
				}
			}
			Fragment fragment = new Fragment(left, name, format, nullFormat);
			fragments.add(fragment);
			i = m.end();
		}
		String left = template.substring(i);
		Fragment fragment = new Fragment(left, null, null, null);
		fragments.add(fragment);
	}

	private static class Fragment {
		String before;
		String macroName;
		String format;
		String nullFormat;

		public Fragment(String before, String macroName, String format, String nullFormat) {
			this.before = before;
			this.macroName = macroName;
			this.format = format;
			this.nullFormat = nullFormat;
		}

		public String expand(Map<String, Object> m) {
			try {
				Object value = macroName != null ? lookup(m, macroName) : "";
				if (value == null && nullFormat != null) {
					String newFormat = nullFormat.replaceAll("[%]", "%1\\$");
					value = String.format(newFormat, value);
				} else if (value == null && nullFormat == null) {
					value = "";
				} else if (value != null && format != null) {
					String newformat = format.replaceAll("[%]", "%1\\$");
					value = String.format(newformat, value);
				}
				return this.before + value.toString();
			} catch (Exception ex) {
				return this.before + "${" + macroName + "|" + format + "=ERR:" + ex.getMessage() + "}";
			}
		}

		private Object lookup(final Map<String, Object> m, final String key) {
			Map<String, Object> map = m;
			Object value = key;

			String words[] = key.split("[.]");
			for (String word : words) {
				Object obj = map.get(word);
				if (obj == null) {
					value = null;
					break;
				} else if (obj instanceof Map) {
					map = (Map<String, Object>) obj;
				} else if (obj instanceof String) {
					value = obj;
					break;
				} else if (obj instanceof Integer) {
					value = obj;
					break;
				} else if (obj instanceof Double) {
					value = obj;
					break;
				} else if (obj instanceof Boolean) {
					value = obj;
					break;
				} else if (obj instanceof Long) {
					value = obj;
					break;
				} else if (obj instanceof Date) {
					value = obj;
					break;
				} else {
					map = ReflectionUtil.toMap(obj);
				}
			}
			return value;
		}

		public String toString() {
			return before + ":" + macroName + ":" + format;
		}
	}

	public static class Bean {
		String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

	public static void main(String[] args) {
		Template t = new Template(
				"Hello ${inner.Name|%s|What?} ${bean.Name} World! ${inner.pi|%4.2f} today=${inner.now|%tm %te,%tY %tH:%tM:%tS} ${hello|%tH}");
		Map<String, Object> context = new HashMap<String, Object>();
		Map<String, Object> innerContext = new HashMap<String, Object>();
		Double pi = 3.14159265;
		Date now = new Date();
		innerContext.put("pi", pi);
		innerContext.put("Namexxx", "Greg");
		innerContext.put("now", now);
		Bean bean = new Bean();
		bean.setName("Smith");
		context.put("bean", bean);
		context.put("inner", innerContext);
		context.put("hello", "world");
		String result = t.expand(context);
		System.out.println(result);
	}
}
