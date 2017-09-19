package dev.wolveringer.bungeeutil.packets.types;

import dev.wolveringer.bungeeutil.packets.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public abstract class PacketPlayXXXHeldItemSlot extends Packet {
	private int slot;
}
