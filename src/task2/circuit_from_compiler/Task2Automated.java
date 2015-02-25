package task2.circuit_from_compiler;
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
public class Task2Automated<t__T> implements IWritable<Task2Automated<t__T>, t__T> {

	public CompEnv<t__T> env;
	public IntegerLib<t__T> intLib;
	public FloatLib<t__T> floatLib;
	private int m;
	private int n;

	public Task2Automated(CompEnv<t__T> env, int m, int n) throws Exception {
		this.env = env;
		this.intLib = new IntegerLib<t__T>(env);
		this.floatLib = new FloatLib<t__T>(env, 24, 8);
		this.m = m;
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

	public Task2Automated<t__T> newObj(t__T[] data) throws Exception {
		if(data == null) {
			data = env.newTArray(this.numBits());
			for(int i=0; i<this.numBits(); ++i) { data[i] = intLib.SIGNAL_ZERO; }
		}
		if(data.length != this.numBits()) return null;
		Task2Automated<t__T> ret = new Task2Automated<t__T>(env, m, n);
		t__T[] tmp;
		int now = 0;
		return ret;
}

	public t__T[] funct(t__T[][] key, int length) throws Exception {
		t__T[] ret = env.inputOfAlice(Utils.fromInt(0, n));
		int i = 0;
		int f_tmp_0 = 0;
		int __tmp0 = f_tmp_0;
		this.obliviousMerge(key, __tmp0, length);
		int f_tmp_2 = 1;
		int __tmp2 = f_tmp_2;
		ret = env.inputOfAlice(Utils.fromInt(__tmp2, n));
		int f_tmp_3 = 1;
		int __tmp3 = f_tmp_3;
		i = __tmp3;
		boolean f_tmp_4 = i < length;
		boolean __tmp4 = f_tmp_4;
		while(__tmp4) {
			int f_tmp_5 = 1;
			int __tmp5 = f_tmp_5;
			int f_tmp_6 = i - __tmp5;
			int __tmp6 = f_tmp_6;
			t__T[] f_tmp_7 = key[__tmp6];
			t__T[] __tmp7 = f_tmp_7;
			t__T[] f_tmp_8 = key[i];
			t__T[] __tmp8 = f_tmp_8;
			t__T f_tmp_9 = intLib.not(intLib.eq(__tmp7, __tmp8));
			t__T __tmp9 = f_tmp_9;
			int f_tmp_10 = 1;
			int __tmp10 = f_tmp_10;
			t__T[] f_tmp_12 = env.inputOfAlice(Utils.fromInt(__tmp10, n));
			t__T[] f_tmp_11 = intLib.add(ret,f_tmp_12);
			t__T[] __tmp11 = f_tmp_11;
			t__T[] __tmp12 = intLib.mux(ret, __tmp11,__tmp9);
			ret = __tmp12;
			int f_tmp_14 = 1;
			int __tmp13 = f_tmp_14;
			int f_tmp_15 = i + __tmp13;
			int __tmp14 = f_tmp_15;
			i = __tmp14;
			boolean f_tmp_16 = i < length;
			__tmp4 = f_tmp_16;
		}
		return ret;

	}
	public void obliviousMerge(t__T[][] key, int lo, int l) throws Exception {
		int k = 0;
		int i = 0;
		int f_tmp_17 = 1;
		int __tmp15 = f_tmp_17;
		boolean f_tmp_18 = l > __tmp15;
		boolean __tmp16 = f_tmp_18;
		if(__tmp16) {
			int f_tmp_19 = 1;
			int __tmp17 = f_tmp_19;
			k = __tmp17;
			boolean f_tmp_20 = k < l;
			boolean __tmp18 = f_tmp_20;
			while(__tmp18) {
				int f_tmp_21 = 1;
				int __tmp19 = f_tmp_21;
				int f_tmp_22 = k << __tmp19;
				int __tmp20 = f_tmp_22;
				k = __tmp20;
				boolean f_tmp_23 = k < l;
				__tmp18 = f_tmp_23;
			}
			int f_tmp_24 = 1;
			int __tmp21 = f_tmp_24;
			int f_tmp_25 = k >> __tmp21;
			int __tmp22 = f_tmp_25;
			k = __tmp22;
			i = lo;
			int f_tmp_26 = lo + l;
			int __tmp23 = f_tmp_26;
			int f_tmp_27 = __tmp23 - k;
			int __tmp24 = f_tmp_27;
			boolean f_tmp_28 = i < __tmp24;
			boolean __tmp25 = f_tmp_28;
			while(__tmp25) {
				int f_tmp_29 = i + k;
				int __tmp26 = f_tmp_29;
				this.compare(key, i, __tmp26);
				int f_tmp_31 = 1;
				int __tmp28 = f_tmp_31;
				int f_tmp_32 = i + __tmp28;
				int __tmp29 = f_tmp_32;
				i = __tmp29;
				int f_tmp_33 = lo + l;
				__tmp23 = f_tmp_33;
				int f_tmp_34 = __tmp23 - k;
				__tmp24 = f_tmp_34;
				boolean f_tmp_35 = i < __tmp24;
				__tmp25 = f_tmp_35;
			}
			this.obliviousMerge(key, lo, k);
			int f_tmp_37 = lo + k;
			int __tmp31 = f_tmp_37;
			int f_tmp_38 = l - k;
			int __tmp32 = f_tmp_38;
			this.obliviousMerge(key, __tmp31, __tmp32);
		} else {
		}

	}
	public void compare(t__T[][] key, int i, int j) throws Exception {
		t__T[] tmp = env.inputOfAlice(Utils.fromInt(0, m));
		t__T[] tmp2 = env.inputOfAlice(Utils.fromInt(0, m));
		t__T[] f_tmp_40 = key[j];
		t__T[] __tmp34 = f_tmp_40;
		tmp = __tmp34;
		t__T[] f_tmp_41 = key[i];
		t__T[] __tmp35 = f_tmp_41;
		tmp2 = __tmp35;
		t__T[] f_tmp_42 = key[i];
		t__T[] __tmp36 = f_tmp_42;
		t__T[] f_tmp_43 = key[j];
		t__T[] __tmp37 = f_tmp_43;
		t__T f_tmp_44 = intLib.not(intLib.geq(__tmp36, __tmp37));
		t__T __tmp38 = f_tmp_44;
		t__T[] f_tmp_45 = key[i];
		t__T[] __tmp39 = f_tmp_45;
		t__T[] __tmp40 = intLib.mux(tmp, __tmp39,__tmp38);
		tmp = __tmp40;
		t__T[] f_tmp_47 = key[i];
		t__T[] __tmp41 = f_tmp_47;
		t__T[] f_tmp_48 = intLib.xor(tmp,__tmp41);
		t__T[] __tmp42 = f_tmp_48;
		tmp = __tmp42;
		t__T[] f_tmp_49 = key[j];
		t__T[] __tmp43 = f_tmp_49;
		t__T[] f_tmp_50 = intLib.xor(tmp,__tmp43);
		t__T[] __tmp44 = f_tmp_50;
		key[i]=__tmp44;
		t__T[] f_tmp_51 = intLib.xor(tmp,tmp2);
		t__T[] __tmp45 = f_tmp_51;
		key[j]=__tmp45;

	}
}
