package segmenting;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Map;

import util.IOUtil;
import util.UnicodeUtil;

public class FileSegmenter {
	
	private static Map<String, Double> MIMap;
	private static Map<String, Double> bigramFreqs;
	private static Map<String, Double> unigramFreqs;
	private static double thresholdMI;
	private static double thresholdDTS;

	public static void setThresholdMI(double thresholdMI) {
		FileSegmenter.thresholdMI = thresholdMI;
	}

	public static void setThresholdDTS(double thresholdDTS) {
		FileSegmenter.thresholdDTS = thresholdDTS;
	}

	public static void setMIMap(Map<String, Double> mIMap) {
		MIMap = mIMap;
	}

	public static void setBigramFreqs(Map<String, Double> bigramFreqs) {
		FileSegmenter.bigramFreqs = bigramFreqs;
	}
	
	public static void setUnigramFreqs(Map<String, Double> unigramFreqs) {
		FileSegmenter.unigramFreqs = unigramFreqs;
	}

	public static void segment(String targetFile) throws IOException {
		
		String text = IOUtil.readFile(targetFile);
		
		// 至少要有四个字符
		if (text.codePointCount(0, text.length()) < 4) {
			return;
		}
		
		PrintWriter out = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream("result/分词结果.txt"), "utf-8")));
		
		String first = UnicodeUtil.unicodeAt(text, 0);
		String second = UnicodeUtil.unicodeAt(text, text.offsetByCodePoints(0, 1));
		String third = UnicodeUtil.unicodeAt(text, text.offsetByCodePoints(0, 2));
		
		StringBuilder sb = new StringBuilder();
		sb.append(first);
		
		int index = 0;
		int maxIndex = text.offsetByCodePoints(text.length(), -4); // 到倒数第四个字符为止
		
		// 遍历每一个字符
		while (index <= maxIndex) {
			String fourth = UnicodeUtil.unicodeAt(text, text.offsetByCodePoints(index, 3));
			
			sb.append(second);
			
			if (shouldSeparate(first, second, third, fourth)) {
				sb.append("  ");
			}
			
			first = second;
			second = third;
			third = fourth;
			
			index = UnicodeUtil.nextIndex(text, index);
		}
		
		sb.append(second);
		sb.append(third);
		
		text = null;
		
		out.print(sb.toString());
		out.close();
	}
	
	/**
	 * 判断第二个字和第三个字之间该不该断
	 */
	private static boolean shouldSeparate(String first, String second, String third, String fourth) {
		
		int firstCode = first.codePointAt(0);
		int secondCode = second.codePointAt(0);
		int thirdCode = third.codePointAt(0);
		int fourthCode = fourth.codePointAt(0);
		
		if (Character.isWhitespace(secondCode) || Character.isWhitespace(thirdCode)) {
			return false;
		} else if (Character.isDigit(secondCode) && Character.isDigit(thirdCode)) {
			return false;
		} else if (Character.isDigit(secondCode) && third.equals(".") && Character.isDigit(fourthCode) ||
				Character.isDigit(firstCode) && second.equals(".") && Character.isDigit(thirdCode)) {
			return false; 
		} else if (UnicodeUtil.isPunctuation(second) || UnicodeUtil.isPunctuation(third)) {
			if (second.equals("—") && third.equals("—") || second.equals("…") && third.equals("…"))
				return false;
			else return true;
		} else {			
			// 互信息
			Double mi = MIMap.get(second + third);
			
			if (mi == null || mi < thresholdMI) {
				return true;
			} else {
			
				// t-测试差
				double dts = calcDTS(first, second, third, fourth);
				
				if (dts > thresholdDTS) {
					return false;
				} else {
					return true;
				}
			}
		}
	}
	
	/**
	 * 计算t-测试差
	 */
	private static double calcDTS(String first, String second, String third, String fourth) {
		double firstTTestValue = calcTTestValue(first, second, third);
		double secondTTestValue = calcTTestValue(second, third, fourth);
		
		return firstTTestValue - secondTTestValue;
	}
	
	/**
	 * 计算t-测试值
	 */
	private static double calcTTestValue(String first, String second, String third) {
	
		Double r_xy = bigramFreqs.get(first + second);
		if (r_xy == null) {
			r_xy = 0d;
		}
		
		Double r_yz = bigramFreqs.get(second + third);
		if (r_yz == null) {
			r_yz = 0d;
		}
		
		Double r_x = unigramFreqs.get(first);
		if (r_x == null) {
			r_x = 1d;
		}
		
		Double r_y = unigramFreqs.get(second);
		if (r_y == null) {
			r_y = 1d;
		}
		
		double numerator = (r_yz / r_y) - (r_xy / r_x);
		double t = 0d;
		
		if (numerator != 0d) {
			double denominator = Math.sqrt(r_xy / Math.pow(r_x, 2.0d) + r_yz / Math.pow(r_y, 2.0d));
			t = numerator / denominator;
		}

		return t;
	}

}
