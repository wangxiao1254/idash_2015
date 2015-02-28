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
public class Int<t__T> implements IWritable<Int<t__T>, t__T> {
	public t__T[] v;

	public CompEnv<t__T> env;
	public IntegerLib<t__T> intLib;
	public FloatLib<t__T> floatLib;
	private int n;

	public Int(CompEnv<t__T> env, int n) throws Exception {
		this.env = env;
		this.intLib = new IntegerLib<t__T>(env);
		this.floatLib = new FloatLib<t__T>(env, 24, 8);
		this.n = n;
		this.v = env.inputOfAlice(Utils.fromInt(0, n));
	}

	public int numBits() {
		return (0)+(n);
	}
	public t__T[] getBits() {
		t__T[] ret = env.newTArray(this.numBits());
		t__T[] tmp_b;
		t__T tmp;
		int now = 0;
		tmp_b = v;
		System.arraycopy(tmp_b, 0, ret, now, tmp_b.length);
		now += tmp_b.length;
		return ret;
}

	public Int<t__T> newObj(t__T[] data) throws Exception {
		if(data == null) {
			data = env.newTArray(this.numBits());
			for(int i=0; i<this.numBits(); ++i) { data[i] = intLib.SIGNAL_ZERO; }
		}
		if(data.length != this.numBits()) return null;
		Int<t__T> ret = new Int<t__T>(env, n);
		t__T[] tmp;
		int now = 0;
		ret.v = env.newTArray(n);
		System.arraycopy(data, now, ret.v, 0, n);
		now += n;
		return ret;
}

}
