package core;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import utils.Formating;

public class PBKDF2 {

	private static short HLEN = 32;
	private HMAC hmac;

	private int l;
	private int r;

	public PBKDF2() {
		hmac = new HMAC();
	}

	public byte[] deriveKey(byte[] password, byte[] salt, int iterationCount, int desiredKeyLength) {
		if (desiredKeyLength > (Math.pow(2, 32) - 1) * HLEN) {
			System.err.println("Desired key too long");
			return null;
		}

		l = (int) Math.ceil((double) desiredKeyLength / HLEN);
		r = desiredKeyLength - (l - 1) * HLEN;

		ByteBuffer bb = ByteBuffer.allocate(HLEN * l);

		for (int i = 0; i < l; i++) {
			bb.put(F(password, salt, iterationCount, i));
		}

		byte[] concated = bb.array();
		byte[] desiredKey = Arrays.copyOfRange(concated, 0, desiredKeyLength);

		System.out.println("Concated: " + Formating.byte2HexStr(concated));
		System.out.println("Last block len: " + r);
		System.out.println("Derived length: " + desiredKey.length);

		return desiredKey;

	}

	private byte[] F(byte[] password, byte[] salt, int iterationCount, int i) {
		List<byte[]> U = new ArrayList<byte[]>();
		byte[] salt_int_concat = Formating.concatenateByteArrays(salt, Formating.intToByteArray(i));

		hmac.generateKeyFromString(new String(password));

		for (int j = 0; j < iterationCount; j++) {
			if (j == 0) {
				U.add(hmac.generateHMAC(salt_int_concat));
				continue;
			}

			U.add(hmac.generateHMAC(U.get(j - 1)));
		}

		byte[] out = U.get(0);

		for (int j = 1; j < U.size(); j++) {
			out = Formating.xorSameLenByteArrays(out, U.get(j), 0);
		}

		System.out.println("Out as string: " + Formating.byte2HexStr(out));
		return out;

	}

}
