package task1b;

public class Statistics {
	public String genotype;
	public char g1;
	public char g2;
	public int numOfG1G1;
	public int numOfG2G2;
	public int numOfG1G2;
	
	public static char[] LookupTable = {'A', 'G', 'C', 'T'};
	
	public static int getindex(char c){
		for(int i = 0; i < 4; ++i)
			if(c == LookupTable[i])
				return i;
		return -1;
	}
}
