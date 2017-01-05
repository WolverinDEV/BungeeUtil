package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import dev.wolveringer.bungeeutil.player.ClientVersion.BigClientVersion;
import dev.wolveringer.bungeeutil.position.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PacketPlayOutNamedSoundEffect extends Packet implements PacketPlayOut{
	private float volume;
	private float pitch;
	private Location loc;
	private String sound;
	private int soundCategory;

	@SuppressWarnings("deprecation")
	@Override
	public void read(PacketDataSerializer s) {
		switch (this.getBigVersion()) {
		case v1_11:
		case v1_10:
		case v1_9:
			this.sound = s.readString(-1);
			this.soundCategory = s.readVarInt();
			this.loc = new Location(s.readInt(), s.readInt(), s.readInt()).dividide(8D);
			this.volume = s.readFloat();
			this.pitch = this.getBigVersion() != BigClientVersion.v1_9 ? s.readFloat() : s.readUnsignedByte();
			break;
		case v1_8:
			this.sound = s.readString(-1);
			this.loc = new Location(s.readInt(), s.readInt(), s.readInt()).dividide(8D);
			this.volume = s.readFloat();
			this.pitch = s.readUnsignedByte();
			break;
		default:
			break;
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void write(PacketDataSerializer s) {
		switch (this.getBigVersion()) {
		case v1_11:
		case v1_10:
		case v1_9:
			s.writeString(this.sound);
			s.writeVarInt(this.soundCategory);
			this.loc.multiply(8D);
			s.writeInt(this.loc.getBlockX());
			s.writeInt(this.loc.getBlockY());
			s.writeInt(this.loc.getBlockZ());
			this.loc.dividide(8D);
			s.writeFloat(this.volume);
			if(this.getBigVersion() != BigClientVersion.v1_9) {
				s.writeFloat(this.pitch);
			} else {
				s.writeByte((int) this.pitch);
			}
			break;
		case v1_8:
			s.writeString(this.sound);
			this.loc.multiply(8D);
			s.writeInt(this.loc.getBlockX());
			s.writeInt(this.loc.getBlockY());
			s.writeInt(this.loc.getBlockZ());
			this.loc.dividide(8D);
			s.writeFloat(this.volume);
			s.writeByte((int) this.pitch);
			break;
		default:
			break;
		}
	}
}
