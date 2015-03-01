package task2.bloomFilter;


import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class BF {
	public MessageDigest sha1;
	public int n, m, k;
	public double dk;
	public boolean[] bs;
	public byte[][]sks;
	SecureRandom rnd = new SecureRandom();
	
	public BF(int n, int m, double constant) {
		init(n,m,constant);	
	}

	public BF(int n, int m) {
		init(n,m,Math.log(2));
	}

	public void init(int n, int m, double constant) {
		this.n  = n;
		this.m = m;
		this.k = 1;
		bs  = new boolean[m];
		sks = new byte[k][10];
		for(int i = 0; i < k ; ++i)
			rnd.nextBytes(sks[i]);
		
		try {
			sha1 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int hash(byte[] sk, String str) {
		sha1.update(str.getBytes());
		sha1.update(sk);
		byte[] a = sha1.digest();

		return  (int) Math.abs(ByteBuffer.wrap(a).getLong() % m);
	}

	ByteBuffer buffer = ByteBuffer.allocate(10+8);
	public int hash(byte[] sk, long index) {
		buffer.clear();
		sha1.update(buffer.putLong(index).put(sk));
		byte[] a = sha1.digest();
		return  (int) Math.abs(ByteBuffer.wrap(a).getLong() % m);
	}

	public void insert(long index) {
		for(int i = 0; i < k; ++i) {
			bs[hash(sks[i], index)] = true;
		}
	}
	
	public void insert(String s) {
		for(int i = 0; i < k; ++i) {
			bs[hash(sks[i], s)] = true;
		}
	}

	public boolean query(int index) {
		for(int i = 0; i < k; ++i) {
			if(bs[hash(sks[i], index)] == false)
				return false;
		}
		return true;
	}

	public int size() {
		int res = 0;
		for(int i = 0; i < m; ++i)
			if(bs[i])
				res++;
		return countToSize(res);
	}

	public int countToSize(double tmp) {
		double f = tmp;
		f = 1 - f / m;
		double log1 = Math.log(f);
		double log2 = Math.log(1-1.0/m);
		return (int) (log1/log2/k);
//		double f = count;
//		f = f/m;
//		f = 1-f;
//		f = Math.log(f);
//		f = f*m;
//		f = -1*(f / dk);
//		return (int)f;
	}
	
	public static int countToSize(double count, double kk, int mm) {
		double f = count;
		f = 1 - f / mm;
		double log1 = Math.log(f);
		double log2 = Math.log(1-1.0/mm);
		return (int) (log1/log2/kk);
//		double f = count;
//		f = f/mm;
//		f = 1-f;
//		f = Math.log(f);
//		f = f*mm;
//		f = -1*(f / kk);
//		return (int)f;
	}
}
