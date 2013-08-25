package util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import training.Entry;

public class IOUtil {
	
	public static void outputMap(String filename, Map<String, Double> map) throws IOException {
		ArrayList<Map.Entry<String, Double>> list = new ArrayList<Map.Entry<String, Double>>(map.entrySet());
		
		// 排序
		Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
			public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
				Double v1 = o1.getValue();
				Double v2 = o2.getValue();
				return -v1.compareTo(v2);
			}
		});
		
		// 输出为可读文件
		PrintWriter fos = new PrintWriter( new BufferedWriter( new OutputStreamWriter( new FileOutputStream(filename + ".txt"), "utf-8")));
		
		// 输出为二进制文件
		DataOutputStream dos = new DataOutputStream( new BufferedOutputStream( new FileOutputStream(filename)));
		
		int lineNo = 1;
		for (Map.Entry<String, Double> e : list) {
			fos.println(lineNo++ + " " + e.getKey() + " " + e.getValue());
			
			dos.writeUTF(e.getKey());
			dos.writeDouble(e.getValue());
		}
		
		fos.close();
		dos.close();
	}
	
	public static void outputList(String filename, List<Entry> list) throws IOException {
		
		// 排序
		Collections.sort(list, new Comparator<Entry>() {
			public int compare(Entry o1, Entry o2) {
				Double v1 = o1.getValue();
				Double v2 = o2.getValue();
				return -v1.compareTo(v2);
			}
		});
		
		// 输出为可读文件
		PrintWriter fos = new PrintWriter( new BufferedWriter( new OutputStreamWriter( 
				new FileOutputStream(filename + ".txt"), "utf-8")));
		
		// 输出为二进制文件
		DataOutputStream dos = new DataOutputStream( new BufferedOutputStream( 
				new FileOutputStream(filename)));
		
		int lineNo = 1;
		for (Entry e : list) {
			fos.println(lineNo++ + " " + e.getKey() + " " + e.getValue());
			
			dos.writeUTF(e.getKey());
			dos.writeDouble(e.getValue());
		}
		
		fos.close();
		dos.close();
	}
	
	public static Map<String, Double> loadMap(String filename) throws IOException{
		DataInputStream dosMI = new DataInputStream(new BufferedInputStream(
				new FileInputStream(filename)));
		
		HashMap<String,Double> map = new HashMap<String, Double>();
		
		while (dosMI.available() > 0) {
			String key = dosMI.readUTF();
			double value = dosMI.readDouble();
			map.put(key, value);
		}
		
		dosMI.close();
		return map;
	}
	
	/**
	 * 按行读取文
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String readFile(String file) throws IOException {
		// 用 utf-8 进行解码
		BufferedReader in = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), Charset.forName("utf-8")));
		StringBuilder sb = new StringBuilder();
		String s;
		while ((s = in.readLine()) != null) {
			sb.append(s);
			sb.append("\n");
		}
		in.close();
		return sb.toString();
	}
}
