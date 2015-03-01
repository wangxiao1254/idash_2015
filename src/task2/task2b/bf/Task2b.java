package task2.task2b.bf;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashSet;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;

import network.Server;
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

			gen.os.write(ByteBuffer.allocate(4).putInt(alicelength).array());
			gen.os.flush();

			int boblength = ByteBuffer.wrap(Server.readBytes(gen.is, 4)).getInt();
			totalSize = boblength+alicelength;
			bf = new BF(boblength+alicelength, NoM*(boblength+alicelength));
			bf2 = new BF(boblength+alicelength, NoM*(boblength+alicelength));
			bf2.sks = bf.sks;
			for(int i = 0; i < bf.k; ++i)
				gen.os.write(bf.sks[i]);
			gen.os.flush();

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
			long tmp = Utils.toLong(gen.outputToAlice(res));
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
			gen.os.write(ByteBuffer.allocate(4).putInt(boblength).array());
			gen.os.flush();

			int alicelength = ByteBuffer.wrap(Server.readBytes(gen.is, 4)).getInt();


			bf = new BF(boblength+alicelength, NoM*(boblength+alicelength));
			bf2 = new BF(boblength+alicelength, NoM*(boblength+alicelength));
			for(int i = 0; i < bf.k; ++i)
				try {
					bf.sks[i] = Server.readBytes(gen.is, bf.sks[i].length);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
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