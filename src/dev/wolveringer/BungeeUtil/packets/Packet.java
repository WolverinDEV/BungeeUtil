package dev.wolveringer.BungeeUtil.packets;

import io.netty.buffer.ByteBuf;
import lombok.Getter;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import net.md_5.bungee.protocol.Protocol;
import net.md_5.bungee.protocol.ProtocolConstants.Direction;
import dev.wolveringer.BungeeUtil.ClientVersion;
import dev.wolveringer.BungeeUtil.ClientVersion.BigClientVersion;
import dev.wolveringer.BungeeUtil.ClientVersion.ProtocollVersion;
import dev.wolveringer.BungeeUtil.CostumPrintStream;
import dev.wolveringer.BungeeUtil.Player;
import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayOut;
import dev.wolveringer.packet.ByteBuffCreator;
import dev.wolveringer.packet.PacketDataSerializer;

public abstract class Packet {
	@Getter
	public static class ProtocollId {
		private ProtocollVersion version;
		private int id;
		
		public ProtocollId(ProtocollVersion version, int id) {
			this.version = version;
			this.id = id;
		}
		
		public ProtocollId(BigClientVersion version, int id) {
			this.version = version.getProtocollVersion();
			this.id = id;
		}
		
		public boolean isValid() {
			return id > 0 && version != null && version != ProtocollVersion.Unsupported;
		}
	}
	
	public static final AtomicLong classInstances = new AtomicLong();
	
	private static AbstractPacketCreator creator;
	
	public static AbstractPacketCreator getCreator() {
		if (creator == null) {
			creator = new NormalPacketCreator();
		}
		return creator;
	}
	
