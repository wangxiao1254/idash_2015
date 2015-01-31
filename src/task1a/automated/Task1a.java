package task1a.automated;

import flexsc.CompEnv;
import gc.GCSignal;
import task1a.PrepareData;
import task1a.Statistics;
import util.EvaRunnable;
import util.GenRunnable;
import util.Utils;
import circuits.arithmetic.IntegerLib;

public class Task1a {
	static public int Width = 9;
	
	public static GCSignal[][] compute(CompEnv<GCSignal> env, IntegerLib<GCSignal> lib,
			GCSignal[][] alice,
			GCSignal[][] bob,
			int numberOfSta) {
		Task1aAutomated a;
		GCSignal[][] ret = new GCSignal[numberOfSta][0];
		try {
			a = new Task1aAutomated(env, Width);
			a.funct(alice, bob, ret, 400, numberOfSta); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	public static class Generator extends GenRunnable<GCSignal> {
		IntegerLib<GCSignal> lib;
		GCSignal[][] alice;
		GCSignal[][] bob;
		int numberOfSta;
		@Override
		public void prepareInput(CompEnv<GCSignal> env) {
			lib = new IntegerLib<GCSignal>(env);
			Statistics[] sta = PrepareData.readFile(args[0], 0);
			boolean[][] input = new boolean[sta.length][Width];
			int current = 0;
			for(int i = 0; i < sta.length; ++i) {
				input[i]= Utils.fromInt(sta[i].numOfG1, Width);
			}
			numberOfSta = sta.length;
			alice = env.inputOfAlice(input);
			bob = env.inputOfBob(input);
		}

		GCSignal[][] res;
		@Override
		public void secureCompute(CompEnv<GCSignal> env) {
			res = compute(env, lib, alice, bob, numberOfSta);
		}

		@Override
		public void prepareOutput(CompEnv<GCSignal> env) {
			// TODO Auto-generated method stub
			for(int i = 0; i < numberOfSta; ++i) {
				int out = Utils.toInt(env.outputToAlice(res[i]));
				System.out.println(i+" "+out/400.0);
			}
		}
	}

	public static class Evaluator extends EvaRunnable<GCSignal> {
		IntegerLib<GCSignal> lib;
		GCSignal[][] alice;
		GCSignal[][] bob;
		int numberOfSta;
		@Override
		public void prepareInput(CompEnv<GCSignal> env) {
			lib = new IntegerLib<GCSignal>(env);
			Statistics[] sta = PrepareData.readFile(args[0], 1);
			boolean[][] input = new boolean[sta.length][Width];
			
			for(int i = 0; i < sta.length; ++i) {
				input[i]= Utils.fromInt(sta[i].numOfG1, Width);
			}
			numberOfSta = sta.length;
			alice = env.inputOfAlice(input);
			bob = env.inputOfBob(input);
		}

		GCSignal[][] res;
		@Override
		public void secureCompute(CompEnv<GCSignal> env) {
			res = compute(env, lib, alice, bob, numberOfSta);
		}

		@Override
		public void prepareOutput(CompEnv<GCSignal> env) {
			for(int i = 0; i < numberOfSta; ++i) {
				env.outputToAlice(res[i]);
			}
		}
	}
}
