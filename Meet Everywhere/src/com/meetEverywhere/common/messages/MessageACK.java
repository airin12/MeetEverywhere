package com.meetEverywhere.common.messages;

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
public class MessageACK implements Serializable{

	private static final long serialVersionUID = -5542524376260432352L;
	private final int hashcodeOfMessage;

	public MessageACK(int hashcode) {
		this.hashcodeOfMessage = hashcode;
	}

	public int getHashcodeOfMessage() {
		return hashcodeOfMessage;
	}
	
	@Override
	public boolean equals(Object o1){
        return hashcodeOfMessage == ((MessageACK) o1).getHashcodeOfMessage();
	}
}
