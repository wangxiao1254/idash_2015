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
public class BF_circuit<t__T> implements IWritable<BF_circuit<t__T>, t__T> {

	public CompEnv<t__T> env;
	public IntegerLib<t__T> intLib;
	public FloatLib<t__T> floatLib;

	public BF_circuit(CompEnv<t__T> env) throws Exception {
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

	public BF_circuit<t__T> newObj(t__T[] data) throws Exception {
		if(data == null) {
			data = env.newTArray(this.numBits());
			for(int i=0; i<this.numBits(); ++i) { data[i] = intLib.SIGNAL_ZERO; }
		}
		if(data.length != this.numBits()) return null;
		BF_circuit<t__T> ret = new BF_circuit<t__T>(env);
		t__T[] tmp;
		int now = 0;
		return ret;
}

	public Pair<t__T, bit<t__T>, Int<t__T>> add(int n, t__T[] x, t__T[] y) throws Exception {
		bit<t__T> cin = new bit<t__T>(env);
		Int<t__T> ret = new Int<t__T>(env, n);
		bit<t__T> t1 = new bit<t__T>(env);
		bit<t__T> t2 = new bit<t__T>(env);
		int i = 0;
		int f_tmp_0 = 0;
		int __tmp0 = f_tmp_0;
		i = __tmp0;
		boolean f_tmp_1 = i < n;
		boolean __tmp1 = f_tmp_1;
		while(__tmp1) {
			t__T __tmp2 = x[i];
			t__T f_tmp_2 = cin.v;
			t__T __tmp3 = f_tmp_2;
			t__T f_tmp_3 = intLib.xor(__tmp2,__tmp3);
			t__T __tmp4 = f_tmp_3;
			t1.v = __tmp4;
			t__T __tmp5 = y[i];
			t__T f_tmp_4 = cin.v;
			t__T __tmp6 = f_tmp_4;
			t__T f_tmp_5 = intLib.xor(__tmp5,__tmp6);
			t__T __tmp7 = f_tmp_5;
			t2.v = __tmp7;
			t__T __tmp8 = x[i];
			t__T f_tmp_6 = t2.v;
			t__T __tmp9 = f_tmp_6;
			t__T f_tmp_7 = intLib.xor(__tmp8,__tmp9);
			t__T __tmp10 = f_tmp_7;
			t__T[] f_tmp_8 = ret.v;
			t__T[] __tmp11 = f_tmp_8;
			__tmp11[i]=__tmp10;
			t__T f_tmp_9 = t1.v;
			t__T __tmp12 = f_tmp_9;
			t__T f_tmp_10 = t2.v;
			t__T __tmp13 = f_tmp_10;
			t__T f_tmp_11 = intLib.and(__tmp12,__tmp13);
			t__T __tmp14 = f_tmp_11;
			t1.v = __tmp14;
			t__T f_tmp_12 = cin.v;
			t__T __tmp15 = f_tmp_12;
			t__T f_tmp_13 = t1.v;
			t__T __tmp16 = f_tmp_13;
			t__T f_tmp_14 = intLib.xor(__tmp15,__tmp16);
			t__T __tmp17 = f_tmp_14;
			cin.v = __tmp17;
			int f_tmp_15 = 1;
			int __tmp18 = f_tmp_15;
			int f_tmp_16 = i + __tmp18;
			int __tmp19 = f_tmp_16;
			i = __tmp19;
			boolean f_tmp_17 = i < n;
			__tmp1 = f_tmp_17;
		}
		Pair<t__T, bit<t__T>, Int<t__T>> f_tmp_18 = new Pair<t__T, bit<t__T>, Int<t__T>>(env, new bit<t__T>(env), new Int<t__T>(env, n));
		f_tmp_18.left = cin;
		f_tmp_18.right = ret;
		Pair<t__T, bit<t__T>, Int<t__T>> __tmp20 = f_tmp_18;
		return __tmp20;

	}
	public t__T[] countOnes(int n, t__T[] x) throws Exception {
		t__T[] first = env.inputOfAlice(Utils.fromInt(0, Utils.logFloor(((n)-((n)/(2)))+(1))));
		t__T[] second = env.inputOfAlice(Utils.fromInt(0, Utils.logFloor(((n)-((n)/(2)))+(1))));
		Pair<t__T, bit<t__T>, Int<t__T>> ret = new Pair<t__T, bit<t__T>, Int<t__T>>(env, new bit<t__T>(env), new Int<t__T>(env, Utils.logFloor((n)-((n)/(2)))));
		t__T[] r = env.inputOfAlice(Utils.fromInt(0, Utils.logFloor((n)+(1))));
		int f_tmp_19 = 1;
		int __tmp21 = f_tmp_19;
		boolean f_tmp_20 = n == __tmp21;
		boolean __tmp22 = f_tmp_20;
		if(__tmp22) {
			return x;
		} else {
		}
		t__T[] __tmp23 = Arrays.copyOfRange(x, 0, (n)/(2));
		t__T[] f_tmp_21 = this.countOnes((n)/(2), __tmp23);
		t__T[] __tmp24 = f_tmp_21;
		t__T[] __tmp25 = intLib.enforceBits(__tmp24, Utils.logFloor(((n)-((n)/(2)))+(1)));
		first = __tmp25;
		t__T[] __tmp26 = Arrays.copyOfRange(x, (n)/(2), n);
		t__T[] f_tmp_22 = this.countOnes((n)-((n)/(2)), __tmp26);
		t__T[] __tmp27 = f_tmp_22;
		t__T[] __tmp28 = intLib.enforceBits(__tmp27, Utils.logFloor(((n)-((n)/(2)))+(1)));
		second = __tmp28;
		t__T[] __tmp29 = intLib.enforceBits(first, Utils.logFloor(((n)-((n)/(2)))+(1)));
		t__T[] __tmp30 = intLib.enforceBits(second, Utils.logFloor(((n)-((n)/(2)))+(1)));
		Pair<t__T, bit<t__T>, Int<t__T>> f_tmp_23 = this.add(Utils.logFloor(((n)-((n)/(2)))+(1)), __tmp29, __tmp30);
		Pair<t__T, bit<t__T>, Int<t__T>> __tmp31 = f_tmp_23;
		ret = __tmp31;
		Int<t__T> f_tmp_24 = ret.right;
		Int<t__T> __tmp32 = f_tmp_24;
		t__T[] f_tmp_25 = __tmp32.v;
		t__T[] __tmp33 = f_tmp_25;
		t__T[] __tmp34 = intLib.enforceBits(__tmp33, Utils.logFloor((n)+(1)));
		r = __tmp34;
		bit<t__T> f_tmp_26 = ret.left;
		bit<t__T> __tmp35 = f_tmp_26;
		t__T f_tmp_27 = __tmp35.v;
		t__T __tmp36 = f_tmp_27;
		r[(Utils.logFloor((n)+(1)))-(1)]=__tmp36;
		return r;

	}
	public t__T[] merge(int n, t__T[] x, t__T[] y) throws Exception {
		t__T[] tmp = env.inputOfAlice(Utils.fromInt(0, n));
		int i = 0;
		int f_tmp_28 = 0;
		int __tmp37 = f_tmp_28;
		i = __tmp37;
		boolean f_tmp_29 = i < n;
		boolean __tmp38 = f_tmp_29;
		while(__tmp38) {
			t__T __tmp39 = x[i];
			t__T __tmp40 = y[i];
			t__T f_tmp_30 = intLib.or(__tmp39,__tmp40);
			t__T __tmp41 = f_tmp_30;
			tmp[i]=__tmp41;
			int f_tmp_31 = 1;
			int __tmp42 = f_tmp_31;
			int f_tmp_32 = i + __tmp42;
			int __tmp43 = f_tmp_32;
			i = __tmp43;
			boolean f_tmp_33 = i < n;
			__tmp38 = f_tmp_33;
		}
		t__T[] f_tmp_34 = this.countOnes(n, tmp);
		t__T[] __tmp44 = f_tmp_34;
		return __tmp44;

	}
}
