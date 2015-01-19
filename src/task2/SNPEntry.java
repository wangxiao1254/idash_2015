package task2;

import java.util.Comparator;

public class SNPEntry implements Comparable<SNPEntry>{
	int op;//0: SUB 1: SNP 2: DEL 3: INS
	int location;
	int value;// ATCG: 0123

	public SNPEntry(int loc, int val, int o) {
		op = o;
		location = loc;
		value = val;
	}

	public SNPEntry() {
	}

	
	public String ToString() {
		return ""+op+" "+location+" "+value;
	}

	@Override
	public int compareTo(SNPEntry o) {
		if(this == o){
			return 0;            
		}
		else if (o!=null ) {    
			if(location <= o.location){
				return -1;
			} else {
				return 1;
			}
		}else{
			return -1;
		}
	}
	
	public static class AscComparator implements Comparator<SNPEntry> {
		@Override
		public int compare(SNPEntry o1, SNPEntry o2) {
			return o1.compareTo(o2);
		}
	}
	
	public static class DscComparator implements Comparator<SNPEntry> {
		@Override
		public int compare(SNPEntry o1, SNPEntry o2) {
			return o2.compareTo(o1);
		}
	}

}