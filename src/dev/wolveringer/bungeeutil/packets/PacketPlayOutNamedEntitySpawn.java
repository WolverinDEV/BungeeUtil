package dev.wolveringer.bungeeutil.packets;

import java.util.UUID;

import dev.wolveringer.BungeeUtil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.entity.datawatcher.DataWatcher;
import dev.wolveringer.nbt.MathHelper;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOutEntity;
import dev.wolveringer.bungeeutil.player.ClientVersion.BigClientVersion;
import dev.wolveringer.bungeeutil.position.Location;
import dev.wolveringer.bungeeutil.profile.GameProfile;

public class PacketPlayOutNamedEntitySpawn extends PacketPlayOutEntity implements PacketPlayOut{
	private GameProfile p = new GameProfile(UUID.randomUUID(), ""+dev.wolveringer.chat.ChatColor.ChatColorUtils.COLOR_CHAR+"cProfile error");
	private UUID uuid;
	private Location loc;
	private byte yaw; // yaw / 256*360
	private byte pitch; // pitch / 256*360
	private int item_id;
	private DataWatcher data;

	public PacketPlayOutNamedEntitySpawn() {}

	public PacketPlayOutNamedEntitySpawn(int id, UUID uuid, Location loc, int hand, DataWatcher w) {
		setEntityId(id);
		this.uuid = uuid;
		this.loc = loc;
		this.yaw = ((byte) (int) (loc.getYaw() * 256.0F / 360.0F));
		this.pitch = ((byte) (int) (loc.getPitch() * 256.0F / 360.0F));
		this.item_id = hand;
		this.data = w;
		this.p = new GameProfile(uuid, ""+dev.wolveringer.chat.ChatColor.ChatColorUtils.COLOR_CHAR+"cError:-202");
	}

	@SuppressWarnings("deprecation")
	public PacketPlayOutNamedEntitySpawn(int id, GameProfile profile, Location loc, int hand, DataWatcher w) {
		setEntityId(id);
		this.p = profile;
		if(profile == null)
			throw new NullPointerException("Profile cant be null");
		this.uuid = profile.getId();
		this.loc = loc;
		this.yaw = ((byte) (int) (loc.getYaw() * 256.0F / 360.0F));
		this.pitch = ((byte) (int) (loc.getPitch() * 256.0F / 360.0F));
		this.item_id = hand;
		this.data = w;
	}

	@SuppressWarnings("deprecation")
	public PacketPlayOutNamedEntitySpawn(int id, UUID uuid, Location loc, GameProfile g, int hand, DataWatcher w) {
		setEntityId(id);
		this.p = g;
		this.uuid = uuid;
		this.loc = loc;
		this.yaw = ((byte) (int) (loc.getYaw() * 256.0F / 360.0F));
		this.pitch = ((byte) (int) (loc.getPitch() * 256.0F / 360.0F));
		this.item_id = hand;
		this.data = w;
	}

	public void read(PacketDataSerializer paramPacketDataSerializer) {
		setEntityId(paramPacketDataSerializer.readVarInt());
		this.uuid = paramPacketDataSerializer.readUUID();
		switch (getBigVersion()) {
		case v1_10:
		case v1_9:
			loc = new Location(paramPacketDataSerializer.readDouble(), paramPacketDataSerializer.readDouble(), paramPacketDataSerializer.readDouble());
			break;
		case v1_8:
			loc = new Location((double) paramPacketDataSerializer.readInt()/32D,(double) paramPacketDataSerializer.readInt()/32D,(double) paramPacketDataSerializer.readInt()/32D);
			break;
		default:
			break;
		}
		this.yaw = paramPacketDataSerializer.readByte();
		this.pitch = paramPacketDataSerializer.readByte();
		if(getBigVersion() == BigClientVersion.v1_8)
			this.item_id = paramPacketDataSerializer.readShort();
		this.data = DataWatcher.createDataWatcher(getBigVersion(),paramPacketDataSerializer);
	}

	@SuppressWarnings("rawtypes")
	public void write(PacketDataSerializer paramPacketDataSerializer) {
		paramPacketDataSerializer.writeVarInt(getEntityId());
		paramPacketDataSerializer.writeUUID(this.uuid);
		switch (getBigVersion()) {
		case v1_9:
		case v1_10:
			paramPacketDataSerializer.writeDouble(loc.getX());
			paramPacketDataSerializer.writeDouble(loc.getY());
			paramPacketDataSerializer.writeDouble(loc.getZ());
			break;
		case v1_8:
			paramPacketDataSerializer.writeInt((int)MathHelper.floor(loc.getX() * 32.0D));
			paramPacketDataSerializer.writeInt((int)MathHelper.floor(loc.getY() * 32.0D));
			paramPacketDataSerializer.writeInt((int)MathHelper.floor(loc.getZ() * 32.0D));
			break;
		default:
			break;
		}
		paramPacketDataSerializer.writeByte(this.yaw);
		paramPacketDataSerializer.writeByte(this.pitch);
		if(getBigVersion() == BigClientVersion.v1_8)
			paramPacketDataSerializer.writeShort(this.item_id);
		this.data.write(paramPacketDataSerializer);
	}

	public DataWatcher getData() {
		return data;
	}

	public GameProfile getGameProfile() {
		return p;
	}

	public void setGameProfile(GameProfile p) {
		this.p = p;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public Location getLocation() {
		return loc;
	}
	public void setLocation(Location loc) {
		this.loc = loc;
	}

	public int getItem_id() {
		return item_id;
	}

	public void setItem_id(int item_id) {
		this.item_id = item_id;
	}

	public void setData(DataWatcher data) {
		this.data = data;
	}
	
	public byte getYaw() {
		return yaw;
	}

	public void setYaw(byte yaw) {
		this.yaw = yaw;
	}

	public byte getPitch() {
		return pitch;
	}

	public void setPitch(byte pitch) {
		this.pitch = pitch;
	}
}
