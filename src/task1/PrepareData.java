package task1;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class PrepareData {
	
	public static void CountFre(Statistics sta, String[] content) {
		char[] tmp = new char[content.length*2];
		int cnt = 0;
		for(int i = 0; i< content.length; ++i){
			tmp[cnt++] = content[i].charAt(0);
			tmp[cnt++] = content[i].charAt(1);
		}
		sta.g1 = tmp[0];
		sta.numOfG1 = 1;
		sta.g2 = 'Z';
		for(int i = 1; i < tmp.length; ++i) {
			if(sta.g1 == tmp[i])
				++sta.numOfG1;
			else if(sta.g2 == 'Z') {
				sta.numOfG2 = 1;
				sta.g2 = tmp[i];
			}
			else if(sta.g2 == tmp[i])
				++sta.numOfG2;
			else {
				System.out.println("Warning: there are 3 types!");
			}
		}
		if(sta.g1 > sta.g2) {
			char a = sta.g1;
			sta.g1 = sta.g2;
			sta.g2 = a;
			int b = sta.numOfG1;
			sta.numOfG1 = sta.numOfG2;
			sta.numOfG2 = b;
		}
	}
	
	public static class StatisticsData {
		public Statistics[] data;
		public int numberOftuples;
	}
	
	public static StatisticsData readFile(String filename) {
		File file = new File(filename);
		Scanner scanner; 
		StatisticsData d = new StatisticsData(); 
		LinkedList<Statistics> lsta = new LinkedList<Statistics>();
		try {
			scanner = new Scanner(file);
			d.numberOftuples = scanner.nextLine().split(" ").length;
			while(scanner.hasNextLine()) {
				scanner.nextLine();
				Statistics sta = new Statistics();
				String[] snp = scanner.nextLine().split(" ");
				CountFre(sta, snp);
				lsta.add(sta);
			}
			scanner.close();			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		d.data = lsta.toArray(new Statistics[0]); 
		return d;
	}
	
	public static void main(String[] args) {
		Statistics[] a = readFile("data/case_chr2_29504091_30044866_part1.txt").data;
		Statistics[] b = readFile("data/case_chr2_29504091_30044866_part2.txt").data;
		Statistics[] c = readFile("data/control_chr2_29504091_30044866_part1.txt").data;
		Statistics[] d = readFile("data/control_chr2_29504091_30044866_part2.txt").data;
		for(int i = 0; i < a.length; ++i) {
			int t1 = a[i].numOfG1 + b[i].numOfG1 + c[i].numOfG1 + d[i].numOfG1;
			int t2 = a[i].numOfG2 + b[i].numOfG2 + c[i].numOfG2 + d[i].numOfG2;
			System.out.println((double)(t1 < t2 ? t1: t2) / (t1+t2));
		}
	}
}
