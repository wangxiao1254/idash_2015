package task2;

import flexsc.CompEnv;
import harness.TestHarness;
import harness.TestSortHarness;
import harness.TestSortHarness.Helper;

import java.util.Arrays;

import org.junit.Test;

public class TestObliviousMergeLib extends TestHarness {

	@Test
	public void testAllCases() throws Exception {
		for (int i = 0; i < 100; i++) {
			int[] a = new int[CompEnv.rnd.nextInt(2000)];
			int[] b = new int[CompEnv.rnd.nextInt(2000)];
			for(int j = 0; j < a.length; ++j) a[j] = CompEnv.rnd.nextInt(10000);
			for(int j = 0; j < b.length; ++j) b[j] = -1*CompEnv.rnd.nextInt(10000);
			Arrays.sort(a);
			Arrays.sort(b);
			for(int j = 0; j < b.length; ++j) b[j] = -1*b[j];
			int[] c = new int[a.length + b.length];
			System.arraycopy(a, 0, c, 0, a.length);
			System.arraycopy(b, 0, c, a.length, b.length);

			TestSortHarness.runThreads(new Helper(c) {
				public <T>T[][] secureCompute(T[][] Signala, CompEnv<T> e) {
					ObliviousMergeLib<T> lib = new ObliviousMergeLib<T>(e);
					lib.bitonicMerge(Signala, lib.SIGNAL_ZERO);
					return Signala;
				}

				@Override
				public int[] plainCompute(int[] intA) {
					Arrays.sort(intA);
					return intA;
				}
			});
		}
	}

}