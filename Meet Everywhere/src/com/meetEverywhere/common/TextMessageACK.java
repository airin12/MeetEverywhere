package com.meetEverywhere.common;

import java.io.Serializable;

/**
 * Klasa s³u¿¹ca do potwierdzania dorêczenia wiadomoœci. Bluetooth w androidzie,
 * nie udostêpnia szybkiego wykrywania zerwania po³¹czenia przy wyjœciu z
 * zasiêgu urz¹dzenia.
 * Pakiet taki jest odpowiednikiem TCP ACK.
 * 
 * @author marekmagik
 * 
 */
public class TextMessageACK implements Serializable{

	private static final long serialVersionUID = -5542524376260432352L;
	private final int hashcodeOfTextMessage;

	public TextMessageACK(int hashcode) {
		this.hashcodeOfTextMessage = hashcode;
	}

	public int getHashcodeOfTextMessage() {
		return hashcodeOfTextMessage;
	}
	
	@Override
	public boolean equals(Object o1){
		if(hashcodeOfTextMessage == ((TextMessageACK) o1).getHashcodeOfTextMessage()){
			return true;
		}else{
			return false;
		}
	}
}
