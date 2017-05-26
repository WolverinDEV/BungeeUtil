package dev.wolveringer.bungeeutil.packets;

import java.util.UUID;

import dev.wolveringer.bungeeutil.entity.datawatcher.DataWatcher;
import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import dev.wolveringer.bungeeutil.player.ClientVersion.BigClientVersion;
import dev.wolveringer.bungeeutil.position.Location;
import dev.wolveringer.bungeeutil.position.Vector;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PacketPlayOutSpawnLivingEntity extends Packet implements PacketPlayOut {
	private int entityId;

	private UUID objectId;
	private byte type;

	private Location location;
	private float headPitch = 0f;
	
	private Vector velocity = new Vector();
	private DataWatcher datawatcher;
	
	public boolean isFalingBlock() {
		return this.type == 70;
	}

	public boolean isItemFrame() {
		return this.type == 71;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void read(PacketDataSerializer s) {
		entityId = s.readVarInt();
		testVersion(() -> objectId = s.readUUID(), true, BigClientVersion.v1_8);
		type = s.readByte();

		switch (getBigVersion()) {
		case v1_8:
			location = new Location(s.readInt(), s.readInt(), s.readInt(), s.readByte() * 360.f / 255.f, s.readByte() * 360.f / 255.f).dividide(32D);
			break;
		case v1_9:
		case v1_10:
		case v1_11:
		case v1_12:
			location = new Location(s.readDouble(), s.readDouble(), s.readDouble(), s.readByte() * 360.f / 255.f, s.readByte() * 360.f / 255.f);
			break;
		}
		
		headPitch = s.readByte() * 360.f / 255.f;
		velocity = new Vector(s.readShort(), s.readShort(), s.readShort());
		datawatcher = DataWatcher.createDataWatcher(getBigVersion(), s);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void write(PacketDataSerializer s) {
		s.writeVarInt(entityId);
		testVersion(() -> s.writeUUID(objectId), true, BigClientVersion.v1_8);
		s.writeByte(type);

		switch (getBigVersion()) {
		case v1_8:
			Location loc = location.clone().multiply(32D);
			s.writeInt(loc.getBlockX());
			s.writeInt(loc.getBlockY());
			s.writeInt(loc.getBlockZ());
			break;
		case v1_9:
		case v1_10:
		case v1_11:
		case v1_12:
			s.writeDouble(location.getX());
			s.writeDouble(location.getY());
			s.writeDouble(location.getZ());
			break;
		}
		s.writeByte((byte) Math.floor(location.getYaw() * 255.f / 306.f));
		s.writeByte((byte) Math.floor(location.getPitch() * 255.f / 306.f));
		s.writeByte((byte) Math.floor(headPitch * 255.f / 306.f));
		
		s.writeShort((int) velocity.getX());
		s.writeShort((int) velocity.getY());
		s.writeShort((int) velocity.getZ());
		
		datawatcher.write(s);
	}
}
