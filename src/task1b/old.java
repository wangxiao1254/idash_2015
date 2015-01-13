//package task1b;
//
//import java.util.Arrays;
//
//import util.Utils;
//import circuits.arithmetic.IntegerLib;
//import flexsc.CompEnv;
//import flexsc.Mode;
//import flexsc.Party;
//
//public class old {
//	static public Mode m = Mode.REAL;
//
//	public static class GenRunnable<T> extends network.Server implements Runnable {
//		String filename1;
//		String filename2;
//		public GenRunnable(String filename1, String filename2) {
//			this.filename1 = filename1;
//			this.filename2 = filename2;
//		}
//		
//		public void run() {
//			try {
//				listen(54321);
//				@SuppressWarnings("unchecked")
//				CompEnv<T> env = CompEnv.getEnv(m, Party.Alice, is, os);
//				IntegerLib<T> lib = new IntegerLib<T>(env);
//
//				
//				T[] alice = env.inputOfAlice(input);
//				T[] bob = env.inputOfBob(input);
//				T[] all = lib.toSignals(400, Width);
//				current = 0;
//				for(int i = 0; i < caseSta.length; ++i) {
//					T[] aliceInput = Arrays.copyOfRange(alice, current, current+Width);
//					T[] bobInput = Arrays.copyOfRange(bob, current, current+Width);
//					current += Width;
//					T[] a = lib.add(aliceInput, bobInput);
//					T[] b = lib.sub(all, a);
//					aliceInput = Arrays.copyOfRange(alice, current, current+Width);
//					bobInput = Arrays.copyOfRange(bob, current, current+Width);
//					current += Width;
//					T[] c = lib.add(aliceInput, bobInput);
//					T[] d = lib.sub(all, a);
//					//compute n - 2*a*c/(a+c) - 2*b*d/(b+d)s 
//				}
//
//				disconnect();
//			} catch (Exception e) {
//				e.printStackTrace();
//				System.exit(1);
//			}
//		}
//	}
//
//	public static class EvaRunnable<T> extends network.Client implements Runnable {
//		String filename1;
//		String filename2;
//		public EvaRunnable(String filename1, String filename2) {
//			this.filename1 = filename1;
//			this.filename2 = filename2;
//		}
//		public void run() {
//			try {
//				connect("localhost", 54321);
//				@SuppressWarnings("unchecked")
//				CompEnv<T> env = CompEnv.getEnv(m, Party.Bob, is, os);
//
//				IntegerLib<T> lib = new IntegerLib<T>(env);
//
//				Statistics[] caseSta = PrepareData.readFile(filename1, 0);
//				Statistics[] controlSta = PrepareData.readFile(filename1, 1);
//				boolean[] input = new boolean[caseSta.length*Width*2];
//				int current = 0;
//				for(int i = 0; i <caseSta.length; ++i) {
//					boolean[] g1 = Utils.fromInt(caseSta[i].numOfG1, Width);
//					System.arraycopy(g1, 0, input, current, Width);
//					current += Width;
//					
//					g1 = Utils.fromInt(controlSta[i].numOfG1, Width);
//					System.arraycopy(g1, 0, input, current, Width);
//					current += Width;
//				}
//				T[] alice = env.inputOfAlice(input);
//				T[] bob = env.inputOfBob(input);
//				T[] all = lib.toSignals(400, Width);
//				current = 0;
//				for(int i = 0; i < caseSta.length; ++i) {
//					T[] aliceInput = Arrays.copyOfRange(alice, current, current+Width);
//					T[] bobInput = Arrays.copyOfRange(bob, current, current+Width);
//					current += Width;
//					T[] a = lib.add(aliceInput, bobInput);
//					T[] b = lib.sub(all, a);
//					aliceInput = Arrays.copyOfRange(alice, current, current+Width);
//					bobInput = Arrays.copyOfRange(bob, current, current+Width);
//					current += Width;
//					T[] c = lib.add(aliceInput, bobInput);
//					T[] d = lib.sub(all, a);
//					
//				}
//
//				disconnect();
//			} catch (Exception e) {
//				e.printStackTrace();
//				System.exit(1);
//			}
//		}
//	}
//
//	static public <T>void runThreads(String filename1, String filename2) throws Exception {
//		GenRunnable<T> gen = new GenRunnable<T>(filename1, filename2);
//		EvaRunnable<T> env = new EvaRunnable<T>(filename1, filename2);
//		Thread tGen = new Thread(gen);
//		Thread tEva = new Thread(env);
//		tGen.start();
//		Thread.sleep(5);
//		tEva.start();
//		tGen.join();
//		tEva.join();
//
//		if (m == Mode.COUNT) {
//			//			System.out.println(env.andgates + " " + env.encs);
//		} else {
//		}
//		// System.out.println(Arrays.toString(gen.z));
//	}
//
//	static public void main(String[] args) throws Exception {
//		runThreads(args[0], args[1]);
//	}
//
//}