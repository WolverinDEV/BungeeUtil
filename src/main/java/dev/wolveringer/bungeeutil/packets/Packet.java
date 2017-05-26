package dev.wolveringer.bungeeutil.packets;

import java.util.concurrent.atomic.AtomicInteger;

import dev.wolveringer.bungeeutil.packetlib.PacketRegistry;
import dev.wolveringer.bungeeutil.packetlib.reader.ByteBuffCreator;
import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import dev.wolveringer.bungeeutil.player.ClientVersion;
import dev.wolveringer.bungeeutil.player.ClientVersion.BigClientVersion;
import dev.wolveringer.bungeeutil.player.Player;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import net.md_5.bungee.protocol.Protocol;
import net.md_5.bungee.protocol.ProtocolConstants.Direction;

public abstract class Packet {
	@Getter
	private int compressedId = -1;
	private transient ClientVersion version = ClientVersion.UnderknownVersion;
	private ClientVersion readedVersion = ClientVersion.UnderknownVersion;
	private ClientVersion writtenVersion = ClientVersion.UnderknownVersion;
	private AtomicInteger usecount = new AtomicInteger();

	public Packet() {
		PacketRegistry.classInstances.addAndGet(1);
	}

	public Packet(byte id) {
		this();
	}

	public Packet(int id) {
		this();
	}

	@Override
	protected void finalize() throws Throwable {
		this.compressedId = -1;
		PacketRegistry.classInstances.addAndGet(-1);
	}

	@Deprecated
	public BigClientVersion getBigVersion() {
		return this.version.getBigVersion();
	}

	public ByteBuf getByteBuf(ClientVersion version) {
		this.version = version;
		this.compressedId = PacketRegistry.getPacketId(version.getProtocollVersion(), this.getClass());
		return this.writeToByteBuff(null, version);
	}

	public Direction getDirection(){
		if (this.compressedId == -1) {
			this.compressedId = PacketRegistry.getPacketId(this.version.getProtocollVersion(), this.getClass());
		}
		return PacketRegistry.getDirection(this.compressedId);
	}

	public int getPacketId(){
		if (this.compressedId == -1) {
			this.compressedId = PacketRegistry.getPacketId(this.version.getProtocollVersion(), this.getClass());
		}
		return PacketRegistry.getPacketId(this.compressedId);
	}

	public Protocol getProtocol() {
		if (this.compressedId == -1) {
			this.compressedId = PacketRegistry.getPacketId(this.version.getProtocollVersion(), this.getClass());
		}
		return PacketRegistry.getProtocoll(this.compressedId);
	}

	public ClientVersion getReadedVersion() {
		return this.readedVersion;
	}

	@Deprecated
	protected ClientVersion getVersion() {
		return this.version;
	}

	public ClientVersion getWrittenVersion() {
		return this.writtenVersion;
	}

	@Deprecated
	public Packet initClass(int id) {
		this.compressedId = id;
		this.version = ClientVersion.UnderknownVersion;
		this.readedVersion = ClientVersion.UnderknownVersion;
		this.writtenVersion = ClientVersion.UnderknownVersion;
		this.usecount = new AtomicInteger(0);
		return this;
	}

	public boolean isUsed(){
		return this.usecount.get() > 0;
	}

	public Packet load(ByteBuf b, ClientVersion version) {
		this.version = this.readedVersion = version;
		this.read(PacketDataSerializer.create(b, version));
		this.version = ClientVersion.UnderknownVersion;
		if (b.readableBytes() != 0) {
			throw new RuntimeException("Did not read all bytes from packet (" + this.getClass().getName() + ")");
		}
		return this;
	}

	public abstract void read(PacketDataSerializer s);

	public void sendPacket(Player p) {
		if (this instanceof PacketPlayOut) {
			p.sendPacket((PacketPlayOut) this);
		} else {
			throw new IllegalStateException("PacketPlayIn cant send to player");
		}
	}

	public void unuse(){
		if(this.usecount.addAndGet(-1) == 0){
			PacketRegistry.getCreator().releasePacket(this);
		}
	}

	public void use(){
		this.usecount.addAndGet(1);
	}

	public abstract void write(PacketDataSerializer s);

	public ByteBuf writeToByteBuff(ByteBuf buf, ClientVersion version) {
		if(PacketRegistry.getPacketId(this.compressedId) < 0) {
			throw new RuntimeException("Unexpected packet id ("+PacketRegistry.getPacketId(this.compressedId) +")");
		}
		PacketDataSerializer s;
		if (buf == null) {
			buf = ByteBuffCreator.createByteBuff();
		}
		if (buf instanceof PacketDataSerializer) {
			s = (PacketDataSerializer) buf;
		}
		else {
			s = PacketDataSerializer.create(PacketRegistry.getPacketId(this.compressedId), version, buf);
		}
		this.version = this.writtenVersion = version;
		this.write(s);
		this.version = ClientVersion.UnderknownVersion;
		return s;
	}
	
	protected void testVersion(Runnable run, BigClientVersion... versions){
		testVersion(run, false, versions);
	}
	protected void testVersion(Runnable run,boolean invert, BigClientVersion... versions){
		for(BigClientVersion version : versions)
			if((version == getBigVersion()) == !invert){
				run.run();
				return;
			}
	}
	
}
