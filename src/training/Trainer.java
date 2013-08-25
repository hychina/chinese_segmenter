package training;

import java.io.IOException;

import util.IOUtil;

public class Trainer {

	public static void main(String[] args) {
		 
		try {
			
			double startTime = System.currentTimeMillis();
			
			Counting.calcFreqs("resource/我的语料.txt"); // 计数
			
			double endTime = System.currentTimeMillis();
			System.out.println("计数用时: " + (endTime - startTime) / 1000d + " 秒");
			
			MI.calcMI(); // 计算互信息
			
			startTime = endTime;
			endTime = System.currentTimeMillis();
			System.out.println("计算互信息用时: " + (endTime - startTime) / 1000d + " 秒");
			
			// 输出文件
			IOUtil.outputList("result/互信息", MI.getMIList());
			IOUtil.outputMap("result/二元组", Counting.getBigramFreqs());
			IOUtil.outputMap("result/一元组", Counting.getUnigramFreqs());
			
			startTime = endTime;
			endTime = System.currentTimeMillis();
			System.out.println("输出文件用时: " + (endTime - startTime) / 1000d + " 秒");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
