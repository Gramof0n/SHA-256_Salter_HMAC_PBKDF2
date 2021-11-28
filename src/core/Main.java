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
		String text = "what do ya want for nothing?";

		hmac.generateKeyFromString("Drugi kljuc");

		System.out.println("Key:");
		System.out.println(hmac.getKeyAsHex());
		System.out.println("HMAC:");
		System.out.println(Formating.byte2HexStr(hmac.generateHMAC(text.getBytes())));

	}

}
