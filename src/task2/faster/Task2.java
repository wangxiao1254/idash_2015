package task2.faster;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashSet;

import network.Server;
import task2.PrepareData;
import task2.SNPEntry;
import util.EvaRunnable;
import util.GenRunnable;
import util.Utils;
import circuits.arithmetic.IntegerLib;
import flexsc.CompEnv;

public class Task2 {
	public static final int NoM = 20;
	
	public static<T> T[] compute(CompEnv<T> env, T[] aliceBF, T[] bobBF) {
		IntegerLib<T> lib = new IntegerLib<>(env);
		System.out.println("Bloom Filter Size: "+aliceBF.length);
		T[] aUb = lib.or(aliceBF, bobBF);
		return lib.numberOfOnes(aUb);
	}
	
	public static class Generator<T> extends GenRunnable<T> {
		T[] aliceBF;
		T[] bobBF;
		T[] res;
		BF bf;
		int totalSize = 0;
		
		@Override
		public void prepareInput(CompEnv<T> gen) {
			HashSet<SNPEntry> data = PrepareData.readFile(args[0]);
			int alicelength = data.size();
			byte[] boblengthraw = null;
			try {
				gen.os.write(ByteBuffer.allocate(4).putInt(data.size()).array());
				gen.os.flush();
				boblengthraw = Server.readBytes(gen.is, 4);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int boblength = ByteBuffer.wrap(boblengthraw).getInt();
			totalSize = boblength+alicelength;
			bf = new BF(boblength+alicelength, NoM*(boblength+alicelength));
			try {
				for(int i = 0; i < bf.k; ++i)
					gen.os.write(bf.sks[i]);
				gen.os.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			for(SNPEntry e : data) {
				bf.insert(e.toNum());
			}
			
			aliceBF = gen.inputOfAlice(bf.bs);
			bobBF =  gen.inputOfBob(bf.bs);
		}

		@Override
		public void secureCompute(CompEnv<T> gen) {
			res = compute(gen, aliceBF, bobBF);
		}

		@Override
		public void prepareOutput(CompEnv<T> gen) {
			int r = Utils.toInt(gen.outputToAlice(res));
			r = bf.countToSize(r);
			System.out.println(2*r- totalSize);
		}		
	}

	public static class Evaluator<T> extends EvaRunnable<T> {
		T[] aliceBF;
		T[] bobBF;
		T[] res;
		
		@Override
		public void prepareInput(CompEnv<T> gen) {
			HashSet<SNPEntry> data = PrepareData.readFile(args[0]);
			int boblength = data.size();
			byte[] alicelengthraw = null;
			try {
				gen.os.write(ByteBuffer.allocate(4).putInt(data.size()).array());
				gen.os.flush();
				alicelengthraw = Server.readBytes(gen.is, 4);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int alicelength = ByteBuffer.wrap(alicelengthraw).getInt();
			

			BF bf = new BF(boblength+alicelength, NoM*(boblength+alicelength));
			for(int i = 0; i < bf.k; ++i)
				try {
					bf.sks[i] = Server.readBytes(gen.is, bf.sks[i].length);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
			for(SNPEntry e : data) {
				bf.insert(e.toNum());
			}
			
			aliceBF = gen.inputOfAlice(bf.bs);
			bobBF =  gen.inputOfBob(bf.bs);
		}

		@Override
		public void secureCompute(CompEnv<T> gen) {
			res = compute(gen, aliceBF, bobBF);
		}

		@Override
		public void prepareOutput(CompEnv<T> gen) {
			gen.outputToAlice(res);
		}
	}
}