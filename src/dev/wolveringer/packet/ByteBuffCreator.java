package dev.wolveringer.packet;

import dev.wolveringer.BungeeUtil.configuration.Configuration;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class ByteBuffCreator {
	@SuppressWarnings("serial")
	private static final class ByteBuffTypeNotFoundException extends RuntimeException {
		ByteBuffTypeNotFoundException(String message) {
			super(message);
		}
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

	public static void copy(ByteBuf from, ByteBuf to) {
		byte[] buff = new byte[from.readableBytes()];
		from.readBytes(buff);
		to.writeBytes(buff);

	}
}
