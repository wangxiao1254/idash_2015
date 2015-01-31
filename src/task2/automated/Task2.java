package task2.automated;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

import network.Server;
import task2.PrepareData;
import task2.SNPEntry;
import util.EvaRunnable;
import util.GenRunnable;
import util.Utils;
import flexsc.CompEnv;
import gc.GCSignal;

public class Task2 {
	public static final int LengthOfLocation = 33;
	public static final int LengthOfIdLoc = 7;
	
	public static GCSignal[] compute(CompEnv<GCSignal> env, GCSignal[][] scData, int length) {
		Task2Automated a;
		GCSignal[] ret = null;
		try {
			a = new Task2Automated(env, scData[0].length, (int) Math.ceil(Math.log(length)/Math.log(2)) );
			ret = a.funct(scData, scData.length);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
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
	
	public static class Generator extends GenRunnable<GCSignal> {
		GCSignal[][] scData;
		int totalSize;
		GCSignal[] res;
		
		@Override
		public void prepareInput(CompEnv<GCSignal> gen) {
			HashSet<SNPEntry> data = PrepareData.readFile(args[0]);
			SNPEntry[] sorted = sortKeyValue(data, true);
			
			boolean[][] clear = new boolean[sorted.length][];
			for(int i = 0; i < clear.length; ++i ) {
				clear[i] = Utils.fromLong(sorted[i].toNum(), LengthOfLocation+4+LengthOfIdLoc);
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

			GCSignal[][] Alice = gen.inputOfAlice(clear);
			GCSignal[][] Bob = gen.inputOfBob(new boolean[boblength][LengthOfLocation+4+LengthOfIdLoc]);
			
			scData = gen.newTArray(Alice.length+Bob.length, LengthOfLocation+4+LengthOfIdLoc);
			
			System.arraycopy(Alice, 0, scData, 0, Alice.length);
			System.arraycopy(Bob, 0, scData, Alice.length, Bob.length);
		}

		@Override
		public void secureCompute(CompEnv<GCSignal> gen) {
			res = compute(gen, scData, totalSize);
		}

		@Override
		public void prepareOutput(CompEnv<GCSignal> gen) {
			int r = Utils.toInt(gen.outputToAlice(res));
			System.out.println(2*r- totalSize);

		}
	}

	public static class Evaluator extends EvaRunnable<GCSignal> {
		GCSignal[][] scData;
		GCSignal[] res;
		int totalSize;

		public void prepareInput(CompEnv<GCSignal> gen) {
			HashSet<SNPEntry> data = PrepareData.readFile(args[0]);
			SNPEntry[] sorted = sortKeyValue(data, false);
			
			boolean[][] clear = new boolean[sorted.length][];
			for(int i = 0; i < clear.length; ++i ) {
				clear[i] = Utils.fromLong(sorted[i].toNum(), LengthOfLocation+4+LengthOfIdLoc);
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
			int alicelength = ByteBuffer.wrap(alicelengthraw).getInt();
			totalSize = alicelength+data.size();
			
			GCSignal[][] Alice = gen.inputOfAlice(new boolean[alicelength][LengthOfLocation+4+LengthOfIdLoc]);
			GCSignal[][] Bob = gen.inputOfBob(clear);
			
			scData = gen.newTArray(Alice.length+Bob.length, LengthOfLocation+4+LengthOfIdLoc);
			
			System.arraycopy(Alice, 0, scData, 0, Alice.length);
			System.arraycopy(Bob, 0, scData, Alice.length, Bob.length);
		}

		@Override
		public void secureCompute(CompEnv<GCSignal> gen) {
			res = compute(gen, scData, totalSize);
		}

		@Override
		public void prepareOutput(CompEnv<GCSignal> gen) {
			gen.outputToAlice(res);
		}
	}
}