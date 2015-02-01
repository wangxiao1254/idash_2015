package task1.task1a.automated;

import flexsc.CompEnv;
import gc.GCSignal;

import java.io.IOException;
import java.nio.ByteBuffer;

import network.Server;
import task1.PrepareData;
import task1.PrepareData.StatisticsData;
import task1.Statistics;
import util.EvaRunnable;
import util.GenRunnable;
import util.Utils;

public class Task1a {
	
	public static GCSignal[][] compute(CompEnv<GCSignal> env, GCSignal[][] alice, GCSignal[][] bob, int numberOfSta, int width) {
		Task1aAutomated a;
		GCSignal[][] ret = new GCSignal[alice.length][0];
		try {
			a = new Task1aAutomated(env, width);
			a.funct(alice, bob, ret, numberOfSta, alice.length);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	public static class Generator extends GenRunnable<GCSignal> {
		GCSignal[][] alice;
		GCSignal[][] bob;
		int numberOfSta;
		int width;
		@Override
		public void prepareInput(CompEnv<GCSignal> env) {
			StatisticsData staData = PrepareData.readFile(args[0]);
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

		GCSignal[][] res;
		@Override
		public void secureCompute(CompEnv<GCSignal> env) {
			res = compute(env, alice, bob, numberOfSta, width);
		}

		@Override
		public void prepareOutput(CompEnv<GCSignal> env) {
			System.out.println(res.length);
			// TODO Auto-generated method stub
			for(int i = 0; i < res.length; ++i) {
				int out = Utils.toInt(env.outputToAlice(res[i]));
				System.out.println(i+" "+out/(double)numberOfSta);
			}
		}
	}


	public static class Evaluator extends EvaRunnable<GCSignal> {
		GCSignal[][] alice;
		GCSignal[][] bob;
		int width;
		int numberOfSta;
		@Override
		public void prepareInput(CompEnv<GCSignal> env) {
			StatisticsData staData = PrepareData.readFile(args[0]);
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

		GCSignal[][] res;
		@Override
		public void secureCompute(CompEnv<GCSignal> env) {
			res = compute(env, alice, bob, numberOfSta, width);
		}

		@Override
		public void prepareOutput(CompEnv<GCSignal> env) {
			for(int i = 0; i < res.length; ++i) {
				env.outputToAlice(res[i]);
			}
		}
	}
}
