package task1b;

import util.EvaRunnable;
import util.GenRunnable;
import util.Utils;
import circuits.arithmetic.FixedPointLib;
import circuits.arithmetic.IntegerLib;
import flexsc.CompEnv;

public class Task1b {
	static public int Width = 9;

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
		@Override
		public void secureCompute(CompEnv<T> gen) {
			IntegerLib<T> lib = new IntegerLib<T>(gen);
			FixedPointLib<T> flib = new FixedPointLib<T>(gen, 40, 20);//10e-6
			// TODO Auto-generated method stub
			for(int i = 0; i < numOfTests; ++i) {
				T[][] caseFre = gen.newTArray(3, 0);
				T[][] controlFre = gen.newTArray(3, 0);
				for(int j = 0; j < 3; ++j) {
					caseFre[i] = lib.add(aliceCase[i][j], bobCase[i][j]);
					controlFre[i] = lib.add(aliceControl[i][j], bobControl[i][j]);
				}
				
				T[] sq = lib.multiplyFull(caseFre[0], caseFre[0]);
				T[] sum = lib.add(caseFre[0], controlFre[0]);
				//compute sum_i sq_i/sum_i
			}
			
			// compute somethinghere 
			
		}

		@Override
		public void prepareOutput(CompEnv<T> gen) {
			// TODO Auto-generated method stub
			
		}

	}

	public static class Evaluator<T> extends EvaRunnable<T> {

		@Override
		public void prepareInput(CompEnv<T> gen) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void secureCompute(CompEnv<T> gen) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void prepareOutput(CompEnv<T> gen) {
			// TODO Auto-generated method stub
			
		}

	}
}
