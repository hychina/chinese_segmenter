package training;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import util.IOUtil;
import util.UnicodeUtil;

public class Counting {
	private static Map<String, Double> unigramFreqs = new HashMap<String, Double>(); // 单个字符
	private static Map<String, Double> bigramFreqs = new HashMap<String, Double>(); // 二元组
	private static int letterCount = 0; // 除标点符号、空白符之外的字符
	private static int letterBigramCount = 0;
	
	
	public static Map<String, Double> getUnigramFreqs() {
		return unigramFreqs;
	}

	public static Map<String, Double> getBigramFreqs() {
		return bigramFreqs;
	}

	public static int getLetterCount() {
		return letterCount;
	}

	public static int getLetterBigramCount() {
		return letterBigramCount;
	}

	public static void calcFreqs(String filename) throws IOException {
		
		String trainingText = IOUtil.readFile(filename);
		
		if (trainingText == null || trainingText.equals(""))
			return;
		
		String first = UnicodeUtil.unicodeAt(trainingText, 0);
		boolean isFirstLetter = !UnicodeUtil.isPunctuation(first) && !Character.isWhitespace(first.codePointAt(0));

		int index = UnicodeUtil.nextIndex(trainingText, 0);
		int maxIndex = trainingText.length();
		
		while (index < maxIndex) {
			String second = UnicodeUtil.unicodeAt(trainingText, index);
			boolean isSecondLetter = !UnicodeUtil.isPunctuation(second) && !Character.isWhitespace(second.codePointAt(0));
			
			if (isFirstLetter) {
				letterCount++;
				
				if (isSecondLetter) {
					letterBigramCount++;
				}
			}
			
			if (isFirstLetter || isSecondLetter) {
				// 第一个字符放进表中
				Double unigramFreq = unigramFreqs.get(first);
				unigramFreq = unigramFreq == null ? 1 : unigramFreq + 1;
				unigramFreqs.put(first, unigramFreq);
				
				// 二元组放进表中
				Double bigramFreq = bigramFreqs.get(first + second);
				bigramFreq = bigramFreq == null ? 1 : bigramFreq + 1;
				bigramFreqs.put(first + second, bigramFreq);
			}
			
			first = second;
			isFirstLetter = isSecondLetter;
			
			index = UnicodeUtil.nextIndex(trainingText, index);
		}
	}
}