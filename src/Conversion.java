public class Conversion extends CASCII {
	public static void main(String[] args) {
		// Problem 1 of Part 3 where I Encrypt the word CRYPTOGRAPHY using SDES with
		// provided key
		String plainText = "CRYPTOGRAPHY";
		byte[] key = { 0, 1, 1, 1, 0, 0, 1, 1, 0, 1 };
		byte[] plainTextToByte = Convert(plainText);
		byte[] result = SDESEncryptor(key, plainTextToByte);
		System.out.println("PlainText");
		printer(plainTextToByte);
		System.out.println("CipherText");
		printer(result);

		byte[] te = SDESDecryptor(key, result);
		
		printer(te);
		String asdf = toString(te);
		System.out.println(asdf);
		
		// End of Problem 1 Part 3
	}

	// This method receives the plaintext which in this case is CRYPTOGARPHY which
	// was earlier converted to binary
	// the 64 bits are split into groups of 8 and then sent through the SDES
	// Encyrptor
	// after going through the encryptor they are concatinated into one array which
	// is the result
	public static byte[] SDESEncryptor(byte[] key, byte[] plainText) {
		byte[] cipherText = new byte[plainText.length];
		byte[] fragment = new byte[8];
		int count = 0;
		int ipt = 0;
		int ict = 0;
		while (count < plainText.length / 8) {
			for (int i = 0; i < 8; i++) {
				fragment[i] = plainText[ipt];
				ipt++;
			}

			byte[] ct = SDES.Encrypt(key, fragment);
			for (int i = 0; i < 8; i++) {
				cipherText[ict] = ct[i];
				ict++;
			}
			count++;
		}
		return cipherText;
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

	// Simple Array printer
	public static void printer(byte[] data) {
		for (int i = 0; i < data.length; i++) {
			System.out.print(data[i]);
		}
		System.out.println();
	}
}
