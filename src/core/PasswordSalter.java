package core;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import utils.Formating;

public class PasswordSalter {

	private final Random r = new Random();
	private MessageDigest md;

	public PasswordSalter() {

	}

	public byte[] salt(String pass) throws NoSuchAlgorithmException {
		md = MessageDigest.getInstance("SHA-256");
		byte[] salt = new byte[32];

		r.nextBytes(salt);

		md.update(Formating.concatenateByteArrays(pass.getBytes(), salt));
		byte[] digest = md.digest();

		return digest;
	}

}
