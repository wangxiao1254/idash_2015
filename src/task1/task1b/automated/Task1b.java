package task1.task1b.automated;

import java.nio.ByteBuffer;

import network.Server;

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

		Task1bAutomated<T> alib = new Task1bAutomated<T>(env);
		return alib.func(aliceCase, aliceControl, bobCase, bobControl, numOfTests);

	}
	public static class Generator<T> extends GenRunnable<T> {
		T[][][] aliceCase;
		T[][][] bobCase;
		T[][][] aliceControl;
		T[][][] bobControl;

		int numOfTests;
		double extraFactor;
		boolean precise;
		@Override
		public void prepareInput(CompEnv<T> env) throws Exception {
			Options options = new Options();
			options.addOption("h", false, "high precision");
			options.addOption("s", "case", true, "case");
			options.addOption("t", "control", true, "control");

			CommandLineParser parser = new BasicParser();
			CommandLine cmd = parser.parse(options, args);

			precise = cmd.hasOption("h");
			if(!cmd.hasOption("s") || !cmd.hasOption("t")) {
				throw new Exception("wrong input");
			}
			StatisticsData caseInput = PrepareData.readFile(cmd.getOptionValue("s"));
			StatisticsData controlInput = PrepareData.readFile(cmd.getOptionValue("t"));
			Statistics[] caseSta = caseInput.data;
			Statistics[] controlSta = controlInput.data;
			boolean[][][] caseData = new boolean[caseSta.length][2][Width];

			int caseLength = ByteBuffer.wrap(Server.readBytes(env.is, 4)).getInt();
			int controlLength = ByteBuffer.wrap(Server.readBytes(env.is, 4)).getInt();

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
			res = compute(env, aliceCase, bobCase, aliceControl, bobControl, numOfTests, precise);
		}

		@Override
		public void prepareOutput(CompEnv<T> env) {
			FloatLib<T> flib = getfloatLib(env, precise, true);
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
		boolean precise;
		@Override
		public void prepareInput(CompEnv<T> env) throws Exception {
			Options options = new Options();
			options.addOption("h", "high_precision", false, "high precision");
			options.addOption("s", "case", true, "case");
			options.addOption("t", "control", true, "control");

			CommandLineParser parser = new BasicParser();
			CommandLine cmd = parser.parse(options, args);

			precise = cmd.hasOption("h");
			if(!cmd.hasOption("s") || !cmd.hasOption("t")) {
				throw new Exception("wrong input");
			}

			StatisticsData caseInput = PrepareData.readFile(cmd.getOptionValue("s"));
			StatisticsData controlInput = PrepareData.readFile(cmd.getOptionValue("t"));
			Statistics[] caseSta = caseInput.data;
			Statistics[] controlSta = controlInput.data;
			boolean[][][] caseData = new boolean[caseSta.length][2][Width];

			env.os.write(ByteBuffer.allocate(4).putInt(caseInput.numberOftuples).array());
			env.os.write(ByteBuffer.allocate(4).putInt(controlInput.numberOftuples).array());
			env.os.flush();

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
			res = compute(env, aliceCase, bobCase, aliceControl, bobControl, numOfTests, precise);
		}

		@Override
		public void prepareOutput(CompEnv<T> env) {
			FloatLib<T> flib = getfloatLib(env, precise, true);
			for(int i = 0; i < numOfTests; ++i)
				flib.outputToAlice(res[i]);
		}
	}
}
