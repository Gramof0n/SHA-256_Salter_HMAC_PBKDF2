package core;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import utils.Formating;

public class Main {

	public static void main(String[] args) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		String test = "Testni string ok";
		Random r = new Random();

		byte[] salt = new byte[32];

		r.nextBytes(salt);

		md.update(Formating.concatenateByteArrays(test.getBytes(StandardCharsets.UTF_8), salt));

		byte[] digest = md.digest();

		System.out.println(Formating.byte2HexStr(digest));
		System.out.println("Salt: " + Formating.byte2HexStr(salt));

	}

}
