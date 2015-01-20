package task2;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

public class PrepareData {

	public static final boolean globalPosition = false;
	static public HashMap<String, Integer> table = new HashMap<String, Integer>();
	static{
		for(int i = 1; i < 23; ++i)
			table.put(String.valueOf(i), i);
		table.put("X", 23);
		table.put("Y", 24);
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

	static HashMap<String, Integer> tmap = new HashMap<String, Integer>(); 

	public static void println(String a) {
		if(tmap.containsKey(a))
		{
			int t = tmap.get(a);
			tmap.put(a, t+1);
		}
		else tmap.put(a, 1);
	}

	public static  void println(){
		Iterator<Entry<String, Integer>> it = tmap.entrySet().iterator();
		//		System.out.println(tmap.size());
		while (it.hasNext()) {
			Map.Entry<String, Integer> pairs = (Entry<String, Integer>)it.next();
			if(pairs.getValue() > 1)
				System.out.println(pairs.getKey()+" "+pairs.getValue());
		}
	}
	public static HashMap<Long, SNPEntry> readFile(String filename) {
		File file = new File(filename);
		HashMap<Long, SNPEntry> map = new HashMap<Long, SNPEntry>();
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

				int op = opToInt(s[s.length-1].split(";")[0].split("=")[1]);
				if(op == 3) {//insert
					for(int j = 0; j < s[3].length(); ++j) {
						int index = (new Integer(s[1])+j);
						SNPEntry entry = new SNPEntry();
						entry.op = op;
						entry.location = index;
						if(! globalPosition ) entry.location = index*25+a;

						entry.value = toInt(s[3].charAt(j));
						map.put(entry.location, entry);
						println(""+entry.location);
					}
				}
				else if(op == 2) {//delete
					for(int j = 0; j < s[3].length(); ++j) {
						int index = (new Integer(s[1])+j);
						SNPEntry entry = new SNPEntry();
						entry.op = op;
						entry.location = index;//(index*25)+a;
						if(! globalPosition ) entry.location = index*25+a;

						entry.value = 0;//does not matter what value it is
						map.put(entry.location, entry);
						println(""+entry.location);
					}
				}
				else {
					for(int j = 0; j < s[4].length(); ++j) {
						int index = (new Integer(s[1])+j);
						SNPEntry entry = new SNPEntry();
						entry.op = op;
						entry.location = index;//(index*25)+a;
						if(! globalPosition ) entry.location = index*25+a;

						entry.value = toInt(s[4].charAt(j));
						map.put(entry.location, entry);
						println(""+entry.location);
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
		HashMap<Long, SNPEntry> a = readFile("data/hu661AD0.snp");
		println();
		tmap.clear();
		HashMap<Long, SNPEntry> b = readFile("data/hu604D39.snp");
		println();

		Iterator<Entry<Long, SNPEntry>> it = a.entrySet().iterator();
		int cnt = 0;
		while (it.hasNext()) {
			Map.Entry<Long, SNPEntry> pairs = (Entry<Long, SNPEntry>)it.next();
			if(b.containsKey(pairs.getKey())){
				//System.out.println(pairs.getKey());
				cnt++;
			}
		}
		System.out.println(a.size()+" "+b.size()+" "+cnt);
	}
}
