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
	private Location loc;
	private int id;
	private boolean onGround;

	@Override
	public void read(PacketDataSerializer s) {
		if(this.getVersion().getVersion() < 16) {
			this.id = s.readInt();
		} else {
			this.id = s.readVarInt();
		}

		switch (this.getBigVersion()) {
		case v1_9:
		case v1_10:
		case v1_11:
			this.loc = new Location(s.readDouble(), s.readDouble(), s.readDouble(),s.readByte()/ 256.0F * 360.0F,s.readByte()/ 256.0F * 360.0F);
			break;
		case v1_8:
		case v1_7:
			this.loc = new Location(s.readInt(), s.readInt(), s.readInt(), s.readByte()/ 256.0F * 360.0F,s.readByte()/ 256.0F * 360.0F).dividide(32D);
			break;
		default:
			break;
		}
		if(this.getVersion().getVersion() >= 22){
			this.onGround = s.readBoolean();
		}
	}

	@Override
	public void write(PacketDataSerializer s) {
		if(this.getVersion().getVersion() < 16){
			s.writeInt(this.id);
		}else{
			s.writeVarInt(this.id);
		}

		switch (this.getBigVersion()) {
		case v1_9:
		case v1_10:
		case v1_11:
			s.writeDouble(this.loc.getX());
			s.writeDouble(this.loc.getY());
			s.writeDouble(this.loc.getZ());

			s.writeByte((int) (this.loc.getYaw() * 256.0F / 360.0F));
			s.writeByte((int) (this.loc.getPitch() * 256.0F / 360.0F));
			break;
		case v1_8:
		case v1_7:
			this.loc = this.loc.multiply(32D);
			s.writeInt(this.loc.getBlockX());
			s.writeInt(this.loc.getBlockY());
			s.writeInt(this.loc.getBlockZ());

			s.writeByte((int) (this.loc.getYaw() * 256.0F / 360.0F));
			s.writeByte((int) (this.loc.getPitch() * 256.0F / 360.0F));
			break;
		default:
			break;
		}

		if(this.getVersion().getVersion() >= 22) {
			s.writeBoolean(this.onGround);
		}
	}
}
