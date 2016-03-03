package dev.wolveringer.BungeeUtil.packets;

import dev.wolveringer.BungeeUtil.ClientVersion.BigClientVersion;
import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayIn;
import dev.wolveringer.api.position.Vector3f;
import dev.wolveringer.packet.PacketDataSerializer;

public class PacketPlayInUseEntity extends Packet implements PacketPlayIn {
	public static enum Action {
		INTERACT,
		ATTACK,
		INTERACT_AT;
	}
	
	private int target;
	private Action action;
	private Vector3f location;
	private int hand = 0;
	
	public PacketPlayInUseEntity() {
		super(0x02);
	}
	
	@Override
	public void read(PacketDataSerializer s) {
		target = getBigVersion() == BigClientVersion.v1_7 ? s.readInt() : s.readVarInt();
		action = Action.values()[getBigVersion() == BigClientVersion.v1_7 ? s.readByte() : s.readVarInt()];
		if ((getBigVersion() == BigClientVersion.v1_8 || getBigVersion() == BigClientVersion.v1_9) && action == Action.INTERACT_AT) location = new Vector3f(s.readFloat(), s.readFloat(), s.readFloat());
		if (getBigVersion() == BigClientVersion.v1_9 && action != Action.ATTACK) hand = s.readVarInt();
	}
	
	@Override
	public void write(PacketDataSerializer s) {
		if (getBigVersion() == BigClientVersion.v1_7) {
			s.writeInt(target);
			s.writeByte(action.ordinal());
		}
		else if (getBigVersion() == BigClientVersion.v1_8 || getBigVersion() == BigClientVersion.v1_9) {
			s.writeVarInt(target);
			s.writeVarInt(action.ordinal());
			if (action == Action.INTERACT_AT) {
				s.writeFloat(location.getX());
				s.writeFloat(location.getY());
				s.writeFloat(location.getZ());
			}
			if (getBigVersion() == BigClientVersion.v1_9 && action != Action.ATTACK) s.writeVarInt(hand);
		}
	}
	
	public Action getAction() {
		return action;
	}
	
	public void setAction(Action action) {
		this.action = action;
	}
	
	public int getTarget() {
		return target;
	}
	
	public void setTarget(int target) {
		this.target = target;
	}
	
	public Vector3f getLocation() {
		return location;
	}
	
	public void setLocation(Vector3f location) {
		this.location = location;
	}
	
	public int getHand() {
		return hand;
	}
	
	public void setHand(int hand) {
		this.hand = hand;
	}
	/**
	 *  
	 */
}
