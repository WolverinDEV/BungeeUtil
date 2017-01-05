package dev.wolveringer.bungeeutil.packets.creator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import dev.wolveringer.bungeeutil.BungeeUtil;
import dev.wolveringer.bungeeutil.cache.UsedClassProcessing;
import dev.wolveringer.bungeeutil.packets.Packet;
import dev.wolveringer.bungeeutil.packets.Packet.ProtocollId;
import dev.wolveringer.bungeeutil.player.ClientVersion;
import dev.wolveringer.bungeeutil.player.Player;
import dev.wolveringer.bungeeutil.player.connection.ProtocollVersion;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import net.md_5.bungee.protocol.Protocol;
import net.md_5.bungee.protocol.ProtocolConstants.Direction;

public class CachedPacketCreator extends AbstractPacketCreator {
	@Getter
	private final AbstractPacketCreator handle;

	private ArrayList<Thread> threads = new ArrayList<>();

	private List<? extends Packet> packets = new ArrayList<>();

	private HashMap<Integer, List<Packet>> newPackets = new HashMap<Integer, List<Packet>>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public List<Packet> get(Object key) {
			List<Packet> out = super.get(key);
			if(out == null) {
				super.put((Integer) key, out = new ArrayList<>());
			}
			return out;
		};
	};

	private HashMap<Class<?>, UsedClassProcessing<?>> cleaner = new HashMap<Class<?>, UsedClassProcessing<?>>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public UsedClassProcessing<?> get(Object key) {
			UsedClassProcessing<?> out = super.get(key);
			if(out == null) {
				super.put((Class<?>) key, out = new UsedClassProcessing<>((Class<?>) key, -1));
			}
			return out;
		};
	};

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Runnable processingWorker = () -> {
		while (true) {
			Packet packet = null;
			synchronized (CachedPacketCreator.this.packets) {
				if(CachedPacketCreator.this.packets.size() > 0){
					packet = CachedPacketCreator.this.packets.remove(0);
				}
			}
			if(packet == null){
				try {
					Thread.sleep(50);
				}catch(Exception e){}
				continue;
			}

			int id = CachedPacketCreator.this.calculate(ProtocollVersion.Unsupported, packet.getProtocol(), packet.getDirection(), packet.getPacketId());
			id = id & 0xFFFF;

			UsedClassProcessing c = CachedPacketCreator.this.cleaner.get(packet.getClass());
			c.processing(packet);
			synchronized (CachedPacketCreator.this.newPackets) {
				CachedPacketCreator.this.newPackets.get(id).add(packet);
			}

		}
	};

	public CachedPacketCreator(AbstractPacketCreator handle, int threads) {
		this.handle = handle;
		for(int i = 0;i<threads;i++) {
			this.threads.add(new Thread(this.processingWorker));
		}

		for(Thread t : this.threads) {
			t.start();
		}
	}
	@Override
	public int countPackets() {
		return this.handle.countPackets();
	}
	@SuppressWarnings("deprecation")
	@Override
	public Packet getPacket0(ProtocollVersion version, Protocol protocol, Direction d, Integer id, ByteBuf b, Player p) {
		int compressed = this.calculate(version, protocol, d, id);
		int scompressed = compressed & 0xFFFF;

		Packet packet = null;
		synchronized (this.newPackets) {

			List<Packet> avPackets = this.newPackets.get(scompressed);
			if(avPackets.size() > 0){
				while (packet == null && avPackets.size() > 0) {
					packet = avPackets.remove(0);
				}
				BungeeUtil.debug("Using cached packet (Packets left: "+avPackets.size()+") ("+packet+")");
			}
		}
		if(packet != null){
			packet.initClass(compressed).load(b, p == null ? ClientVersion.UnderknownVersion : p.getVersion());
		}
		else {
			packet = this.handle.getPacket0(version, protocol, d, id, b, p);
		}
		return packet;
	}

	@Override
	public int getPacketId(ProtocollVersion version, Class<? extends Packet> clazz) {
		return this.handle.getPacketId(version, clazz);
	}

	public int getPacketsToProcessing(){
		synchronized (this.packets) {
			return this.packets.size();
		}
	}

	public HashMap<Class<? extends Packet>, Integer> getProcessedPackets(){
		 HashMap<Class<? extends Packet>, Integer> out = new HashMap<Class<? extends Packet>, Integer>();
		synchronized (this.newPackets) {
			 for(Entry<Integer, List<Packet>> e : this.newPackets.entrySet()) {
				for(Packet p : e.getValue()) {
					out.put(p.getClass(), out.getOrDefault(p.getClass(), 0) + 1);
				}
			}
		}
		return out;
	}

	@Override
	public List<Class<? extends Packet>> getRegisteredPackets() {
		return this.handle.getRegisteredPackets();
	}

	@Override
	public int loadPacket(ProtocollVersion version, Protocol p, Direction d, Integer id, Class<? extends Packet> clazz) {
		return this.handle.loadPacket(version, p, d, id, clazz);
	}

	@Override
	public void registerPacket(Protocol p, Direction d, Class<? extends Packet> clazz, ProtocollId... ids) {
		this.handle.registerPacket(p, d, clazz, ids);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Packet> void releasePacket(T packet) {
		synchronized (this.packets) {
			((List<T>) this.packets).add(packet);
		}
	}

	@Override
	public void unregisterPacket(ProtocollVersion version, Protocol p, Direction d, Integer id) {
		this.handle.unregisterPacket(version, p, d, id);
	}
}