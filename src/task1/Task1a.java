package task1;

import java.util.Arrays;

import util.Utils;
import circuits.arithmetic.IntegerLib;
import flexsc.CompEnv;
import flexsc.Mode;
import flexsc.Party;

public class Task1a {
	static public Mode m = Mode.REAL;
	static public int Width = 9;

	public static class GenRunnable<T> extends network.Server implements Runnable {
		boolean[] z;
		String filename;
		public GenRunnable(String filename) {
			this.filename = filename;
		}
		public void run() {
			try {
				listen(54321);
				@SuppressWarnings("unchecked")
				CompEnv<T> env = CompEnv.getEnv(m, Party.Alice, is, os);
				IntegerLib<T> lib = new IntegerLib<T>(env);

				Statistics[] sta = PrepareData.readFile(filename, 0);
				boolean[] input = new boolean[sta.length*Width];
				int current = 0;
				for(int i = 0; i <sta.length; ++i) {
					boolean[] g1 = Utils.fromInt(sta[i].numOfG1, Width);
					System.arraycopy(g1, 0, input, current, Width);
					current += Width;
				}
				T[] alice = env.inputOfAlice(input);
				T[] bob = env.inputOfBob(input);
				T[] half = lib.toSignals(200, Width);
				T[] all = lib.toSignals(400, Width);
				for(int i = 0; i < sta.length; ++i) {
					T[] aliceInput = Arrays.copyOfRange(alice, i*Width, i*Width+Width);
					T[] bobInput = Arrays.copyOfRange(bob, i*Width, i*Width+Width);
					T[] freOfG1 = lib.add(aliceInput, bobInput);
					T g1IsMinod = lib.leq(freOfG1, half);
					T[] res = lib.mux(lib.sub(all, freOfG1), freOfG1, g1IsMinod);
					int out = Utils.toInt(env.outputToAlice(res));
					System.out.println(i+" "+out/400.0);
				}

				disconnect();
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

	public static class EvaRunnable<T> extends network.Client implements Runnable {
		String filename;
		public EvaRunnable(String filename) {
			this.filename = filename;
		}

		public void run() {
			try {
				connect("localhost", 54321);
				@SuppressWarnings("unchecked")
				CompEnv<T> env = CompEnv.getEnv(m, Party.Bob, is, os);

				IntegerLib<T> lib = new IntegerLib<T>(env);

				Statistics[] sta = PrepareData.readFile("data/case_chr2_29504091_30044866.txt", 1);
				// Statistics[] sta = PrepareData.readFile("/home/kartik/code/idash_competition/data/case_chr2_29504091_30044866.txt", 1);
				boolean[] input = new boolean[sta.length*Width];
				int current = 0;
				for(int i = 0; i <sta.length; ++i) {
					boolean[] g1 = Utils.fromInt(sta[i].numOfG1, Width);
					System.arraycopy(g1, 0, input, current, Width);
					current += Width;
				}
				T[] alice = env.inputOfAlice(input);
				T[] bob = env.inputOfBob(input);
				T[] half = lib.toSignals(200, Width);
				T[] all = lib.toSignals(400, Width);
				for(int i = 0; i < sta.length; ++i) {
					T[] aliceInput = Arrays.copyOfRange(alice, i*Width, i*Width+Width);
					T[] bobInput = Arrays.copyOfRange(bob, i*Width, i*Width+Width);
					T[] freOfG1 = lib.add(aliceInput, bobInput);
					T g1IsMinod = lib.leq(freOfG1, half);
					T[] res = lib.mux(lib.sub(all, freOfG1), freOfG1, g1IsMinod);
					env.outputToAlice(res);
					//					System.out.println("\n"+i+" "+sta[i].numOfG1);
				}

				disconnect();
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

	static public <T>void runThreads(String filename) throws Exception {
		GenRunnable<T> gen = new GenRunnable<T>(filename);
		EvaRunnable<T> env = new EvaRunnable<T>(filename);
		Thread tGen = new Thread(gen);
		Thread tEva = new Thread(env);
		tGen.start();
		Thread.sleep(5);
		tEva.start();
		tGen.join();
		tEva.join();

		if (m == Mode.COUNT) {
			//			System.out.println(env.andgates + " " + env.encs);
		} else {
		}
		// System.out.println(Arrays.toString(gen.z));
	}

	static public void main(String[] args) throws Exception {
		runThreads(args[0]);
//		runThreads("/home/kartik/code/idash_competition/data/control_chr2_29504091_30044866.txt");
	}

}