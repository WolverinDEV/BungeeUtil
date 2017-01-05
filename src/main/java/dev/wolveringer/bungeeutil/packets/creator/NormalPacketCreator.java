package dev.wolveringer.bungeeutil.packets.creator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dev.wolveringer.bungeeutil.BungeeUtil;
import dev.wolveringer.bungeeutil.ExceptionUtils;
import dev.wolveringer.bungeeutil.packets.Packet;
import dev.wolveringer.bungeeutil.packets.Packet.ProtocollId;
import dev.wolveringer.bungeeutil.player.ClientVersion;
import dev.wolveringer.bungeeutil.player.Player;
import dev.wolveringer.bungeeutil.player.connection.ProtocollVersion;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.md_5.bungee.protocol.Protocol;
import net.md_5.bungee.protocol.ProtocolConstants.Direction;

public class NormalPacketCreator extends AbstractPacketCreator {
	@AllArgsConstructor
	@Getter
	private static class PacketHolder<T extends Packet> {
		private Constructor<T> constuctor;

		public T newInstance() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			return this.constuctor.newInstance();
		}
	}

	private PacketHolder<?>[] packetsId = new PacketHolder[(ProtocollVersion.values().length & 0x0F) << 16 | (Protocol.values().length & 0x0F) << 12 | (Direction.values().length & 0x0F) << 8 | 0xFF]; // Calculate max packet compressed id. (0xFF = Max ID)

	@SuppressWarnings("unchecked")
	private HashMap<Class<? extends Packet>, Integer>[] classToId = new HashMap[ProtocollVersion.values().length];

	private List<Class<? extends Packet>> registerPackets = new ArrayList<>();
	private boolean packetListChanged = true;

	public NormalPacketCreator() {
		for(int i = 0;i<this.classToId.length;i++) {
			this.classToId[i] = new HashMap<>();
		}
	}

	@Override
	public int countPackets() {
		return this.getRegisteredPackets().size();
	}

	@SuppressWarnings("deprecation")
	protected Packet getPacket0(int compressed,Player p, ByteBuf b){
		PacketHolder<?> cons = this.packetsId[compressed];
		if(cons == null) {
			return null;
		}
		try {
			Packet packet = cons.newInstance();
			if (p == null || p.getVersion() == null){
				BungeeUtil.debug("Version of '"+(p == null ? "<Null client>" : p.getName())+"' is undefined");
				return packet.initClass(compressed).load(b, ClientVersion.UnderknownVersion);
			} else {
				return packet.initClass(compressed).load(b, p.getVersion());
			}
		}
		catch (Exception e) {
			throw ExceptionUtils.createRuntimeException(ExceptionUtils.setExceptionMessage(e, "Packet error (Version: " + (p == null ? "unknown" : p.getVersion()) + ", Class: " + (cons == null || cons.getConstuctor() == null ? "null" : cons.getConstuctor().getDeclaringClass().getName()) + ", Id: 0x"+Integer.toHexString(this.getPacketId(compressed)).toUpperCase()+") -> "+e.getMessage()));
		}
	}

	@Override
	public Packet getPacket0(ProtocollVersion version,Protocol protocol, Direction d, Integer id, ByteBuf b, Player p) {
		int compressed = this.calculate(version, protocol, d, id);
		Packet packet = null;
		if ((packet = this.getPacket0(compressed, p, b)) == null) {
			if(version.getBasedVersion().getProtocollVersion() == version){ //Fallback (based version) (1.8-1.9)
				return null;
			}
			else{
				return this.getPacket0(version.getBasedVersion().getProtocollVersion(), protocol, d, id, b, p);
			}
		}
		return packet;
	}

	public Packet getPacket1(ProtocollVersion version,ProtocollVersion orginalVersion,Protocol protocol, Direction d, Integer id, ByteBuf b, Player p) {
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public int getPacketId(ProtocollVersion version,Class<? extends Packet> clazz) {
		if(version == ProtocollVersion.Unsupported){
			throw new NullPointerException("Unsupported version!");
		}
		if (!clazz.getName().endsWith("$-1")) {
			while (clazz.getName().contains("$")) {
				clazz = (Class<? extends Packet>) clazz.getSuperclass();
			}
		}
		if (!this.classToId[version.ordinal()].containsKey(clazz)) {
			if(version.getBasedVersion().getProtocollVersion() != version) {
				return this.getPacketId(version.getBasedVersion().getProtocollVersion(),clazz);
			} else {
				return -1;
			}
		}
		return this.classToId[version.ordinal()].get(clazz);
	}

	@Override
	public List<Class<? extends Packet>> getRegisteredPackets() {
		if (this.packetListChanged) {
			this.registerPackets.clear();
			for (PacketHolder<?> element : this.packetsId) {
				if(element == null) {
					continue;
				}
				Constructor<? extends Packet> constructor = element.getConstuctor();
				if (constructor == null) {
					continue;
				}
				if(!this.registerPackets.contains(constructor.getDeclaringClass())) {
					this.registerPackets.add(constructor.getDeclaringClass());
				}
			}
			this.packetListChanged = false;
		}
		return this.registerPackets;
	}


	@Override
	public int loadPacket(ProtocollVersion version,Protocol p, Direction d, Integer id, Class<? extends Packet> clazz) {
		int compressedId = this.calculate(version,p, d, id);
		this.classToId[version.ordinal()].put(clazz, compressedId);
		return compressedId;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void registerPacket(Protocol p, Direction d, Class<? extends Packet> clazz, ProtocollId... ids) {
		try {
			for(ProtocollId id : ids) {
				if(id != null && id.isValid()){
					this.packetsId[this.loadPacket(id.getVersion(),p, d, id.getId(), clazz)] = new PacketHolder(clazz == null ? null : (Constructor<?>) clazz.getConstructor());
				}
			}
		}
		catch (NoSuchMethodException | SecurityException ex) {
			System.out.println(clazz);
			ex.printStackTrace();
		}
		this.packetListChanged = true;
	}

	@Override
	public <T extends Packet> void releasePacket(T obj) {}

	@Override
	public void unregisterPacket(ProtocollVersion version,Protocol p, Direction d, Integer id) {
		this.packetsId[this.calculate(version,p, d, id)] = null;
		this.packetListChanged = true;
	}
}
