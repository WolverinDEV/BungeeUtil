package dev.wolveringer.util;

public class ByteString {
	private byte[] string;

	public ByteString(String in) {
		string = in.getBytes();
	}

	public ByteString(byte[] in) {
		string = in;
	}

	public void setString(String in) {
		string = in.getBytes();
	}

	public String getString() {
		return new String(string, 0, string.length);
	}

	public byte[] getBytes() {
		return this.string;
	}
}
