package dev.wolveringer.bungeeutil.packets.creator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

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
	
	public int getPacketsToProcessing(){
		synchronized (packets) {
			return packets.size();
		}
	}
	
	public HashMap<Class<? extends Packet>, Integer> getProcessedPackets(){
		 HashMap<Class<? extends Packet>, Integer> out = new HashMap<Class<? extends Packet>, Integer>();
		synchronized (newPackets) {
			 for(Entry<Integer, List<Packet>> e : newPackets.entrySet())
				 for(Packet p : e.getValue())
					 out.put(p.getClass(), out.getOrDefault(p.getClass(), 0) + 1);
		}
		return out;
	}
	
	public CachedPacketCreator(AbstractPacketCreator handle, int threads) {
		this.handle = handle;
		for(int i = 0;i<threads;i++)
			this.threads.add(new Thread(processingWorker));
		
		for(Thread t : this.threads)
			t.start();
	}
	
	private List<? extends Packet> packets = new ArrayList<>();
	
	@SuppressWarnings("serial")
	private HashMap<Integer, List<Packet>> newPackets = new HashMap<Integer, List<Packet>>(){
		public List<Packet> get(Object key) {
			List<Packet> out = super.get(key);
			if(out == null)
				super.put((Integer) key, out = new ArrayList<>());
			return out;
		};
	};
	
	private Object newPacketsLock = new Object();
	private HashMap<Class, UsedClassProcessing<?>> cleaner = new HashMap<Class, UsedClassProcessing<?>>(){
		public UsedClassProcessing<?> get(Object key) {
			UsedClassProcessing out = super.get(key);
			if(out == null)
				super.put((Class) key, out = new UsedClassProcessing<>((Class) key, -1));
			return out;
		};
	};
	private Runnable processingWorker = new Runnable() {
		@Override
		public void run() {
			while (true) {
				Packet packet = null;
				synchronized (packets) {
					if(packets.size() > 0){
						packet = packets.remove(0);
					}
				}
				if(packet == null){
					try {
						Thread.sleep(50);
					}catch(Exception e){}
					continue;
				}
				
				int id = calculate(ProtocollVersion.Unsupported, packet.getProtocol(), packet.getDirection(), packet.getPacketId());
				id = id & 0xFFFF;
				
				UsedClassProcessing c = cleaner.get(packet.getClass());
				c.processing(packet);
				synchronized (newPackets) {
					newPackets.get((Integer) id).add(packet);
				}
			
			}
		}
	};
	
	@Override
	public Packet getPacket0(ProtocollVersion version, Protocol protocol, Direction d, Integer id, ByteBuf b, Player p) {
		int compressed = calculate(version, protocol, d, id);
		int scompressed = compressed & 0xFFFF;
		
		Packet packet = null;
		synchronized (newPackets) {
			
			List<Packet> avPackets = newPackets.get((Integer) scompressed);
			if(avPackets.size() > 0){
				while (packet == null && avPackets.size() > 0) {
					packet = (Packet) avPackets.remove(0);
				}
				System.out.println("Cached packet! ("+packet+")");
			}
		}
		if(packet != null){
			packet.initClass(compressed).load(b, p == null ? ClientVersion.UnderknownVersion : p.getVersion());
		}
		else {
			packet = handle.getPacket0(version, protocol, d, id, b, p);
		}
		return packet;
	}
	
	@Override
	public <T extends Packet> void releasePacket(T packet) {
		synchronized (packets) {
			((List<T>) packets).add(packet);
		}
	}

	@Override
	public int loadPacket(ProtocollVersion version, Protocol p, Direction d, Integer id, Class<? extends Packet> clazz) {
		return handle.loadPacket(version, p, d, id, clazz);
	}

	@Override
	public void registerPacket(Protocol p, Direction d, Class<? extends Packet> clazz, ProtocollId... ids) {
		handle.registerPacket(p, d, clazz, ids);
	}

	@Override
	public void unregisterPacket(ProtocollVersion version, Protocol p, Direction d, Integer id) {
		handle.unregisterPacket(version, p, d, id);
	}

	@Override
	public int countPackets() {
		return handle.countPackets();
	}

	@Override
	public int getPacketId(ProtocollVersion version, Class<? extends Packet> clazz) {
		return handle.getPacketId(version, clazz);
	}

	@Override
	public List<Class<? extends Packet>> getRegisteredPackets() {
		return handle.getRegisteredPackets();
	}
}