	static {
		registerPacket(Protocol.LOGIN, Direction.TO_CLIENT, 0x00, 0x00, PacketLoginDisconnect.class);
		
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, 0x02, 0x0F, PacketPlayOutChat.class); // ->0x0F
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, PacketPlayOutPlayerListHeaderFooter.class, new ProtocollId(BigClientVersion.v1_8, 0x47), new ProtocollId(BigClientVersion.v1_9, 0x48), new ProtocollId(ProtocollVersion.v1_9_4, 0x47));// ->0x48
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, 0x08, 0x2E, PacketPlayOutPosition.class); // Changed -> 0x2E
		
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, PacketPlayOutEntityTeleport.class, new ProtocollId(BigClientVersion.v1_8, 0x18), new ProtocollId(BigClientVersion.v1_9, 0x4A), new ProtocollId(ProtocollVersion.v1_9_4, 0x49)); // Changed -> 0x2E | 1.9.4 other id!
		
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, 0x19, 0x34, PacketPlayOutEntityHeadRotation.class);
		
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, 0x32, 0x11, PacketPlayOutTransaction.class); // -> 0x11
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, 0x2E, 0x12, PacketPlayOutCloseWindow.class); // -> 0x12
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, 0x2D, 0x13, PacketPlayOutOpenWindow.class); // -> 0x13
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, 0x30, 0x14, PacketPlayOutWindowItems.class);// -> 0x14
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, 0x31, 0x15, PacketPlayOutWindowData.class);// -> 0x15
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, 0x2F, 0x16, PacketPlayOutSetSlot.class); // -> 0x16
		
		registerPacket(Protocol.GAME, Direction.TO_SERVER, 0x3F, 0x18, PacketPlayOutPluginMessage.class);
		
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, 0x04, 0x3C, PacketPlayOutEntityEquipment.class); // Chaned
		
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, 0x2A, 0x22, PacketPlayOutWorldParticles.class);
		
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, 0x40, 0x1A, PacketPlayOutDisconnect.class); // 0x1A
		
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, 0x3E, 0x41, PacketPlayOutScoreboardTeam.class); // -> 0x41
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, 0x3D, 0x38, PacketPlayOutScoreboardDisplayObjective.class);// -> 0x38
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, 0x3B, 0x3F, PacketPlayOutScoreboardObjective.class); // -> 0x3F
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, 0x3C, 0x42, PacketPlayOutScoreboardScore.class); // -> 0x42
		
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, 0x37, 0x07, PacketPlayOutStatistic.class); // -> 0x07
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, 0x13, 0x30, PacketPlayOutEntityDestroy.class);// -> 0x30
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, PacketPlayOutEntityEffect.class, new ProtocollId(BigClientVersion.v1_8, 0x1D), new ProtocollId(BigClientVersion.v1_9, 0x4C), new ProtocollId(ProtocollVersion.v1_9_4, 0x4B));// Changed ->
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, 0x1E, 0x31, PacketPlayOutRemoveEntityEffect.class);
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, 0x2B, 0x31, PacketPlayOutGameStateChange.class);// -> 0x31
		
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, 0x38, 0x2D, PacketPlayOutPlayerInfo.class);// -> 0x2D
		
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, 0x09, 0x37, PacketPlayOutHeldItemSlot.class);// -> 0x37
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, 0x33, 0x46, PacketPlayOutUpdateSign.class);
		
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, null, 0x0C, PacketPlayOutBossBar.class); // Only 1.9 :) Best Bar-Update Ever!
		
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, PacketPlayOutNamedSoundEffect.class, new ProtocollId(BigClientVersion.v1_8, 0x29), new ProtocollId(BigClientVersion.v1_9, 0x19), new ProtocollId(ProtocollVersion.v1_9_4, 0x46)); // Changed
		
		registerPacket(Protocol.GAME, Direction.TO_CLIENT, 0x0C, 0x05, PacketPlayOutNamedEntitySpawn.class);
		// registerPacket(Protocol.GAME, Direction.TO_CLIENT, 0x21, PacketPlayOutMapChunk.class); //TODO Chunk Serelizer (Premium bungee src)
		// registerPacket(Protocol.GAME, Direction.TO_CLIENT, 0x26, PacketPlayOutMapChunkBulk.class); //TODO Chunk Serelizer (Premium bungee src)
		
		registerPacket(Protocol.GAME, Direction.TO_SERVER, 0x16, 0x03, PacketPlayInClientState.class); // Changed
		
		registerPacket(Protocol.GAME, Direction.TO_SERVER, 0x17, 0x09, PacketPlayInPluginMessage.class);
		
		registerPacket(Protocol.GAME, Direction.TO_SERVER, 0x01, 0x02, PacketPlayInChat.class); // -> 0x02
		registerPacket(Protocol.GAME, Direction.TO_SERVER, 0x02, 0x0A, PacketPlayInUseEntity.class); // -> changed
		registerPacket(Protocol.GAME, Direction.TO_SERVER, 0x09, 0x17, PacketPlayInHeldItemSlot.class);// -> 0x17
		
		registerPacket(Protocol.GAME, Direction.TO_SERVER, 0x04, 0x0C, PacketPlayInPosition.class);// -> 0x0C
		registerPacket(Protocol.GAME, Direction.TO_SERVER, 0x06, 0x0D, PacketPlayInPositionLook.class);// -> 0x0D
		registerPacket(Protocol.GAME, Direction.TO_SERVER, 0x05, 0x0E, PacketPlayInLook.class);// -> 0x0E
		
		registerPacket(Protocol.GAME, Direction.TO_SERVER, 0x0E, 0x07, PacketPlayInWindowClick.class); // 0x07
		registerPacket(Protocol.GAME, Direction.TO_SERVER, 0x0D, 0x08, PacketPlayInCloseWindow.class); // 0x08
		registerPacket(Protocol.GAME, Direction.TO_SERVER, 0x07, 0x13, PacketPlayInBlockDig.class); // 0x13
		
		registerPacket(Protocol.GAME, Direction.TO_SERVER, 0x0A, 0x1A, PacketPlayInArmAnimation.class);
		registerPacket(Protocol.GAME, Direction.TO_SERVER, 0x08, 0x1C, PacketPlayInBlockPlace.class);
		// TODO Make it working! registerPacket(Protocol.GAME, Direction.TO_SERVER, 0x12, PacketPlayInUpdateSign.class); //Changed from ChatComponent to String
	}
	
	public static int calculate(ProtocollVersion version, Protocol p, Direction d, Integer id) {
		return getCreator().calculate(version, p, d, id);
	}
	
	public static int getPacketId(int base) {
		return getCreator().getPacketId(base);
	}
	
	public static Protocol getProtocoll(int base) {
		return getCreator().getProtocoll(base);
	}
	
	public static Direction getDirection(int base) {
		return getCreator().getDirection(base);
	}
	
	public static Packet getPacket(ProtocollVersion version, Protocol s, Direction d, ByteBuf b, Player p) {
		return getCreator().getPacket(version, s, d, b, p);
	}
	
	public static void listPackets(CostumPrintStream out) {
		getCreator().listPackets(out);
	}
	
	public static void listPackets() {
		getCreator().listPackets();
	}
	
	public static int loadPacket(ProtocollVersion version, Protocol p, Direction d, Integer id, Class<? extends Packet> clazz) {
		return getCreator().loadPacket(version, p, d, id, clazz);
	}
	
	@Deprecated
	public static void registerPacket(Protocol p, Direction d, Integer v1_8_id, Integer v1_9_id, Class<? extends Packet> clazz) {
		if (v1_8_id == null && v1_9_id == null) throw new RuntimeException("All packet ids are null!");
		if (v1_8_id == null) v1_8_id = -1;
		if (v1_9_id == null) v1_9_id = -1;
		registerPacket(p, d, clazz, new ProtocollId(BigClientVersion.v1_8, v1_8_id), new ProtocollId(BigClientVersion.v1_9, v1_9_id));
	}
	
	public static void registerPacket(Protocol p, Direction d, Class<? extends Packet> clazz, ProtocollId... ids) {
		getCreator().registerPacket(p, d, clazz, ids);
	}
	
	public static void unregisterPacket(ProtocollVersion version, Protocol p, Direction d, Integer id) {
		getCreator().unregisterPacket(version, p, d, id);
	}
	
	public static int countPackets() {
		return getCreator().countPackets();
	}
	
	public static int getPacketId(ProtocollVersion version, Class<? extends Packet> clazz) {
		return getCreator().getPacketId(version, clazz);
	}
	
	public static List<Class<? extends Packet>> getRegisteredPackets() {
		return getCreator().getRegisteredPackets();
	}
	
	/*
	 * -------------------------------------------------------------------------
	 * ------------------------------
	 */
	
	private int compressedId = -1;
	private transient ClientVersion version = ClientVersion.UnderknownVersion;
	private ClientVersion readedVersion = ClientVersion.UnderknownVersion;
	private ClientVersion writtenVersion = ClientVersion.UnderknownVersion;
	
	protected Packet setcompressedId(int id) {
		this.compressedId = id;
		return this;
	}
	
	public Packet() {
		classInstances.addAndGet(1);
	}
	
	public Packet(int id) {
		this();
	}
	
	public Packet(byte id) {
		this();
	}
	
	public Protocol getProtocol() {
		if (compressedId == -1) compressedId = getPacketId(version.getProtocollVersion(), this.getClass());
		return getProtocoll(compressedId);
	}
	
	public ByteBuf getByteBuf(ClientVersion version) {
		this.version = version;
		compressedId = getPacketId(version.getProtocollVersion(), this.getClass());
		return writeToByteBuff(null, version);
	}
	
	public ByteBuf writeToByteBuff(ByteBuf buf, ClientVersion version) {
		PacketDataSerializer s;
		if (buf == null) {
			buf = ByteBuffCreator.createByteBuff();
		}
		if (buf instanceof PacketDataSerializer) {
			s = (PacketDataSerializer) buf;
		}
		else {
			s = PacketDataSerializer.create(getPacketId(compressedId), version, buf);
		}
		this.version = (writtenVersion = version);
		this.write(s);
		this.version = ClientVersion.UnderknownVersion;
		return s;
	}
	
	protected Packet load(ByteBuf b, ClientVersion version) {
		this.version = (readedVersion = version);
		read(PacketDataSerializer.create(b, version));
		this.version = ClientVersion.UnderknownVersion;
		if (b.readableBytes() != 0) throw new RuntimeException("Did not read all bytes from packet (" + this.getClass().getName() + ")");
		return this;
	}
	
	public abstract void read(PacketDataSerializer s);
	
	public void sendPacket(Player p) {
		if (this instanceof PacketPlayOut) p.sendPacket((PacketPlayOut) this);
		else throw new IllegalStateException("PacketPlayIn cant send to player");
	}
	
	public abstract void write(PacketDataSerializer s);
	
	@Deprecated
	protected BigClientVersion getBigVersion() {
		return version.getBigVersion();
	}
	
	public ClientVersion getReadedVersion() {
		return readedVersion;
	}
	
	public ClientVersion getWrittenVersion() {
		return writtenVersion;
	}
	
	@Deprecated
	protected ClientVersion getVersion() {
		return version;
	}
	
	@Override
	protected void finalize() throws Throwable {
		compressedId = -1;
		classInstances.addAndGet(-1);
	}
}
