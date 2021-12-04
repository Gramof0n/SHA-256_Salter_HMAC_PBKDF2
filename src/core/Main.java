package core;

import java.security.NoSuchAlgorithmException;

import utils.Formating;

public class Main {

	public static void main(String[] args) throws NoSuchAlgorithmException {

		PasswordSalter salter = new PasswordSalter();
		salter.generateRandomSalt();
		System.out.println(Formating.byte2HexStr(salter.salt("test")));
		System.out.println(salter.saltAsHex());

		System.out.println();
		System.out.println("===============HMAC==============");

		HMAC hmac = new HMAC();
		String text = "test";

		hmac.generateKeyFromString("testing");

		System.out.println("Key:");
		System.out.println(hmac.getKeyAsHex());
		System.out.println("HMAC:");
		System.out.println(Formating.byte2HexStr(hmac.generateHMAC(text.getBytes())));
		System.out.println("----------PBKDF2----------------");
		PBKDF2 pbkdf = new PBKDF2();

		byte[] derivedKey = pbkdf.deriveKey(text.getBytes(), "test".getBytes(), 10, 301);
		System.out.println("Derived key: " + Formating.byte2HexStr(derivedKey));
		;
	}

}
