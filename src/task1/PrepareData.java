package task1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
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
	
	public static Statistics[] readFile(String filename, int party) {
		File file = new File(filename);
		Statistics[] res = null;
		Scanner scanner;
		try {
			scanner = new Scanner(file);
			String a = scanner.nextLine();
			int testCases = a.split(" ").length;
			res = new Statistics[testCases];
			for(int i = 0; i < testCases; ++i) {
				scanner.nextLine();
				res[i] = new Statistics();
				res[i].genotype = scanner.nextLine().replace(" ", "");
				String[] snp = Arrays.copyOfRange(scanner.nextLine().split(" "), party*100, 100*(party+1));
				CountFre(res[i], snp);
//				System.out.println(res[i].genotype + " "+ res[i].g1+" "+res[i].numOfG1+" "+res[i].g2+" "+res[i].numOfG2);
			}
			scanner.close();			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	public static void main(String[] args) {
		readFile("data/case_chr2_29504091_30044866.txt", 0);
	}
}
