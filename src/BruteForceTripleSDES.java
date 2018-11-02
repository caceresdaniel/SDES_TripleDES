public class BruteForceTripleSDES extends CASCII {
	public static void main(String[] args) {
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
	}
}
