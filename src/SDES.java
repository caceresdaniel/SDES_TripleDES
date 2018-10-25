public class SDES {
	public static void main(String[] args) 
	{
		byte[] key = {1,0,1,0,0,0,0,0,1,0};
		
		byte[] newKey = P10(key);
		
		for(int i =0; i < newKey.length;i++) {
			System.out.println(newKey[i]);
		}
		

	}
	
	public static byte[] Encrypt(byte[] rawkey, byte[] plaintext) {
		return null;
	}
	
	public static byte[] Decrypt(byte[] rawkey, byte[] ciphertext) {
		return null;
	}
	
	public static void InitialPermutaion() {
		
	}
	// takes in data (ciphertext, plaintext) and a 10-bit key, the 10-bit key generates 2 8-bit keys
	public static void ComplexFunction() {
		
	}
	
	public static void SinglePermutation() {
		
	}
	
	public static byte[] P10(byte[] rawkey) {
		
		byte[] pKey = new byte[10];
		byte[] firstHalf = new byte[5];
		byte[] secondHalf = new byte[5];
		
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
		
		for(int i = 0; i < 5; i++) {
			firstHalf[i] = pKey[i];
		}
		
		for(int i = 5; i < 10; i++) {
			secondHalf[i - 5] = pKey[i];
		}
		
		byte[] shiftedFirst = circularLeftShift(firstHalf);
		byte[] shiftedSecond = circularLeftShift(secondHalf);
		
		for(int i = 0; i < 5; i++) {
			pKey[i] = shiftedFirst[i];
		}
		for(int i = 5; i < 10; i++) {
			pKey[i] = shiftedSecond[i-5];
		}
		
		return pKey;
	}
	
	public static byte[] circularLeftShift(byte[] partialKey) {
		byte[] newPartial = new byte[5];
		
		for(int i = 3; i > -1; i--) {
			newPartial[i] = partialKey[i+1]; 
		}
		
		newPartial[4] = partialKey[0];
		
		return newPartial;
	}
	
	public static byte[] P8(byte[] rawkey) {
		
		return null;
	}
}
