package task2.std;

import circuits.BitonicSortLib;
import flexsc.CompEnv;

public class ObliviousMergeLib<T> extends BitonicSortLib<T>{
	public ObliviousMergeLib(CompEnv<T> e) {
		super(e);
	}

	public void bitonicMerge(T[][] a, T dir){
		bitonicMerge(a, 0, a.length, dir);
	}
	
	public void bitonicMergeWithPayload(T[][] key, T[][] value, T dir) {
		bitonicMergeWithPayload(key, value, 0, key.length, dir);
	}
}
