package us.thinkable.xcore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * A number of useful utility methods
 * 
 * @author Gregory Smith
 *
 */
public class FileUtil {

	/**
	 * Returns an InputStream based on the path. If the path begins with "*"
	 * then the stream is read from the jar's resources otherwise it's read from
	 * the filesystem.
	 * 
	 * @param path
	 * @return
	 */
	public static InputStream getStream(String path) {
		InputStream is = null;

		// try getting it from the file system first
		is = newFileInputStream(path);

		if (is == null) {
			// if the path begins with a '/' we may have to trim that off
			is = newFileInputStream(path.substring(1));
		}
		if (is == null) {
			// if the file system failed, try getting it from the resource
			// system
			is = FileUtil.class.getResourceAsStream(path);
		}
		return is;
	}

	public static URL getURL(String path) {
		URL url = null;

		// try getting it from the resource first
		url = FileUtil.class.getResource(path);

		if (url == null) {
			// if the resource path failed, try getting it from the file
			// system
			File file = new File(path);
			if (file.exists()) {
				url = newURL(path);
			}
		}

		if (url == null) {
			// if the path begins with a '/' we have to trim that off
			File file = new File(path.substring(1));
			if (file.exists()) {
				url = newURL(path.substring(1));
			}
		}

		return url;
	}

	private static FileInputStream newFileInputStream(String path) {
		try {
			FileInputStream fis = new FileInputStream(path);
			return fis;
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	private static URL newURL(String path) {
		try {
			URL url = new URL("file:" + path);
			return url;
		} catch (MalformedURLException e) {
			return null;
		}
	}

	/**
	 * Read an entire text file from a pathname. If the path begins with a *
	 * then it reads from the resource
	 * 
	 * @param is
	 * @return @
	 */
	public static String fileRead(String path) {
		InputStream is = getStream(path);
		return fileRead(is);
	}

	public static String fileRead(File infile) {
		InputStream is = getStream(infile.getAbsolutePath());
		return fileRead(is);
	}

	/**
	 * Read an entire text file from an InputStream
	 * 
	 * @param is
	 * @return @
	 */
	public static String fileRead(InputStream is) {
		try {
			StringBuffer sb = new StringBuffer();
			InputStreamReader isr = null;
			try {
				isr = new InputStreamReader(is, "UTF8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			Reader in = new BufferedReader(isr);
			int ch;
			while ((ch = in.read()) > -1) {
				sb.append((char) ch);
			}
			in.close();
			return sb.toString();
		} catch (IOException ex) {
			throw new CoreException(ex);
		}
	}

	/**
	 * Read a text file into list of strings
	 * 
	 * @param infile
	 *            the file to read
	 * @return a list of Strings, one per each line in the text file
	 */
	public static List<String> fileReadLines(InputStream is) {
		try {
			List<String> result = new ArrayList<String>();
			DataInputStream in = new DataInputStream(is);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				result.add(strLine);
			}
			in.close();
			return result;
		} catch (IOException ex) {
			throw new CoreException(ex);
		}
	}

	public static List<String> fileReadLines(String path) {
		InputStream is = getStream(path);
		return fileReadLines(is);
	}

	public static List<String> fileReadLines(File infile) {
		InputStream is = getStream(infile.getAbsolutePath());
		return fileReadLines(is);
	}

	public static List<String> fileReadLinesSansComments(InputStream is) {
		List<String> result = new ArrayList<String>();
		List<String> strings = fileReadLines(is);
		for (String strLine : strings) {
			if (strLine.startsWith("#include")) {
				System.out.println(strLine);
				String[] words = strLine.split(" ");
				if (words.length > 1) {
					List<String> lines = fileReadLinesSansComments(words[1]);
					result.addAll(lines);
				}
			} else if (strLine.startsWith("#")) {
				continue;
			}
			if (strLine.trim().length() == 0) {
				continue;
			}
			result.add(strLine);
		}
		return result;
	}

	public static List<String> fileReadLinesSansComments(String path) {
		InputStream is = getStream(path);
		return fileReadLinesSansComments(is);
	}

	public static List<String> fileReadLinesSansComments(File file) {
		InputStream is = getStream(file.getAbsolutePath());
		return fileReadLinesSansComments(is);
	}

	public static List<Map<String, String>> fileReadKeyValues(InputStream is) {
		List<Map<String, String>> elements = new ArrayList<Map<String, String>>();
		List<String> list = fileReadLinesSansComments(is);
		for (String line : list) {
			// System.out.println(line);
			Map<String, String> map = new HashMap<String, String>();
			elements.add(map);
			parseKeyValueString(line, map);
		}
		return elements;
	}

	public static void parseKeyValueString(String line, Map<String, String> map) {
		String[] fields = line.split("\\t");
		for (String field : fields) {
			if (field.length() == 0) {
				continue;
			}
			int index = field.indexOf('=');
			if (index != -1) {
				String key = field.substring(0, index).trim();
				String value = field.substring(index + 1).trim();
				map.put(key, value);
			} else {
				map.put(field.trim(), "");
			}
		}
	}

	public static List<Map<String, String>> fileReadKeyValues(String path) {
		InputStream is = getStream(path);
		return fileReadKeyValues(is);
	}

	public static List<Map<String, String>> fileReadKeyValues(File file) {
		InputStream is = getStream(file.getAbsolutePath());
		return fileReadKeyValues(is);
	}

	public static Map<String, String> fileReadProperties(InputStream is) {
		Map<String, String> map = new HashMap<String, String>();
		List<String> list = fileReadLinesSansComments(is);
		for (String line : list) {
			// System.out.println(line);
			int index = line.indexOf('=');
			if (index != -1) {
				String key = line.substring(0, index);
				String value = line.substring(index + 1);
				map.put(key, value);
			} else {
				map.put(line, "");
			}
		}
		return map;
	}

	public static Map<String, String> fileReadProperties(String path) {
		InputStream is = getStream(path);
		return fileReadProperties(is);
	}

	public static Map<String, String> fileReadProperties(File file) {
		InputStream is = getStream(file.getAbsolutePath());
		return fileReadProperties(is);
	}

	/**
	 * writes a list of strings to a file
	 * 
	 * @param outfile
	 *            the file to write to
	 * @param lines
	 *            the list of lines to write to the file (one line per String) @
	 */
	public static void fileWriteLines(File outfile, List<String> lines) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(outfile));
			for (String line : lines) {
				writer.write(line);
				writer.newLine();
			}
			writer.close();
		} catch (IOException ex) {
			throw new CoreException(ex);
		}
	}

