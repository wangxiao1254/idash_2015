package task2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import util.EvaRunnable;
import util.GenRunnable;
import util.Utils;
import flexsc.CompEnv;

public class Task2 {
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
	
	public static SNPEntry[]  sortKeyValue(HashMap<Integer, SNPEntry> map, boolean asc) {
		SNPEntry[] res = new SNPEntry[map.size()];
		Iterator<Entry<Integer, SNPEntry>> it = map.entrySet().iterator();
		int cnt = 0;
		while (it.hasNext()) {
			Map.Entry<Integer, SNPEntry> pairs = it.next();
			res[cnt] = pairs.getValue();
		}
		Arrays.sort(res, asc? new SNPEntry.AscComparator() : new SNPEntry.AscComparator());
		return res;
	}
	
	public static class Generator<T> extends GenRunnable<T> {
		T[][] key;
		T[][] value;
		T[] res;
		@Override
		public void prepareInput(CompEnv<T> gen) {
			HashMap<Integer, SNPEntry> data = PrepareData.readFile(args[0]);
			SNPEntry[] sorted = sortKeyValue(data, true);
			boolean[][] keyClear = new boolean[sorted.length][];
			boolean[][] valClear = new boolean[sorted.length][];
			for(int i = 0; i < key.length; ++i ) {
				keyClear[i] = Utils.fromInt(sorted[i].location, LengthOfLocation);
				valClear[i] = new boolean[4];
				System.arraycopy(Utils.fromInt(sorted[i].op, 2), 0, valClear[i], 0, 2);
				System.arraycopy(Utils.fromInt(sorted[i].value, 2), 0, valClear[i], 2, 2);
			}
			T[][] Alicekey = gen.inputOfAlice(keyClear);
			T[][] Alicevalue = gen.inputOfAlice(valClear);
			T[][] Bobkey = gen.inputOfBob(keyClear);
			T[][] Bobvalue = gen.inputOfBob(valClear);
			key = gen.newTArray(Alicekey.length+Bobkey.length, LengthOfLocation);
			System.arraycopy(Alicekey, 0, key, 0, Alicekey.length);
			System.arraycopy(Bobkey, 0, key, Alicekey.length, Bobkey.length);
			
			System.arraycopy(Alicevalue, 0, value, 0, Alicevalue.length);
			System.arraycopy(Bobvalue, 0, value, Alicevalue.length, Bobvalue.length);
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
			HashMap<Integer, SNPEntry> data = PrepareData.readFile(args[0]);
			SNPEntry[] sorted = sortKeyValue(data, false);
			boolean[][] keyClear = new boolean[sorted.length][];
			boolean[][] valClear = new boolean[sorted.length][];
			for(int i = 0; i < key.length; ++i ) {
				keyClear[i] = Utils.fromInt(sorted[i].location, LengthOfLocation);
				valClear[i] = new boolean[4];
				System.arraycopy(Utils.fromInt(sorted[i].op, 2), 0, valClear[i], 0, 2);
				System.arraycopy(Utils.fromInt(sorted[i].value, 2), 0, valClear[i], 2, 2);
			}
			T[][] Alicekey = gen.inputOfAlice(keyClear);
			T[][] Alicevalue = gen.inputOfAlice(valClear);
			T[][] Bobkey = gen.inputOfBob(keyClear);
			T[][] Bobvalue = gen.inputOfBob(valClear);
			key = gen.newTArray(Alicekey.length+Bobkey.length, LengthOfLocation);
			System.arraycopy(Alicekey, 0, key, 0, Alicekey.length);
			System.arraycopy(Bobkey, 0, key, Alicekey.length, Bobkey.length);
			
			System.arraycopy(Alicevalue, 0, value, 0, Alicevalue.length);
			System.arraycopy(Bobvalue, 0, value, Alicevalue.length, Bobvalue.length);
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
