package task2;

import java.util.Arrays;
import java.util.HashMap;

import util.EvaRunnable;
import util.GenRunnable;
import util.Utils;
import flexsc.CompEnv;

public class Task1b {
	static final int LengthOfLocation = 35;
	static final int LengthOfEntry = LengthOfLocation + 4; 

	public static<T> T[] compute(CompEnv<T> env, T[][] key, T[][] value) {
		ObliviousMergeLib<T> lib = new ObliviousMergeLib<T>(env);
		lib.bitonicMergeWithPayload(key, value, lib.SIGNAL_ZERO);
		
		T[] resBit = lib.zeros(key.length);
		for(int i = 0; i < key.length-1; ++i) {
			T[] pos1 = key[i];
			T[] op1 = Arrays.copyOfRange(value[i], 0, 2);
			T[] val1 = Arrays.copyOfRange(value[i], 2, 4);
			T[] pos2 = key[i+1];
			T[] op2 = Arrays.copyOfRange(value[i+1], 0, 2);
			T[] val2 = Arrays.copyOfRange(value[i+1], 2, 4);
			
			T posEq = lib.eq(pos1, pos2);
			T opEq = lib.eq(op1, op2);
			T valEq = lib.eq(val1, val2);
			resBit[i] = lib.and(posEq, opEq);
			resBit[i] = lib.and(resBit[i], valEq);
		}
		return lib.numberOfOnes(resBit);
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
			res = compute(gen, key, value);
			
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
			res = compute(gen, key, value);
		}

		@Override
		public void prepareOutput(CompEnv<T> gen) {
			gen.outputToAlice(res);
		}
	}
}
