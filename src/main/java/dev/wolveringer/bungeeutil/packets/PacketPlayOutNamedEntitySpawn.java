package dev.wolveringer.bungeeutil.packets;

import java.util.UUID;

import dev.wolveringer.bungeeutil.entity.datawatcher.DataWatcher;
import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOutEntity;
import dev.wolveringer.bungeeutil.player.ClientVersion.BigClientVersion;
import dev.wolveringer.bungeeutil.position.Location;
import dev.wolveringer.bungeeutil.profile.GameProfile;
import dev.wolveringer.nbt.MathHelper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PacketPlayOutNamedEntitySpawn extends PacketPlayOutEntity implements PacketPlayOut{
	private GameProfile p = new GameProfile(UUID.randomUUID(), ""+dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR+"cProfile error");
	private UUID uuid;
	private Location loc;
	private byte yaw; // yaw / 256*360
	private byte pitch; // pitch / 256*360
	private int item_id;
	private DataWatcher data;

	public PacketPlayOutNamedEntitySpawn(int id, GameProfile profile, Location loc, int hand, DataWatcher w) {
		this.setEntityId(id);
		this.p = profile;
		if(profile == null) {
			throw new NullPointerException("Profile cant be null");
		}
		this.uuid = profile.getId();
		this.loc = loc;
		this.yaw = (byte) (int) (loc.getYaw() * 256.0F / 360.0F);
		this.pitch = (byte) (int) (loc.getPitch() * 256.0F / 360.0F);
		this.item_id = hand;
		this.data = w;
	}

	public PacketPlayOutNamedEntitySpawn(int id, UUID uuid, Location loc, GameProfile g, int hand, DataWatcher w) {
		this.setEntityId(id);
		this.p = g;
		this.uuid = uuid;
		this.loc = loc;
		this.yaw = (byte) (int) (loc.getYaw() * 256.0F / 360.0F);
		this.pitch = (byte) (int) (loc.getPitch() * 256.0F / 360.0F);
		this.item_id = hand;
		this.data = w;
	}

	public PacketPlayOutNamedEntitySpawn(int id, UUID uuid, Location loc, int hand, DataWatcher w) {
		this.setEntityId(id);
		this.uuid = uuid;
		this.loc = loc;
		this.yaw = (byte) (int) (loc.getYaw() * 256.0F / 360.0F);
		this.pitch = (byte) (int) (loc.getPitch() * 256.0F / 360.0F);
		this.item_id = hand;
		this.data = w;
		this.p = new GameProfile(uuid, ""+dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR+"cError:-202");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void read(PacketDataSerializer paramPacketDataSerializer) {
		this.setEntityId(paramPacketDataSerializer.readVarInt());
		this.uuid = paramPacketDataSerializer.readUUID();
		switch (this.getBigVersion()) {
		case v1_11:
		case v1_10:
		case v1_9:
			this.loc = new Location(paramPacketDataSerializer.readDouble(), paramPacketDataSerializer.readDouble(), paramPacketDataSerializer.readDouble());
			break;
		case v1_8:
			this.loc = new Location(paramPacketDataSerializer.readInt()/32D,paramPacketDataSerializer.readInt()/32D,paramPacketDataSerializer.readInt()/32D);
			break;
		default:
			break;
		}
		this.yaw = paramPacketDataSerializer.readByte();
		this.pitch = paramPacketDataSerializer.readByte();
		if(this.getBigVersion() == BigClientVersion.v1_8) {
			this.item_id = paramPacketDataSerializer.readShort();
		}
		this.data = DataWatcher.createDataWatcher(this.getBigVersion(),paramPacketDataSerializer);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void write(PacketDataSerializer paramPacketDataSerializer) {
		paramPacketDataSerializer.writeVarInt(this.getEntityId());
		paramPacketDataSerializer.writeUUID(this.uuid);
		switch (this.getBigVersion()) {
		case v1_9:
		case v1_10:
		case v1_11:
			paramPacketDataSerializer.writeDouble(this.loc.getX());
			paramPacketDataSerializer.writeDouble(this.loc.getY());
			paramPacketDataSerializer.writeDouble(this.loc.getZ());
			break;
		case v1_8:
			paramPacketDataSerializer.writeInt(MathHelper.floor(this.loc.getX() * 32.0D));
			paramPacketDataSerializer.writeInt(MathHelper.floor(this.loc.getY() * 32.0D));
			paramPacketDataSerializer.writeInt(MathHelper.floor(this.loc.getZ() * 32.0D));
			break;
		default:
			break;
		}
		paramPacketDataSerializer.writeByte(this.yaw);
		paramPacketDataSerializer.writeByte(this.pitch);
		if(this.getBigVersion() == BigClientVersion.v1_8) {
			paramPacketDataSerializer.writeShort(this.item_id);
		}
		this.data.write(paramPacketDataSerializer);
	}
}
