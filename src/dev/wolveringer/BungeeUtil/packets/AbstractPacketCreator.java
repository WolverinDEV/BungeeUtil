package dev.wolveringer.BungeeUtil.packets;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.md_5.bungee.protocol.Protocol;
import net.md_5.bungee.protocol.ProtocolConstants.Direction;
import dev.wolveringer.BungeeUtil.ClientVersion.ProtocollVersion;
import dev.wolveringer.BungeeUtil.packets.Packet.ProtocollId;
import dev.wolveringer.BungeeUtil.BungeeUtil;
import dev.wolveringer.BungeeUtil.CostumPrintStream;
import dev.wolveringer.BungeeUtil.Player;
import dev.wolveringer.chat.ChatColor.ChatColorUtils;
import dev.wolveringer.terminal.table.TerminalTable;
import dev.wolveringer.terminal.table.TerminalTable.Align;
import dev.wolveringer.terminal.table.TerminalTable.TerminalColumn;

public abstract class AbstractPacketCreator {
	public int calculate(ProtocollVersion version,Protocol p, Direction d, Integer id) {
		int x = ((version.ordinal() & 0xFF) << 16) | ((p.ordinal() & 0x0F) << 12) | ((d.ordinal() & 0x0F) << 8) | (id & 0xFF);
		return x;
	}

	public ProtocollVersion getPacketVersion(int base){
		return ProtocollVersion.values()[((int) (base >> 16)) & 0xFF];
	}
	
	public int getPacketId(int base) {
		return base & 0xFF;
	}

	public Protocol getProtocoll(int base) {
		return Protocol.values()[((int) (base >> 12)) & 0x0F];
	}

	public Direction getDirection(int base) {
		int size = ((int) (base >> 8)) & 0x0F;
		if(Direction.values().length < size)
			return Direction.TO_SERVER;
		return Direction.values()[size];
	}

	public Packet getPacket(ProtocollVersion version,Protocol s, Direction d, ByteBuf x, Player p) {
		if(x.readableBytes() < 1)
			return null;
		x.markReaderIndex().markWriterIndex();
		Packet y = getPacket0(version,s, d, (int)(x.readUnsignedByte()), x, p); // faster
		x.resetReaderIndex().resetWriterIndex();
		return y;
	}

	public void listPackets(CostumPrintStream out) {
		HashMap<ProtocollVersion, HashMap<Direction, HashMap<Integer, Class<? extends Packet>>>> packets = new HashMap<ProtocollVersion, HashMap<Direction, HashMap<Integer, Class<? extends Packet>>>>();
		for (ProtocollVersion c : ProtocollVersion.values()) {
			if(!c.isSupported())
				continue;
			packets.put(c, new HashMap<Direction, HashMap<Integer, Class<? extends Packet>>>());
			for (Direction d : Direction.values())
				packets.get(c).put(d, new HashMap<Integer, Class<? extends Packet>>());
		}
		
		for (Class<? extends Packet> packet : getRegisteredPackets()) {
			for(ProtocollVersion version : packets.keySet()){
				int compressed = getPacketId(version, packet);
				if(getPacketId(compressed) != 0xFF)
					packets.get(version).get(getDirection(compressed)).put(getPacketId(compressed), packet);
			}
		}
		
		HashMap<ProtocollVersion, Integer> rowMapping = new HashMap<>();
		List<TerminalColumn> columns = new ArrayList<>();
		columns.add(new TerminalColumn("§eName", Align.RIGHT));
		columns.add(new TerminalColumn("§eDirection", Align.CENTER));
		List<ProtocollVersion> versions = Arrays.asList(ProtocollVersion.values());
		Collections.sort(versions,new Comparator<ProtocollVersion>() {
			@Override
			public int compare(ProtocollVersion o1, ProtocollVersion o2) {
				return Integer.compare(o1.getProtocollVersion(), o2.getProtocollVersion());
			}
		});
		for(ProtocollVersion version : versions){
			if(version == ProtocollVersion.Unsupported)
				continue;
			rowMapping.put(version, columns.size());
			columns.add(new TerminalColumn("§e"+version.name(), Align.CENTER));
		}
		TerminalTable table = new TerminalTable(columns.toArray(new TerminalColumn[0]));
		
		HashMap<Class<? extends Packet>, String[]> packetRow = new HashMap<Class<? extends Packet>, String[]>();
		for(Entry<ProtocollVersion, HashMap<Direction, HashMap<Integer, Class<? extends Packet>>>> protocolls : packets.entrySet()){
			for(Entry<Direction, HashMap<Integer, Class<? extends Packet>>> directions : protocolls.getValue().entrySet()){
				List<Entry<Integer, Class<? extends Packet>>> packetIds = new ArrayList<>(directions.getValue().entrySet());
				Collections.sort(packetIds, new Comparator<Entry<Integer, Class<? extends Packet>>>() {
					@Override
					public int compare(Entry<Integer, Class<? extends Packet>> o1, Entry<Integer, Class<? extends Packet>> o2) {
						return Integer.compare(o1.getKey(), o1.getKey());
					}
				});
				
				for(Entry<Integer, Class<? extends Packet>> packet : packetIds){
					if(!packetRow.containsKey(packet.getValue())){
						String[] ids = new String[columns.size()];
						Arrays.fill(ids, 2, ids.length, "§6nan");
						packetRow.put(packet.getValue(), ids);
					}
					String packetIdHex = "§c0x"+(Integer.toHexString(packet.getKey()).length() == 1 ? "0":"")+Integer.toHexString(packet.getKey()).toUpperCase();
					packetRow.get(packet.getValue())[rowMapping.get(protocolls.getKey())] = packetIdHex;
					if(packetRow.get(packet.getValue())[0] == null){
						packetRow.get(packet.getValue())[0] = "§6"+packet.getValue().getName().replaceAll(packet.getValue().getName().split("\\.")[packet.getValue().getName().split("\\.").length - 1], ChatColorUtils.COLOR_CHAR+"b" + packet.getValue().getName().split("\\.")[packet.getValue().getName().split("\\.").length - 1]);
					}
					if(packetRow.get(packet.getValue())[1] == null){
						packetRow.get(packet.getValue())[1] = "§a"+directions.getKey().name();
					}
				}
			}
		}
	}

	public void listPackets() {
		listPackets(new CostumPrintStream() {
			@Override
			public void println(String s) {
				BungeeUtil.getInstance().sendMessage(s);
			}

			@Override
			public void print(String s) {
				BungeeUtil.getInstance().sendMessage(s);
			}
		});
	}

	public abstract Packet getPacket0(ProtocollVersion version,Protocol protocol, Direction d, Integer id, ByteBuf b, Player p);

	public abstract int loadPacket(ProtocollVersion version,Protocol p, Direction d, Integer id, Class<? extends Packet> clazz);

	public abstract void registerPacket(Protocol p, Direction d, Class<? extends Packet> clazz,ProtocollId... ids);

	public abstract void unregisterPacket(ProtocollVersion version,Protocol p, Direction d, Integer id);

	public abstract int countPackets();

	public abstract int getPacketId(ProtocollVersion version,Class<? extends Packet> clazz);

	public abstract List<Class<? extends Packet>> getRegisteredPackets();
}
