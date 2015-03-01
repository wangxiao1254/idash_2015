package task2.task2b;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class SNPEntry{
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
	public int op;
	public SNPEntry(long loc, String val, int op) {
		location = loc;
		value = val;
		this.op = op;
	}

	public SNPEntry() {
	}
	
	public static long HashToLong(String a, int range) {
		sha1.update(a.getBytes());
		long res = ByteBuffer.wrap( sha1.digest()).getLong();
		return Math.abs(res%(1L<<(range-1)));
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
	
	public String Pos(int i) {
		return location+""+i;
	}
	
	public String PosVal(int i) {
		return value+""+location+""+i;
	}
}