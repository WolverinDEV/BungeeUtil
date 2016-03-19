package dev.wolveringer.BungeeUtil.packets;

import java.util.List;

import com.google.common.collect.Lists;

import dev.wolveringer.BungeeUtil.ClientVersion.BigClientVersion;
import dev.wolveringer.BungeeUtil.PlayerInfoData;
import dev.wolveringer.BungeeUtil.gameprofile.GameProfile;
import dev.wolveringer.BungeeUtil.gameprofile.Property;
import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayOut;
import dev.wolveringer.chat.ChatSerializer;
import dev.wolveringer.chat.IChatBaseComponent;
import dev.wolveringer.packet.PacketDataSerializer;

public class PacketPlayOutPlayerInfo extends Packet implements PacketPlayOut {

	public static enum EnumPlayerInfoAction {
		ADD_PLAYER,
		UPDATE_GAMEMODE,
		UPDATE_PING,
		UPDATE_DISPLAY_NAME,
		REMOVE_PLAYER;
	}

	private EnumPlayerInfoAction action;
	private final List<PlayerInfoData> data = Lists.newArrayList();

	public boolean profile = true;

	public PacketPlayOutPlayerInfo() {
		super(0x38);
	}

	public PacketPlayOutPlayerInfo(EnumPlayerInfoAction paramEnumPlayerInfoAction, PlayerInfoData... player) {
		super(0x38);
		this.action = paramEnumPlayerInfoAction;
		for(PlayerInfoData localEntityPlayer : player){
			this.data.add(localEntityPlayer);
		}
	}

	public void read(PacketDataSerializer s) {
		if(getVersion().getBigVersion() == BigClientVersion.v1_8 || getBigVersion() == BigClientVersion.v1_9){
			this.action = ((EnumPlayerInfoAction) EnumPlayerInfoAction.values()[s.readVarInt()]);
			int profiles = s.readVarInt();
			for(int x = 0;x < profiles;x++){
				GameProfile gameporfile = null;
				int ping = 0;
				int gamemode = 0;
				IChatBaseComponent nickname = null;
				switch (this.action) {
					case ADD_PLAYER:
						gameporfile = new GameProfile(s.readUUID(), s.readString(-1));
						int length = s.readVarInt();
						for(int n = 0;n < length;n++){
							String key = s.readString(-1);
							String value = s.readString(-1);
							if(s.readBoolean()){
								gameporfile.getProperties().put(key, new Property(key, value, s.readString(-1)));
							}else{
								gameporfile.getProperties().put(key, new Property(key, value));
							}
						}
						gamemode = s.readVarInt();
						ping = s.readVarInt();
						if(s.readBoolean()){
							nickname = ChatSerializer.fromJSON(s.readString(-1));
						}
						break;
					case UPDATE_GAMEMODE:
						gameporfile = new GameProfile(s.readUUID(), null);
						gamemode = s.readVarInt();
						break;
					case UPDATE_PING:
						gameporfile = new GameProfile(s.readUUID(), null);
						ping = s.readVarInt();
						break;
					case UPDATE_DISPLAY_NAME:
						gameporfile = new GameProfile(s.readUUID(), null);
						if(s.readBoolean())
							nickname = s.readRawString();
						break;
					case REMOVE_PLAYER:
						gameporfile = new GameProfile(s.readUUID(), null);
				}
				this.data.add(new PlayerInfoData(gameporfile, ping, gamemode, nickname));
			}
		}else{
			String user = s.readString(-1);
			boolean b = s.readBoolean();
			int ping = s.readShort();
			if(b)
				action = EnumPlayerInfoAction.ADD_PLAYER;
			else
				action = EnumPlayerInfoAction.REMOVE_PLAYER;
			this.data.add(new PlayerInfoData(null, ping, -1, user));
		}
	}

	@SuppressWarnings("static-access")
	public void write(PacketDataSerializer paramPacketDataSerializer) {
		if(getVersion().getBigVersion() == BigClientVersion.v1_8 || getBigVersion() == BigClientVersion.v1_9){
			paramPacketDataSerializer.writeVarInt(this.action.ordinal());
			paramPacketDataSerializer.writeVarInt(this.data.size());
			for(PlayerInfoData localPlayerInfoData : this.data){
				switch ((this.action.ordinal() + 1)) {
					case 1:
						paramPacketDataSerializer.writeUUID(localPlayerInfoData.getGameprofile().getId()); //UUID
						paramPacketDataSerializer.writeString(localPlayerInfoData.getGameprofile().getName()); //REAL NAME

						paramPacketDataSerializer.writeVarInt(localPlayerInfoData.getGameprofile().getProperties().size()); //PROPETY-SIZE
						for(Property localProperty : localPlayerInfoData.getGameprofile().getProperties().values()){
							paramPacketDataSerializer.writeString(localProperty.getName()); //PROTETY NAME
							paramPacketDataSerializer.writeString(localProperty.getValue()); //PROTETY VALUE
							paramPacketDataSerializer.writeBoolean(localProperty.hasSignature());
							if(localProperty.hasSignature())
								paramPacketDataSerializer.writeString(localProperty.getSignature()); //PROTETY SIGNATURE
						}

						paramPacketDataSerializer.writeVarInt(localPlayerInfoData.getGamemode()); //PROTETY GAMEMODE
						paramPacketDataSerializer.writeVarInt(localPlayerInfoData.getPing());
						paramPacketDataSerializer.writeBoolean(localPlayerInfoData.getName() != null);
						if(localPlayerInfoData.getName() != null){
							paramPacketDataSerializer.writeRawString(localPlayerInfoData.getName());
						}
						break;
					case 2:
						paramPacketDataSerializer.writeUUID(localPlayerInfoData.getGameprofile().getId());
						paramPacketDataSerializer.writeVarInt(localPlayerInfoData.getGamemode());
						break;
					case 3:
						paramPacketDataSerializer.writeUUID(localPlayerInfoData.getGameprofile().getId());
						paramPacketDataSerializer.writeVarInt(localPlayerInfoData.getPing());
						break;
					case 4:
						paramPacketDataSerializer.writeUUID(localPlayerInfoData.getGameprofile().getId());
						if(localPlayerInfoData.getName() == null){
							paramPacketDataSerializer.writeBoolean(false);
						}else{
							paramPacketDataSerializer.writeBoolean(true);
							paramPacketDataSerializer.writeRawString(localPlayerInfoData.getName());
						}
						break;
					case 5:
						paramPacketDataSerializer.writeUUID(localPlayerInfoData.getGameprofile().getId());
				}
			}
		}else{
			PlayerInfoData d = data.get(0);
			System.out.print(d + ":" + action);
			paramPacketDataSerializer.writeString(d.getUsername().length() > 16 ? d.getUsername().substring(0, 16) : d.getUsername());
			paramPacketDataSerializer.writeBoolean(action != action.REMOVE_PLAYER);
			paramPacketDataSerializer.writeShort(d.getPing());
		}
	}

	public EnumPlayerInfoAction getAction() {
		return action;
	}

	public void setAction(EnumPlayerInfoAction action) {
		this.action = action;
	}

	public List<PlayerInfoData> getData() {
		return data;
	}

	@Override
	public String toString() {
		return "PacketPlayOutPlayerInfo [action=" + action + ", data=" + data + "]";
	}

}