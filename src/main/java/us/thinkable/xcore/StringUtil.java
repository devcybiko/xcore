package us.thinkable.xcore;

/**
 * A set of methods to test strings/chars for inclusion in certain sets Some of
 * these methods are overcome by Characters (1.5) methods
 * 
 * @author Gregory Smith
 *
 */
public class StringUtil {

	/**
	 * returns true if 'c' is a digit (character 0-9)
	 * 
	 * @param c
	 *            the character to test
	 * @return true if 'c' is a digit
	 */
	public static boolean isDigit(char c) {
		return ('0' <= c && c <= '9');
	}

	/**
	 * returns true if 'c' is a lower case letter (character a-z)
	 * 
	 * @param c
	 *            the character to test
	 * @return true if 'c' is a lower case letter
	 */
	public static boolean isLower(char c) {
		return 'a' <= c && c <= 'z';
	}

	/**
	 * returns true if 'c' is an upper case letter (character A-Z)
	 * 
	 * @param c
	 *            the character to test
	 * @return true if 'c' is an upper case letter
	 */
	public static boolean isUpper(char c) {
		return 'A' <= c && c <= 'Z';
	}

	/**
	 * returns true if 'c' is in the string 's' Useful for arbitrary character
	 * type checking
	 * 
	 * @param c
	 *            the character to test
	 * @param s
	 *            the string to examine
	 * @return true if 'c' is in the string 's'
	 */
	public static boolean inString(String s, char c) {
		return s.indexOf(c) != -1;
	}

	/**
	 * returns true if 'c' is a whitespace character (space, new line, carriage
	 * return, tab)
	 * 
	 * @param c
	 *            the character to test
	 * @return true if 'c' is a whitespace character
	 */
	public static boolean isWhitespace(char c) {
		return inString(" \t\n\r", c);
	}

	/**
	 * returns true if 's' contains only upper case letters
	 * 
	 * @param s
	 *            the string to test
	 * @return true if 's' has only upper case letters
	 */
	public static boolean isAllUpper(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (!isUpper(s.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * returns true if 's' starts with an upper case letter
	 */
	public static boolean isCapitalized(String s) {
		if (s.equals("")) {
			return false;
		}
		return isUpper(s.charAt(0));
	}

	/**
	 * converts word to capitalized (first letter in caps, others unmodified)
	 */
	public static String capitalize(String s) {
		if (s.length() == 0)
			return s;
		if (s.length() == 1)
			return s.toUpperCase();
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}

	/**
	 * returns true if 's' contains only lower case letters
	 * 
	 * @param s
	 *            the string to test
	 * @return true if 's' has only lower case letters
	 */
	public static boolean isAllLower(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (!isLower(s.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * returns true if 's' contains only digits
	 * 
	 * @param s
	 *            the string to test
	 * @return true if 's' has only digits
	 */
	public static boolean isAllDigits(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (!isDigit(s.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * returns true if 's' contains only whitespace letters
	 * 
	 * @param s
	 *            the string to test
	 * @return true if 's' has only whitespace letters
	 */
	public static boolean isAllWhitespace(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (!isWhitespace(s.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static boolean isEmpty(String s) {
		return (s == null || s.isEmpty());
	}

	public static boolean isGUID(String s) {
		return s.length() == 32 && s.matches("\\p{XDigit}+");
	}

	public static String smartQuotesToHtmlEncoding(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c >= 8192) {
				sb.append("&#");
				sb.append(Integer.toString(c));
				sb.append(";");
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

}
