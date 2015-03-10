package task2.circuit_from_compiler;
import circuits.arithmetic.FloatLib;
import circuits.arithmetic.IntegerLib;
import flexsc.CompEnv;
import flexsc.IWritable;
public class Pair<t__T, T1 extends IWritable<T1,t__T>, T2 extends IWritable<T2,t__T>> implements IWritable<Pair<t__T, T1, T2>, t__T> {
	public T1 left;
	public T2 right;

	public CompEnv<t__T> env;
	public IntegerLib<t__T> intLib;
	public FloatLib<t__T> floatLib;
	private T1 factoryT1;
	private T2 factoryT2;

	public Pair(CompEnv<t__T> env, T1 factoryT1, T2 factoryT2) throws Exception {
		this.env = env;
		this.intLib = new IntegerLib<t__T>(env);
		this.floatLib = new FloatLib<t__T>(env, 24, 8);
		this.factoryT1 = factoryT1;
		this.factoryT2 = factoryT2;
		this.left = factoryT1.newObj(null);
		this.right = factoryT2.newObj(null);
	}

	public int numBits() {
		int sum = 0;
		sum += factoryT1.numBits();
		sum += factoryT2.numBits();
		return sum;
	}

	public t__T[] getBits() {
		t__T[] ret = env.newTArray(this.numBits());
		t__T[] tmp_b;
		t__T tmp;
		int now = 0;
		tmp_b = this.left.getBits();
		System.arraycopy(tmp_b, 0, ret, now, tmp_b.length);
		now += tmp_b.length;
		tmp_b = this.right.getBits();
		System.arraycopy(tmp_b, 0, ret, now, tmp_b.length);
		now += tmp_b.length;
		return ret;
}

	public Pair<t__T, T1, T2> newObj(t__T[] data) throws Exception {
		if(data == null) {
			data = env.newTArray(this.numBits());
			for(int i=0; i<this.numBits(); ++i) { data[i] = intLib.SIGNAL_ZERO; }
		}
		if(data.length != this.numBits()) return null;
		Pair<t__T, T1, T2> ret = new Pair<t__T, T1, T2>(env, factoryT1, factoryT2);
		t__T[] tmp;
		int now = 0;
		tmp = env.newTArray(this.factoryT1.numBits());
		System.arraycopy(data, now, tmp, 0, tmp.length);
		now += tmp.length;
		ret.left = ret.factoryT1.newObj(tmp);
		tmp = env.newTArray(this.factoryT2.numBits());
		System.arraycopy(data, now, tmp, 0, tmp.length);
		now += tmp.length;
		ret.right = ret.factoryT2.newObj(tmp);
		return ret;
}

}
