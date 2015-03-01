package task2.bloomFilter;


import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;

public class TestBF {
	public static double test(int n, int m, double constant) throws NoSuchAlgorithmException  {
		BF bf = new BF(n, m, constant);
		for(int i = 0; i < n; i++) {
			bf.insert(i);
		}
		int res = bf.size();

		return (res-n)/(double)n;
	}

	public static void main(String[] args) throws NoSuchAlgorithmException, FileNotFoundException {
		int n = 800;//new Integer(args[0]);

		for(int m = 1000; m <= 200000; m+=1000) {
			int cases = 1000;
			double res=0;
			for(int i = 0; i < cases; ++i) {
				res += test(n,m,Math.log(2));
			}
			System.out.println(m+"\t"+(res/cases));
		}
	}

}