package task1b;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class PrepareData {
	
	public static void CountFre(Statistics sta, String[] content) {
		sta.g1 = content[0].charAt(0);
		for(int i = 0; i < content.length; ++i) {
			if(content[i].charAt(0) != sta.g1) {
				sta.g2 = content[i].charAt(0); 
			}
			if(content[i].charAt(1) != sta.g1) {
				sta.g2 = content[i].charAt(1); 
			}
		}
		if(sta.g1 > sta.g2) {
			char a = sta.g1;
			sta.g1 = sta.g2;
			sta.g2 = a;
		}

		String g1g2 = sta.g1 +""+sta.g2;
		String g1g1 = sta.g1 +""+sta.g1;
		String g2g2 = sta.g2 +""+sta.g2;
		

		for(int i = 0; i< content.length; ++i){
			if(g1g2.equals(content[i]))
				sta.numOfG1G2++;
			else if(g1g1.equals(content[i]))
				sta.numOfG1G1++;
			else if(g2g2.equalsIgnoreCase(content[i]))
				sta.numOfG2G2++;
			else
				System.out.println("How Come!");
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
//				System.out.println(res[i].genotype + " "+ res[i].g1+res[i].g2+" "+res[i].numOfG1G1+" "+res[i].numOfG1G2+" "+res[i].numOfG2G2);
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
