public class BruteForceTripleSDES extends CASCII {
	public static void main(String[] args) {
		String m1 = "00011111100111111110011111101100111000000011001011110010101010110001011101001101000000110011010111111110000000001010111111000001010010111001111001010101100000110111100011111101011100100100010101000011001100101000000101111011000010011010111100010001001000100001111100100000001000000001101101000000001010111010000001000010011100101111001101111011001001010001100010100000";
		byte[] msg1 = StringToByte(m1);
		byte[][] keys = keyGenerator();

		for (int j = 0; j < 1024; j++) {
			for (int i = 0; i < 1024; i++) {
				System.out.print("Decryption run on keys: ");
				printer(keys[j]);
				System.out.print(" and ");
				printer(keys[i]);
				System.out.println();
				byte[] result = TSDESDecryptor(keys[j],keys[i], msg1);
				String plainText = toString(result);
				System.out.println(plainText);
			}
		}

	}

	public static byte[] TSDESDecryptor(byte[] key1, byte[] key2, byte[] cipherText) {
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
			byte[] pt = TripleSDES.Decrypt(key1,key2, fragment);
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
	}
}
