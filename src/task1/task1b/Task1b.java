package task1.task1b;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;

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

	public static<T> FloatLib<T> getfloatLib(CompEnv<T> env, boolean precise, boolean automated){
		if(automated)
			return new FloatLib<T>(env, 24, 8);
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
		FloatLib<T> flib = getfloatLib(env, precise, false);
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

	public static<T> T[][] computeAuto(CompEnv<T> env, T[][][] aliceCase,
			T[][][] bobCase,
			T[][][] aliceControl,
			T[][][] bobControl, int numOfTests, boolean precise) throws Exception {
		IntegerLib<T> lib = new IntegerLib<T>(env);
		FloatLib<T> flib = getfloatLib(env, precise, true);
		for(int i = 0; i < aliceCase.length; ++i)
			for(int j = 0; j < aliceCase[i].length; ++j)
				aliceCase[i][j] = lib.toSecureFloat(aliceCase[i][j], flib);

		for(int i = 0; i < bobCase.length; ++i)
			for(int j = 0; j < bobCase[i].length; ++j)
				bobCase[i][j] = lib.toSecureFloat(bobCase[i][j], flib);

		for(int i = 0; i < aliceControl.length; ++i)
			for(int j = 0; j < aliceControl[i].length; ++j) 
				aliceControl[i][j] = lib.toSecureFloat(aliceControl[i][j], flib);

		for(int i = 0; i < bobControl.length; ++i)
			for(int j = 0; j < bobControl[i].length; ++j)
				bobControl[i][j] = lib.toSecureFloat(bobControl[i][j], flib);

		Task1bAutomated<T> alib = new Task1bAutomated<T>(env, numOfTests);

		return alib.func(aliceCase, aliceControl, bobCase, bobControl);
	}


	static CommandLine processArgs(String[] args) throws Exception {
		Options options = new Options();
		options.addOption("h", false, "automated");
		options.addOption("a", false, "automated");
		options.addOption("c", "case", true, "case");
		options.addOption("t", "control", true, "control");

		CommandLineParser parser = new BasicParser();
		CommandLine cmd = parser.parse(options, args);

		if(!cmd.hasOption("c") || !cmd.hasOption("t")) {
			throw new Exception("wrong input");
		}
		if(cmd.hasOption("a"))
			System.out.println("Running the program with automatically generated circuits");
		else
			System.out.println("Running the program with manually generated circuits");
		return cmd;
	}

	public static class Generator<T> extends GenRunnable<T> {
		T[][][] aliceCase;
		T[][][] bobCase;
		T[][][] aliceControl;
		T[][][] bobControl;

		int numOfTests;
		double extraFactor;
		boolean precise, automated;
		@Override
		public void prepareInput(CompEnv<T> env) throws Exception {
			CommandLine cmd = processArgs(args);
			precise = cmd.hasOption("h");
			automated = cmd.hasOption("a");
			StatisticsData caseInput = PrepareData.readFile(cmd.getOptionValue("c"));
			StatisticsData controlInput = PrepareData.readFile(cmd.getOptionValue("t"));

			Statistics[] caseSta = caseInput.data;
			Statistics[] controlSta = controlInput.data;
			boolean[][][] caseData = new boolean[caseSta.length][2][Width];

			int caseLength = env.channel.readInt();
			int controlLength = env.channel.readInt();

			//extraFactor = n/(r*s)

			extraFactor = 2*(caseLength + controlLength + caseInput.numberOftuples+ controlInput.numberOftuples);
			extraFactor /= 2*(caseLength + caseInput.numberOftuples);
			extraFactor /= 2*(controlLength + controlInput.numberOftuples);

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
		public void secureCompute(CompEnv<T> env) throws Exception {
			if(automated)
				res = computeAuto(env, aliceCase, bobCase, aliceControl, bobControl, numOfTests, precise);
			else
				res = compute(env, aliceCase, bobCase, aliceControl, bobControl, numOfTests, precise);
		}

		@Override
		public void prepareOutput(CompEnv<T> env) {
			FloatLib<T> flib = getfloatLib(env, precise, automated);
			for(int i = 0; i < res.length; ++i)
				System.out.println(flib.outputToAlice(res[i])*extraFactor);
		}

	}

	public static class Evaluator<T> extends EvaRunnable<T> {
		T[][][] aliceCase;
		T[][][] bobCase;
		T[][][] aliceControl;
		T[][][] bobControl;
		int numOfTests;
		boolean precise, automated;
		@Override
		public void prepareInput(CompEnv<T> env) throws Exception {
			CommandLine cmd = processArgs(args);
			precise = cmd.hasOption("h");
			automated = cmd.hasOption("a");
			StatisticsData caseInput = PrepareData.readFile(cmd.getOptionValue("c"));
			StatisticsData controlInput = PrepareData.readFile(cmd.getOptionValue("t"));

			Statistics[] caseSta = caseInput.data;
			Statistics[] controlSta = controlInput.data;
			boolean[][][] caseData = new boolean[caseSta.length][2][Width];

			env.channel.writeInt(caseInput.numberOftuples);
			env.channel.writeInt(controlInput.numberOftuples);
			env.channel.flush();

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
		public void secureCompute(CompEnv<T> env) throws Exception {
			if(automated)
				res = computeAuto(env, aliceCase, bobCase, aliceControl, bobControl, numOfTests, precise);
			else
				res = compute(env, aliceCase, bobCase, aliceControl, bobControl, numOfTests, precise);
		}

		@Override
		public void prepareOutput(CompEnv<T> env) {
			FloatLib<T> flib = getfloatLib(env, precise, automated);
			for(int i = 0; i < numOfTests; ++i)
				flib.outputToAlice(res[i]);
		}
	}
}
