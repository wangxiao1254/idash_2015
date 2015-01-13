package task1a;

public class Statistics {
	public String genotype;
	public char g1;
	public char g2;
	public int numOfG1;
	public int numOfG2;
	
	public static char[] LookupTable = {'A', 'G', 'C', 'T'};
	
	public static int getindex(char c){
		for(int i = 0; i < 4; ++i)
			if(c == LookupTable[i])
				return i;
		return -1;
	}
}
