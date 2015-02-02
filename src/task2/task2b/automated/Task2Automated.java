package task2.task2b.automated;
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
public class Task2Automated implements IWritable<Task2Automated, GCSignal> {

	public CompEnv<GCSignal> env;
	public IntegerLib<GCSignal> intLib;
	public FloatLib<GCSignal> floatLib;
	private int m;
	private int n;

	public Task2Automated(CompEnv<GCSignal> env, int m, int n) throws Exception {
		this.env = env;
		this.intLib = new IntegerLib<GCSignal>(env);
		this.floatLib = new FloatLib<GCSignal>(env, 24, 8);
		this.m = m;
		this.n = n;
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

	public Task2Automated newObj(GCSignal[] data) throws Exception {
		if(data == null) {
			data = new GCSignal[this.numBits()];
			for(int i=0; i<this.numBits(); ++i) { data[i] = intLib.SIGNAL_ZERO; }
		}
		if(data.length != this.numBits()) return null;
		Task2Automated ret = new Task2Automated(env, m, n);
		GCSignal[] tmp;
		int now = 0;
		return ret;
}

	public GCSignal[] funct(GCSignal[][] key, int length) throws Exception {
		GCSignal[] ret = env.inputOfAlice(Utils.fromInt(0, n));
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
			GCSignal[] f_tmp_7 = key[__tmp6];
			GCSignal[] __tmp7 = f_tmp_7;
			GCSignal[] f_tmp_8 = key[i];
			GCSignal[] __tmp8 = f_tmp_8;
			GCSignal f_tmp_9 = intLib.not(intLib.eq(__tmp7, __tmp8));
			GCSignal __tmp9 = f_tmp_9;
			int f_tmp_10 = 1;
			int __tmp10 = f_tmp_10;
			GCSignal[] f_tmp_12 = env.inputOfAlice(Utils.fromInt(__tmp10, n));
			GCSignal[] f_tmp_11 = intLib.add(ret,f_tmp_12);
			GCSignal[] __tmp11 = f_tmp_11;
			GCSignal[] f_tmp_13 = intLib.mux(ret, __tmp11,__tmp9);
			GCSignal[] __tmp12 = f_tmp_13;
			ret = __tmp12;
			GCSignal f_tmp_14 = intLib.not(__tmp9);
			GCSignal __tmp13 = f_tmp_14;
			int f_tmp_15 = 1;
			int __tmp14 = f_tmp_15;
			int f_tmp_16 = i + __tmp14;
			int __tmp15 = f_tmp_16;
			i = __tmp15;
			boolean f_tmp_17 = i < length;
			__tmp4 = f_tmp_17;
		}
		return ret;
	}
	public void obliviousMerge(GCSignal[][] key, int lo, int l) throws Exception {
		int k = 0;
		int i = 0;
		int f_tmp_18 = 1;
		int __tmp16 = f_tmp_18;
		boolean f_tmp_19 = l > __tmp16;
		boolean __tmp17 = f_tmp_19;
		if(__tmp17) {
			int f_tmp_20 = 1;
			int __tmp18 = f_tmp_20;
			k = __tmp18;
			boolean f_tmp_21 = k < l;
			boolean __tmp19 = f_tmp_21;
			while(__tmp19) {
				int f_tmp_22 = 1;
				int __tmp20 = f_tmp_22;
				int f_tmp_23 = k << __tmp20;
				int __tmp21 = f_tmp_23;
				k = __tmp21;
				boolean f_tmp_24 = k < l;
				__tmp19 = f_tmp_24;
			}
			int f_tmp_25 = 1;
			int __tmp22 = f_tmp_25;
			int f_tmp_26 = k >> __tmp22;
			int __tmp23 = f_tmp_26;
			k = __tmp23;
			i = lo;
			int f_tmp_27 = lo + l;
			int __tmp24 = f_tmp_27;
			int f_tmp_28 = __tmp24 - k;
			int __tmp25 = f_tmp_28;
			boolean f_tmp_29 = i < __tmp25;
			boolean __tmp26 = f_tmp_29;
			while(__tmp26) {
				int f_tmp_30 = i + k;
				int __tmp27 = f_tmp_30;
				this.compare(key, i, __tmp27);
				int f_tmp_32 = 1;
				int __tmp29 = f_tmp_32;
				int f_tmp_33 = i + __tmp29;
				int __tmp30 = f_tmp_33;
				i = __tmp30;
				int f_tmp_34 = lo + l;
				__tmp24 = f_tmp_34;
				int f_tmp_35 = __tmp24 - k;
				__tmp25 = f_tmp_35;
				boolean f_tmp_36 = i < __tmp25;
				__tmp26 = f_tmp_36;
			}
			this.obliviousMerge(key, lo, k);
			int f_tmp_38 = lo + k;
			int __tmp32 = f_tmp_38;
			int f_tmp_39 = l - k;
			int __tmp33 = f_tmp_39;
			this.obliviousMerge(key, __tmp32, __tmp33);
		} else {
		}

	}
	public void compare(GCSignal[][] key, int i, int j) throws Exception {
		GCSignal[] tmp = env.inputOfAlice(Utils.fromInt(0, m));
		GCSignal[] tmp2 = env.inputOfAlice(Utils.fromInt(0, m));
		GCSignal[] f_tmp_41 = key[j];
		GCSignal[] __tmp35 = f_tmp_41;
		tmp = __tmp35;
		GCSignal[] f_tmp_42 = key[i];
		GCSignal[] __tmp36 = f_tmp_42;
		tmp2 = __tmp36;
		GCSignal[] f_tmp_43 = key[i];
		GCSignal[] __tmp37 = f_tmp_43;
		GCSignal[] f_tmp_44 = key[j];
		GCSignal[] __tmp38 = f_tmp_44;
		GCSignal f_tmp_45 = intLib.not(intLib.geq(__tmp37, __tmp38));
		GCSignal __tmp39 = f_tmp_45;
		GCSignal[] f_tmp_46 = key[i];
		GCSignal[] __tmp40 = f_tmp_46;
		GCSignal[] f_tmp_47 = intLib.mux(tmp, __tmp40,__tmp39);
		GCSignal[] __tmp41 = f_tmp_47;
		tmp = __tmp41;
		GCSignal f_tmp_48 = intLib.not(__tmp39);
		GCSignal __tmp42 = f_tmp_48;
		GCSignal[] f_tmp_49 = key[i];
		GCSignal[] __tmp43 = f_tmp_49;
		GCSignal[] f_tmp_50 = intLib.xor(tmp,__tmp43);
		GCSignal[] __tmp44 = f_tmp_50;
		tmp = __tmp44;
		GCSignal[] f_tmp_51 = key[j];
		GCSignal[] __tmp45 = f_tmp_51;
		GCSignal[] f_tmp_52 = intLib.xor(tmp,__tmp45);
		GCSignal[] __tmp46 = f_tmp_52;
		key[i]=__tmp46;
		GCSignal[] f_tmp_53 = intLib.xor(tmp,tmp2);
		GCSignal[] __tmp47 = f_tmp_53;
		key[j]=__tmp47;

	}
}
