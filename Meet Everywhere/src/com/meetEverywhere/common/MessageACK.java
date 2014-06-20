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
		if(hashcodeOfMessage == ((MessageACK) o1).getHashcodeOfMessage()){
			return true;
		}else{
			return false;
		}
	}
}
