package dev.wolveringer.BungeeUtil;

import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;
import dev.wolveringer.BungeeUtil.packets.Packet;

public class PacketHandleEvent<T extends Packet> extends Event implements Cancellable {
	private boolean cancelled = false;
	private T packet;
	private Player player;

	public PacketHandleEvent(T p, Player pl) {
		this.packet = p;
		this.player = pl;
	}

	public T getPacket() {
		return packet;
	}

	public Player getPlayer() {
		return player;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean c) {
		this.cancelled = c;
	}

	public void setPacket(T p) {
		this.packet = p;
	}

	/**
	 * {@code The player change dont will change the destination of the packet!!!}
	 */

	public void setPlayer(Player pl) {
		this.player = pl;
	}

	@Override
	public String toString() {
		return "PacketHandleEvent@" + System.identityHashCode(this) + "[cancelled=" + this.cancelled + ", packet=" + this.packet + ", player=" + this.player + "]";
	}
}
