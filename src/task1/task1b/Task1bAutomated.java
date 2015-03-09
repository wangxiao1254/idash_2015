package task1.task1b;
import circuits.arithmetic.FloatLib;
import circuits.arithmetic.IntegerLib;
import flexsc.CompEnv;
import flexsc.IWritable;
public class Task1bAutomated<t__T> implements IWritable<Task1bAutomated<t__T>, t__T> {

	public CompEnv<t__T> env;
	public IntegerLib<t__T> intLib;
	public FloatLib<t__T> floatLib;
	private int n;

	public Task1bAutomated(CompEnv<t__T> env, int n) throws Exception {
		this.env = env;
		this.intLib = new IntegerLib<t__T>(env);
		this.floatLib = new FloatLib<t__T>(env, 24, 8);
		this.n = n;
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
		Task1bAutomated<t__T> ret = new Task1bAutomated<t__T>(env, n);
		t__T[] tmp;
		int now = 0;
		return ret;
}

	public t__T[][] func(t__T[][][] alice_case, t__T[][][] alice_control, t__T[][][] bob_case, t__T[][][] bob_control) throws Exception {
		t__T[][] ret = env.newTArray(n, 0);
		for(int _j_2 = 0; _j_2 < n; ++_j_2) {
			ret[_j_2] = floatLib.inputOfAlice(0.0);
		}
		int i = 0;
		t__T[] a = floatLib.inputOfAlice(0.0);
		t__T[] b = floatLib.inputOfAlice(0.0);
		t__T[] c = floatLib.inputOfAlice(0.0);
		t__T[] d = floatLib.inputOfAlice(0.0);
		t__T[] g = floatLib.inputOfAlice(0.0);
		t__T[] k = floatLib.inputOfAlice(0.0);
		t__T[] tmp = floatLib.inputOfAlice(0.0);
		int f_tmp_0 = 0;
		int __tmp0 = f_tmp_0;
		i = __tmp0;
		boolean f_tmp_1 = i < n;
		boolean __tmp1 = f_tmp_1;
		while(__tmp1) {
			t__T[][] f_tmp_2 = alice_case[i];
			t__T[][] __tmp2 = f_tmp_2;
			int f_tmp_3 = 0;
			int __tmp3 = f_tmp_3;
			t__T[] f_tmp_4 = __tmp2[__tmp3];
			t__T[] __tmp4 = f_tmp_4;
			t__T[][] f_tmp_5 = bob_case[i];
			t__T[][] __tmp5 = f_tmp_5;
			int f_tmp_6 = 0;
			int __tmp6 = f_tmp_6;
			t__T[] f_tmp_7 = __tmp5[__tmp6];
			t__T[] __tmp7 = f_tmp_7;
			t__T[] f_tmp_8 = floatLib.add(__tmp4,__tmp7);
			t__T[] __tmp8 = f_tmp_8;
			a = __tmp8;
			t__T[][] f_tmp_9 = alice_case[i];
			t__T[][] __tmp9 = f_tmp_9;
			int f_tmp_10 = 1;
			int __tmp10 = f_tmp_10;
			t__T[] f_tmp_11 = __tmp9[__tmp10];
			t__T[] __tmp11 = f_tmp_11;
			t__T[][] f_tmp_12 = bob_case[i];
			t__T[][] __tmp12 = f_tmp_12;
			int f_tmp_13 = 1;
			int __tmp13 = f_tmp_13;
			t__T[] f_tmp_14 = __tmp12[__tmp13];
			t__T[] __tmp14 = f_tmp_14;
			t__T[] f_tmp_15 = floatLib.add(__tmp11,__tmp14);
			t__T[] __tmp15 = f_tmp_15;
			b = __tmp15;
			t__T[][] f_tmp_16 = alice_control[i];
			t__T[][] __tmp16 = f_tmp_16;
			int f_tmp_17 = 0;
			int __tmp17 = f_tmp_17;
			t__T[] f_tmp_18 = __tmp16[__tmp17];
			t__T[] __tmp18 = f_tmp_18;
			t__T[][] f_tmp_19 = bob_control[i];
			t__T[][] __tmp19 = f_tmp_19;
			int f_tmp_20 = 0;
			int __tmp20 = f_tmp_20;
			t__T[] f_tmp_21 = __tmp19[__tmp20];
			t__T[] __tmp21 = f_tmp_21;
			t__T[] f_tmp_22 = floatLib.add(__tmp18,__tmp21);
			t__T[] __tmp22 = f_tmp_22;
			c = __tmp22;
			t__T[][] f_tmp_23 = alice_control[i];
			t__T[][] __tmp23 = f_tmp_23;
			int f_tmp_24 = 1;
			int __tmp24 = f_tmp_24;
			t__T[] f_tmp_25 = __tmp23[__tmp24];
			t__T[] __tmp25 = f_tmp_25;
			t__T[][] f_tmp_26 = bob_control[i];
			t__T[][] __tmp26 = f_tmp_26;
			int f_tmp_27 = 1;
			int __tmp27 = f_tmp_27;
			t__T[] f_tmp_28 = __tmp26[__tmp27];
			t__T[] __tmp28 = f_tmp_28;
			t__T[] f_tmp_29 = floatLib.add(__tmp25,__tmp28);
			t__T[] __tmp29 = f_tmp_29;
			d = __tmp29;
			t__T[] f_tmp_30 = floatLib.add(a,c);
			t__T[] __tmp30 = f_tmp_30;
			g = __tmp30;
			t__T[] f_tmp_31 = floatLib.add(b,d);
			t__T[] __tmp31 = f_tmp_31;
			k = __tmp31;
			t__T[] f_tmp_32 = floatLib.multiply(a,d);
			t__T[] __tmp32 = f_tmp_32;
			t__T[] f_tmp_33 = floatLib.multiply(b,c);
			t__T[] __tmp33 = f_tmp_33;
			t__T[] f_tmp_34 = floatLib.sub(__tmp32,__tmp33);
			t__T[] __tmp34 = f_tmp_34;
			tmp = __tmp34;
			t__T[] f_tmp_35 = floatLib.multiply(tmp,tmp);
			t__T[] __tmp35 = f_tmp_35;
			tmp = __tmp35;
			t__T[] f_tmp_36 = floatLib.multiply(g,k);
			t__T[] __tmp36 = f_tmp_36;
			t__T[] f_tmp_37 = floatLib.div(tmp,__tmp36);
			t__T[] __tmp37 = f_tmp_37;
			ret[i]=__tmp37;
			int f_tmp_38 = 1;
			int __tmp38 = f_tmp_38;
			int f_tmp_39 = i + __tmp38;
			int __tmp39 = f_tmp_39;
			i = __tmp39;
			boolean f_tmp_40 = i < n;
			__tmp1 = f_tmp_40;
		}
		return ret;

	}
}
