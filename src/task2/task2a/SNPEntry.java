package task2.task2a;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;


public class SNPEntry  implements Comparable<SNPEntry>{
	static public MessageDigest sha1;
	static {
		try {
			sha1 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public long location;
	public String value;// ATCG: 0123

	public SNPEntry(long loc, String val) {
		location = loc;
		value = val;
	}
	
	public static long HashToLong(String a, int range){
		sha1.update(a.getBytes());
		long res = ByteBuffer.wrap( sha1.digest()).getLong();
		return Math.abs(res);
	}
	
	public static BigInteger HashToBI(String a, int range){
		BigInteger m = BigInteger.ONE;
		sha1.update(a.getBytes());
		BigInteger res = new BigInteger(sha1.digest());
		return res.mod(m.pow(range-1));
	}

	public SNPEntry() {
	}
	
	@Override
	public boolean  equals(Object obj) {
		if(this == obj){
			return true;            
		}
		SNPEntry o = (SNPEntry) obj;
		if (o!=null ) {
			if(value == o.value &&
					location == o.location
					)
				return true;
			else return false;

		} else {
			return false;
		}
	};

	@Override
	public String toString() {
		return location+""+value;
	}
	
	public BigInteger hash() {
		sha1.update(value.getBytes());
		sha1.update(ByteBuffer.allocate(8).putLong(location));
		return new BigInteger(sha1.digest());
	}
	
	@Override
	public int compareTo(SNPEntry o) { 
		return hash().compareTo(o.hash());
	}
	
	public static class AscComparator implements Comparator<SNPEntry> {
		@Override
		public int compare(SNPEntry o1, SNPEntry o2) {
			return o1.compareTo(o2);
		}
	}

	public static class DscComparator implements Comparator<SNPEntry> {
		@Override
		public int compare(SNPEntry o1, SNPEntry o2) {
			return o2.compareTo(o1);
		}
	}
}