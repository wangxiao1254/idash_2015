package task2.obliviousmerge;

import java.util.Arrays;

import org.junit.Test;

import flexsc.CompEnv;

public class obliviousMerge {
	public static void bitonicMerge(int[] key, int lo, int n, boolean dir) {
		if (n > 1) {
			int m = greatestPowerOfTwoLessThan(n);
			for (int i = lo; i < lo + n - m; i++)
				compare(key, i, i + m, dir);
			bitonicMerge(key, lo, m, dir);
			bitonicMerge(key, lo + m, n - m, dir);
		}
	}

	private static void compare(int[] key, int i, int j, boolean dir) {
		boolean greater = key[i] > key[j];
		if(greater == dir) {
			int temp = key[j];
			key[j] = key[i];
			key[i] = temp;
		}
	}

	private static int greatestPowerOfTwoLessThan(int n) {
		int k = 1;
		while (k < n)
			k = k << 1;
		return k >> 1;
	}
	
	
	@Test
	public void TestCases(){
		for(int i = 0; i < 100000; ++i)
			test();
	}
	
		
	public static void test() {		
		int[] a = new int[CompEnv.rnd.nextInt(2000)];
		int[] b = new int[CompEnv.rnd.nextInt(2000)];
		for(int i = 0; i < a.length; ++i) a[i] = CompEnv.rnd.nextInt(10000);
		for(int i = 0; i < b.length; ++i) b[i] = -1*CompEnv.rnd.nextInt(10000);
		Arrays.sort(a);
		Arrays.sort(b);
		for(int i = 0; i < b.length; ++i) b[i] = -1*b[i];
		//System.out.println(Arrays.toString(a));
		//System.out.println(Arrays.toString(b));
		int[] c = new int[a.length + b.length];
		System.arraycopy(a, 0, c, 0, a.length);
		System.arraycopy(b, 0, c, a.length, b.length);
		bitonicMerge(c, 0, c.length, false);
		for(int i = 0; i < c.length-1; ++i)
				if(c[i] < c[i+1]) {
					System.out.println(Arrays.toString(c));
				}
		//System.out.println("pass");
	}
}
