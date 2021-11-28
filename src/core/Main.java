package core;

import java.security.NoSuchAlgorithmException;

import utils.Formating;

public class Main {

	public static void main(String[] args) throws NoSuchAlgorithmException {

		PasswordSalter salter = new PasswordSalter();
		salter.generateRandomSalt();
		System.out.println(Formating.byte2HexStr(salter.salt("test")));
		System.out.println(salter.saltAsHex());
	}

}
