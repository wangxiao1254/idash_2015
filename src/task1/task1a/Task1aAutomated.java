package task1.task1a;
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
public class Task1aAutomated<t__T> implements IWritable<Task1aAutomated<t__T>, t__T> {

	public CompEnv<t__T> env;
	public IntegerLib<t__T> intLib;
	public FloatLib<t__T> floatLib;
	private int m;

	public Task1aAutomated(CompEnv<t__T> env, int m) throws Exception {
		this.env = env;
		this.intLib = new IntegerLib<t__T>(env);
		this.floatLib = new FloatLib<t__T>(env, 24, 8);
		this.m = m;
	}

	public int numBits() {
		return 0;
	}
	public t__T[] getBits() {
		t__T[] ret = env.newTArray(this.numBits());
		t__T[] tmp_b;
		t__T tmp;
		int now = 0;
		return ret;
}

	public Task1aAutomated<t__T> newObj(t__T[] data) throws Exception {
		if(data == null) {
			data = env.newTArray(this.numBits());
			for(int i=0; i<this.numBits(); ++i) { data[i] = intLib.SIGNAL_ZERO; }
		}
		if(data.length != this.numBits()) return null;
		Task1aAutomated<t__T> ret = new Task1aAutomated<t__T>(env, m);
		t__T[] tmp;
		int now = 0;
		return ret;
}

	public void funct(t__T[][] alice_data, t__T[][] bob_data, t__T[][] ret, int total_instances, int test_cases) throws Exception {
		t__T[] total = env.inputOfAlice(Utils.fromInt(0, m));
		t__T[] half = env.inputOfAlice(Utils.fromInt(0, m));
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
			t__T[] f_tmp_4 = alice_data[i];
			t__T[] __tmp4 = f_tmp_4;
			t__T[] f_tmp_5 = bob_data[i];
			t__T[] __tmp5 = f_tmp_5;
			t__T[] f_tmp_6 = intLib.add(__tmp4,__tmp5);
			t__T[] __tmp6 = f_tmp_6;
			ret[i]=__tmp6;
			t__T[] f_tmp_7 = ret[i];
			t__T[] __tmp7 = f_tmp_7;
			t__T f_tmp_8 = intLib.not(intLib.leq(__tmp7, half));
			t__T __tmp8 = f_tmp_8;
			t__T[] f_tmp_9 = ret[i];
			t__T[] __tmp9 = f_tmp_9;
			t__T[] f_tmp_10 = intLib.sub(total,__tmp9);
			t__T[] __tmp10 = f_tmp_10;
			t__T[] f_tmp_11 = ret[i];
			t__T[] __tmp11 = f_tmp_11;
			t__T[] __tmp12 = intLib.mux(__tmp11, __tmp10,__tmp8);
			ret[i]=__tmp12;
			int f_tmp_13 = 1;
			int __tmp13 = f_tmp_13;
			int f_tmp_14 = i + __tmp13;
			int __tmp14 = f_tmp_14;
			i = __tmp14;
			boolean f_tmp_15 = i < test_cases;
			__tmp3 = f_tmp_15;
		}

	}
}
