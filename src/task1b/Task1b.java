package task1b;

import util.EvaRunnable;
import util.GenRunnable;
import util.Utils;
import circuits.arithmetic.FixedPointLib;
import circuits.arithmetic.IntegerLib;
import flexsc.CompEnv;
import flexsc.Flag;

public class Task1b {
	static public int Width = 9;
	static public int FWidth = 35;
	static public int FOffset = 20;

	public static class Generator<T> extends GenRunnable<T> {
		T[][][] aliceCase;
		T[][][] bobCase;
		T[][][] aliceControl;
		T[][][] bobControl;

		int numOfTests;
		@Override
		public void prepareInput(CompEnv<T> gen) {
			Statistics[] caseSta = PrepareData.readFile(args[0], 0);
			Statistics[] controlSta = PrepareData.readFile(args[1], 0);
			boolean[][][] caseData = new boolean[caseSta.length][3][Width];
			for(int i = 0; i < caseSta.length; ++i) {
				caseData[i][0] = Utils.fromInt(caseSta[i].numOfG1G1, Width);
				caseData[i][1] = Utils.fromInt(caseSta[i].numOfG1G2, Width);
				caseData[i][2] = Utils.fromInt(caseSta[i].numOfG2G2, Width);
			}

			boolean[][][] controlData = new boolean[controlSta.length][3][Width];
			for(int i = 0; i < caseSta.length; ++i) {
				controlData[i][0] = Utils.fromInt(controlSta[i].numOfG1G1, Width);
				controlData[i][1] = Utils.fromInt(controlSta[i].numOfG1G2, Width);
				controlData[i][2] = Utils.fromInt(controlSta[i].numOfG2G2, Width);
			}
			aliceCase = gen.inputOfAlice(caseData);
			aliceControl = gen.inputOfAlice(controlData);
			bobCase = gen.inputOfBob(caseData);
			bobControl = gen.inputOfBob(controlData);
			numOfTests = caseSta.length;
		}

		T[][] res;
		FixedPointLib<T> flib;
		@Override
		public void secureCompute(CompEnv<T> gen) {
			res = gen.newTArray(numOfTests, 0);
			IntegerLib<T> lib = new IntegerLib<T>(gen);
			flib = new FixedPointLib<T>(gen, FWidth, FOffset);
			// TODO Auto-generated method stub
			for(int i = 0; i < numOfTests; ++i) {
				T[][] caseFre = gen.newTArray(3, 0);
				T[][] controlFre = gen.newTArray(3, 0);
				for(int j = 0; j < 3; ++j) {
					caseFre[j] = lib.add(aliceCase[i][j], bobCase[i][j]);
					controlFre[j] = lib.add(aliceControl[i][j], bobControl[i][j]);
				}

				T[] resCase = flib.publicValue(0);
				T[] resControl = flib.publicValue(0);
				for(int k = 0; k < 3; ++k) {
					T[] sq = lib.multiplyFull(caseFre[k], caseFre[k]);
					T[] sq2 = lib.multiplyFull(controlFre[k], controlFre[k]);
					
					T[] sum = lib.add(caseFre[k], controlFre[k]);
					//compute sum_i sq_i/sum_i
					T[] fsq = lib.padSignal(sq, FWidth);
					fsq = lib.leftPublicShift(fsq, FOffset);
					T[] fsq2 = lib.padSignal(sq2, FWidth);
					fsq2 = lib.leftPublicShift(fsq2, FOffset);
					T[] fsum = lib.padSignal(sum, FWidth);
					fsum= lib.leftPublicShift(fsum, FOffset);					
					T[] div = flib.div(fsq, fsum);
					resCase = flib.add(resCase, div);

					T[] div2 = flib.div(fsq2, fsum);
					resControl = flib.add(resControl, div2);
				}
				resCase = flib.multiply(resCase, flib.publicValue(1.0/200));
				resControl = flib.multiply(resControl, flib.publicValue(1.0/200));
				res[i] = flib.add(resCase, resControl);
				System.out.println(i+" "+Flag.sw.ands);
			}
		}

