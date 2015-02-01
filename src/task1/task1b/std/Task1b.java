package task1.task1b.std;

//import task1a.PrepareData.StatisticsData;
import java.io.IOException;
import java.nio.ByteBuffer;

import network.Server;
import task1.PrepareData;
import task1.PrepareData.StatisticsData;
import task1.Statistics;
import util.EvaRunnable;
import util.GenRunnable;
import util.Utils;
import circuits.arithmetic.FixedPointLib;
import circuits.arithmetic.IntegerLib;
import flexsc.CompEnv;

public class Task1b {
	static public int Width = 11;
	static public int FWidth = 60;
	static public int FOffset = 20;

	public static<T> T[][] compute(CompEnv<T> env, T[][][] aliceCase,
			T[][][] bobCase,
			T[][][] aliceControl,
			T[][][] bobControl, int numOfTests, int numberOfSta) {
		T[][] res = env.newTArray(numOfTests, 0);
		IntegerLib<T> lib = new IntegerLib<T>(env);
		FixedPointLib<T> flib = new FixedPointLib<T>(env, FWidth, FOffset);
		for(int i = 0; i < numOfTests; ++i) {
			
			T[] a = lib.add(aliceCase[i][0], bobCase[i][0]);
			T[] b = lib.add(aliceCase[i][1], bobCase[i][1]);
			T[] c = lib.add(aliceControl[i][0], bobControl[i][0]);
			T[] d = lib.add(aliceControl[i][1], bobControl[i][1]);
			T[] g = lib.add(a, c);
			T[] k = lib.add(b, d);
						
			T[] fa = lib.leftPublicShift(lib.padSignal(a, FWidth), FOffset);
			T[] fb = lib.leftPublicShift(lib.padSignal(b, FWidth), FOffset);
			T[] fc = lib.leftPublicShift(lib.padSignal(c, FWidth), FOffset);
			T[] fd = lib.leftPublicShift(lib.padSignal(d, FWidth), FOffset);
			T[] fg = lib.leftPublicShift(lib.padSignal(g, FWidth), FOffset);
			T[] fk = lib.leftPublicShift(lib.padSignal(k, FWidth), FOffset);
			
//			if(env.getParty() == Party.Alice)
//			System.out.println(flib.outputToAlice(fa)+" "+flib.outputToAlice(fb)+" "+flib.outputToAlice(fc)
//					      +" "+flib.outputToAlice(fd)+" "+flib.outputToAlice(fg)+" "+flib.outputToAlice(fk));
//			else {flib.outputToAlice(fa);flib.outputToAlice(fb);flib.outputToAlice(fc);flib.outputToAlice(fd);flib.outputToAlice(fg);flib.outputToAlice(fk);}
			
			T[] tmp = flib.sub(flib.multiply(fa, fd), flib.multiply(fb, fc));
			tmp = flib.multiply(tmp, tmp);
			
			res[i] = flib.div(tmp, flib.multiply(fg, fk));
		}
		return res;

	}
	public static class Generator<T> extends GenRunnable<T> {
		T[][][] aliceCase;
		T[][][] bobCase;
		T[][][] aliceControl;
		T[][][] bobControl;

		int numOfTests;
		int numberOfSta;
		@Override
		public void prepareInput(CompEnv<T> env) {
			StatisticsData caseInput = PrepareData.readFile(args[0]);
			StatisticsData controlInput = PrepareData.readFile(args[1]);
			Statistics[] caseSta = caseInput.data;
			Statistics[] controlSta = controlInput.data;
			boolean[][][] caseData = new boolean[caseSta.length][2][Width];
			int boblength = 0;
			try {
				env.os.write(ByteBuffer.allocate(4).putInt(caseInput.numberOftuples).array());
				env.os.flush();
				boblength = ByteBuffer.wrap(Server.readBytes(env.is, 4)).getInt();
			} catch (IOException e) {
				// TODO Auto-enverated catch block
				e.printStackTrace();
			}
			numberOfSta = 2*(boblength + caseInput.numberOftuples);
			for(int i = 0; i < caseSta.length; ++i) {
				caseData[i][0] = Utils.fromInt(caseSta[i].numOfG1, Width);
				caseData[i][1] = Utils.fromInt(caseSta[i].numOfG2, Width);
			}

			boolean[][][] controlData = new boolean[controlSta.length][2][Width];
			for(int i = 0; i < caseSta.length; ++i) {
				controlData[i][0] = Utils.fromInt(controlSta[i].numOfG1, Width);
				controlData[i][1] = Utils.fromInt(controlSta[i].numOfG2, Width);
			}
			aliceCase = env.inputOfAlice(caseData);
			aliceControl = env.inputOfAlice(controlData);
			bobCase = env.inputOfBob(caseData);
			bobControl = env.inputOfBob(controlData);
			numOfTests = caseSta.length;
		}

		T[][] res;
		@Override
		public void secureCompute(CompEnv<T> env) {
			res = compute(env, aliceCase, bobCase, aliceControl, bobControl, numOfTests, numberOfSta);
		}

		@Override
		public void prepareOutput(CompEnv<T> env) {
			FixedPointLib<T> flib = new FixedPointLib<T>(env, FWidth, FOffset);
			for(int i = 0; i < res.length; ++i)
				System.out.println(flib.outputToAlice(res[i])/200.0);
		}

	}

	public static class Evaluator<T> extends EvaRunnable<T> {
		T[][][] aliceCase;
		T[][][] bobCase;
		T[][][] aliceControl;
		T[][][] bobControl;
		int numOfTests;
		int numberOfSta;
		@Override
		public void prepareInput(CompEnv<T> env) {
			StatisticsData caseInput = PrepareData.readFile(args[0]);
			StatisticsData controlInput = PrepareData.readFile(args[1]);
			Statistics[] caseSta = caseInput.data;
			Statistics[] controlSta = controlInput.data;
			boolean[][][] caseData = new boolean[caseSta.length][2][Width];
			int boblength = 0;
			try {
				env.os.write(ByteBuffer.allocate(4).putInt(caseInput.numberOftuples).array());
				env.os.flush();
				boblength = ByteBuffer.wrap(Server.readBytes(env.is, 4)).getInt();
			} catch (IOException e) {
				// TODO Auto-enverated catch block
				e.printStackTrace();
			}
			numberOfSta = 2*(boblength + caseInput.numberOftuples);
			for(int i = 0; i < caseSta.length; ++i) {
				caseData[i][0] = Utils.fromInt(caseSta[i].numOfG1, Width);
				caseData[i][1] = Utils.fromInt(caseSta[i].numOfG2, Width);
			}

			boolean[][][] controlData = new boolean[controlSta.length][2][Width];
			for(int i = 0; i < caseSta.length; ++i) {
				controlData[i][0] = Utils.fromInt(controlSta[i].numOfG1, Width);
				controlData[i][1] = Utils.fromInt(controlSta[i].numOfG2, Width);
			}
			aliceCase = env.inputOfAlice(caseData);
			aliceControl = env.inputOfAlice(controlData);
			bobCase = env.inputOfBob(caseData);
			bobControl = env.inputOfBob(controlData);
			numOfTests = caseSta.length;
		}
		T[][] res;

		@Override
		public void secureCompute(CompEnv<T> env) {
			res = compute(env, aliceCase, bobCase, aliceControl, bobControl, numOfTests, numberOfSta);
		}

		@Override
		public void prepareOutput(CompEnv<T> env) {
			FixedPointLib<T> flib = new FixedPointLib<T>(env, FWidth, FOffset);
			for(int i = 0; i < numOfTests; ++i)
				flib.outputToAlice(res[i]);
		}
	}
}
