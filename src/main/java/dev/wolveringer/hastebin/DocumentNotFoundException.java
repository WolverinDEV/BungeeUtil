package dev.wolveringer.hastebin;

@SuppressWarnings("serial")
public class DocumentNotFoundException extends RuntimeException{
	public DocumentNotFoundException(String message) {
		super(message);
	}
	
}
