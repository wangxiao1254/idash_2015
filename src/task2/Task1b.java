package task2;

import java.util.HashMap;

import util.EvaRunnable;
import util.GenRunnable;
import util.Utils;
import flexsc.CompEnv;

public class Task1b {
	static final int LengthOfLocation = 35;
	static final int LengthOfResult = 10;
	static final int LengthOfEntry = LengthOfLocation + 4; 

	public static<T> void compute(CompEnv<T> env, T[][] key, T[][] value, T[] res) {
		ObliviousMergeLib<T> lib = new ObliviousMergeLib<T>(env);
		lib.bitonicMergeWithPayload(key, value, lib.SIGNAL_ZERO);
		res = lib.zeros(LengthOfResult);
		
		for(int i = 0; i < key.length-1; ++i) {
			
		}
	}
	
	public static class Generator<T> extends GenRunnable<T> {
		T[][] key;
		T[][] value;
		T[] res;
		@Override
		public void prepareInput(CompEnv<T> gen) {
			HashMap<Integer, SNPEntry> data = PrepareData.readFile(args[0]);
			
		}


		@Override
		public void secureCompute(CompEnv<T> gen) {
			compute(gen, key, value, res);
			
		}

		@Override
		public void prepareOutput(CompEnv<T> gen) {
			System.out.println(Utils.toInt(gen.outputToAlice(res)));
		}

	}

	public static class Evaluator<T> extends EvaRunnable<T> {
		T[][] key;
		T[][] value;
		T[] res;
		
		@Override
		public void prepareInput(CompEnv<T> gen) {
			// TODO Auto-generated method stub
		}

		@Override
		public void secureCompute(CompEnv<T> gen) {
			compute(gen, key, value, res);
		}

		@Override
		public void prepareOutput(CompEnv<T> gen) {
			gen.outputToAlice(res);
		}
	}
}
