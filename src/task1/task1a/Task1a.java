package task1.task1a;

import java.io.IOException;
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
import circuits.arithmetic.IntegerLib;
import flexsc.CompEnv;

public class Task1a {
	public static<T> T[][] compute(CompEnv<T> env, T[][] alice, T[][] bob, int numberOfSta, int width) {
		IntegerLib<T> lib = new IntegerLib<>(env);
		T[] half = lib.toSignals(numberOfSta/2, width);
		T[] all = lib.toSignals(numberOfSta, width);
		T[][] res = env.newTArray(alice.length, 0);
		for(int i = 0; i < alice.length; ++i) {
			T[] freOfG1 = lib.add(alice[i], bob[i]);
			T g1IsMinod = lib.leq(freOfG1, half);
			res[i] = lib.mux(lib.sub(all, freOfG1), freOfG1, g1IsMinod);
		}
		return res;
	}
	
	public static<T> T[][] computeAutomated(CompEnv<T> env, T[][] alice, T[][] bob, int numberOfSta, int width) {
		Task1aAutomated<T> a;
		T[][] ret = env.newTArray(alice.length, 0);
		try {
			a = new Task1aAutomated<T>(env, width);
			a.funct(alice, bob, ret, numberOfSta, alice.length);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	public static class Generator<T> extends GenRunnable<T> {
		T[][] alice;
		T[][] bob;
		int numberOfSta;
		int width;
		boolean automated;
		@Override
		public void prepareInput(CompEnv<T> env) throws Exception {
			Options options = new Options();
			options.addOption("a", false, "automated");
			options.addOption("f", "case", true, "file");

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
			
			StatisticsData staData = PrepareData.readFile(cmd.getOptionValue("f"));
			Statistics[] sta = staData.data;
			int boblength = 0;
			try {
				env.os.write(ByteBuffer.allocate(4).putInt(staData.numberOftuples).array());
				env.os.flush();
				boblength = ByteBuffer.wrap(Server.readBytes(env.is, 4)).getInt();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			numberOfSta = 2*(boblength + staData.numberOftuples);
			width = (int) Math.ceil(Math.log(numberOfSta)/Math.log(2));
			
			boolean[][] input = new boolean[sta.length][width];
			for(int i = 0; i < sta.length; ++i) {
				input[i]= Utils.fromInt(sta[i].numOfG1, width);
			}
			
			alice = env.inputOfAlice(input);
			bob = env.inputOfBob(input);		}

		T[][] res;
		@Override
		public void secureCompute(CompEnv<T> env) {
			if(automated)
				res = computeAutomated(env, alice, bob, numberOfSta, width);
			else
				res = compute(env, alice, bob, numberOfSta, width);
		}

		@Override
		public void prepareOutput(CompEnv<T> env) {
			System.out.println(res.length);
			// TODO Auto-generated method stub
			for(int i = 0; i < res.length; ++i) {
				int out = Utils.toInt(env.outputToAlice(res[i]));
				System.out.println(i+" "+out/(double)numberOfSta);
			}
		}
	}


	public static class Evaluator<T> extends EvaRunnable<T> {
		T[][] alice;
		T[][] bob;
		int width;
		int numberOfSta;
		boolean automated;
		@Override
		public void prepareInput(CompEnv<T> env) throws Exception {
			Options options = new Options();
			options.addOption("a", false, "automated");
			options.addOption("f", "case", true, "file");

			CommandLineParser parser = new BasicParser();
			CommandLine cmd = parser.parse(options, args);

			automated = cmd.hasOption("a");
			if(!cmd.hasOption("f")) {
				throw new Exception("wrong input");
			}

			
			StatisticsData staData = PrepareData.readFile(cmd.getOptionValue("f"));
			Statistics[] sta = staData.data;
			int boblength = 0;
			try {
				env.os.write(ByteBuffer.allocate(4).putInt(staData.numberOftuples).array());
				env.os.flush();
				boblength = ByteBuffer.wrap(Server.readBytes(env.is, 4)).getInt();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			numberOfSta = 2*(boblength + staData.numberOftuples);
			width = (int) Math.ceil(Math.log(numberOfSta)/Math.log(2));
			
			boolean[][] input = new boolean[sta.length][width];
			for(int i = 0; i < sta.length; ++i) {
				input[i]= Utils.fromInt(sta[i].numOfG1, width);
			}
			
			alice = env.inputOfAlice(input);
			bob = env.inputOfBob(input);
		}

		T[][] res;
		@Override
		public void secureCompute(CompEnv<T> env) {
			if(automated)
				res = computeAutomated(env, alice, bob, numberOfSta, width);
			else
				res = compute(env, alice, bob, numberOfSta, width);
		}

		@Override
		public void prepareOutput(CompEnv<T> env) {
			for(int i = 0; i < res.length; ++i) {
				env.outputToAlice(res[i]);
			}
		}
	}
}
