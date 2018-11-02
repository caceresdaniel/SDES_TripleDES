public class BruteForceSDES extends CASCII {

	public static void main(String[] args) {
		String m1 = "1011011001111001001011101111110000111110100000000001110111010001111011111101101100010011000000101101011010101000101111100011101011010111100011101001010111101100101110000010010101110001110111011111010101010100001100011000011010101111011111010011110111001001011100101101001000011011111011000010010001011101100011011110000000110010111111010000011100011111111000010111010100001100001010011001010101010000110101101111111010010110001001000001111000000011110000011110110010010101010100001000011010000100011010101100000010111000000010101110100001000111010010010101110111010010111100011111010101111011101111000101001010001101100101100111001110111001100101100011111001100000110100001001100010000100011100000000001001010011101011100101000111011100010001111101011111100000010111110101010000000100110110111111000000111110111010100110000010110000111010001111000101011111101011101101010010100010111100011100000001010101110111111101101100101010011100111011110101011011";
		byte[] msg1 = StringToByte(m1);
		byte[][] keys = keyGenerator();
		
		for(int i = 0; i < 1024; i++) {
			System.out.print("Decyprtion run on key: ");
			printer(keys[i]);
			byte[] result = SDESDecryptor(keys[i], msg1);
			String plainText = toString(result);
			System.out.println(plainText);
		}
		
		
	}

	public static byte[] SDESDecryptor(byte[] key, byte[] cipherText) {
		byte[] result = new byte[cipherText.length];
		byte[] fragment = new byte[8];
		int count = 0;
		int ipt = 0;
		int ict = 0;
		while (count < cipherText.length / 8) {
			for (int i = 0; i < 8; i++) {
				fragment[i] = cipherText[ict];
				ict++;
			}
			byte[] pt = SDES.Decrypt(key, fragment);
			for (int i = 0; i < 8; i++) {
				result[ipt] = pt[i];
				ipt++;
			}
			count++;
		}
		return result;
	}
	
	public static byte[][] keyGenerator() {
		byte[][] keys = new byte[1024][10];
		for (int i = 0; i < 1024; i++) {
			byte[] s = new byte[11];
			int mask = 1024;
			int count = 0;
			while (mask > 0) {
				if ((mask & i) == 0) {
					s[count] = 0;
				} else {
					s[count] = 1;
				}
				mask = mask >> 1;
				count++;
			}
			int index = 1;
			for (int j = 0; j < 10; j++) {
				keys[i][j] = s[index];
				index++;
			}
		}
		return keys;
	}

	public static byte[] StringToByte(String s) {
		byte[] result = new byte[s.length()];
		char[] c = s.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '0') {
				result[i] = 0;
			} else if (c[i] == '1') {
				result[i] = 1;
			}
		}
		return result;
	}

	// Simple Array printer
	public static void printer(byte[] data) {
		for (int i = 0; i < data.length; i++) {
			System.out.print(data[i]);
		}
		System.out.println();
	}
}
