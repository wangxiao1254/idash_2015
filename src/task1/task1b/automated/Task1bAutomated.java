package task1.task1b.automated;
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
public class Task1bAutomated<t__T> implements IWritable<Task1bAutomated<t__T>, t__T> {

	public CompEnv<t__T> env;
	public IntegerLib<t__T> intLib;
	public FloatLib<t__T> floatLib;

	public Task1bAutomated(CompEnv<t__T> env) throws Exception {
		this.env = env;
		this.intLib = new IntegerLib<t__T>(env);
		this.floatLib = new FloatLib<t__T>(env, 24, 8);
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

	public Task1bAutomated<t__T> newObj(t__T[] data) throws Exception {
		if(data == null) {
			data = env.newTArray(this.numBits());
			for(int i=0; i<this.numBits(); ++i) { data[i] = intLib.SIGNAL_ZERO; }
		}
		if(data.length != this.numBits()) return null;
		Task1bAutomated<t__T> ret = new Task1bAutomated<t__T>(env);
		t__T[] tmp;
		int now = 0;
		return ret;
}

	public t__T[][] func(t__T[][][] alice_case, t__T[][][] alice_control, t__T[][][] bob_case, t__T[][][] bob_control, int testcases) throws Exception {
		t__T[][] ret = env.newTArray(200, 0);
		for(int _j_2 = 0; _j_2 < 200; ++_j_2) {
			ret[_j_2] = floatLib.inputOfAlice(0.0);
		}
		int i = 0;
		t__T[] resCase = floatLib.inputOfAlice(0.0);
		t__T[] resControl = floatLib.inputOfAlice(0.0);
		t__T[][] control_freq = env.newTArray(3, 0);
		for(int _j_2 = 0; _j_2 < 3; ++_j_2) {
			control_freq[_j_2] = floatLib.inputOfAlice(0.0);
		}
		t__T[][] case_freq = env.newTArray(3, 0);
		for(int _j_2 = 0; _j_2 < 3; ++_j_2) {
			case_freq[_j_2] = floatLib.inputOfAlice(0.0);
		}
		int j = 0;
		int k = 0;
		t__T[] tmp = floatLib.inputOfAlice(0.0);
		int f_tmp_0 = 0;
		int __tmp0 = f_tmp_0;
		i = __tmp0;
		boolean f_tmp_1 = i < testcases;
		boolean __tmp1 = f_tmp_1;
		while(__tmp1) {
			double f_tmp_2 = 0.0;
			double __tmp2 = f_tmp_2;
			resCase = floatLib.inputOfAlice(__tmp2);
			double f_tmp_3 = 0.0;
			double __tmp3 = f_tmp_3;
			resControl = floatLib.inputOfAlice(__tmp3);
			int f_tmp_4 = 0;
			int __tmp4 = f_tmp_4;
			j = __tmp4;
			int f_tmp_5 = 3;
			int __tmp5 = f_tmp_5;
			boolean f_tmp_6 = j < __tmp5;
			boolean __tmp6 = f_tmp_6;
			while(__tmp6) {
				t__T[][] f_tmp_7 = alice_control[i];
				t__T[][] __tmp7 = f_tmp_7;
				t__T[] f_tmp_8 = __tmp7[j];
				t__T[] __tmp8 = f_tmp_8;
				t__T[][] f_tmp_9 = bob_control[i];
				t__T[][] __tmp9 = f_tmp_9;
				t__T[] f_tmp_10 = __tmp9[j];
				t__T[] __tmp10 = f_tmp_10;
				t__T[] f_tmp_11 = floatLib.add(__tmp8,__tmp10);
				t__T[] __tmp11 = f_tmp_11;
				control_freq[j]=__tmp11;
				t__T[][] f_tmp_12 = alice_case[i];
				t__T[][] __tmp12 = f_tmp_12;
				t__T[] f_tmp_13 = __tmp12[j];
				t__T[] __tmp13 = f_tmp_13;
				t__T[][] f_tmp_14 = bob_case[i];
				t__T[][] __tmp14 = f_tmp_14;
				t__T[] f_tmp_15 = __tmp14[j];
				t__T[] __tmp15 = f_tmp_15;
				t__T[] f_tmp_16 = floatLib.add(__tmp13,__tmp15);
				t__T[] __tmp16 = f_tmp_16;
				case_freq[j]=__tmp16;
				int f_tmp_17 = 1;
				int __tmp17 = f_tmp_17;
				int f_tmp_18 = j + __tmp17;
				int __tmp18 = f_tmp_18;
				j = __tmp18;
				int f_tmp_19 = 3;
				__tmp5 = f_tmp_19;
				boolean f_tmp_20 = j < __tmp5;
				__tmp6 = f_tmp_20;
			}
			int f_tmp_21 = 0;
			int __tmp19 = f_tmp_21;
			k = __tmp19;
			int f_tmp_22 = 3;
			int __tmp20 = f_tmp_22;
			boolean f_tmp_23 = k < __tmp20;
			boolean __tmp21 = f_tmp_23;
			while(__tmp21) {
				t__T[] f_tmp_24 = case_freq[k];
				t__T[] __tmp22 = f_tmp_24;
				t__T[] f_tmp_25 = control_freq[k];
				t__T[] __tmp23 = f_tmp_25;
				t__T[] f_tmp_26 = floatLib.add(__tmp22,__tmp23);
				t__T[] __tmp24 = f_tmp_26;
				tmp = __tmp24;
				t__T[] f_tmp_27 = control_freq[k];
				t__T[] __tmp25 = f_tmp_27;
				t__T[] f_tmp_28 = control_freq[k];
				t__T[] __tmp26 = f_tmp_28;
				t__T[] f_tmp_29 = floatLib.multiply(__tmp25,__tmp26);
				t__T[] __tmp27 = f_tmp_29;
				t__T[] f_tmp_30 = floatLib.div(__tmp27,tmp);
				t__T[] __tmp28 = f_tmp_30;
				t__T[] f_tmp_31 = floatLib.add(resControl,__tmp28);
				t__T[] __tmp29 = f_tmp_31;
				resControl = __tmp29;
				t__T[] f_tmp_32 = case_freq[k];
				t__T[] __tmp30 = f_tmp_32;
				t__T[] f_tmp_33 = case_freq[k];
				t__T[] __tmp31 = f_tmp_33;
				t__T[] f_tmp_34 = floatLib.multiply(__tmp30,__tmp31);
				t__T[] __tmp32 = f_tmp_34;
				t__T[] f_tmp_35 = floatLib.div(__tmp32,tmp);
				t__T[] __tmp33 = f_tmp_35;
				t__T[] f_tmp_36 = floatLib.add(resCase,__tmp33);
				t__T[] __tmp34 = f_tmp_36;
				resCase = __tmp34;
				int f_tmp_37 = 1;
				int __tmp35 = f_tmp_37;
				int f_tmp_38 = k + __tmp35;
				int __tmp36 = f_tmp_38;
				k = __tmp36;
				int f_tmp_39 = 3;
				__tmp20 = f_tmp_39;
				boolean f_tmp_40 = k < __tmp20;
				__tmp21 = f_tmp_40;
			}
			t__T[] f_tmp_41 = floatLib.add(resCase,resControl);
			t__T[] __tmp37 = f_tmp_41;
			double f_tmp_42 = 200.0;
			double __tmp38 = f_tmp_42;
			t__T[] f_tmp_44 = floatLib.inputOfAlice(__tmp38);
			t__T[] f_tmp_43 = floatLib.div(__tmp37,f_tmp_44);
			t__T[] __tmp39 = f_tmp_43;
			ret[i]=__tmp39;
			int f_tmp_45 = 1;
			int __tmp40 = f_tmp_45;
			int f_tmp_46 = i + __tmp40;
			int __tmp41 = f_tmp_46;
			i = __tmp41;
			boolean f_tmp_47 = i < testcases;
			__tmp1 = f_tmp_47;
		}
		return ret;

	}
}
