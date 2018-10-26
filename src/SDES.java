import java.io.UnsupportedEncodingException;

public class SDES {
	public static void main(String[] args) throws UnsupportedEncodingException {
		byte[] key = { 1, 0, 1, 0, 0, 0, 0, 0, 1, 0 };
		byte[] tess = {0,1,0,0};

		byte[] newKey = P10(key);
		
		byte[] test = S0(tess);
		
		System.out.println();
		for (int i = 0; i < newKey.length; i++) {
			System.out.print(newKey[i]);
		}

	}

	public static byte[] Encrypt(byte[] rawkey, byte[] plaintext) {
		byte[] cipherText = new byte[8];
		return cipherText;
	}

	public static byte[] Decrypt(byte[] rawkey, byte[] ciphertext) {
		byte[] plainText = new byte[8];
		return plainText;
	}

	public static byte[] IP(byte[] data) {
		byte[] permutatedData = new byte[8];
		
		permutatedData[0] = data[1];
		permutatedData[1] = data[5];
		permutatedData[2] = data[2];
		permutatedData[3] = data[0];
		permutatedData[4] = data[3];
		permutatedData[5] = data[7];
		permutatedData[6] = data[4];
		permutatedData[7] = data[6];
		
		return permutatedData;
	}
	
	public static byte[] iIP(byte[] data) {
		byte[] ipermutatedData = new byte[8];
		
		ipermutatedData[0] = data[3];
		ipermutatedData[1] = data[0];
		ipermutatedData[2] = data[2];
		ipermutatedData[3] = data[4];
		ipermutatedData[4] = data[6];
		ipermutatedData[5] = data[1];
		ipermutatedData[6] = data[7];
		ipermutatedData[7] = data[5];
		
		return ipermutatedData;
	}

	public static void Fsubk(byte[] data) {
		byte[] expansion = new byte[8];
		byte[] firstHalf = new byte[4];
		byte[] secondHalf = new byte[4];
		
		expansion[0] = data[3];
		expansion[1] = data[0];
		expansion[2] = data[1];
		expansion[3] = data[2];
		expansion[4] = data[1];
		expansion[5] = data[2];
		expansion[6] = data[3];
		expansion[7] = data[0];
		
		for(int i = 0; i < 4; i++) {
			firstHalf[i] = expansion[i];
		}
		for(int i = 4; i < 8; i++) {
			secondHalf[i - 4] = expansion[i];
		}
		
		
	}
	
	//Receives the data from Fsubk which is a 4 byte data string that was expanded to 8 bytes
	//which is then xor'd with the key corresponding to what part it is on (k1 for first half, k2 for second half)
	public static void XOR(byte[] data, byte[] key) {
		byte[] result = new byte[8];
		
		result[0] = (byte) (data[0] ^ key[0]);
		result[1] = (byte) (data[1] ^ key[1]);
		result[2] = (byte) (data[2] ^ key[2]);
		result[3] = (byte) (data[3] ^ key[3]);
		result[4] = (byte) (data[4] ^ key[4]);
		result[5] = (byte) (data[5] ^ key[5]);
		result[6] = (byte) (data[6] ^ key[6]);
		result[7] = (byte) (data[7] ^ key[7]);
		
	}
	
	public static byte[] S0(byte[] data) throws UnsupportedEncodingException {
		int[][] s0 = {{1,3,0,3},{0,2,2,1},{3,1,1,3},{2,0,3,2}};
		byte[] p1 = new byte[2];
		byte[] p2 = new byte[2];
		int row, col;
		
		p1[0] = data[0];
		p1[1] = data[3];
		p2[0] = data[1];
		p2[1] = data[2];
		
		row = RowColFinder(p1);
		col = RowColFinder(p2);
		
		int dec = s0[col][row];
		
		byte[] half = DecToByteRay(dec);
		
		return half;
	}
	
	public static byte[] S1(byte[] data) {
		int[][] s1 = {{0,2,3,2},{1,0,0,1},{2,1,1,0},{3,3,0,3}};
		byte[] p1 = new byte[2];
		byte[] p2 = new byte[2];
		int row, col;
		
		p1[0] = data[0];
		p1[1] = data[3];
		p2[0] = data[1];
		p2[1] = data[2];
		
		row = RowColFinder(p1);
		col = RowColFinder(p2);
		
		int dec = s1[col][row];
		
		byte[] half = DecToByteRay(dec);
		
		return half;
	}
	
