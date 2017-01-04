package dev.wolveringer.bungeeutil.packets.creator;

import io.netty.buffer.ByteBuf;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.protocol.Protocol;
import net.md_5.bungee.protocol.ProtocolConstants.Direction;
import dev.wolveringer.bungeeutil.BungeeUtil;
import dev.wolveringer.bungeeutil.CostumPrintStream;
import dev.wolveringer.bungeeutil.chat.ChatColorUtils;
import dev.wolveringer.bungeeutil.packets.Packet;
import dev.wolveringer.bungeeutil.packets.Packet.ProtocollId;
import dev.wolveringer.bungeeutil.player.Player;
import dev.wolveringer.bungeeutil.player.connection.ProtocollVersion;
import dev.wolveringer.terminal.string.ColoredChar;
import dev.wolveringer.terminal.table.TerminalTable;
import dev.wolveringer.terminal.table.TerminalTable.Align;
import dev.wolveringer.terminal.table.TerminalTable.TerminalColumn;
import dev.wolveringer.terminal.table.TerminalTable.TerminalRow;

public abstract class AbstractPacketCreator {
	public int calculate(ProtocollVersion version, Protocol p, Direction d, Integer id) {
		int x = ((version.ordinal() & 0xFF) << 16) | ((p.ordinal() & 0x0F) << 12) | ((d.ordinal() & 0x0F) << 8) | (id & 0xFF);
		return x;
	}
	
	public ProtocollVersion getPacketVersion(int base) {
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
		if (Direction.values().length < size)
			return Direction.TO_SERVER;
		return Direction.values()[size];
	}

	public Packet getPacket(ProtocollVersion version, Protocol s, Direction d, ByteBuf buffer, Player p) {
		if (buffer.readableBytes() < 1)
			return null;
		int readerIndex = buffer.readerIndex();
		Packet packet = getPacket0(version, s, d, (int) buffer.readUnsignedByte(), buffer, p);
		buffer.readerIndex(readerIndex);
		return packet;
	}

