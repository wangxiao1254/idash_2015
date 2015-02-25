package task2.task2a.std;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashSet;

import network.Server;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;

import task2.bloomFilter.BF;
import task2.circuit_from_compiler.Task2Automated;
import task2.obliviousmerge.ObliviousMergeLib;
import task2.task2a.PrepareData;
import task2.task2a.SNPEntry;
import util.EvaRunnable;
import util.GenRunnable;
import util.Utils;
import flexsc.CompEnv;

public class Task2 {
	public static final int SP = 40;
	
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
	
	
	public static<T> T[] computeAuto(CompEnv<T> env, T[][] scData) {
		Task2Automated<T> a;
		T[] ret = null;
		try {
			a = new Task2Automated<T>(env, scData[0].length, (int) Math.ceil(Math.log(scData.length)/Math.log(2)) );
			ret = a.funct(scData, scData.length);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	public static class Generator<T> extends GenRunnable<T> {
		T[][] scData;
		T[] res;
		BF bf;
		int totalSize = 0;
		boolean automated;
		@Override
		public void prepareInput(CompEnv<T> gen) throws Exception {
			Options options = new Options();
			options.addOption("a", false, "automated");
			options.addOption("f", "file", true, "file");

			CommandLineParser parser = new BasicParser();
			CommandLine cmd = parser.parse(options, args);

			automated = cmd.hasOption("a");
			if(!cmd.hasOption("f")) {
				throw new Exception("wrong input");
			}
			if(automated)
				System.out.println("Running the program with automatically generated circuits");
			else
				System.out.println("Running the program with manually generated circuits");

			HashSet<SNPEntry> data = PrepareData.readFile(cmd.getOptionValue("f"));
			int alicelength = data.size();
			
			byte[] boblengthraw = null;
			try {
				gen.os.write(ByteBuffer.allocate(4).putInt(data.size()).array());
				gen.os.flush();
				boblengthraw = Server.readBytes(gen.is, 4);
			} catch (IOException e) {
				e.printStackTrace();
			}
			int boblength = ByteBuffer.wrap(boblengthraw).getInt();
			totalSize = boblength+alicelength;
			int LEN = (int) (Math.log(totalSize)/Math.log(2)+SP);
			long[] in = new long[alicelength];
			int cnt = 0;
			for(SNPEntry e : data) {
				in[cnt] = SNPEntry.HashToLong(e.toString(), LEN);
				cnt++;
			}
			Arrays.sort(in);
			
			boolean[][] clear = new boolean[alicelength][];
			for(int i = 0; i < in.length;  ++i)
				clear[i] = Utils.fromLong(in[i], LEN);
			
			T[][] Alice = gen.inputOfAlice(clear);
			T[][] Bob = gen.inputOfBob(new boolean[boblength][LEN]);
			
			scData = gen.newTArray(totalSize, LEN);
			
			System.arraycopy(Alice, 0, scData, 0, Alice.length);
			System.arraycopy(Bob, 0, scData, Alice.length, Bob.length);
		}

		@Override
		public void secureCompute(CompEnv<T> gen) {
			if(automated)
				res = computeAuto(gen, scData);
			else
				res = compute(gen, scData);
		}

		@Override
		public void prepareOutput(CompEnv<T> gen) {
			int r = Utils.toInt(gen.outputToAlice(res));
//			r = bf.countToSize(r);
			System.out.println("res"+(2*r-totalSize));
		}		
	}

	public static class Evaluator<T> extends EvaRunnable<T> {
		T[][] scData;
		T[] res;
		boolean automated;
		
		@Override
		public void prepareInput(CompEnv<T> gen) throws Exception {
			Options options = new Options();
			options.addOption("a", false, "automated");
			options.addOption("f", "file", true, "file");

			CommandLineParser parser = new BasicParser();
			CommandLine cmd = parser.parse(options, args);

			automated = cmd.hasOption("a");
			if(!cmd.hasOption("f")) {
				throw new Exception("wrong input");
			}
			if(automated)
				System.out.println("Running the program with automatically generated circuits");
			else
				System.out.println("Running the program with manually generated circuits");

			HashSet<SNPEntry> data = PrepareData.readFile(cmd.getOptionValue("f"));
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
			int totalSize = alicelength+boblength;
			int LEN = (int) (Math.log(totalSize)/Math.log(2)+SP);
			
			long[] in = new long[boblength];
			int cnt = 0;
			for(SNPEntry e : data) {
				in[cnt] = -1*SNPEntry.HashToLong(e.toString(), LEN);
				cnt++;
			}
			Arrays.sort(in);
			
			boolean[][] clear = new boolean[boblength][];
			for(int i = 0; i < in.length;  ++i)
				clear[i] = Utils.fromLong(-1*in[i], LEN);

			T[][] Alice = gen.inputOfAlice(new boolean[alicelength][LEN]);
			T[][] Bob = gen.inputOfBob(clear);
			scData = gen.newTArray(Alice.length+Bob.length, LEN);
			
			System.arraycopy(Alice, 0, scData, 0, Alice.length);
			System.arraycopy(Bob, 0, scData, Alice.length, Bob.length);
		}

		@Override
		public void secureCompute(CompEnv<T> gen) {
			if(automated)
				res = computeAuto(gen, scData);
			else
				res = compute(gen, scData);
		}

		@Override
		public void prepareOutput(CompEnv<T> gen) {
			gen.outputToAlice(res);
		}
	}
}