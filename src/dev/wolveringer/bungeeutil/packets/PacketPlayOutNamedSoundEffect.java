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
	
	@Override
	public void read(PacketDataSerializer s) {
		switch (getBigVersion()) {
		case v1_11:
		case v1_10:
		case v1_9:
			sound = s.readString(-1);
			soundCategory = s.readVarInt();
			loc = new Location(s.readInt(), s.readInt(), s.readInt()).dividide(8D);
			volume = s.readFloat();
			pitch = getBigVersion() != BigClientVersion.v1_9 ? s.readFloat() : s.readUnsignedByte();
			break;
		case v1_8:
			sound = s.readString(-1);
			loc = new Location(s.readInt(), s.readInt(), s.readInt()).dividide(8D);
			volume = s.readFloat();
			pitch = s.readUnsignedByte();
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
			s.writeString(sound);
			s.writeVarInt(soundCategory);
			loc.multiply(8D);
			s.writeInt(loc.getBlockX());
			s.writeInt(loc.getBlockY());
			s.writeInt(loc.getBlockZ());
			loc.dividide(8D);
			s.writeFloat(volume);
			if(getBigVersion() != BigClientVersion.v1_9)
				s.writeFloat(pitch);
			else
				s.writeByte((int) pitch);
			break;
		case v1_8:
			s.writeString(sound);
			loc.multiply(8D);
			s.writeInt(loc.getBlockX());
			s.writeInt(loc.getBlockY());
			s.writeInt(loc.getBlockZ());
			loc.dividide(8D);
			s.writeFloat(volume);
			s.writeByte((int) pitch);
			break;
		default:
			break;
		}
	}
}
