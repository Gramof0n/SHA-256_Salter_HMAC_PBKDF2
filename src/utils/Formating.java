package utils;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Formating {

	public static byte[] hexStr2Byte(String hexStr) {
		int len = hexStr.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(hexStr.charAt(i), 16) << 4)
					+ Character.digit(hexStr.charAt(i + 1), 16));
		}
		return data;
	}

	final protected static char[] hexArray = "0123456789abcdef".toCharArray();

	public static String byte2HexStr(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	public static String stringToHex(String arg) {
		return String.format("%02x", new BigInteger(1, arg.getBytes(StandardCharsets.UTF_8)));
	}

	public static byte[] padWithPKCS7(byte[] plain, int blockSize) {
		int paddingAmount = blockSize - (plain.length % blockSize);
		byte[] out = new byte[plain.length + paddingAmount];

		byte[] padLookup = { 0x10, 0x0f, 0x0e, 0x0d, 0x0c, 0x0b, 0x0a, 0x09, 0x08, 0x07, 0x06, 0x05, 0x04, 0x03, 0x02,
				0x01 };

		for (int i = 0; i < plain.length; i++) {
			out[i] = plain[i];
		}

		for (int i = plain.length; i < out.length; i++) {
			out[i] = padLookup[plain.length % blockSize];
		}

		return out;
	}

	public static byte[] unpadPKCS7(byte[] padded, int blocksize) {
		int padLen = padded[padded.length - 1];
		byte[] out = new byte[padded.length - padLen];

		for (int i = 0; i < out.length; i++) {
			out[i] = padded[i];
		}
		return out;
	}

	public static byte[] longToBytes(long x) {
		ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
		buffer.putLong(x);
		return buffer.array();
	}

	public static long bytesToLong(byte[] bytes) {
		ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
		buffer.put(bytes);
		buffer.flip();
		return buffer.getLong();
	}

	public static byte[] concatenateByteArrays(byte[] first, byte[] second) {
		byte[] concatenated = new byte[first.length + second.length];

		System.arraycopy(first, 0, concatenated, 0, first.length);
		System.arraycopy(second, 0, concatenated, first.length, second.length);

		return concatenated;
	}
}
