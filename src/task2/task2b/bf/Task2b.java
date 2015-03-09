package task2.task2b.bf;

import java.util.Arrays;
import java.util.HashSet;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;

import task2.bloomFilter.BF;
import task2.circuit_from_compiler.BF_circuit;
import task2.task2b.PrepareData;
import task2.task2b.SNPEntry;
import util.EvaRunnable;
import util.GenRunnable;
import util.Utils;
import circuits.arithmetic.IntegerLib;
import flexsc.CompEnv;
import flexsc.Flag;

public class Task2b {
	public static int NoM = 3;

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

	public static int parameterChoose(int totalSize) {
		int l;
		if(totalSize <= 500)
			l = 15000;
		else if (totalSize <= 30000)
			l = 30000;
		else l =  totalSize;
		return l;
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
			throw new Exception("argument format is wrong!");
		}
		return cmd;
	}

	public static class Generator<T> extends GenRunnable<T> {
		T[] aliceBF, aliceBF2;
		T[] bobBF, bobBF2;
		T[] res, res2;
		int totalSize = 0;
		boolean automated;
		BF bf, bf2;
		@Override
		public void prepareInput(CompEnv<T> gen) throws Exception {
			CommandLine cmd = processArgs(args);
			automated = cmd.hasOption("a");

			if(cmd.hasOption("p"))
				NoM = new Integer(cmd.getOptionValue("p"));
			HashSet<SNPEntry> data = PrepareData.readFile(cmd.getOptionValue("f"));

			int alicelength = 0;			for(SNPEntry e : data) alicelength +=e.value.length();

			gen.channel.writeInt(alicelength);
			gen.channel.flush();

			int boblength = gen.channel.readInt();
			totalSize = boblength+alicelength;

			if(cmd.hasOption("p")) {
				NoM = new Integer(cmd.getOptionValue("p"));
				bf = new BF(boblength+alicelength, (int) (NoM*totalSize));
				bf2 = new BF(boblength+alicelength, (int) (NoM*totalSize));
			}
			else  {
				bf = new BF(boblength+alicelength, parameterChoose(totalSize));
				bf2 = new BF(boblength+alicelength, parameterChoose(totalSize));
			}

			bf2.sks = bf.sks;
			for(int i = 0; i < bf.k; ++i)
				gen.channel.writeByte(bf.sks[i],bf.sks[i].length);
			gen.channel.flush();

			for(SNPEntry e : data) {
				//for every pos: d+=max(d(x), d(y))
				for(int i = 0; i < e.value.length(); ++i){
					bf.insert(e.Pos(i));
				}
			}

			for(SNPEntry e : data) {
				// compute intersection which means: posx==posy & x==y
				for(int i = 0; i < e.value.length(); ++i)
					bf2.insert(e.PosVal(i));
			}
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

			res2 = lib.zeros(32);
			for(int i = 0; i < bf2.bs.length; i+=Flag.OTBlockSize) {
				int len = Math.min(bf2.bs.length, i+Flag.OTBlockSize);
				aliceBF = gen.inputOfAlice(Arrays.copyOfRange(bf2.bs, i, len));
				bobBF =  gen.inputOfBob(Arrays.copyOfRange(bf2.bs, i, len));
				T[] tmp = null;
				if(automated)
					tmp = computeAuto(gen, lib.padSignal(aliceBF, nextPower(aliceBF.length)), lib.padSignal(bobBF, nextPower(bobBF.length)));
				else tmp = compute(gen, aliceBF, bobBF);				res2 = lib.add(res2, lib.padSignal(tmp, 32));
			}

			T[] m_X = lib.sub(lib.toSignals(bf.m, res.length), res);
			T[] m_Y = lib.sub(lib.toSignals(bf.m, res2.length), res2);
			int length = 2*Math.max(m_X.length, m_Y.length)+2;
			res = lib.multiply(lib.padSignal(m_X, length), lib.padSignal(m_Y, length));
		}

		@Override
		public void prepareOutput(CompEnv<T> gen) {
			double tmp = Utils.toLong(gen.outputToAlice(res));
			int result = (int) (Math.log(tmp/bf.m/bf.m)/bf.k/Math.log(1.0-1.0/bf.m) - totalSize);
			System.out.println("Edit Distance: "+result );
		}		
	}

	public static class Evaluator<T> extends EvaRunnable<T> {
		T[] aliceBF;
		T[] bobBF;
		T[] aliceBF2;
		T[] bobBF2;
		T[] res, res2;
		BF bf, bf2;
		boolean automated;
		@Override
		public void prepareInput(CompEnv<T> gen) throws Exception {
			CommandLine cmd = processArgs(args);
			automated = cmd.hasOption("a");

			if(cmd.hasOption("p"))
				NoM = new Integer(cmd.getOptionValue("p"));
			HashSet<SNPEntry> data = PrepareData.readFile(cmd.getOptionValue("f"));

			int boblength = 0;			for(SNPEntry e : data) boblength +=e.value.length();
			gen.channel.writeInt(boblength);
			gen.channel.flush();

			int alicelength = gen.channel.readInt();
			int totalSize = alicelength + boblength;

			if(cmd.hasOption("p")) {
				NoM = new Integer(cmd.getOptionValue("p"));
				bf = new BF(boblength+alicelength, (int) (NoM*totalSize));
				bf2 = new BF(boblength+alicelength, (int) (NoM*totalSize));
			}
			else  {
				bf = new BF(boblength+alicelength, parameterChoose(totalSize));
				bf2 = new BF(boblength+alicelength, parameterChoose(totalSize));
			}

			for(int i = 0; i < bf.k; ++i)
				bf.sks[i] = gen.channel.readBytes(bf.sks[i].length);

			bf2.sks = bf.sks;


			for(SNPEntry e : data) {
				for(int i = 0; i < e.value.length(); ++i)
					bf.insert(e.Pos(i));
			}

			for(SNPEntry e : data) {
				for(int i = 0; i < e.value.length(); ++i)
					bf2.insert(e.PosVal(i));
			}
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

			res2 = lib.zeros(32);
			for(int i = 0; i < bf2.bs.length; i+=Flag.OTBlockSize) {
				int len = Math.min(bf2.bs.length, i+Flag.OTBlockSize);
				aliceBF = gen.inputOfAlice(Arrays.copyOfRange(bf2.bs, i, len));
				bobBF =  gen.inputOfBob(Arrays.copyOfRange(bf2.bs, i, len));
				T[] tmp = null;
				if(automated)
					tmp = computeAuto(gen, lib.padSignal(aliceBF, nextPower(aliceBF.length)), lib.padSignal(bobBF, nextPower(bobBF.length)));
				else tmp = compute(gen, aliceBF, bobBF);
				res2 = lib.add(res2, lib.padSignal(tmp, 32));
			}


			//			IntegerLib<T> lib = new IntegerLib<T>(gen);
			T[] m_X = lib.sub(lib.toSignals(bf.m, res.length), res);
			T[] m_Y = lib.sub(lib.toSignals(bf.m, res2.length), res2);
			int length = 2*Math.max(m_X.length, m_Y.length)+2;
			res = lib.multiply(lib.padSignal(m_X, length), lib.padSignal(m_Y, length));
		}

		@Override
		public void prepareOutput(CompEnv<T> gen) {
			gen.outputToAlice(res);
		}
	}
}