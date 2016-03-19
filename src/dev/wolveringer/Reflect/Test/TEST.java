package dev.wolveringer.Reflect.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;

import dev.wolveringer.BungeeUtil.packets.Packet;
import dev.wolveringer.chat.ChatSerializer;

public class TEST {
	public static void main(String[] args) {
		System.out.print(ChatSerializer.toJSONString(ChatSerializer.fromMessage("�aSleef-Event")));
		/*
		PacketHandler h = new PacketHandler() {
			public void handle(PacketHandleEvent e) {
				Main.sendMessage(e.getPacket());
			};
		};

		PacketLib.addHandler(new PacketHandler<PacketPlayOutChat>() {
			@Override
			public void handle(PacketHandleEvent<PacketPlayOutChat> e) {
				//Handle Packet
			}
		});
		
		PacketLib.addHandler(h);
		PacketLib.handle(new PacketHandleEvent<PacketPlayOutChat>(new PacketPlayOutChat(), null));
		PacketLib.handle(new PacketHandleEvent<Packet>(new PacketPlayOutCloseWindow(), null));
		*/
		/*
		 * /*
		 * String s = "HELLO: ";
		 * for(int i = 0;i<100;i++){
		 * try{
		 * Thread.sleep(100);
		 * }catch (InterruptedException e){
		 * e.printStackTrace();
		 * }
		 * s+=".";
		 * System.out.print("\r"+s);
		 * }
		 * long ns = System.nanoTime();
		 * long ms = System.currentTimeMillis();
		 * try{
		 * Thread.sleep(1);
		 * }catch (InterruptedException e){
		 * e.printStackTrace();
		 * }
		 * System.out.print((System.nanoTime()-ns)+":"+(System.currentTimeMillis(
		 * )-ms));
		 */
	}

	private static Class getPacketType(Class s) {
		for(Type interfaces : s.getGenericInterfaces())
			for(Type c : ((ParameterizedType) interfaces).getActualTypeArguments())
				try{
					if(c.equals(Packet.class))
						continue;
					return Class.forName(c.toString().split(" ")[1]);
				}catch (ClassNotFoundException e){
					e.printStackTrace();
				}
		return Packet.class;
	}

	private static boolean checkVersion(String version1, String version2) {
		String[] v1 = version1.split("\\.");
		String[] v2 = version2.split("\\.");
		for(int i = 0;i < Collections.max(Arrays.asList(v1.length, v2.length));i++){
			if(i + 1 < v1.length && i + 1 >= v2.length)
				return !isNotZero(Arrays.copyOfRange(v1, i + 1, v1.length));
			else if(i + 1 < v2.length && i + 1 >= v1.length)
				return isNotZero(Arrays.copyOfRange(v2, i + 1, v2.length));
			else if(Integer.parseInt(v1[i]) < Integer.parseInt(v2[i]))
				return true;
		}
		return false;
	}

	private static boolean isNotZero(String[] x) {
		for(String s : x)
			if(Integer.parseInt(s) != 0)
				return true;
		return false;
	}
}