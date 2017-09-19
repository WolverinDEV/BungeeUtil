package dev.wolveringer.network;

import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;

public class Cleaner {
	public static long cleaned_bytes = 0;

	public static void destroyDirectByteBuffer(ByteBuf buf) {
		try{
			ReferenceCountUtil.release(buf);
		}catch(Exception e){
			System.out.print("Error: 101");
		}
	}
}