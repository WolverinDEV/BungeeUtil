package dev.wolveringer.BungeeUtil.packets;

import dev.wolveringer.BungeeUtil.ClientVersion.BigClientVersion;
import dev.wolveringer.BungeeUtil.ClientVersion.ProtocollVersion;
import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayOut;
import dev.wolveringer.api.SoundEffect;
import dev.wolveringer.api.position.Location;
import dev.wolveringer.packet.PacketDataSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//1.8 -> 0x29
@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public class PacketPlayOutNamedSoundEffect extends Packet implements PacketPlayOut{
	private float volume;
	private float pitch;
	private Location loc;
	private String sound;
	private int soundCategory;
	
	public PacketPlayOutNamedSoundEffect() {}

	@Override
	public void read(PacketDataSerializer s) {
		if(getBigVersion().equals(BigClientVersion.v1_10) || getBigVersion().equals(BigClientVersion.v1_9)) {
			sound = s.readString(-1);
			soundCategory = s.readVarInt();
			loc = new Location(s.readInt(), s.readInt(), s.readInt()).dividide(8D);
			volume = s.readFloat();
			pitch = s.readUnsignedByte();
		} else if(getBigVersion().equals(BigClientVersion.v1_8)) {
			sound = s.readString(-1);
			loc = new Location(s.readInt(), s.readInt(), s.readInt()).dividide(8D);
			volume = s.readFloat();
			pitch = s.readUnsignedByte();
		}
	}
	
	@Override
	public void write(PacketDataSerializer s) {
		if(getBigVersion().equals(BigClientVersion.v1_10) || getBigVersion().equals(BigClientVersion.v1_9)){
			s.writeString(sound);
			s.writeVarInt(soundCategory);
			loc.multiply(8D);
			s.writeInt(loc.getBlockX());
			s.writeInt(loc.getBlockY());
			s.writeInt(loc.getBlockZ());
			loc.dividide(8D);
			s.writeFloat(volume);
			s.writeFloat(pitch);
		} else if(getBigVersion() == BigClientVersion.v1_8){
			s.writeString(sound);
			loc.multiply(8D);
			s.writeInt(loc.getBlockX());
			s.writeInt(loc.getBlockY());
			s.writeInt(loc.getBlockZ());
			loc.dividide(8D);
			s.writeFloat(volume);
			s.writeByte((int) pitch);
		}
	}
}
