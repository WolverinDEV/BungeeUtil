package dev.wolveringer.bungeeutil.packetlib;

import dev.wolveringer.bungeeutil.packets.Packet;

public interface PacketHandler<T extends Packet> {
	public void handle(PacketHandleEvent<T> e);
}
