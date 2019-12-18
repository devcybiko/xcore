package us.thinkable.xcore.app;

import us.thinkable.xcore.FileUtil;
import us.thinkable.xcore.StringUtil;

public class SmartQuotesToHtmlEncoding {
	public static void main(String[] args) {
		String str = FileUtil.fileRead(System.in);
		String converted = StringUtil.smartQuotesToHtmlEncoding(str);
		System.out.println(converted);
	}
}
