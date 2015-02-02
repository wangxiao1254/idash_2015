package task2.task2b.std;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

import network.Server;
import task2.Constants;
import task2.obliviousmerge.ObliviousMergeLib;
import task2.task2b.PrepareData;
import task2.task2b.SNPEntry;
import util.EvaRunnable;
import util.GenRunnable;
import util.Utils;
import flexsc.CompEnv;

public class Task2 {
	
	public static<T> T[] compute(CompEnv<T> env, T[][] scData) {
		ObliviousMergeLib<T> lib = new ObliviousMergeLib<T>(env);
		System.out.println(scData.length);
		System.out.println("merging data");
		lib.bitonicMerge(scData, lib.SIGNAL_ZERO);
		System.out.println("linear scaning");
		T[] resBit = lib.zeros(scData.length);
		for(int i = 0; i < scData.length-1; ++i) {			
			T eq = lib.eq(scData[i], scData[i+1]);
			resBit[i] = lib.not(eq);
		}
		System.out.println("linear scanned");
		T[] res = lib.numberOfOnes(resBit);
		res = lib.add(res, lib.toSignals(1, res.length));
		return res;
	}
		
	public static SNPEntry[]  sortKeyValue(HashSet<SNPEntry> map, boolean asc) {
		SNPEntry[] res = new SNPEntry[map.size()];
		Iterator<SNPEntry> it = map.iterator();
		int cnt = 0;
		while (it.hasNext()) {
			res[cnt++] = it.next();
		}
		Arrays.sort(res, asc? new SNPEntry.AscComparator() : new SNPEntry.DscComparator());
		return res;
	}
	
	public static class Generator<T> extends GenRunnable<T> {
		T[][] scData;
		int totalSize;
		T[] res;
		int totallength = 2+Constants.LengthOfLocation;
		
		@Override
		public void prepareInput(CompEnv<T> gen) {
			HashSet<SNPEntry> data = PrepareData.readFile(args[0]);
			SNPEntry[] sorted = sortKeyValue(data, true);
			
			boolean[][] clear = new boolean[sorted.length][];
			for(int i = 0; i < clear.length; ++i ) {
				clear[i] = Utils.fromLong(sorted[i].toNum(), totallength);
			}
			
			byte[] boblengthraw = null;
			try {
				gen.os.write(ByteBuffer.allocate(4).putInt(clear.length).array());
				gen.os.flush();
				boblengthraw = Server.readBytes(gen.is, 4);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int boblength = ByteBuffer.wrap(boblengthraw).getInt();
			totalSize = boblength+data.size();

			T[][] Alice = gen.inputOfAlice(clear);
			T[][] Bob = gen.inputOfBob(new boolean[boblength][totallength]);
			
			scData = gen.newTArray(Alice.length+Bob.length, totallength);
			
			System.arraycopy(Alice, 0, scData, 0, Alice.length);
			System.arraycopy(Bob, 0, scData, Alice.length, Bob.length);
		}

		@Override
		public void secureCompute(CompEnv<T> gen) {
			res = compute(gen, scData);
		}

		@Override
		public void prepareOutput(CompEnv<T> gen) {
			int r = Utils.toInt(gen.outputToAlice(res));
			System.out.println(2*r- totalSize);

		}
	}

	public static class Evaluator<T> extends EvaRunnable<T> {
		T[][] scData;
		T[] res;
		int totallength = 2+Constants.LengthOfLocation;

		public void prepareInput(CompEnv<T> gen) {
			HashSet<SNPEntry> data = PrepareData.readFile(args[0]);
			SNPEntry[] sorted = sortKeyValue(data, false);
			
			boolean[][] clear = new boolean[sorted.length][];
			for(int i = 0; i < clear.length; ++i ) {
				clear[i] = Utils.fromLong(sorted[i].toNum(), totallength);
			}
			
			byte[] alicelengthraw = null;
			try {
				gen.os.write(ByteBuffer.allocate(4).putInt(clear.length).array());
				gen.os.flush();
				alicelengthraw = Server.readBytes(gen.is, 4);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int boblength = ByteBuffer.wrap(alicelengthraw).getInt();
			
			T[][] Alice = gen.inputOfAlice(new boolean[boblength][totallength]);
			T[][] Bob = gen.inputOfBob(clear);
			
			scData = gen.newTArray(Alice.length+Bob.length, totallength);
			
			System.arraycopy(Alice, 0, scData, 0, Alice.length);
			System.arraycopy(Bob, 0, scData, Alice.length, Bob.length);
		}

		@Override
		public void secureCompute(CompEnv<T> gen) {
			res = compute(gen, scData);
		}

		@Override
		public void prepareOutput(CompEnv<T> gen) {
			gen.outputToAlice(res);
		}
	}
}