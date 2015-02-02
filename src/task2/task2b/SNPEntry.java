package task2.task2b;

import java.util.Comparator;

import task2.Constants;


public class SNPEntry implements Comparable<SNPEntry>{
	public long location;
	public int value;// ATCG: 0123

	public SNPEntry(int loc, int val) {
		location = loc;
		value = val;
	}

	public SNPEntry() {
		location=value=0;
	}
	@Override
	public boolean  equals(Object obj) {
		if(this == obj){
			return true;            
		}
		SNPEntry o = (SNPEntry) obj;
		if (o!=null ) {
			if(value == o.value &&
					location == o.location
					)
				return true;
			else return false;

		} else {
			return false;
		}
	};

	public long toNum(){
		long res = value;
		res<<= Constants.LengthOfLocation;
		res+= location;
		return res;
	}
	@Override
	public String toString() {
		return ""+location+" "+value;
	}
	@Override
	public int compareTo(SNPEntry o) {//according to all
		if(this == o){
			return 0;            
		}
		else if (o!=null ) {

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