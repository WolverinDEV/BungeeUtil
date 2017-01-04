package dev.wolveringer.bungeeutil;

public class ByteString {
	private byte[] string;

	public ByteString(byte[] in) {
		this.string = in;
	}

	public ByteString(String in) {
		this.string = in.getBytes();
	}

	public byte[] getBytes() {
		return this.string;
	}

	public String getString() {
		return new String(this.string, 0, this.string.length);
	}

	public void setString(String in) {
		this.string = in.getBytes();
	}
}