		@Override
		public void prepareOutput(CompEnv<T> gen) {
			System.out.println(numOfTests);
			for(int i = 0; i < numOfTests; ++i)
				System.out.println(flib.outputToAlice(res[i]));
		}

	}

	public static class Evaluator<T> extends EvaRunnable<T> {

		T[][][] aliceCase;
		T[][][] bobCase;
		T[][][] aliceControl;
		T[][][] bobControl;
		FixedPointLib<T> flib;
		int numOfTests;
		@Override
		public void prepareInput(CompEnv<T> gen) {
			Statistics[] caseSta = PrepareData.readFile(args[0], 1);
			Statistics[] controlSta = PrepareData.readFile(args[1], 1);
			boolean[][][] caseData = new boolean[caseSta.length][3][Width];
			for(int i = 0; i < caseSta.length; ++i) {
				caseData[i][0] = Utils.fromInt(caseSta[i].numOfG1G1, Width);
				caseData[i][1] = Utils.fromInt(caseSta[i].numOfG1G2, Width);
				caseData[i][2] = Utils.fromInt(caseSta[i].numOfG2G2, Width);
			}

			boolean[][][] controlData = new boolean[controlSta.length][3][Width];
			for(int i = 0; i < caseSta.length; ++i) {
				controlData[i][0] = Utils.fromInt(controlSta[i].numOfG1G1, Width);
				controlData[i][1] = Utils.fromInt(controlSta[i].numOfG1G2, Width);
				controlData[i][2] = Utils.fromInt(controlSta[i].numOfG2G2, Width);
			}


			aliceCase = gen.inputOfAlice(caseData);
			aliceControl = gen.inputOfAlice(controlData);
			bobCase = gen.inputOfBob(caseData);
			bobControl = gen.inputOfBob(controlData);
			numOfTests = caseSta.length;
		}
		T[][] res;

		@Override
		public void secureCompute(CompEnv<T> gen) {
			res = gen.newTArray(numOfTests, 0);
			IntegerLib<T> lib = new IntegerLib<T>(gen);
			flib = new FixedPointLib<T>(gen, FWidth, FOffset);
			// TODO Auto-generated method stub
			for(int i = 0; i < numOfTests; ++i) {
				T[][] caseFre = gen.newTArray(3, 0);
				T[][] controlFre = gen.newTArray(3, 0);
				for(int j = 0; j < 3; ++j) {
					caseFre[j] = lib.add(aliceCase[i][j], bobCase[i][j]);
					controlFre[j] = lib.add(aliceControl[i][j], bobControl[i][j]);
				}

				T[] resCase = flib.publicValue(0);
				T[] resControl = flib.publicValue(0);
				for(int k = 0; k < 3; ++k) {
					//					System.out.println(Arrays.toString(caseFre[k])+" "+i+""+k);
					T[] sq = lib.multiplyFull(caseFre[k], caseFre[k]);
					T[] sq2 = lib.multiplyFull(controlFre[k], controlFre[k]);

					T[] sum = lib.add(caseFre[k], controlFre[k]);
					//compute sum_i sq_i/sum_i
					T[] fsq = lib.padSignal(sq, FWidth);
					fsq = lib.leftPublicShift(fsq, FOffset);
					T[] fsq2 = lib.padSignal(sq2, FWidth);
					fsq2 = lib.leftPublicShift(fsq2, FOffset);
					T[] fsum = lib.padSignal(sum, FWidth);
					fsum= lib.leftPublicShift(fsum, FOffset);

					T[] div = flib.div(fsq, fsum);
					resCase = flib.add(resCase, div);

					T[] div2 = flib.div(fsq2, fsum);
					resControl = flib.add(resControl, div2);

				}
				resCase = flib.multiply(resCase, flib.publicValue(1.0/200));
				resControl = flib.multiply(resControl, flib.publicValue(1.0/200));
				res[i] = flib.add(resCase, resControl);
			}
		}

		@Override
		public void prepareOutput(CompEnv<T> gen) {
			for(int i = 0; i < numOfTests; ++i)
				flib.outputToAlice(res[i]);
		}
	}
}
