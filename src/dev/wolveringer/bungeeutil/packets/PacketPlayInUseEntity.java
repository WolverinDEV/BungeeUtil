package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayIn;
import dev.wolveringer.bungeeutil.player.ClientVersion.BigClientVersion;
import dev.wolveringer.bungeeutil.position.Vector3f;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
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
	
	@Override
	public void read(PacketDataSerializer s) {
		
		switch (getBigVersion()) {
		case v1_11:
		case v1_10:
		case v1_9:
		case v1_8:
			
			target = s.readVarInt();
			action = Action.values()[s.readVarInt()];
			if(action == Action.INTERACT_AT)
				location = new Vector3f(s.readFloat(), s.readFloat(), s.readFloat());
			if(action != Action.ATTACK && getBigVersion() != BigClientVersion.v1_8)
				hand = s.readVarInt();
			break;
		case v1_7:
			target = s.readInt();
			action = Action.values()[s.readByte()];
			break;
		default:
			break;
		}
	}
	
	@Override
	public void write(PacketDataSerializer s) {
		switch (getBigVersion()) {
		case v1_11:
		case v1_10:
		case v1_9:
		case v1_8:
			s.writeVarInt(target);
			s.writeVarInt(action.ordinal());
			if (action == Action.INTERACT_AT) {
				s.writeFloat(location.getX());
				s.writeFloat(location.getY());
				s.writeFloat(location.getZ());
			}
			if(action != Action.ATTACK && getBigVersion() != BigClientVersion.v1_8)
				s.writeVarInt(hand);
			break;
		case v1_7:
			s.writeInt(target);
			s.writeByte(action.ordinal());
			break;
		default:
			break;
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

	@Override
	public String toString() {
		return "PacketPlayInUseEntity [target=" + target + ", action=" + action + ", location=" + location + ", hand=" + hand + "]";
	}
}
