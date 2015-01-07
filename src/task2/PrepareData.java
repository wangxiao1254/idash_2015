package task2;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

public class PrepareData {
	
	static public HashMap<String, Integer> table = new HashMap<String, Integer>();
	static{
		for(int i = 1; i < 23; ++i)
			table.put(String.valueOf(i), i);
		table.put("X", 23);
		table.put("Y", 24);
	}
	public static HashMap<Integer, String>[] readFile(String filename) {
		File file = new File(filename);
		HashMap<Integer, String> map[] = new HashMap[25];
		for(int i = 0; i < map.length; ++i)
			map[i] = new HashMap<Integer, String>();
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
				if(a == 1) {
					if(!s[s.length-1].matches(".*DEL.*") && !s[s.length-1].matches(".*INS.*") )
						System.out.println(s[1] +" "+s[3]+" "+s[4]);
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
		readFile("data/hu661AD0.snp");
	}
}
