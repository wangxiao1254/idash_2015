package task1.task1b.std;

import java.io.IOException;
import java.nio.ByteBuffer;

import network.Server;
import task1.PrepareData;
import task1.PrepareData.StatisticsData;
import task1.Statistics;
import util.EvaRunnable;
import util.GenRunnable;
import util.Utils;
import circuits.arithmetic.FloatLib;
import circuits.arithmetic.IntegerLib;
import flexsc.CompEnv;

public class Task1b {
	static public int Width = 12;

	public static<T> FloatLib<T> getfloatLib(CompEnv<T> env, boolean precise){
		if(precise)
			return new FloatLib<T>(env, 30, 10);
		else return new FloatLib<T>(env, 20, 10);
	}
	public static<T> T[][] compute(CompEnv<T> env, T[][][] aliceCase,
			T[][][] bobCase,
			T[][][] aliceControl,
			T[][][] bobControl, int numOfTests, boolean precise) {
		T[][] res = env.newTArray(numOfTests, 0);
		IntegerLib<T> lib = new IntegerLib<T>(env);
		FloatLib<T> flib = getfloatLib(env, precise);
		for(int i = 0; i < numOfTests; ++i) {
			T[] a = lib.add(aliceCase[i][0], bobCase[i][0]);
			T[] b = lib.add(aliceCase[i][1], bobCase[i][1]);
			T[] c = lib.add(aliceControl[i][0], bobControl[i][0]);
			T[] d = lib.add(aliceControl[i][1], bobControl[i][1]);
			T[] g = lib.add(a, c);
			T[] k = lib.add(b, d);
						
			T[] fa = lib.toSecureFloat(a, flib);
			T[] fb = lib.toSecureFloat(b, flib);
			T[] fc = lib.toSecureFloat(c, flib);
			T[] fd = lib.toSecureFloat(d, flib);
			T[] fg = lib.toSecureFloat(g, flib);
			T[] fk = lib.toSecureFloat(k, flib);
						
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
		boolean precise;
		@Override
		public void prepareInput(CompEnv<T> env) {
			if(args[0].equals("pre"))
				precise = true;
			else precise = false;
			StatisticsData caseInput = PrepareData.readFile(args[1]);
			StatisticsData controlInput = PrepareData.readFile(args[2]);
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
			res = compute(env, aliceCase, bobCase, aliceControl, bobControl, numOfTests, precise);
		}

		@Override
		public void prepareOutput(CompEnv<T> env) {
			FloatLib<T> flib = getfloatLib(env, precise);
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
		boolean precise;
		@Override
		public void prepareInput(CompEnv<T> env) {
			if(args[0].equals("pre"))
				precise = true;
			else precise = false;
			
			StatisticsData caseInput = PrepareData.readFile(args[1]);
			StatisticsData controlInput = PrepareData.readFile(args[2]);
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
			res = compute(env, aliceCase, bobCase, aliceControl, bobControl, numOfTests, precise);
		}

		@Override
		public void prepareOutput(CompEnv<T> env) {
			FloatLib<T> flib = getfloatLib(env, precise);
			for(int i = 0; i < numOfTests; ++i)
				flib.outputToAlice(res[i]);
		}
	}
}
