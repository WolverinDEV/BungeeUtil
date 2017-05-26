package dev.wolveringer.bungeeutil.packetlib;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import dev.wolveringer.bungeeutil.BungeeUtil;
import dev.wolveringer.bungeeutil.CostumPrintStream;
import dev.wolveringer.bungeeutil.packets.Packet;
import dev.wolveringer.bungeeutil.packets.PacketLoginDisconnect;
import dev.wolveringer.bungeeutil.packets.PacketPlayInArmAnimation;
import dev.wolveringer.bungeeutil.packets.PacketPlayInBlockDig;
import dev.wolveringer.bungeeutil.packets.PacketPlayInBlockPlace;
import dev.wolveringer.bungeeutil.packets.PacketPlayInChat;
import dev.wolveringer.bungeeutil.packets.PacketPlayInClientState;
import dev.wolveringer.bungeeutil.packets.PacketPlayInCloseWindow;
import dev.wolveringer.bungeeutil.packets.PacketPlayInHeldItemSlot;
import dev.wolveringer.bungeeutil.packets.PacketPlayInLook;
import dev.wolveringer.bungeeutil.packets.PacketPlayInPluginMessage;
import dev.wolveringer.bungeeutil.packets.PacketPlayInPosition;
import dev.wolveringer.bungeeutil.packets.PacketPlayInPositionLook;
import dev.wolveringer.bungeeutil.packets.PacketPlayInUseEntity;
import dev.wolveringer.bungeeutil.packets.PacketPlayInWindowClick;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutBlockChange;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutBossBar;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutChat;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutCloseWindow;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutDisconnect;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutEntityDestroy;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutEntityEffect;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutEntityEquipment;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutEntityHeadRotation;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutEntityTeleport;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutGameStateChange;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutHeldItemSlot;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutNamedSoundEffect;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutOpenSign;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutOpenWindow;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutPlayerInfo;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutPlayerListHeaderFooter;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutPluginMessage;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutPosition;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutRemoveEntityEffect;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutScoreboardDisplayObjective;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutScoreboardObjective;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutScoreboardScore;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutScoreboardTeam;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutSetSlot;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutSpawnEntityObject;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutSpawnGlobalObject;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutSpawnLivingEntity;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutSpawnPlayer;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutStatistic;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutTileData;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutTitle;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutTransaction;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutUpdateSign;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutWindowData;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutWindowItems;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutWorldParticles;
import dev.wolveringer.bungeeutil.packets.creator.AbstractPacketCreator;
import dev.wolveringer.bungeeutil.packets.creator.CachedPacketCreator;
import dev.wolveringer.bungeeutil.packets.creator.NormalPacketCreator;
import dev.wolveringer.bungeeutil.player.Player;
import dev.wolveringer.bungeeutil.player.ClientVersion.BigClientVersion;
import dev.wolveringer.bungeeutil.player.connection.ProtocollVersion;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import net.md_5.bungee.protocol.Protocol;
import net.md_5.bungee.protocol.ProtocolConstants.Direction;

public class PacketRegistry {
	@Getter
	public static class ProtocollId {
		private ProtocollVersion version;
		private int id;

		public ProtocollId(BigClientVersion version, int id) {
			this.version = version.getProtocollVersion();
			this.id = id;
		}

		public ProtocollId(ProtocollVersion version, int id) {
			this.version = version;
			this.id = id;
		}

		public boolean isValid() {
			return this.id >= 0 && this.version != null && this.version != ProtocollVersion.Unsupported;
		}

		@Override
		public String toString() {
			return "ProtocollId [version=" + this.version + ", id=" + Integer.toHexString(this.id) + "]";
		}
	}

	public static final AtomicLong classInstances = new AtomicLong();

	private static AbstractPacketCreator creator;

	private static ProtocollId[] _891011(int _8, int _9, int _10, int _11){
		return new ProtocollId[]{new ProtocollId(BigClientVersion.v1_8, _8), new ProtocollId(BigClientVersion.v1_9, _9), new ProtocollId(BigClientVersion.v1_10, _10), new ProtocollId(BigClientVersion.v1_11, _11)};
	}
	
