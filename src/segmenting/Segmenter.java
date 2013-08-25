package segmenting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import util.AssessUtil;
import util.IOUtil;

public class Segmenter {

	public static void main(String[] args) throws IOException {
		System.out.println("正在加载数据...");
		double startTime = System.currentTimeMillis();
		
		try {
			FileSegmenter.setMIMap(IOUtil.loadMap("result/互信息"));
			FileSegmenter.setBigramFreqs(IOUtil.loadMap("result/二元组"));
			FileSegmenter.setUnigramFreqs(IOUtil.loadMap("result/一元组"));
		} catch (IOException e) {
			System.out.println("加载数据出错...");
			return;
		}
		
		double endTime = System.currentTimeMillis();
		System.out.println("加载成功，用时 " + (endTime - startTime) / 1000d + " 秒");
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in, "GBK"));
		boolean isValid = true;
		
		FileSegmenter.setThresholdMI(2.053d);
		FileSegmenter.setThresholdDTS(-16d);
		
		while (true) {
			if (isValid) {
				System.out.print("请输入待分词文件的路径(如: resource/pku_test.txt)，直接回车退出:");
			}
			
			String targetFile = in.readLine();
			
			if (targetFile.isEmpty()) {
				break;
			}
			
			try {
				startTime = System.currentTimeMillis();
				
				FileSegmenter.segment(targetFile);
				
				endTime = System.currentTimeMillis();
				System.out.println("分词用时 " + (endTime - startTime) / 1000d + " 秒");
				
				isValid = true;
				
				try {
					AssessUtil.showResult("resource/pku_test_gold.txt", "result/分词结果.txt");
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				System.out.print("输入有误，请重新输入:");
				isValid = false;
			}
		}
		
		in.close();
	}
}