	public void listPackets(CostumPrintStream out) {
		HashMap<ProtocollVersion, HashMap<Direction, HashMap<Integer, Class<? extends Packet>>>> packets = new HashMap<ProtocollVersion, HashMap<Direction, HashMap<Integer, Class<? extends Packet>>>>();
		for (ProtocollVersion c : ProtocollVersion.values()) {
			if (!c.isSupported())
				continue;
			packets.put(c, new HashMap<Direction, HashMap<Integer, Class<? extends Packet>>>());
			for (Direction d : Direction.values())
				packets.get(c).put(d, new HashMap<Integer, Class<? extends Packet>>());
		}

		for (Class<? extends Packet> packet : getRegisteredPackets()) {
			for (ProtocollVersion version : packets.keySet()) {
				int compressed = getPacketId(version, packet);
				if (getPacketId(compressed) != 0xFF)
					packets.get(version).get(getDirection(compressed)).put(getPacketId(compressed), packet);
			}
		}

		HashMap<ProtocollVersion, Integer> rowMapping = new HashMap<>();
		List<TerminalColumn> columns = new ArrayList<>();
		columns.add(new TerminalColumn("§eName", Align.RIGHT));
		columns.add(new TerminalColumn("§eDirection", Align.CENTER));
		List<ProtocollVersion> versions = Arrays.asList(ProtocollVersion.values());
		Collections.sort(versions, new Comparator<ProtocollVersion>() {
			@Override
			public int compare(ProtocollVersion o1, ProtocollVersion o2) {
				return Integer.compare(o1.getProtocollVersion(), o2.getProtocollVersion());
			}
		});
		for (ProtocollVersion version : versions) {
			if (version == ProtocollVersion.Unsupported)
				continue;
			rowMapping.put(version, columns.size());
			columns.add(new TerminalColumn("§e" + version.name(), Align.CENTER));
		}
		TerminalTable table = new TerminalTable(columns.toArray(new TerminalColumn[0]));

		HashMap<Class<? extends Packet>, String[]> packetRow = new HashMap<Class<? extends Packet>, String[]>();
		for (Entry<ProtocollVersion, HashMap<Direction, HashMap<Integer, Class<? extends Packet>>>> protocolls : packets.entrySet()) {
			for (Entry<Direction, HashMap<Integer, Class<? extends Packet>>> directions : protocolls.getValue().entrySet()) {
				List<Entry<Integer, Class<? extends Packet>>> packetIds = new ArrayList<>(directions.getValue().entrySet());
				for (Entry<Integer, Class<? extends Packet>> packet : packetIds) {
					if (!packetRow.containsKey(packet.getValue())) {
						String[] ids = new String[columns.size()];
						Arrays.fill(ids, 2, ids.length, "§6nan");
						packetRow.put(packet.getValue(), ids);
					}
					String packetIdHex = "§c0x" + (Integer.toHexString(packet.getKey()).length() == 1 ? "0" : "") + Integer.toHexString(packet.getKey()).toUpperCase();
					packetRow.get(packet.getValue())[rowMapping.get(protocolls.getKey())] = packetIdHex;
					if (packetRow.get(packet.getValue())[0] == null) {
						packetRow.get(packet.getValue())[0] = "§6" + packet.getValue().getName().replaceAll(packet.getValue().getName().split("\\.")[packet.getValue().getName().split("\\.").length - 1], ChatColorUtils.COLOR_CHAR + "b" + packet.getValue().getName().split("\\.")[packet.getValue().getName().split("\\.").length - 1]);
					}
					if (packetRow.get(packet.getValue())[1] == null) {
						packetRow.get(packet.getValue())[1] = "§a" + directions.getKey().name();
					}
				}
			}
		}
		
		List<String[]> rows = new ArrayList<>(packetRow.values());
		Collections.sort(rows, new Comparator<String[]>() {
			@Override
			public int compare(String[] o1, String[] o2) {
				return o1[0].compareTo(o2[0]);
			}
		});
		for (String[] row : rows)
			table.addRow(row);
		table.setRowSeperator(new TerminalTable.RowSeperator() {
			@Override
			public ColoredChar getSeperator(TerminalRow row, int rowIndex, int columnFrom, int columnTo) {
				if(columnFrom < 2)
					return new ColoredChar("|");
				String oldPacketIds = row.getColumns()[columnFrom].get(rowIndex);
				String newPacketIds = row.getColumns()[columnTo].get(rowIndex);
				if(oldPacketIds.equalsIgnoreCase("§6nan"))
					oldPacketIds = "0x-1";
				if(newPacketIds.equalsIgnoreCase("§6nan"))
					newPacketIds = "0x-1";
				BigInteger oldPacketId = new BigInteger(ChatColor.stripColor(oldPacketIds).substring(2),16);
				BigInteger newPacketId = new BigInteger(ChatColor.stripColor(newPacketIds).substring(2),16);
				switch (oldPacketId.compareTo(newPacketId)) {
				case -1:
					return new ColoredChar("§a≠");
				case 0:
					return new ColoredChar("§7|");
				case 1:
					return new ColoredChar("§a≠");
				default:
					break;
				}
				return new ColoredChar("§5X");
			}
			@Override
			public ColoredChar getDefaultSeperator() {
				return new ColoredChar("§7|");
			}
		});
		
		for (String line : table.buildLines())
			out.println(line);
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

	public abstract Packet getPacket0(ProtocollVersion version, Protocol protocol, Direction d, Integer id, ByteBuf b, Player p);

	public abstract int loadPacket(ProtocollVersion version, Protocol p, Direction d, Integer id, Class<? extends Packet> clazz);

	public abstract void registerPacket(Protocol p, Direction d, Class<? extends Packet> clazz, ProtocollId... ids);

	public abstract void unregisterPacket(ProtocollVersion version, Protocol p, Direction d, Integer id);

	public abstract int countPackets();

	public abstract int getPacketId(ProtocollVersion version, Class<? extends Packet> clazz);

	public abstract List<Class<? extends Packet>> getRegisteredPackets();
	
	public abstract <T extends Packet> void releasePacket(T obj);
}
