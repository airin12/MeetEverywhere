package com.meetEverywhere.common.messages;

import java.io.Serializable;

/**
 * Klasa służąca do potwierdzania doręczenia wiadomości. Bluetooth w androidzie,
 * nie udostępnia szybkiego wykrywania zerwania połączenia przy wyjściu z
 * zasięgu urządzenia.
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
