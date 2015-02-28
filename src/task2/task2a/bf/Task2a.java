package task2.task2a.bf;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashSet;

import network.Server;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;

import task2.bloomFilter.BF;
import task2.circuit_from_compiler.BF_circuit;
import task2.task2a.PrepareData;
import task2.task2a.SNPEntry;
import util.EvaRunnable;
import util.GenRunnable;
import util.Utils;
import circuits.arithmetic.IntegerLib;
import flexsc.CompEnv;
import flexsc.Flag;

public class Task2a {
	public static int NoM = 5;

	public static<T> T[] compute(CompEnv<T> env, T[] aliceBF, T[] bobBF) {
		IntegerLib<T> lib = new IntegerLib<>(env);
		T[] aUb = lib.or(aliceBF, bobBF);
		return lib.numberOfOnes(aUb);
	}
	
	public static int nextPower(int a) {
		int i = 1;
		while(i < a) {
			i*=2;
		}
		return i;
	}
	
	public static<T> T[] computeAuto(CompEnv<T> env, T[] aliceBF, T[] bobBF) throws Exception {
		BF_circuit<T> lib2 = new BF_circuit<T>(env);
		return lib2.merge(aliceBF.length, aliceBF, bobBF);
	}
	

	static CommandLine processArgs(String[] args) throws Exception {
		Options options = new Options();
		options.addOption("f", "file", true, "file");
		options.addOption("p", "filprecisione", true, "precision");
		options.addOption("a", "automated", false, "automated");

		CommandLineParser parser = new BasicParser();
		CommandLine cmd = parser.parse(options, args);

		if(!cmd.hasOption("f")) {
			throw new Exception("wrong input");
		}
		return cmd;
	}

	public static class Generator<T> extends GenRunnable<T> {
		T[] aliceBF;
		T[] bobBF;
		T[] res;
		int totalSize;
		boolean automated;
		BF bf;
		@Override
		public void prepareInput(CompEnv<T> gen) throws Exception {
			CommandLine cmd = processArgs(args);
			automated = cmd.hasOption("a");
			if(cmd.hasOption("p"))
				NoM = new Integer(cmd.getOptionValue("p"));
			HashSet<SNPEntry> data = PrepareData.readFile(cmd.getOptionValue("f"));

			int alicelength = data.size();
			gen.os.write(ByteBuffer.allocate(4).putInt(data.size()).array());
			gen.os.flush();
			int boblength = ByteBuffer.wrap(Server.readBytes(gen.is, 4)).getInt();
			totalSize = boblength+alicelength;
			
			bf = new BF(boblength+alicelength, NoM*(boblength+alicelength));
			for(int i = 0; i < bf.k; ++i)
				gen.os.write(bf.sks[i]);
			gen.os.flush();

			for(SNPEntry e : data)
				bf.insert(e.toString());
		}

		@Override
		public void secureCompute(CompEnv<T> gen) throws Exception {
			IntegerLib<T> lib = new IntegerLib<>(gen);
			res = lib.zeros(32);
			for(int i = 0; i < bf.bs.length; i+=Flag.OTBlockSize) {
				int len = Math.min(bf.bs.length, i+Flag.OTBlockSize);
				aliceBF = gen.inputOfAlice(Arrays.copyOfRange(bf.bs, i, len));
				bobBF =  gen.inputOfBob(Arrays.copyOfRange(bf.bs, i, len));
				T[] tmp = null;
				if(automated)
					tmp = computeAuto(gen, lib.padSignal(aliceBF, nextPower(aliceBF.length)), lib.padSignal(bobBF, nextPower(bobBF.length)));
				else tmp = compute(gen, aliceBF, bobBF);
				res = lib.add(res, lib.padSignal(tmp, 32));
			}
		}
		@Override
		public void prepareOutput(CompEnv<T> gen) {
			int r = Utils.toInt(gen.outputToAlice(res));
			r = BF.countToSize(r, bf.k, bf.m);
			System.out.println("Hamming Distance is: "+(2*r-totalSize));
		}		
	}

	public static class Evaluator<T> extends EvaRunnable<T> {
		T[] aliceBF;
		T[] bobBF;
		T[] res;
		BF bf;
		boolean automated;
		@Override
		public void prepareInput(CompEnv<T> gen) throws Exception {
			CommandLine cmd = processArgs(args);
			automated = cmd.hasOption("a");
			if(cmd.hasOption("p"))
				NoM = new Integer(cmd.getOptionValue("p"));
			HashSet<SNPEntry> data = PrepareData.readFile(cmd.getOptionValue("f"));
			int boblength = data.size();

			gen.os.write(ByteBuffer.allocate(4).putInt(data.size()).array());
			gen.os.flush();
			int alicelength = ByteBuffer.wrap(Server.readBytes(gen.is, 4)).getInt();

			bf = new BF(boblength+alicelength, NoM*(boblength+alicelength));
			for(int i = 0; i < bf.k; ++i)
				bf.sks[i] = Server.readBytes(gen.is, bf.sks[i].length);

			for(SNPEntry e : data)
				bf.insert(e.toString());
		}

		@Override
		public void secureCompute(CompEnv<T> gen) throws Exception {
			IntegerLib<T> lib = new IntegerLib<>(gen);
			res = lib.zeros(32);
			for(int i = 0; i < bf.bs.length; i+=Flag.OTBlockSize) {
				int len = Math.min(bf.bs.length, i+Flag.OTBlockSize);
				aliceBF = gen.inputOfAlice(Arrays.copyOfRange(bf.bs, i, len));
				bobBF =  gen.inputOfBob(Arrays.copyOfRange(bf.bs, i, len));
				T[] tmp = null;
				if(automated)
					tmp = computeAuto(gen, lib.padSignal(aliceBF, nextPower(aliceBF.length)), lib.padSignal(bobBF, nextPower(bobBF.length)));
				else tmp = compute(gen, aliceBF, bobBF);
				res = lib.add(res, lib.padSignal(tmp, 32));
			}
		}

		@Override
		public void prepareOutput(CompEnv<T> gen) {
			gen.outputToAlice(res);
		}
	}
}