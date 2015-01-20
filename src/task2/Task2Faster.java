package task2;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import network.Server;
import util.EvaRunnable;
import util.GenRunnable;
import util.Utils;
import flexsc.CompEnv;

public class Task2Faster {
	static final int LengthOfLocation = 32; 

	public static<T> T[] compute(CompEnv<T> env, T[][] key, T[][] value,
			T[] addtionalLengthAlice, T[] addtionalLengthBob) {
		ObliviousMergeLib<T> lib = new ObliviousMergeLib<T>(env);
		System.out.println(key.length);
		System.out.println("sorting data");
		lib.bitonicMergeWithPayload(key, value, lib.SIGNAL_ZERO);
		System.out.println("linear scaning");
		T[] resBit = lib.zeros(key.length);
		for(int i = 0; i < key.length-1; ++i) {
			T[] pos1 = key[i];
			T[] val1 = Arrays.copyOfRange(value[i], 0, 2);
			T[] pos2 = key[i+1];
			T[] val2 = Arrays.copyOfRange(value[i+1], 0, 2);
			
			T posEq = lib.eq(pos1, pos2);
			T valEq = lib.eq(val1, val2);
			resBit[i] = lib.and(posEq, valEq);
			resBit[i] = lib.not(resBit[i]);
		}
		System.out.println("linear scanned");
		T[] raw = lib.numberOfOnes(resBit);
		T[] result = lib.add(lib.padSignal(raw, 32), addtionalLengthAlice);
		result = lib.add(result, addtionalLengthBob);
		return result;
	}
	
	public static SNPEntry[] filter(SNPEntry[] data) {
		boolean[] test = new boolean[data.length];
		int cnt = 0;
		for(int i = 0; i < data.length; ++i) {
			test[i] = true;
			if(data[i].op == 2 || data[i].op == 3)
				test[i] = false;
			else ++cnt;
		}
		SNPEntry[] res = new SNPEntry[cnt];
		int cnt2 = 0;
		for(int i = 0; i < data.length; ++i) {
			if(test[i])
				res[cnt2++] = data[i];
		}
		return res;
	}
	
	public static SNPEntry[]  sortKeyValue(HashMap<Long, SNPEntry> map, boolean asc) {
		SNPEntry[] res = new SNPEntry[map.size()];
		Iterator<Entry<Long, SNPEntry>> it = map.entrySet().iterator();
		int cnt = 0;
		while (it.hasNext()) {
			Map.Entry<Long, SNPEntry> pairs = it.next();
			res[cnt++] = pairs.getValue();
		}
		Arrays.sort(res, asc? new SNPEntry.AscComparator() : new SNPEntry.DscComparator());
		return res;
	}
	
	public static class Generator<T> extends GenRunnable<T> {
		T[][] key;
		T[][] value;
		T[] res;
		T[] addtionalLengthAlice;
		T[] addtionalLengthBob;
		
		@Override
		public void prepareInput(CompEnv<T> gen) {
			HashMap<Long, SNPEntry> data = PrepareData.readFile(args[0]);
			SNPEntry[] sorted = sortKeyValue(data, true);
			int lengthBefore = sorted.length;
			sorted = filter(sorted);
			int lengthDiff = lengthBefore-sorted.length;
			
			boolean[][] keyClear = new boolean[sorted.length][];
			boolean[][] valClear = new boolean[sorted.length][];
			for(int i = 0; i < keyClear.length; ++i ) {
				keyClear[i] = Utils.fromLong(sorted[i].location, LengthOfLocation);
				valClear[i] = Utils.fromInt(sorted[i].value, 2);
			}
			
			byte[] boblengthraw = null;
			try {
				gen.os.write(ByteBuffer.allocate(4).putInt(keyClear.length).array());
				gen.os.flush();
				boblengthraw = Server.readBytes(gen.is, 4);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int boblength = ByteBuffer.wrap(boblengthraw).getInt();
			
			addtionalLengthAlice = gen.inputOfAlice(Utils.fromInt(lengthDiff, 32));
			T[][] Alicekey = gen.inputOfAlice(keyClear);
			T[][] Alicevalue = gen.inputOfAlice(valClear);
			
			addtionalLengthBob = gen.inputOfBob(new boolean[32]);
			T[][] Bobkey = gen.inputOfBob(new boolean[boblength][LengthOfLocation]);
			T[][] Bobvalue = gen.inputOfBob(new boolean[boblength][2]);
			
			key = gen.newTArray(Alicekey.length+Bobkey.length, LengthOfLocation);
			value = gen.newTArray(Alicevalue.length+Bobvalue.length, 2);
			
			System.arraycopy(Alicekey, 0, key, 0, Alicekey.length);
			System.arraycopy(Bobkey, 0, key, Alicekey.length, Bobkey.length);
			System.arraycopy(Alicevalue, 0, value, 0, Alicevalue.length);
			System.arraycopy(Bobvalue, 0, value, Alicevalue.length, Bobvalue.length);
		}

		@Override
		public void secureCompute(CompEnv<T> gen) {
			res = compute(gen, key, value, addtionalLengthAlice, addtionalLengthBob);
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
		T[] addtionalLengthAlice;
		T[] addtionalLengthBob;
		
		@Override
		public void prepareInput(CompEnv<T> gen) {
			HashMap<Long, SNPEntry> data = PrepareData.readFile(args[0]);
			SNPEntry[] sorted = sortKeyValue(data, false);
			int lengthBefore = sorted.length;
			sorted = filter(sorted);
			int lengthDiff = lengthBefore - sorted.length;
			
			boolean[][] keyClear = new boolean[sorted.length][];
			boolean[][] valClear = new boolean[sorted.length][];
			for(int i = 0; i < keyClear.length; ++i ) {
				keyClear[i] = Utils.fromLong(sorted[i].location, LengthOfLocation);
				valClear[i] = Utils.fromInt(sorted[i].value, 2);
			}
			
			
			byte[] alicelengthraw = null;
			try {
				gen.os.write(ByteBuffer.allocate(4).putInt(keyClear.length).array());
				gen.os.flush();
				alicelengthraw = Server.readBytes(gen.is, 4);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int alicelength = ByteBuffer.wrap(alicelengthraw).getInt();
			
			addtionalLengthAlice = gen.inputOfAlice(new boolean[32]);
			T[][] Alicekey = gen.inputOfAlice(new boolean[alicelength][LengthOfLocation]);
			T[][] Alicevalue = gen.inputOfAlice(new boolean[alicelength][2]);
			
			addtionalLengthBob = gen.inputOfBob(Utils.fromInt(lengthDiff, 32));
			T[][] Bobkey = gen.inputOfBob(keyClear);
			T[][] Bobvalue = gen.inputOfBob(valClear);
			key = gen.newTArray(Alicekey.length+Bobkey.length, LengthOfLocation);
			value = gen.newTArray(Alicevalue.length+Bobvalue.length, 2);
			
			System.arraycopy(Alicekey, 0, key, 0, Alicekey.length);
			System.arraycopy(Bobkey, 0, key, Alicekey.length, Bobkey.length);
			System.arraycopy(Alicevalue, 0, value, 0, Alicevalue.length);
			System.arraycopy(Bobvalue, 0, value, Alicevalue.length, Bobvalue.length);
		}

		@Override
		public void secureCompute(CompEnv<T> gen) {
			res = compute(gen, key, value, addtionalLengthAlice, addtionalLengthBob);
		}

		@Override
		public void prepareOutput(CompEnv<T> gen) {
			gen.outputToAlice(res);
		}
	}
}