	/**
	 * Writes a entire string to a file Arguably, the input 'contents' should be
	 * a StringBuffer But to make the method more generic String is used A
	 * StringBuffer can be written by doing foo.toString() on the StringBuffer
	 * 
	 * @param contents
	 *            the string to be written
	 * @param outfile
	 *            the file to write to @
	 */
	public static void fileWrite(File outfile, String contents) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(outfile));
			writer.write(contents);
			writer.flush();
			writer.close();
		} catch (IOException ex) {
			throw new CoreException(ex);
		}
	}

	public static String runCommand(File templateFile, String... context) {
		String cmd = templateExpand(templateFile, context);
		return runCommand(cmd);
	}

	public static String runCommand(File templateFile, Map<String, String> context) {
		String cmd = templateExpand(templateFile, context);
		return runCommand(cmd);
	}

	public static String runCommand(String cmd) {
		String[] tokens = cmd.split(" ");
		return runCommand(tokens);
	}

	public static String runCommand(String... tokens) {
		try {
			for (int i = 0; i < tokens.length; i++) {
				tokens[i] = tokens[i].trim();
			}

			ProcessBuilder builder = new ProcessBuilder(tokens);
			// System.out.println(builder.command());
			builder.redirectErrorStream(true);
			Process process = builder.start();
			ConsoleReader consoleReader = new ConsoleReader(process.getInputStream());
			consoleReader.start();
			process.waitFor();
			consoleReader.join();
			String result = consoleReader.getResult();
			return result;
		} catch (Exception ex) {
			return ex.getMessage();
		}
	}

	private static class ConsoleReader extends Thread {
		private InputStream is;

		private StringWriter sw;

		ConsoleReader(InputStream is) {
			this.is = is;
			sw = new StringWriter();
		}

		@Override
		public void run() {
			try {
				int c;
				while ((c = is.read()) != -1) {
					sw.write(c);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		String getResult() {
			return sw.toString();
		}
	}

	public static String templateExpand(File file, Map<String, String> context) {
		String template = fileRead(file);
		return templateExpand(template, context);
	}

	public static String templateExpand(String s, Map<String, String> context) {
		String result = s;
		for (Entry<String, String> entry : context.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();

			String pattern = "\\$\\{" + key + "\\}";
			result = Pattern.compile(pattern).matcher(result).replaceAll(Matcher.quoteReplacement(value));
		}
		return result;
	}

	public static String templateExpand(File file, String... args) {
		String template = fileRead(file);
		return templateExpand(template, args);
	}

	public static String templateExpand(String s, String... args) {
		String result = s;
		for (int i = 0; i < args.length; i += 2) {
			String key = args[i];
			String value = args[i + 1];

			String pattern = "\\$\\{" + key + "\\}";
			result = Pattern.compile(pattern).matcher(result).replaceAll(Matcher.quoteReplacement(value));
		}
		return result;
	}

	public static String getFileEncoding() {
		String value = System.getProperty("file.encoding");
		return value;
	}

	public static String getFileEncodingPkg() {
		String value = System.getProperty("file.encoding.pkg");
		return value;
	}

	public static String getFileSeparator() {
		String value = System.getProperty("file.separator");
		return value;
	}

	public static String getLineSeparator() {
		String value = System.getProperty("line.separator");
		return value;
	}

	public static String getPathSeparator() {
		String value = System.getProperty("path.separator");
		return value;
	}

	public static Node fileReadXML(File infile) {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			dbFactory.setNamespaceAware(true);
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(infile);

			// optional, but recommended
			// read this -
			// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static JSONObject fileReadJSON(String data) {
		JSONParser parser = new JSONParser();
		JSONObject json;
		try {
			if (data.startsWith("{")) {
				json = (JSONObject) parser.parse(data);
			} else {
				String s = fileRead(data);
				json = (JSONObject) parser.parse(s);
			}
		} catch (ParseException e) {
			throw new CoreException(e);
		}
		return json;
	}

	public static JSONObject fileReadJSON(File infile) {
		JSONParser parser = new JSONParser();
		JSONObject json;
		try {
			FileInputStream fis = new FileInputStream(infile);
			String s = fileRead(fis);
			json = (JSONObject) parser.parse(s);
		} catch (ParseException e) {
			throw new CoreException(e);
		} catch (FileNotFoundException e) {
			throw new CoreException(e);
		}
		return json;
	}

	public static JSONObject fileReadJSON(InputStream is) {
		JSONParser parser = new JSONParser();
		JSONObject json;
		try {
			String s = fileRead(is);
			json = (JSONObject) parser.parse(s);
		} catch (ParseException e) {
			throw new CoreException(e);
		}
		return json;
	}

	public static Document toDocument(Object json) {
		try {
			DocumentBuilderFactory bf = DocumentBuilderFactory.newInstance();
			bf.setNamespaceAware(true);
			DocumentBuilder builder = bf.newDocumentBuilder();
			Document document = builder.newDocument();
			Element documentElement = document.createElement("root");
			document.appendChild(documentElement);
			fromJson(document, document.getDocumentElement(), json);
			return document;
		} catch (Exception e) {
			throw new CoreException(e);
		}
	}

	private static Element fromJson(Document document, Element element, Object object) {
		if (object instanceof JSONArray) {
			return fromJson(document, element, (JSONArray) object);
		} else if (object instanceof JSONObject) {
			return fromJson(document, element, (JSONObject) object);
		} else if (object instanceof String) {
			element.setTextContent((String) object);
		}
		return element;
	}

	private static Element fromJson(Document document, Element element, String name, JSONArray jsonArray) {
		for (Object object : jsonArray.toArray()) {
			Element newElement = document.createElement(name);
			fromJson(document, newElement, object);
			element.appendChild(newElement);
		}
		return element;
	}

	private static Element fromJson(Document document, Element element, JSONObject jsonObject) {
		Set<Entry<String, Object>> entrySet = jsonObject.entrySet();
		for (Entry<String, Object> entry : entrySet) {
			String name = entry.getKey();
			Object obj = entry.getValue();
			if (obj instanceof String) {
				Element newElement = document.createElement(name);
				newElement.setTextContent((String) obj);
				element.appendChild(newElement);
			} else if (obj instanceof JSONArray) {
				fromJson(document, element, name, (JSONArray) obj);
			} else {
				Element newElement = document.createElement(name);
				fromJson(document, newElement, obj);
				element.appendChild(newElement);
			}
		}
		return element;
	}
}