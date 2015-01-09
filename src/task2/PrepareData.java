package task2;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

public class PrepareData {

	static public HashMap<String, Integer> table = new HashMap<String, Integer>();
	static{
		for(int i = 1; i < 23; ++i)
			table.put(String.valueOf(i), i);
		table.put("X", 23);
		table.put("Y", 24);
	}

	public static int toInt(char a) {
		if(a == 'A')
			return 0;
		if(a == 'T')
			return 1;
		if(a == 'C')
			return 2;
		if(a == 'G')
			return 3;
		return -1;
	}

	public static HashMap<Integer, Integer> readFile(String filename) {
		File file = new File(filename);
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		Scanner scanner;
		try {
			scanner = new Scanner(file);
			while(scanner.hasNextLine() && scanner.nextLine().startsWith("#")) {
			}
			while(scanner.hasNextLine()) {
				String tmp = scanner.nextLine();
				String[] s = tmp.split("\\s+");
				//				System.out.println(Arrays.toString(s));
				int a  = table.get(s[0]);
				if(!s[s.length-1].matches(".*DEL.*") && !s[s.length-1].matches(".*INS.*") 
						&& s[3].length() == s[4].length()) {
					for(int j = 0; j < s[4].length(); ++j) {
						int index = (new Integer(s[1])+j);
//						System.out.println(index +" "+s[3].charAt(j)+" "+s[4].charAt(j));
						map.put((index<<5)+a, toInt(s[4].charAt(j)));
					}
				}
			}

			scanner.close();			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}

	public static void main(String[] args) {
		HashMap<Integer, Integer> a = readFile("data/hu661AD0.snp");
		HashMap<Integer, Integer> b = readFile("data/hu604D39.snp");
		
		
		Iterator<Entry<Integer, Integer>> it = a.entrySet().iterator();
		int cnt = 0;
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        if(b.containsKey(pairs.getKey()))
	        		cnt++;
	    }
	    System.out.println(a.size()+" "+b.size()+" "+cnt);
	}
}
