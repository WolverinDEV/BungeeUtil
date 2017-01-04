package dev.wolveringer.bungeeutil.packetlib.reader;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import com.google.common.primitives.UnsignedBytes;

import dev.wolveringer.bungeeutil.Configuration;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class ByteBuffCreator {
	@SuppressWarnings("serial")
	private static final class ByteBuffTypeNotFoundException extends RuntimeException {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		ByteBuffTypeNotFoundException(String message) {
			super(message);
		}
	}

	public static void copy(ByteBuf from, ByteBuf to) {
		byte[] buff = new byte[from.readableBytes()];
		from.readBytes(buff);
		to.writeBytes(buff);
	}

	public static ByteBuf createByteBuff() {
		switch (Configuration.getByteBuffType().toLowerCase()) {
			case "direct":
				return Unpooled.directBuffer();
			case "heap":
				return Unpooled.buffer();
			default:
				throw new ByteBuffTypeNotFoundException("ByteBuff Type '" + Configuration.getByteBuffType().toLowerCase() + "' is missing");
		}
	}

	public static void main(String[] args) throws Exception{
		UUID uuid = UUID.nameUUIDFromBytes("WolverinDEV".getBytes());
		System.out.println("UUID: "+uuid+" ("+uuid.toString().equalsIgnoreCase("a323593b-db7e-34e9-b235-eba6187c9b7b")+")");
		System.out.println("UUID: a323593b-db7e-34e9-f235-eba6187c9b7b");

		MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nsae) {
            throw new InternalError("MD5 not supported");
        }
        byte[] md5Bytes = md.digest("WolverinDEV".getBytes());
        md5Bytes[6]  &= 0x0f;  /* clear version        */
        md5Bytes[6]  |= 0x30;  /* set to version 3     */
        //md5Bytes[8]  &= 0x3f;  /* clear variant        */
        //md5Bytes[8]  |= 0x80;  /* set to IETF variant  */

        int index = 0;
        for(byte b : md5Bytes) {
			System.out.println(index+++" -> "+Integer.toHexString(UnsignedBytes.toInt(b)));
		}
	}
}
