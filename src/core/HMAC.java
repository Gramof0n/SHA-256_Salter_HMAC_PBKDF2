package core;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import utils.Formating;

public class HMAC {

	private final int BLOCK_SIZE = 32;
	private final byte[] IPAD = { 0x36, 0x36, 0x36, 0x36, 0x36, 0x36, 0x36, 0x36, 0x36, 0x36, 0x36, 0x36, 0x36, 0x36,
			0x36, 0x36, 0x36, 0x36, 0x36, 0x36, 0x36, 0x36, 0x36, 0x36, 0x36, 0x36, 0x36, 0x36, 0x36, 0x36, 0x36,
			0x36 };
	private final byte[] OPAD = { 0x5C, 0x5C, 0x5C, 0x5C, 0x5C, 0x5C, 0x5C, 0x5C, 0x5C, 0x5C, 0x5C, 0x5C, 0x5C, 0x5C,
			0x5C, 0x5C, 0x5C, 0x5C, 0x5C, 0x5C, 0x5C, 0x5C, 0x5C, 0x5C, 0x5C, 0x5C, 0x5C, 0x5C, 0x5C, 0x5C, 0x5C,
			0x5C };

	private MessageDigest md;
	private byte[] key;
	private Random r;

	public HMAC() {
		r = new Random();
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public void generateRandomKey() {
		key = new byte[BLOCK_SIZE];
		r.nextBytes(key);
	}

	public void generateKeyFromString(String keyString) {
		if (keyString.getBytes().length < BLOCK_SIZE) {
			key = new byte[BLOCK_SIZE];
			for (int i = 0; i < BLOCK_SIZE; i++) {
				if (i < keyString.getBytes().length) {
					key[i] = keyString.getBytes()[i];
					continue;
				}
				key[i] = 0x00;
			}
			return;
		}
		if (keyString.getBytes().length > BLOCK_SIZE) {
			key = md.digest(keyString.getBytes());
			return;
		}

		key = keyString.getBytes();
	}

	public byte[] generateHMAC(byte[] text) {
		if (key == null) {
			System.err.println("Key not initialized");
			return null;
		}

		byte[] xor_key_ipad = Formating.xorSameLenByteArrays(key, IPAD);
		byte[] xor_key_opad = Formating.xorSameLenByteArrays(key, OPAD);

		byte[] key_text_concat = Formating.concatenateByteArrays(xor_key_ipad, text);

		byte[] key_text_concat_hashed = md.digest(key_text_concat);

		return md.digest(Formating.concatenateByteArrays(xor_key_opad, key_text_concat_hashed));
	}

	public String getKeyAsHex() {
		return Formating.byte2HexStr(key);
	}

}
