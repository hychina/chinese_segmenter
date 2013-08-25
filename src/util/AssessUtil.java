package util;

import java.io.IOException;
import java.text.DecimalFormat;


public class AssessUtil {
	
	private static int myWords;
	private static int stdWords;
	private static int correctWords;
	
	private static final int newLine = "\n".codePointAt(0);
	private static final int space = " ".codePointAt(0);
	
	public static double showResult(String stdFile, String myFile) throws IOException { 
		countWords(stdFile, myFile);
		
		double precision = (double)correctWords / (double)myWords * 100d;
		double recall = (double)correctWords / (double)stdWords * 100d;
		double f = 2 * precision * recall / (precision + recall);
		
		System.out.println("标准答案总词数" + stdWords);
		System.out.println("分词结果总词数" + myWords);
		System.out.println("切分正确的词数" + correctWords);
		
		DecimalFormat df = new DecimalFormat("###.00"); 
		System.out.println("准确率：" + df.format(precision));
		System.out.println("召回率：" + df.format(recall));
		System.out.println("F-指标：" + df.format(f));
		
		return f;
	}
	
	/**
	 * 计算标准答案和分词结果中的总词数，以及分词结果中切分正确的词数
	 * @param stdFile
	 * @param myFile
	 * @throws IOException
	 */
	private static void countWords(String stdFile, String myFile) throws IOException {
		
		myWords = 0;
		stdWords = 0;
		correctWords = 0;
		
		String standard = IOUtil.readFile(stdFile);
		String my = IOUtil.readFile(myFile);
		
		// 去掉开头和结尾处的空格
		standard = standard.trim();
		my = my.trim(); 
		
		// 当前切分位置
		int cutPositionStd = 0;
		int cutPositionMy = 0; 
		
		// 当前切分位置的后一个词的开始位置索引
		int indexStd = 0; 
		int indexMy = 0; 
		
		int[] stdWordLengthAndNextIndex;
		int[] myWordLengthAndNextIndex;
		
		while (indexStd < standard.length() && indexMy < my.length()) {
			
			stdWordLengthAndNextIndex = getWordLengthAndNextIndex(standard, indexStd);
			myWordLengthAndNextIndex = getWordLengthAndNextIndex(my, indexMy);
			
			if (cutPositionStd == cutPositionMy) {
				// 切分位置都后移
				cutPositionStd += stdWordLengthAndNextIndex[0];
				cutPositionMy += myWordLengthAndNextIndex[0];
				indexStd = stdWordLengthAndNextIndex[1];
				indexMy = myWordLengthAndNextIndex[1];
				
				if (cutPositionStd == cutPositionMy) {
					correctWords++;
				}
				
				stdWords++;
				myWords++;
			} else if (cutPositionStd < cutPositionMy) {
				cutPositionStd += stdWordLengthAndNextIndex[0];
				indexStd = stdWordLengthAndNextIndex[1];
				
				stdWords++;
			} else {
				cutPositionMy += myWordLengthAndNextIndex[0];
				indexMy = myWordLengthAndNextIndex[1];
				
				myWords++;
			}
		}
		
		while (indexStd < standard.length()) {
			 indexStd = getWordLengthAndNextIndex(standard, indexStd)[1];
			 stdWords++;
		} 
		
		while (indexMy < my.length()) {
			indexMy = getWordLengthAndNextIndex(my, indexMy)[1];
			myWords++;
		}
	}
	
	/**
	 * 返回整型数组，第一个值是当前词长度，第二个值是下一个词开始位置索引
	 * @param text
	 * @param index
	 * @return
	 */
	private static int[] getWordLengthAndNextIndex(String text, int index) {
		int oldIndex = index;
		int current;
		
		while (index < text.length()) {
			
			current = text.codePointAt(index);
			
			if (current == newLine || current == space) {
				break;
			}
			
			index = UnicodeUtil.nextIndex(text, index);
		}
		
		int[] returnValue = new int[2];
		returnValue[0] = text.codePointCount(oldIndex, index);
		
		while (index < text.length()) {
			current = text.codePointAt(index);
			
			if (current != newLine && current != space) {
				break;
			}
			
			index = UnicodeUtil.nextIndex(text, index);
		}
		
		returnValue[1] = index;
		return returnValue;
	}
}
