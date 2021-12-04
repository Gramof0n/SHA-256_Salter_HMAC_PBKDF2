package core;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import utils.Formating;

public class PasswordSalter {

	private final Random r = new Random();
	private MessageDigest md;
	public byte[] salt;

	public PasswordSalter() {

	}

	public void generateRandomSalt() {
		salt = new byte[32];
		r.nextBytes(salt);
	}

	public void generateSaltFromString(String saltString) {
		salt = saltString.getBytes();
	}

	public byte[] salt(String pass) throws NoSuchAlgorithmException {
		if (salt == null) {
			System.err.println("No salt generated");
			return null;
		}
		md = MessageDigest.getInstance("SHA-256");

		md.update(Formating.concatenateByteArrays(pass.getBytes(), salt));
		byte[] digest = md.digest();

		return digest;
	}

	public String saltAsHex() {
		if (salt == null) {
			System.err.println("No salt generated");
			return "";
		}
		return Formating.byte2HexStr(salt);
	}

}
