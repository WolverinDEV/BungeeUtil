package dev.wolveringer.BungeeUtil.packets;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import net.md_5.bungee.protocol.Protocol;
import net.md_5.bungee.protocol.ProtocolConstants.Direction;
import dev.wolveringer.BungeeUtil.ClientVersion.BigClientVersion;
import dev.wolveringer.BungeeUtil.CostumPrintStream;
import dev.wolveringer.BungeeUtil.Main;
import dev.wolveringer.BungeeUtil.Player;
import dev.wolveringer.chat.ChatColor.ChatColorUtils;

public abstract class AbstractPacketCreator {
	public int calculate(BigClientVersion version,Protocol p, Direction d, Integer id) {
		int x = ((version.ordinal() & 0x0F) << 16) | ((p.ordinal() & 0x0F) << 12) | ((d.ordinal() & 0x0F) << 8) | (id & 0xFF);
		return x;
	}

	public BigClientVersion getPacketVersion(int base){
		return BigClientVersion.values()[((int) (base >> 16)) & 0x0F];
	}
	
	public int getPacketId(int base) {
		return base & 0xFF;
	}

	public Protocol getProtocoll(int base) {
		return Protocol.values()[((int) (base >> 12)) & 0x0F];
	}

	public Direction getDirection(int base) {
		return Direction.values()[((int) (base >> 8)) & 0x0F];
	}

	public Packet getPacket(BigClientVersion version,Protocol s, Direction d, ByteBuf x, Player p) {
		x.markReaderIndex().markWriterIndex();
		int id;
		Packet y = getPacket0(version,s, d, (int)(id = x.readUnsignedByte()), x, p); // faster
		x.resetReaderIndex().resetWriterIndex();
		return y;
	}

	public void listPackets(CostumPrintStream out) {
		HashMap<Protocol, HashMap<Direction, HashMap<Integer, Class<? extends Packet>>>> packets = new HashMap<Protocol, HashMap<Direction, HashMap<Integer, Class<? extends Packet>>>>();
		for (Protocol c : Protocol.values()) {
			packets.put(c, new HashMap<Direction, HashMap<Integer, Class<? extends Packet>>>());
			for (Direction d : Direction.values())
				packets.get(c).put(d, new HashMap<Integer, Class<? extends Packet>>());
		}
		
		for (Class<? extends Packet> packet : getRegisteredPackets()) {
			int compressed = getPacketId(BigClientVersion.v1_9,packet); //TODO list bouth id's
			if(compressed == -1)
				compressed = getPacketId(BigClientVersion.v1_8,packet);
			packets.get(getProtocoll(compressed)).get(getDirection(compressed)).put(getPacketId(compressed), packet);
		}
		
		out.println(ChatColorUtils.COLOR_CHAR+"cPackete:");
		for (Protocol x : Protocol.values()) {
			out.println(" "+ChatColorUtils.COLOR_CHAR+"eProtocol: "+ChatColorUtils.COLOR_CHAR+"5" + x.name());
			ArrayList typs = new ArrayList();
			
			for (Direction d : Direction.values())
				typs.addAll(packets.get(x).get(d).values());
				
			int i = 0;
			boolean conatainsRedefined = false;
			for (Object o : typs)
				if (o != null) {
					if (o.toString().endsWith("$-1")) conatainsRedefined = true;
				}
			for (Object o : typs)
				if (o != null) if (o.toString().length() + (conatainsRedefined ? " (Redefined)".length() : 0) > i) i = o.toString().length() + (conatainsRedefined ? " (Redefined)".length() : 0);
			for (Direction d : Direction.values()) {
				TreeMap<Integer, String> a = new TreeMap<Integer, String>();
				for (Integer c : packets.get(x).get(d).keySet()) {
					String s = "  "+ChatColorUtils.COLOR_CHAR+"6PacketName: "+ChatColorUtils.COLOR_CHAR+"a";
					if (packets.get(x).get(d).get(c) == null) continue;
					String name = packets.get(x).get(d).get(c).getName().replace("$-1", "");
					s += right( name.replaceAll(name.split("\\.")[name.split("\\.").length - 1], ChatColorUtils.COLOR_CHAR+"b" + name.split("\\.")[name.split("\\.").length - 1]), i); //"+COLOR_CHAR+"c(Redefined)
					s += "  "+ChatColorUtils.COLOR_CHAR+"6ID: "+ChatColorUtils.COLOR_CHAR+"e" + right("0x" + (Integer.toHexString(c).length() == 1 ? "0" + Integer.toHexString(c) : Integer.toHexString(c)).toUpperCase(), 4) + " "+ChatColorUtils.COLOR_CHAR+"6Direction: "+ChatColorUtils.COLOR_CHAR+"5" + d.toString().toUpperCase();
					a.put(c, s);
				}
				for (Integer c : a.keySet())
					out.println(a.get(c));
			}
		}
	}

	private String right(String s, int i) {
		while (s.length() < i) {
			s += " ";
		}
		return s;
	}

	public void listPackets() {
		listPackets(new CostumPrintStream() {
			@Override
			public void println(String s) {
				Main.sendMessage(s);
			}

			@Override
			public void print(String s) {
				Main.sendMessage(s);
			}
		});
	}

	/**
	 * 20000 ns faster
	 * 
	 * @param protocol
	 * @param d
	 * @param id
	 * @param b
	 * @param p
	 * @return
	 */
	public abstract Packet getPacket0(BigClientVersion version,Protocol protocol, Direction d, Integer id, ByteBuf b, Player p);

	public abstract int loadPacket(BigClientVersion version,Protocol p, Direction d, Integer id, Class<? extends Packet> clazz);

	public abstract void registerPacket(Protocol p, Direction d, Integer v1_8_id,Integer v1_9_id, Class<? extends Packet> clazz);

	public abstract void unregisterPacket(BigClientVersion version,Protocol p, Direction d, Integer id);

	public abstract int countPackets();

	public abstract int getPacketId(BigClientVersion version,Class<? extends Packet> clazz);

	public abstract List<Class<? extends Packet>> getRegisteredPackets();
}
