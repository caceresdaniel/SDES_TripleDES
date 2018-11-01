public class TripleSDES {
	public static void main(String[] args) {

		byte[] k1 = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		byte[] k2 = { 1, 0, 0, 0, 1, 0, 1, 1, 1, 0 };
		byte[] k3 = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
		byte[] k4 = { 0, 1, 1, 0, 1, 0, 1, 1, 1, 0 };
		byte[] k5 = { 1, 0, 1, 1, 1, 0, 1, 1, 1, 1 };
		byte[] k6 = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
		byte[] pt1 = { 0, 0, 0, 0, 0, 0, 0, 0 };
		byte[] pt2 = { 1, 1, 0, 1, 0, 1, 1, 1 };
		byte[] pt3 = { 1, 0, 1, 0, 1, 0, 1, 0 };
		byte[] ct1 = { 1, 1, 1, 0, 0, 1, 1, 0 };
		byte[] ct2 = { 0, 1, 0, 1, 0, 0, 0, 0 };
		byte[] ct3 = { 1, 0, 0, 0, 0, 0, 0, 0 };
		byte[] ct4 = { 1, 0, 0, 1, 0, 0, 1, 0 };

		byte[] r1 = Encrypt(k1, k1, pt1);
		byte[] r2 = Encrypt(k2, k4, pt2);
		byte[] r3 = Encrypt(k2, k4, pt3);
		byte[] r4 = Encrypt(k3, k3, pt3);
		byte[] r5 = Decrypt(k2, k2, ct1);
		byte[] r6 = Decrypt(k5, k2, ct2);
		byte[] r7 = Decrypt(k1, k1, ct3);
		byte[] r8 = Decrypt(k6, k6, ct4);

		System.out.println("*****************Start of Answers for Q's******************");
		System.out.println("*****************CipherText Answers******************");
		printer(r1);
		printer(r2);
		printer(r3);
		printer(r4);
		System.out.println("*****************PlainText Answers******************");
		printer(r5);
		printer(r6);
		printer(r7);
		printer(r8);

	}

	// E3DES(p) = EDES(k1,DDES(k2,EDES(k1, p)))
	public static byte[] Encrypt(byte[] rawkey1, byte[] rawkey2, byte[] plaintext) {
		byte[] cipherText = NormalEncrypt(rawkey1, NormalDecrypt(rawkey2, NormalEncrypt(rawkey1, plaintext)));
		return cipherText;
	}

	// D3DES(c) = DDES(k1,EDES(k2,DDES(k1, c)))
	public static byte[] Decrypt(byte[] rawkey1, byte[] rawkey2, byte[] ciphertext) {
		byte[] plainText = NormalDecrypt(rawkey1, NormalEncrypt(rawkey2, NormalDecrypt(rawkey1, ciphertext)));
		return plainText;
	}

	// Created the 2 keys from the 10-bit key entered
	// since the function I created combines the key I split it in half and get key
	// 1 and key 2
	// I then go down the order of functions necessary to encrypt
	public static byte[] NormalEncrypt(byte[] rawkey, byte[] plaintext) {
		byte[] k1 = new byte[8];
		byte[] k2 = new byte[8];
		byte[] combinedKeys = P10(rawkey);

		for (int i = 0; i < 8; i++) {
			k1[i] = combinedKeys[i];
		}
		for (int i = 8; i < 16; i++) {
			k2[i - 8] = combinedKeys[i];
		}

		byte[] initPermData = IP(plaintext);
		byte[] firstCall = fk(initPermData, k1);
		byte[] switched = SwitchFunction(firstCall);
		byte[] lastCall = fk(switched, k2);
		byte[] cipherText = iIP(lastCall);

		return cipherText;
	}

	// Same as encryption but this time the steps are opposite to decrypt
	public static byte[] NormalDecrypt(byte[] rawkey, byte[] ciphertext) {
		byte[] k1 = new byte[8];
		byte[] k2 = new byte[8];
		byte[] combinedKeys = P10(rawkey);

		for (int i = 0; i < 8; i++) {
			k1[i] = combinedKeys[i];
		}
		for (int i = 8; i < 16; i++) {
			k2[i - 8] = combinedKeys[i];
		}

		byte[] inversePermutation = iIP(ciphertext);
		byte[] firstCall = fk(inversePermutation, k2);
		byte[] switched = SwitchFunction(firstCall);
		byte[] lastCall = fk(switched, k1);
		byte[] plainText = IP(lastCall);
		return plainText;
	}

	// This is the first step in encryption and last step in decryption
	// the PlainText/CipherText is permutated there seems to be an actual formula
	// but
	// since the indices were given and what where goes just hardcoded
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

	// fk receives the data two times the first time it receives it is after the
	// inital permutation when encrypting or inverse permutation when decrpyting
	// the first time around it grabs the first 4 bits, and sends the last 4 bits
	// to the F function the result of the F function and the first for bits are
	// then XOR'd for the result
	// after they are XOR'd they are then sent to the swap function which swaps the
	// first 4 bits with the last 4 bits
	// and then repeats the process
	public static byte[] fk(byte[] data, byte[] key) {
		byte[] R = new byte[4];
		byte[] L = new byte[4];

		for (int i = 0; i < 4; i++) {
			L[i] = data[i];
		}
		for (int i = 4; i < 8; i++) {
			R[i - 4] = data[i];
		}

		byte[] result1 = F(R, key);

		L[0] = (byte) (L[0] ^ result1[0]);
		L[1] = (byte) (L[1] ^ result1[1]);
		L[2] = (byte) (L[2] ^ result1[2]);
		L[3] = (byte) (L[3] ^ result1[3]);

		for (int i = 0; i < 4; i++) {
			data[i] = L[i];
		}

		return data;
	}

	// swaps the first 4 bits with the last 4 bits
	public static byte[] SwitchFunction(byte[] data) {
		for (int i = 0; i < 4; i++) {
			byte first = data[0];
			System.arraycopy(data, 1, data, 0, data.length - 1);
			data[data.length - 1] = first;
		}
		return data;
	}

	// receives data with size of 4 bytes, this data is then permutated and turned
	// into 8 bytes
	// the data is then xor'd with the given key, key depends on what iteration of
	// fk is being done
	// and if its decrptyion or encryption, after it has been XORD, the data gets
	// split in half
	// the left side goes to S0 and the right side goes to S1, the results are then
	// returned to fk
	public static byte[] F(byte[] data, byte[] key) {
		byte[] expansion = new byte[8];
		byte[] firstHalf = new byte[4];
		byte[] secondHalf = new byte[4];
		byte[] combined = new byte[4];

		expansion[0] = data[3];
		expansion[1] = data[0];
		expansion[2] = data[1];
		expansion[3] = data[2];
		expansion[4] = data[1];
		expansion[5] = data[2];
		expansion[6] = data[3];
		expansion[7] = data[0];

		byte[] resultFromXOR = XOR(expansion, key);

		for (int i = 0; i < 4; i++) {
			firstHalf[i] = resultFromXOR[i];
		}
		for (int i = 4; i < 8; i++) {
			secondHalf[i - 4] = resultFromXOR[i];
		}

		byte[] S0Result = S0(firstHalf);
		byte[] S1Result = S1(secondHalf);

		for (int i = 0; i < 2; i++) {
			combined[i] = S0Result[i];
		}
		for (int i = 2; i < 4; i++) {
			combined[i] = S1Result[i - 2];
		}

		byte[] result = P4(combined);

		return result;
	}

	// inverse of the inital permutation, as stated before theres a algorithm
	// dont know the formula so just hard coded it
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

	// Receives the data from Fsubk which is a 4 byte data string that was expanded
	// to 8 bytes
	// which is then xor'd with the key corresponding to what part it is on (k1 for
	// first half, k2 for second half)
	public static byte[] XOR(byte[] data, byte[] key) {
		byte[] result = new byte[8];

		result[0] = (byte) (data[0] ^ key[0]);
		result[1] = (byte) (data[1] ^ key[1]);
		result[2] = (byte) (data[2] ^ key[2]);
		result[3] = (byte) (data[3] ^ key[3]);
		result[4] = (byte) (data[4] ^ key[4]);
		result[5] = (byte) (data[5] ^ key[5]);
		result[6] = (byte) (data[6] ^ key[6]);
		result[7] = (byte) (data[7] ^ key[7]);

		return result;
	}

	// receives the first half of data that was processed in the F function and then
	// finds
	// the corresponding row and col, finds the row and col by using parts of the
	// data and
	// converting it in to decimal and using that as reference for the 4x4 matrix,
	// the result
	// from the matrix is then turned into binary
	public static byte[] S0(byte[] data) {
		int[][] s0 = { { 1, 3, 0, 3 }, { 0, 2, 2, 1 }, { 3, 1, 1, 3 }, { 2, 0, 3, 2 } };
		byte[] p1 = new byte[2];
		byte[] p2 = new byte[2];
		int row, col;

		p1[0] = data[0];
		p1[1] = data[3];
		p2[0] = data[1];
		p2[1] = data[2];

		row = binToDec(p1);
		col = binToDec(p2);

		int dec = s0[col][row];

		byte[] half = DecToByteRay(dec);

		return half;
	}

	// Same as S0 but for right side bits
	public static byte[] S1(byte[] data) {
		int[][] s1 = { { 0, 2, 3, 2 }, { 1, 0, 0, 1 }, { 2, 1, 1, 0 }, { 3, 3, 0, 3 } };
		byte[] p1 = new byte[2];
		byte[] p2 = new byte[2];
		int row, col;

		p1[0] = data[0];
		p1[1] = data[3];
		p2[0] = data[1];
		p2[1] = data[2];

		row = binToDec(p1);
		col = binToDec(p2);

		int dec = s1[col][row];

		byte[] half = DecToByteRay(dec);

		return half;
	}

	// converts decimal to binary in a very horrible way
	public static byte[] DecToByteRay(int dec) {
		byte[] bytes = new byte[2];

		if (dec == 0) {
			bytes[0] = 0;
			bytes[1] = 0;
		} else if (dec == 1) {
			bytes[0] = 0;
			bytes[1] = 1;
		} else if (dec == 2) {
			bytes[0] = 1;
			bytes[1] = 0;
		} else if (dec == 3) {
			bytes[0] = 1;
			bytes[1] = 1;
		}

		return bytes;
	}

	// converts binary to decimal
	public static int binToDec(byte[] bits) {
		int ans = 0;

		if (bits[0] == 0 && bits[1] == 0) {
			ans = 0;
		} else if (bits[0] == 0 && bits[1] == 1) {
			ans = 1;
		} else if (bits[0] == 1 && bits[1] == 0) {
			ans = 2;
		} else if (bits[0] == 1 && bits[1] == 1) {
			ans = 3;
		}

		return ans;
	}

	// as all permutations go there is an algorithm but i dont know it
	// after the permutation the data is split into two halves,
	// both halves do a circular left shift by 1 bit
	// after the first shift they are combined and then sent P8
	// which then gives the first key
	// the combined from before are then split again and the another circular left
	// shift but
	// by 2 bits this time, recombined then sent to P8 to generate 2nd key
	// keys are combined into 16 bit key for ease of use later on in program
	public static byte[] P10(byte[] rawkey) {
		byte[] pKey = new byte[10];
		byte[] firstHalf = new byte[5];
		byte[] secondHalf = new byte[5];

		// Permutating the key
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

		// These two for loops seperate the permutated key into 2 halves
		for (int i = 0; i < 5; i++) {
			firstHalf[i] = pKey[i];
		}
		for (int i = 5; i < 10; i++) {
			secondHalf[i - 5] = pKey[i];
		}

		// Circular shift left once on the both halves of the key
		byte[] shiftedFirst = circularLeftShift(firstHalf, 1);
		byte[] shiftedSecond = circularLeftShift(secondHalf, 1);

		// combining those two halves back together
		byte[] combinedKey1 = KeyCombiner(shiftedFirst, shiftedSecond);

		// then permutating the combined halves and creating key 1
		byte[] k1 = P8(combinedKey1);

		// the halves go through another iteration of circular left shift but twice this
		// time
		byte[] secondShift1 = circularLeftShift(shiftedFirst, 2);
		byte[] secondShift2 = circularLeftShift(shiftedSecond, 2);

		// they are once again combined back together
		byte[] combinedKey2 = KeyCombiner(secondShift1, secondShift2);

		// the new combined data goes though another permutation to generate key 2
		byte[] k2 = P8(combinedKey2);

		byte[] combinedKeys = new byte[16];

		for (int i = 0; i < k1.length; i++) {
			combinedKeys[i] = k1[i];
		}
		for (int i = 8; i < 16; i++) {
			combinedKeys[i] = k2[i - 8];
		}

		return combinedKeys;
	}

	// as all permutatoins dont know algorithm so hard coded
	// this is used to find the keys
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

	// this is used after the Sboxes have been utilized and
	// the data is now turned into a 4 bit sized data
	// which is then sent here to be permutated
	public static byte[] P4(byte[] data) {
		byte[] p4 = new byte[4];

		p4[0] = data[1];
		p4[1] = data[3];
		p4[2] = data[2];
		p4[3] = data[0];

		return p4;
	}

	// circular left shift by n amount of shifts
	public static byte[] circularLeftShift(byte[] partialKey, int shift) {
		for (int i = 0; i < shift; i++) {
			byte first = partialKey[0];
			System.arraycopy(partialKey, 1, partialKey, 0, partialKey.length - 1);
			partialKey[partialKey.length - 1] = first;
		}
		return partialKey;
	}

	// just combines two byte arrays
	public static byte[] dataCombiner(byte[] set1, byte[] set2) {
		byte[] combinedData = new byte[10];

		for (int i = 0; i < 4; i++) {
			combinedData[i] = set1[i];
		}
		for (int i = 4; i < 8; i++) {
			combinedData[i] = set2[i - 4];
		}
		return combinedData;
	}

	// used this to combine the keys
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

	// just a for loop to print out arrays
	public static void printer(byte[] data) {
		for (int i = 0; i < data.length; i++) {
			System.out.print(data[i]);
		}
		System.out.println();
	}
}
