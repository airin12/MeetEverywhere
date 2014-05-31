package com.meetEverywhere.common;

import java.io.Serializable;

/**
 * Klasa s�u��ca do potwierdzania dor�czenia wiadomo�ci. Bluetooth w androidzie,
 * nie udost�pnia szybkiego wykrywania zerwania po��czenia przy wyj�ciu z
 * zasi�gu urz�dzenia.
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
