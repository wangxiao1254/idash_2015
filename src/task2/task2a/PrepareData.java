package task2.task2a;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class PrepareData {

	static public HashMap<String, Integer> table = new HashMap<String, Integer>();
	static{
		for(int i = 1; i < 23; ++i)
			table.put(String.valueOf(i), i);
		table.put("X", 23);
		table.put("Y", 24);
		table.put("M", 25);
	}

	public static int opToInt(String a) {
		if(a.equals("SUB"))return 0;
		else if(a.equals("SNP")) return 1;
		else if(a.equals("DEL")) return 2;
		else if(a.equals("INS")) return 3;
		else{
			try {
				throw new Exception("unsupported op type!");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
			return -1;
		}
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
		else{
			try {
				throw new Exception("unsupported format!");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
			return -1;
		}

	}


	public static HashSet<SNPEntry> readFile(String filename) {
		File file = new File(filename);
		HashSet<SNPEntry> map = new HashSet<SNPEntry>();
		Scanner scanner;
		try {
			scanner = new Scanner(file);
			while(scanner.hasNextLine() && scanner.nextLine().startsWith("#")) {
			}
			while(scanner.hasNextLine()) {
				String tmp = scanner.nextLine();
				String[] s = tmp.split("\\s+");
				int a  = table.get(s[0]);
				int op = opToInt(s[s.length-1].split(";")[0].split("=")[1]);
				if(op == 3 || op == 2) {//insert
				}
				else {
						long index = new Long(s[1]);
						SNPEntry entry = new SNPEntry();
						entry.location = index*26+a;
						entry.value = s[4];
//						System.out.println(entry);
						map.add(entry);
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
		HashSet<SNPEntry> a = readFile("data/hu661AD0.snp");
		HashSet<SNPEntry> b = readFile("data/hu604D39.snp");
		HashSet<SNPEntry> aub = new HashSet<SNPEntry>();
		aub.addAll(b);
		aub.addAll(a);
		
		HashSet<String> aa = new HashSet<String>();
		for(SNPEntry o : a) {
			aa.add(o.toString());
		}
		for(SNPEntry o : b) {
			aa.add(o.toString());
		}
		System.out.println(a.size()+" "+b.size()+" "+aa.size());
	}
}