	public static byte[] DecToByteRay(int dec) {
		byte[] bytes = new bytes[2];
		
		if(dec == 0) {
			bytes[0] = 0;
			bytes[1] = 0;
		} else if(dec == 1) {
			bytes[0] = 0;
			bytes[1] = 1;
		} else if(dec == 2) {
			bytes[0] = 1;
			bytes[1] = 0;
		} else if(dec == 3) {
			bytes[0] = 1;
			bytes[1] = 1;
		}
		
		return bytes;
	}
	
	public static int RowColFinder(byte[] bits) {
		int ans  = 0;
		
		if(bits[0] == 0 && bits[1] == 0) {
			ans = 0;
		} else if(bits[0] == 0 && bits[1] == 1) {
			ans = 1;
		}else if(bits[0] == 1 && bits[1] == 0) {
			ans = 2;
		}else if(bits[0] == 1 && bits[1] == 1) {
			ans = 3;
		}
		
		return ans;
	}

	public static byte[] P10(byte[] rawkey) {
		byte[] pKey = new byte[10];
		byte[] firstHalf = new byte[5];
		byte[] secondHalf = new byte[5];
		
		//Permutating the key
		pKey[0] = rawkey[2];
		pKey[1] = rawkey[4];
		pKey[2] = rawkey[1];
		pKey[3] = rawkey[6];
		pKey[4] = rawkey[3];
		pKey[5] = rawkey[9];
		pKey[6] = rawkey[0];
		pKey[7] = rawkey[8];
		pKey[8] = rawkey[7];
		pKey[9] = rawkey[5];
		
		//These two for loops seperate the permutated key into 2 halves
		for (int i = 0; i < 5; i++) {
			firstHalf[i] = pKey[i];
		}
		for (int i = 5; i < 10; i++) {
			secondHalf[i - 5] = pKey[i];
		}
		
		//Circular shift left once on the both halves of the key
		byte[] shiftedFirst = circularLeftShift(firstHalf, 1);
		byte[] shiftedSecond = circularLeftShift(secondHalf, 1);
		
		//combining those two halves back together
		byte[] combinedKey1 = KeyCombiner(shiftedFirst, shiftedSecond);
		
		//then permutating the combined halves and creating key 1
		byte[] k1 = P8(combinedKey1);
		
		//the halves go through another iteration of circular left shift but twice this time
		byte[] secondShift1 = circularLeftShift(shiftedFirst, 2);
		byte[] secondShift2 = circularLeftShift(shiftedSecond, 2);
		
		//they are once again combined back together
		byte[] combinedKey2 = KeyCombiner(secondShift1, secondShift2);
		
		//the new combined data goes though another permutation to generate key 2
		byte[] k2 = P8(combinedKey2);

		return pKey;
	}
	
	public static byte[] P8(byte[] rawkey) {
		byte[] pKey = new byte[8];
		
		pKey[0] = rawkey[5];
		pKey[1] = rawkey[2];
		pKey[2] = rawkey[6];
		pKey[3] = rawkey[3];
		pKey[4] = rawkey[7];
		pKey[5] = rawkey[4];
		pKey[6] = rawkey[9];
		pKey[7] = rawkey[8];
		
		return pKey;
	}
	
	public static byte[] P4(byte[] data) {
		byte[] p4 = new byte[4];
		
		p4[0] = data[1];
		p4[1] = data[3];
		p4[2] = data[2];
		p4[3] = data[0];
		
		return p4;
	}

	public static byte[] circularLeftShift(byte[] partialKey, int shift) {
			for (int i = 0; i < shift; i++) {
				byte first = partialKey[0];
				System.arraycopy(partialKey, 1, partialKey, 0, partialKey.length-1);
				partialKey[partialKey.length-1] = first;
			}
		return partialKey;
	}
	
	public static byte[] KeyCombiner(byte[] set1, byte[] set2) {
		byte[] combinedKey = new byte[10];
		
		for (int i = 0; i < 5; i++) {
			combinedKey[i] = set1[i];
		}
		for (int i = 5; i < 10; i++) {
			combinedKey[i] = set2[i - 5];
		}
		return combinedKey;
	}

}
