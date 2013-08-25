package training;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import util.UnicodeUtil;

/**
 * 计算互信息
 */
public class MI {
	
	private static List<Entry> MIList; // 互信息

	public static List<Entry> getMIList() {
		return MIList;
	}

	public static void calcMI() throws IOException {
		// 得到统计信息
		int letterBigramCount = Counting.getLetterBigramCount();
		int letterCount = Counting.getLetterCount();
		Map<String, Double> bigramFreqs = Counting.getBigramFreqs();
		Map<String, Double> unigramFreqs = Counting.getUnigramFreqs();
		
		MIList = new ArrayList<Entry>(bigramFreqs.size());
		
		final double para = 2 * Math.log(letterCount) - Math.log(letterBigramCount);
		
		Set<String> bigrams = bigramFreqs.keySet();
		
		for (String bigram : bigrams) {

			double bigram﻿Freq = bigramFreqs.get(bigram);
			String first = UnicodeUtil.unicodeAt(bigram, 0);
			String second = UnicodeUtil.unicodeAt(bigram, UnicodeUtil.nextIndex(bigram, 0));
			
			// 有一个是标点符号，不计算互信息
			if (UnicodeUtil.isPunctuation(first) || UnicodeUtil.isPunctuation(second)) { 
				continue;
			}
			
			double firstFreq = unigramFreqs.get(first);
			double secondFreq = unigramFreqs.get(second);
			double mi = Math.log(bigram﻿Freq) - Math.log(firstFreq) - Math.log(secondFreq) + para;
			
			MIList.add(new Entry(bigram, mi));
		}
	}
}
