package dev.wolveringer.BungeeUtil;

import dev.wolveringer.BungeeUtil.packets.Packet;

public interface PacketHandler<T extends Packet> {
	public void handle(PacketHandleEvent<T> e);
}