	static {
		registerPacket(Protocol.LOGIN, Direction.TO_CLIENT, PacketLoginDisconnect.class, new ProtocollId(BigClientVersion.v1_8, 0x00), new ProtocollId(BigClientVersion.v1_9, 0x00), new ProtocollId(BigClientVersion.v1_10, 0x00), new ProtocollId(BigClientVersion.v1_11, 0x00));

		registerPacket(Protocol.GAME, Direction.TO_CLIENT, PacketPlayOutChat.class, new ProtocollId(BigClientVersion.v1_8, 0x02), new ProtocollId(BigClientVersion.v1_9, 0x0F), new ProtocollId(BigClientVersion.v1_10, 0x0F), new ProtocollId(BigClientVersion.v1_11, 0x0F)); // ->0x0F
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, PacketPlayOutPlayerListHeaderFooter.class, new ProtocollId(BigClientVersion.v1_8, 0x47), new ProtocollId(BigClientVersion.v1_9, 0x48), new ProtocollId(ProtocollVersion.v1_9_4, 0x47), new ProtocollId(BigClientVersion.v1_10, 0x47), new ProtocollId(BigClientVersion.v1_11, 0x47));// ->0x48
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, null, new ProtocollId(ProtocollVersion.v1_9_4, 0x48));

		registerPacket(Protocol.GAME, Direction.TO_CLIENT, PacketPlayOutPosition.class, new ProtocollId(BigClientVersion.v1_8, 0x08), new ProtocollId(BigClientVersion.v1_9, 0x2E), new ProtocollId(BigClientVersion.v1_10, 0x2E), new ProtocollId(BigClientVersion.v1_11, 0x2E)); // Changed -> 0x2E

		registerPacket(Protocol.GAME, Direction.TO_CLIENT, PacketPlayOutEntityTeleport.class, new ProtocollId(BigClientVersion.v1_8, 0x18), new ProtocollId(BigClientVersion.v1_9, 0x4A), new ProtocollId(ProtocollVersion.v1_9_4, 0x49), new ProtocollId(BigClientVersion.v1_10, 0x49), new ProtocollId(BigClientVersion.v1_11, 0x49)); // Changed -> 0x2E | 1.9.4 other id!
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, null, new ProtocollId(ProtocollVersion.v1_9_4, 0x4A));

		registerPacket(Protocol.GAME, Direction.TO_CLIENT, PacketPlayOutEntityHeadRotation.class, new ProtocollId(BigClientVersion.v1_8, 0x19), new ProtocollId(BigClientVersion.v1_9, 0x34), new ProtocollId(BigClientVersion.v1_10, 0x34), new ProtocollId(BigClientVersion.v1_11, 0x34));

		registerPacket(Protocol.GAME, Direction.TO_CLIENT, PacketPlayOutTransaction.class, new ProtocollId(BigClientVersion.v1_8, 0x32), new ProtocollId(BigClientVersion.v1_9, 0x11), new ProtocollId(BigClientVersion.v1_10, 0x11), new ProtocollId(BigClientVersion.v1_11, 0x11)); // -> 0x11
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, PacketPlayOutCloseWindow.class, new ProtocollId(BigClientVersion.v1_8, 0x2E), new ProtocollId(BigClientVersion.v1_9, 0x12), new ProtocollId(BigClientVersion.v1_10, 0x12), new ProtocollId(BigClientVersion.v1_11, 0x12));// -> 0x12
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, PacketPlayOutOpenWindow.class, new ProtocollId(BigClientVersion.v1_8, 0x2D), new ProtocollId(BigClientVersion.v1_9, 0x13), new ProtocollId(BigClientVersion.v1_10, 0x13), new ProtocollId(BigClientVersion.v1_11, 0x13)); // -> 0x13
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, PacketPlayOutWindowItems.class, new ProtocollId(BigClientVersion.v1_8, 0x30), new ProtocollId(BigClientVersion.v1_9, 0x14), new ProtocollId(BigClientVersion.v1_10, 0x14), new ProtocollId(BigClientVersion.v1_11, 0x14));// -> 0x14
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, PacketPlayOutWindowData.class, new ProtocollId(BigClientVersion.v1_8, 0x31), new ProtocollId(BigClientVersion.v1_9, 0x15), new ProtocollId(BigClientVersion.v1_10, 0x15), new ProtocollId(BigClientVersion.v1_11, 0x15));// -> 0x15
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, PacketPlayOutSetSlot.class, new ProtocollId(BigClientVersion.v1_8, 0x2F), new ProtocollId(BigClientVersion.v1_9, 0x16), new ProtocollId(BigClientVersion.v1_10, 0x16), new ProtocollId(BigClientVersion.v1_11, 0x16)); // -> 0x16

		registerPacket(Protocol.GAME, Direction.TO_CLIENT, PacketPlayOutPluginMessage.class, new ProtocollId(BigClientVersion.v1_8, 0x3F), new ProtocollId(BigClientVersion.v1_9, 0x18), new ProtocollId(BigClientVersion.v1_10, 0x18), new ProtocollId(BigClientVersion.v1_11, 0x18));

		registerPacket(Protocol.GAME, Direction.TO_CLIENT, PacketPlayOutEntityEquipment.class, new ProtocollId(BigClientVersion.v1_8, 0x04), new ProtocollId(BigClientVersion.v1_9, 0x3C), new ProtocollId(BigClientVersion.v1_10, 0x3C), new ProtocollId(BigClientVersion.v1_11, 0x3C)); // Chaned

		registerPacket(Protocol.GAME, Direction.TO_CLIENT, PacketPlayOutWorldParticles.class, new ProtocollId(BigClientVersion.v1_8, 0x2A), new ProtocollId(BigClientVersion.v1_9, 0x22), new ProtocollId(BigClientVersion.v1_10, 0x22), new ProtocollId(BigClientVersion.v1_11, 0x22));

		registerPacket(Protocol.GAME, Direction.TO_CLIENT, PacketPlayOutDisconnect.class, new ProtocollId(BigClientVersion.v1_8, 0x40), new ProtocollId(BigClientVersion.v1_9, 0x1A), new ProtocollId(BigClientVersion.v1_10, 0x1A), new ProtocollId(BigClientVersion.v1_11, 0x1A)); // 0x1A

		registerPacket(Protocol.GAME, Direction.TO_CLIENT, PacketPlayOutScoreboardTeam.class, new ProtocollId(BigClientVersion.v1_8, 0x3E), new ProtocollId(BigClientVersion.v1_9, 0x41), new ProtocollId(BigClientVersion.v1_10, 0x41), new ProtocollId(BigClientVersion.v1_11, 0x41)); // -> 0x41
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, PacketPlayOutScoreboardDisplayObjective.class, new ProtocollId(BigClientVersion.v1_8, 0x3D), new ProtocollId(BigClientVersion.v1_9, 0x38), new ProtocollId(BigClientVersion.v1_10, 0x38), new ProtocollId(BigClientVersion.v1_11, 0x38));// -> 0x38
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, PacketPlayOutScoreboardObjective.class, new ProtocollId(BigClientVersion.v1_8, 0x3B), new ProtocollId(BigClientVersion.v1_9, 0x3F), new ProtocollId(BigClientVersion.v1_10, 0x3F), new ProtocollId(BigClientVersion.v1_11, 0x3F)); // -> 0x3F
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, PacketPlayOutScoreboardScore.class, new ProtocollId(BigClientVersion.v1_8, 0x3C), new ProtocollId(BigClientVersion.v1_9, 0x42), new ProtocollId(BigClientVersion.v1_10, 0x42), new ProtocollId(BigClientVersion.v1_11, 0x42)); // -> 0x42

		registerPacket(Protocol.GAME, Direction.TO_CLIENT, PacketPlayOutStatistic.class, new ProtocollId(BigClientVersion.v1_8, 0x37), new ProtocollId(BigClientVersion.v1_9, 0x07), new ProtocollId(BigClientVersion.v1_10, 0x07), new ProtocollId(BigClientVersion.v1_11, 0x07)); // -> 0x07
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, PacketPlayOutEntityDestroy.class, new ProtocollId(BigClientVersion.v1_8, 0x13), new ProtocollId(BigClientVersion.v1_9, 0x30), new ProtocollId(BigClientVersion.v1_10, 0x30), new ProtocollId(BigClientVersion.v1_11, 0x30));// -> 0x30
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, PacketPlayOutEntityEffect.class, new ProtocollId(BigClientVersion.v1_8, 0x1D), new ProtocollId(BigClientVersion.v1_9, 0x4C), new ProtocollId(ProtocollVersion.v1_9_4, 0x4B), new ProtocollId(BigClientVersion.v1_10, 0x4B), new ProtocollId(BigClientVersion.v1_11, 0x4B));// Changed ->
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, null, new ProtocollId(ProtocollVersion.v1_9_4, 0x4C));

		registerPacket(Protocol.GAME, Direction.TO_CLIENT, PacketPlayOutRemoveEntityEffect.class, new ProtocollId(BigClientVersion.v1_8, 0x1E), new ProtocollId(BigClientVersion.v1_9, 0x31), new ProtocollId(BigClientVersion.v1_10, 0x31), new ProtocollId(BigClientVersion.v1_11, 0x31));
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, PacketPlayOutGameStateChange.class, new ProtocollId(BigClientVersion.v1_8, 0x2B), new ProtocollId(BigClientVersion.v1_9, 0x1E), new ProtocollId(BigClientVersion.v1_10, 0x1E), new ProtocollId(BigClientVersion.v1_11, 0x1E));// -> 0x31

		registerPacket(Protocol.GAME, Direction.TO_CLIENT, PacketPlayOutPlayerInfo.class, new ProtocollId(BigClientVersion.v1_8, 0x38), new ProtocollId(BigClientVersion.v1_9, 0x2D), new ProtocollId(BigClientVersion.v1_10, 0x2D), new ProtocollId(BigClientVersion.v1_11, 0x2D));// -> 0x2D

		registerPacket(Protocol.GAME, Direction.TO_CLIENT, PacketPlayOutHeldItemSlot.class, new ProtocollId(BigClientVersion.v1_8, 0x09), new ProtocollId(BigClientVersion.v1_9, 0x37), new ProtocollId(BigClientVersion.v1_10, 0x37), new ProtocollId(BigClientVersion.v1_11, 0x37));// -> 0x37
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, PacketPlayOutUpdateSign.class, new ProtocollId(BigClientVersion.v1_8, 0x33)); // 1.9 -> TitleEntityNBTData ;)

		registerPacket(Protocol.GAME, Direction.TO_CLIENT, PacketPlayOutBossBar.class, new ProtocollId(BigClientVersion.v1_9, 0x0C), new ProtocollId(BigClientVersion.v1_10, 0x0C), new ProtocollId(BigClientVersion.v1_11, 0x0C));

		registerPacket(Protocol.GAME, Direction.TO_CLIENT, PacketPlayOutNamedSoundEffect.class, new ProtocollId(BigClientVersion.v1_8, 0x29), new ProtocollId(BigClientVersion.v1_9, 0x19), new ProtocollId(ProtocollVersion.v1_9_4, 0x19), new ProtocollId(BigClientVersion.v1_10, 0x19), new ProtocollId(BigClientVersion.v1_11, 0x19)); // Changed
		//registerPacket(Protocol.GAME, Direction.TO_CLIENT, null, new ProtocollId(ProtocollVersion.v1_9_4, 0x19));

		registerPacket(Protocol.GAME, Direction.TO_CLIENT, PacketPlayOutSpawnPlayer.class, new ProtocollId(BigClientVersion.v1_8, 0x0C), new ProtocollId(BigClientVersion.v1_9, 0x05), new ProtocollId(BigClientVersion.v1_10, 0x05), new ProtocollId(BigClientVersion.v1_11, 0x05));
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, PacketPlayOutTitle.class, new ProtocollId(BigClientVersion.v1_8, 0x45), new ProtocollId(BigClientVersion.v1_9, 0x45), new ProtocollId(BigClientVersion.v1_10, 0x45), new ProtocollId(BigClientVersion.v1_11, 0x45));
		// registerPacket(Protocol.GAME, Direction.TO_CLIENT, 0x21, PacketPlayOutMapChunk.class, new ProtocollId(BigClientVersion.v1_8, 0x00), new ProtocollId(BigClientVersion.v1_9, 0x00), new ProtocollId(BigClientVersion.v1_10, 0x00); //Request packet src on spigotmc via pm!
		// registerPacket(Protocol.GAME, Direction.TO_CLIENT, 0x26, PacketPlayOutMapChunkBulk.class, new ProtocollId(BigClientVersion.v1_8, 0x00), new ProtocollId(BigClientVersion.v1_9, 0x00), new ProtocollId(BigClientVersion.v1_10, 0x00); //TODO Chunk Serelizer (Premium bungee src)
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, PacketPlayOutBlockChange.class, new ProtocollId(BigClientVersion.v1_8, 0x23)/*, new ProtocollId(BigClientVersion.v1_9, 0x37), new ProtocollId(BigClientVersion.v1_10, 0x37), new ProtocollId(BigClientVersion.v1_11, 0x37)*/); //TODO
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, PacketPlayOutTileData.class, new ProtocollId(BigClientVersion.v1_8, 0x35)/*, new ProtocollId(BigClientVersion.v1_9, 0x37), new ProtocollId(BigClientVersion.v1_10, 0x37), new ProtocollId(BigClientVersion.v1_11, 0x37)*/); //TODO
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, PacketPlayOutOpenSign.class, new ProtocollId(BigClientVersion.v1_8, 0x36)/*, new ProtocollId(BigClientVersion.v1_9, 0x37), new ProtocollId(BigClientVersion.v1_10, 0x37), new ProtocollId(BigClientVersion.v1_11, 0x37)*/); //TODO
		
		registerPacket(Protocol.GAME, Direction.TO_SERVER, PacketPlayInClientState.class, new ProtocollId(BigClientVersion.v1_8, 0x16), new ProtocollId(BigClientVersion.v1_9, 0x03), new ProtocollId(BigClientVersion.v1_10, 0x03), new ProtocollId(BigClientVersion.v1_11, 0x03)); // Changed
		//
		registerPacket(Protocol.GAME, Direction.TO_SERVER, PacketPlayInPluginMessage.class,new ProtocollId(BigClientVersion.v1_8, 0x17),  new ProtocollId(BigClientVersion.v1_9, 0x09), new ProtocollId(BigClientVersion.v1_10, 0x09), new ProtocollId(BigClientVersion.v1_11, 0x09));

		registerPacket(Protocol.GAME, Direction.TO_SERVER, PacketPlayInChat.class, new ProtocollId(BigClientVersion.v1_8, 0x01), new ProtocollId(BigClientVersion.v1_9, 0x02), new ProtocollId(BigClientVersion.v1_10, 0x02), new ProtocollId(BigClientVersion.v1_11, 0x02)); // -> 0x02
		registerPacket(Protocol.GAME, Direction.TO_SERVER, PacketPlayInUseEntity.class, new ProtocollId(BigClientVersion.v1_8, 0x02), new ProtocollId(BigClientVersion.v1_9, 0x0A), new ProtocollId(BigClientVersion.v1_10, 0x0A), new ProtocollId(BigClientVersion.v1_11, 0x0A)); // -> changed
		registerPacket(Protocol.GAME, Direction.TO_SERVER, PacketPlayInHeldItemSlot.class, new ProtocollId(BigClientVersion.v1_8, 0x09), new ProtocollId(BigClientVersion.v1_9, 0x17), new ProtocollId(BigClientVersion.v1_10, 0x17), new ProtocollId(BigClientVersion.v1_11, 0x17));// -> 0x17

		registerPacket(Protocol.GAME, Direction.TO_SERVER, PacketPlayInPosition.class, new ProtocollId(BigClientVersion.v1_8, 0x04), new ProtocollId(BigClientVersion.v1_9, 0x0C), new ProtocollId(BigClientVersion.v1_10, 0x0C), new ProtocollId(BigClientVersion.v1_11, 0x0C));// -> 0x0C
		registerPacket(Protocol.GAME, Direction.TO_SERVER, PacketPlayInPositionLook.class, new ProtocollId(BigClientVersion.v1_8, 0x06), new ProtocollId(BigClientVersion.v1_9, 0x0D), new ProtocollId(BigClientVersion.v1_10, 0x0D), new ProtocollId(BigClientVersion.v1_11, 0x0D));// -> 0x0D
		registerPacket(Protocol.GAME, Direction.TO_SERVER, PacketPlayInLook.class, new ProtocollId(BigClientVersion.v1_8, 0x05), new ProtocollId(BigClientVersion.v1_9, 0x0E), new ProtocollId(BigClientVersion.v1_10, 0x0E), new ProtocollId(BigClientVersion.v1_11, 0x0E));// -> 0x0E

		registerPacket(Protocol.GAME, Direction.TO_SERVER, PacketPlayInWindowClick.class, new ProtocollId(BigClientVersion.v1_8, 0x0E), new ProtocollId(BigClientVersion.v1_9, 0x07), new ProtocollId(BigClientVersion.v1_10, 0x07), new ProtocollId(BigClientVersion.v1_11, 0x07)); // 0x07
		registerPacket(Protocol.GAME, Direction.TO_SERVER, PacketPlayInCloseWindow.class, new ProtocollId(BigClientVersion.v1_8, 0x0D), new ProtocollId(BigClientVersion.v1_9, 0x08), new ProtocollId(BigClientVersion.v1_10, 0x08), new ProtocollId(BigClientVersion.v1_11, 0x08)); // 0x08
		registerPacket(Protocol.GAME, Direction.TO_SERVER, PacketPlayInBlockDig.class, new ProtocollId(BigClientVersion.v1_8, 0x07), new ProtocollId(BigClientVersion.v1_9, 0x13), new ProtocollId(BigClientVersion.v1_10, 0x13), new ProtocollId(BigClientVersion.v1_11, 0x13)); // 0x13

		registerPacket(Protocol.GAME, Direction.TO_SERVER, PacketPlayInArmAnimation.class, new ProtocollId(BigClientVersion.v1_8, 0x0A), new ProtocollId(BigClientVersion.v1_9, 0x1A), new ProtocollId(BigClientVersion.v1_10, 0x1A), new ProtocollId(BigClientVersion.v1_11, 0x1A));
		registerPacket(Protocol.GAME, Direction.TO_SERVER, PacketPlayInBlockPlace.class, new ProtocollId(BigClientVersion.v1_8, 0x08), new ProtocollId(BigClientVersion.v1_9, 0x1C), new ProtocollId(BigClientVersion.v1_10, 0x1C), new ProtocollId(BigClientVersion.v1_11, 0x1C));
		// TODO Make it working! registerPacket(Protocol.GAME, Direction.TO_SERVER, 0x12, PacketPlayInUpdateSign.class, new ProtocollId(BigClientVersion.v1_8, 0x00), new ProtocollId(BigClientVersion.v1_9, 0x00), new ProtocollId(BigClientVersion.v1_10, 0x00); //Changed from ChatComponent to String
		
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, PacketPlayOutSpawnEntityObject.class, _891011(0x0E, 0x00, 0x00, 0x00));
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, PacketPlayOutSpawnGlobalObject.class, _891011(0x2C, 0x02, 0x02, 0x02));
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, PacketPlayOutSpawnLivingEntity.class, _891011(0x0F, 0x03, 0x03, 0x03));
	}

	public static int calculate(ProtocollVersion version, Protocol p, Direction d, Integer id) {
		return getCreator().calculate(version, p, d, id);
	}

	public static int countPackets() {
		return getCreator().countPackets();
	}

	public static AbstractPacketCreator getCreator() {
		if (creator == null) {
			if(System.getProperty("bungeeutil.packet.no_cache") != null){
				creator = new NormalPacketCreator();
				BungeeUtil.getInstance().sendMessage("§6Dont use CachedPacketCreator!");
			} else {
				BungeeUtil.getInstance().sendMessage("§aUsing CachedPacketCreator!");
				creator = new CachedPacketCreator(new NormalPacketCreator(), Integer.getInteger("bungeeutil.packet.cache_threads", Runtime.getRuntime().availableProcessors() * 2));
			}
		}
		return creator;
	}

	public static Direction getDirection(int base) {
		return getCreator().getDirection(base);
	}

	public static Packet getPacket(ProtocollVersion version, Protocol s, Direction d, ByteBuf b, Player p) {
		return getCreator().getPacket(version, s, d, b, p);
	}

	public static int getPacketId(int base) {
		return getCreator().getPacketId(base);
	}

	public static int getPacketId(ProtocollVersion version, Class<? extends Packet> clazz) {
		return getCreator().getPacketId(version, clazz);
	}

	public static Protocol getProtocoll(int base) {
		return getCreator().getProtocoll(base);
	}

	public static List<Class<? extends Packet>> getRegisteredPackets() {
		return getCreator().getRegisteredPackets();
	}

	public static void listPackets() {
		getCreator().listPackets();
	}

	public static void listPackets(CostumPrintStream out) {
		getCreator().listPackets(out);
	}

	public static int loadPacket(ProtocollVersion version, Protocol p, Direction d, Integer id, Class<? extends Packet> clazz) {
		return getCreator().loadPacket(version, p, d, id, clazz);
	}

	public static void registerPacket(Protocol p, Direction d, Class<? extends Packet> clazz, ProtocollId... ids) {
		getCreator().registerPacket(p, d, clazz, ids);
	}

	public static void unregisterPacket(ProtocollVersion version, Protocol p, Direction d, Integer id) {
		getCreator().unregisterPacket(version, p, d, id);
	}
}
