package task1a;

import java.util.Arrays;

import util.EvaRunnable;
import util.GenRunnable;
import util.Utils;
import circuits.arithmetic.IntegerLib;
import flexsc.CompEnv;

public class Task1a {
	static public int Width = 9;
	
	public static<T> void compute(CompEnv<T> env, IntegerLib<T> lib,
			T[] alice,
			T[] bob,
			int numberOfSta, T[][] res) {
		T[] half = lib.toSignals(200, Width);
		T[] all = lib.toSignals(400, Width);
		res = env.newTArray(numberOfSta, 0);
		for(int i = 0; i < numberOfSta; ++i) {
			T[] aliceInput = Arrays.copyOfRange(alice, i*Width, i*Width+Width);
			T[] bobInput = Arrays.copyOfRange(bob, i*Width, i*Width+Width);
			T[] freOfG1 = lib.add(aliceInput, bobInput);
			T g1IsMinod = lib.leq(freOfG1, half);
			res[i] = lib.mux(lib.sub(all, freOfG1), freOfG1, g1IsMinod);
		}
	}
	public static class Generator<T> extends GenRunnable<T> {
		IntegerLib<T> lib;
		T[] alice;
		T[] bob;
		int numberOfSta;
		@Override
		public void prepareInput(CompEnv<T> env) {
			lib = new IntegerLib<T>(env);
			Statistics[] sta = PrepareData.readFile(args[0], 0);
			boolean[] input = new boolean[sta.length*Width];
			int current = 0;
			for(int i = 0; i < sta.length; ++i) {
				boolean[] g1 = Utils.fromInt(sta[i].numOfG1, Width);
				System.arraycopy(g1, 0, input, current, Width);
				current += Width;
			}
			numberOfSta = sta.length;
			alice = env.inputOfAlice(input);
			bob = env.inputOfBob(input);
		}

		T[][] res;
		@Override
		public void secureCompute(CompEnv<T> env) {
			compute(env, lib, alice, bob, numberOfSta, res);
		}

		@Override
		public void prepareOutput(CompEnv<T> env) {
			// TODO Auto-generated method stub
			for(int i = 0; i < numberOfSta; ++i) {
				int out = Utils.toInt(env.outputToAlice(res[i]));
				System.out.println(i+" "+out/400.0);
			}
		}
	}

	public static class Evaluator<T> extends EvaRunnable<T> {
		IntegerLib<T> lib;
		T[] alice;
		T[] bob;
		int numberOfSta;
		@Override
		public void prepareInput(CompEnv<T> env) {
			lib = new IntegerLib<T>(env);
			Statistics[] sta = PrepareData.readFile(args[0], 1);
			boolean[] input = new boolean[sta.length*Width];
			int current = 0;
			for(int i = 0; i <sta.length; ++i) {
				boolean[] g1 = Utils.fromInt(sta[i].numOfG1, Width);
				System.arraycopy(g1, 0, input, current, Width);
				current += Width;
			}
			numberOfSta = sta.length;
			alice = env.inputOfAlice(input);
			bob = env.inputOfBob(input);
		}

		T[][] res;
		@Override
		public void secureCompute(CompEnv<T> env) {
			compute(env, lib, alice, bob, numberOfSta, res);
		}

		@Override
		public void prepareOutput(CompEnv<T> env) {
			for(int i = 0; i < numberOfSta; ++i) {
				env.outputToAlice(res[i]);
			}
		}
	}
}
