package task1.task1a.automated;
import java.security.SecureRandom;
import oram.SecureArray;
import oram.CircuitOram;
import flexsc.Mode;
import flexsc.Party;
import flexsc.CompEnv;
import java.util.BitSet;
import circuits.arithmetic.IntegerLib;
import circuits.arithmetic.FloatLib;
import util.Utils;
import gc.regular.GCEva;
import gc.regular.GCGen;
import gc.GCSignal;
import java.util.Arrays;
import java.util.Random;
import flexsc.IWritable;
import flexsc.Comparator;
import java.lang.reflect.Array;
public class Task1aAutomated implements IWritable<Task1aAutomated, GCSignal> {

	public CompEnv<GCSignal> env;
	public IntegerLib<GCSignal> intLib;
	public FloatLib<GCSignal> floatLib;
	private int m;

	public Task1aAutomated(CompEnv<GCSignal> env, int m) throws Exception {
		this.env = env;
		this.intLib = new IntegerLib<GCSignal>(env);
		this.floatLib = new FloatLib<GCSignal>(env, 24, 8);
		this.m = m;
	}

	public int numBits() {
		return 0;
	}
	public GCSignal[] getBits() {
		GCSignal[] ret = new GCSignal[this.numBits()];
		GCSignal[] tmp_b;
		GCSignal tmp;
		int now = 0;
		return ret;
}

	public Task1aAutomated newObj(GCSignal[] data) throws Exception {
		if(data == null) {
			data = new GCSignal[this.numBits()];
			for(int i=0; i<this.numBits(); ++i) { data[i] = intLib.SIGNAL_ZERO; }
		}
		if(data.length != this.numBits()) return null;
		Task1aAutomated ret = new Task1aAutomated(env, m);
		GCSignal[] tmp;
		int now = 0;
		return ret;
}

	public void funct(GCSignal[][] alice_data, GCSignal[][] bob_data, GCSignal[][] ret, int total_instances, int test_cases) throws Exception {
		GCSignal[] total = env.inputOfAlice(Utils.fromInt(0, m));
		GCSignal[] half = env.inputOfAlice(Utils.fromInt(0, m));
		int i = 0;
		total = env.inputOfAlice(Utils.fromInt(total_instances, m));
		int f_tmp_0 = 2;
		int __tmp0 = f_tmp_0;
		int f_tmp_1 = total_instances / __tmp0;
		int __tmp1 = f_tmp_1;
		half = env.inputOfAlice(Utils.fromInt(__tmp1, m));
		int f_tmp_2 = 0;
		int __tmp2 = f_tmp_2;
		i = __tmp2;
		boolean f_tmp_3 = i < test_cases;
		boolean __tmp3 = f_tmp_3;
		while(__tmp3) {
			GCSignal[] f_tmp_4 = alice_data[i];
			GCSignal[] __tmp4 = f_tmp_4;
			GCSignal[] f_tmp_5 = bob_data[i];
			GCSignal[] __tmp5 = f_tmp_5;
			GCSignal[] f_tmp_6 = intLib.add(__tmp4,__tmp5);
			GCSignal[] __tmp6 = f_tmp_6;
			ret[i]=__tmp6;
			GCSignal[] f_tmp_7 = ret[i];
			GCSignal[] __tmp7 = f_tmp_7;
			GCSignal f_tmp_8 = intLib.not(intLib.leq(__tmp7, half));
			GCSignal __tmp8 = f_tmp_8;
			GCSignal[] f_tmp_9 = ret[i];
			GCSignal[] __tmp9 = f_tmp_9;
			GCSignal[] f_tmp_10 = intLib.sub(total,__tmp9);
			GCSignal[] __tmp10 = f_tmp_10;
			GCSignal[] f_tmp_11 = ret[i];
			GCSignal[] __tmp11 = f_tmp_11;
			GCSignal[] f_tmp_12 = intLib.mux(__tmp11, __tmp10,__tmp8);
			GCSignal[] __tmp12 = f_tmp_12;
			ret[i]=__tmp12;
			GCSignal f_tmp_13 = intLib.not(__tmp8);
			GCSignal __tmp13 = f_tmp_13;
			int f_tmp_14 = 1;
			int __tmp14 = f_tmp_14;
			int f_tmp_15 = i + __tmp14;
			int __tmp15 = f_tmp_15;
			i = __tmp15;
			boolean f_tmp_16 = i < test_cases;
			__tmp3 = f_tmp_16;
		}

	}
}
