package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UnicodeUtil {

	private static Pattern pattern = Pattern.compile("[\\[\\]`~(){}'\":;,.<>?!……（）——｛｝【】《》‘’；：“”，。、？！]");
	
	/**
	 * 判断是否为标点符号
	 */
	public static boolean isPunctuation(String character) {
		
		Matcher matcher = pattern.matcher(character);
		
		if (matcher.matches()) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 返回指定位置的字符
	 * @param text
	 * @param index
	 * @return
	 */
	public static String unicodeAt(String text, int index) {
		if (Character.isLowSurrogate(text.charAt(index))) {
			index--;
		}
		return String.valueOf(Character.toChars(text.codePointAt(index)));
	}
	
	/**
	 * 给出字符串中下一个字符的索引位置
	 * @param text
	 * @param index
	 * @return
	 */
	public static int nextIndex(String text, int index) {
		return index + Character.charCount(text.codePointAt(index));
	}
}
