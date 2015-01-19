package task2;

public class SNPEntry {
	int op;//0: SUB 1: SNP 2: DEL 3: INS
	int location;
	int value;// ATCG: 0123
	
	public String ToString() {
		return ""+op+" "+location+" "+value;
	}
}
