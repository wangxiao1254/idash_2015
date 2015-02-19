package task2.task2bco;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;





public class SNPEntry  implements Comparable<SNPEntry>{
	static public MessageDigest sha1;
	static {
		try {
			sha1 = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public long location;
	public String value;// ATCG: 0123
	public int op;
	public SNPEntry(long loc, String val, int op) {
		location = loc;
		value = val;
		this.op = op;
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
	
	public String hashPos(int i) {
		sha1.update(ByteBuffer.allocate(8).putLong(location).array());
		sha1.update(ByteBuffer.allocate(4).putInt(i).array());
		return new String(sha1.digest());
	}
	public String hashPosVal(int i) {
		sha1.update(ByteBuffer.allocate(8).putLong(location).array());
		sha1.update(ByteBuffer.allocate(4).putInt(i).array());
		sha1.update(value.getBytes());
		String a = new String(sha1.digest());
//		System.out.println(a);
		return a;
	}
	
	public String Pos(int i) {
		return location+""+i;
	}
	
	public String PosVal(int i) {
		return location+""+i+value;
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