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
public class bit<t__T> implements IWritable<bit<t__T>, t__T> {
	public t__T v;

	public CompEnv<t__T> env;
	public IntegerLib<t__T> intLib;
	public FloatLib<t__T> floatLib;

	public bit(CompEnv<t__T> env) throws Exception {
		this.env = env;
		this.intLib = new IntegerLib<t__T>(env);
		this.floatLib = new FloatLib<t__T>(env, 24, 8);
		this.v = env.inputOfAlice(false);
	}

	public int numBits() {
		return (0)+(1);
	}
	public t__T[] getBits() {
		t__T[] ret = env.newTArray(this.numBits());
		t__T[] tmp_b;
		t__T tmp;
		int now = 0;
		tmp = v;
		ret[now] = tmp;
		now ++;
		return ret;
}

	public bit<t__T> newObj(t__T[] data) throws Exception {
		if(data == null) {
			data = env.newTArray(this.numBits());
			for(int i=0; i<this.numBits(); ++i) { data[i] = intLib.SIGNAL_ZERO; }
		}
		if(data.length != this.numBits()) return null;
		bit<t__T> ret = new bit<t__T>(env);
		t__T[] tmp;
		int now = 0;
		ret.v = data[now];
		now ++;
		return ret;
}

}
