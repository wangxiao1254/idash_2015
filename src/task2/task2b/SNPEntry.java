package task2.task2b;

import java.util.Comparator;

import task2.Constants;


public class SNPEntry implements Comparable<SNPEntry>{
	public long location;
	public int i_dloc;
	public int op;//0: SUB 1: SNP 2: DEL 3: INS
	public int value;// ATCG: 0123

	public SNPEntry(int loc, int val, int idloc, int o) {
		op = o;
		location = loc;
		value = val;
		i_dloc = idloc;
	}

	public SNPEntry() {
		location=op=value=i_dloc=0;
	}
	@Override
	public boolean  equals(Object obj) {
		if(this == obj){
			return true;            
		}
		SNPEntry o = (SNPEntry) obj;
		if (o!=null ) {
			if(i_dloc == o.i_dloc &&
					value == o.value &&
					op == o.op &&
					location == o.location
					)
				return true;
			else return false;

		} else {
			return false;
		}
	};

	public long toNum(){
		long res = i_dloc;
		res <<=2;
		res +=op;
		res<<=2;
		res+=value;
		res<<= Constants.LengthOfLocation;
		res+= location;
		return res;
	}
	@Override
	public String toString() {
		return ""+location+" "+op+" "+" "+i_dloc+" "+value;
	}
	@Override
	public int compareTo(SNPEntry o) {//according to all
		if(this == o){
			return 0;            
		}
		else if (o!=null ) {
			if(i_dloc < o.i_dloc){
				return -1;
			} else if (i_dloc > o.i_dloc) {
				return 1;
			}
			else {
				if(op < o.op){
					return -1;
				} else if (op > o.op) {
					return 1;
				}
				else {
					if(value < o.value){
						return -1;
					} else if (value > o.value) {
						return 1;
					}
					else {
						if(location < o.location){
							return -1;
						} else {//if (location >= o.location) {
							return 1;
						}
					}	
				}				
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