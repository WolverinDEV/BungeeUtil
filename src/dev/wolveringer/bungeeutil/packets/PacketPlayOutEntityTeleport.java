package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import dev.wolveringer.bungeeutil.position.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PacketPlayOutEntityTeleport extends Packet implements PacketPlayOut {
	Location loc;
	int id;
	private boolean onGround;

	public void read(PacketDataSerializer s) {
		if(getVersion().getVersion() < 16)
			id = s.readInt();
		else
			id = s.readVarInt();
		
		switch (getBigVersion()) {
		case v1_9:
		case v1_10:
		case v1_11:
			loc = new Location(s.readDouble(), s.readDouble(), s.readDouble(),((float)s.readByte())/ 256.0F * 360.0F,((float)s.readByte())/ 256.0F * 360.0F);
			break;
		case v1_8:
		case v1_7:
			loc = new Location(s.readInt(), s.readInt(), s.readInt(), ((float)s.readByte())/ 256.0F * 360.0F,((float)s.readByte())/ 256.0F * 360.0F).dividide(32D);
			break;
		default:
			break;
		}
		if(getVersion().getVersion() >= 22){
			onGround = s.readBoolean();
		}
	}

	@Override
	public void write(PacketDataSerializer s) {
		if(getVersion().getVersion() < 16){
			s.writeInt(id);
		}else{
			s.writeVarInt(id);
		}
		
		switch (getBigVersion()) {
		case v1_9:
		case v1_10:
		case v1_11:
			s.writeDouble(loc.getX());
			s.writeDouble(loc.getY());
			s.writeDouble(loc.getZ());
			
			s.writeByte((int) (loc.getYaw() * 256.0F / 360.0F));
			s.writeByte((int) (loc.getPitch() * 256.0F / 360.0F));
			break;
		case v1_8:
		case v1_7:
			loc = loc.multiply(32D);
			s.writeInt(loc.getBlockX());
			s.writeInt(loc.getBlockY());
			s.writeInt(loc.getBlockZ());

			s.writeByte((int) (loc.getYaw() * 256.0F / 360.0F));
			s.writeByte((int) (loc.getPitch() * 256.0F / 360.0F));
			break;
		default:
			break;
		}

		if(getVersion().getVersion() >= 22)
			s.writeBoolean(this.onGround);
	}
}
