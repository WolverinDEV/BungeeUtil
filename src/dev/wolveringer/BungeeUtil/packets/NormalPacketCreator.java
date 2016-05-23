package dev.wolveringer.BungeeUtil.packets;

import io.netty.buffer.ByteBuf;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.md_5.bungee.protocol.Protocol;
import net.md_5.bungee.protocol.ProtocolConstants.Direction;
import dev.wolveringer.BungeeUtil.ClientVersion;
import dev.wolveringer.BungeeUtil.ClientVersion.BigClientVersion;
import dev.wolveringer.BungeeUtil.ClientVersion.ProtocollVersion;
import dev.wolveringer.BungeeUtil.Main;
import dev.wolveringer.BungeeUtil.Player;
import dev.wolveringer.BungeeUtil.packets.Packet.ProtocollId;

public class NormalPacketCreator extends AbstractPacketCreator {
	@SuppressWarnings("unchecked")
	private Constructor<? extends Packet>[] packetsId = new Constructor[((ProtocollVersion.values().length & 0x0F) << 16) | ((Protocol.values().length & 0x0F) << 12) | ((Direction.values().length & 0x0F) << 8) | 0xFF]; // Calculate max packet compressed id. (0xFF = Max ID)
	@SuppressWarnings("unchecked")
	private HashMap<Class<? extends Packet>, Integer>[] classToId = new HashMap[BigClientVersion.values().length];
	
	private List<Class<? extends Packet>> registerPackets = new ArrayList<>();
	private boolean changed = true;
	
	public NormalPacketCreator() {
		for(int i = 0;i<classToId.length;i++)
			classToId[i] = new HashMap<>();
	}
	
	@SuppressWarnings("unchecked")
	public int getPacketId(ProtocollVersion version,Class<? extends Packet> clazz) {
		if(version == ProtocollVersion.Unsupported){
			throw new NullPointerException("Unsupported version!");
		}
		if (!clazz.getName().endsWith("$-1")) while (clazz.getName().contains("$")) {
			clazz = (Class<? extends Packet>) clazz.getSuperclass();
		}
		if (!classToId[version.ordinal()].containsKey(clazz)) //throw new NullPointerException("Packet " + clazz.getName() + " not loadet.");
			return -1;
		return   classToId[version.ordinal()].get(clazz);
	}
	
	public List<Class<? extends Packet>> getRegisteredPackets() {
		if (changed) {
			registerPackets.clear();
			for (int i = 0; i < packetsId.length; i++) {
				Constructor<? extends Packet> constructor = packetsId[i];
				if (constructor == null) continue;
				registerPackets.add(constructor.getDeclaringClass());
			}
			changed = false;
		}
		return registerPackets;
	}
	
	public Packet getPacket0(ProtocollVersion version,Protocol protocol, Direction d, Integer id, ByteBuf b, Player p) {
		int compressed = calculate(version,protocol, d, id);
		Constructor<? extends Packet> cons = null;
		try {
			if ((cons = packetsId[compressed]) == null) {
				if(version.getBasedVersion().getProtocollVersion() == version) //Fallback (based version) (1.8-1.9)
					return null;
				else
					return getPacket0(version.getBasedVersion().getProtocollVersion(), protocol, d, id, b, p);
			}
			Packet packet = cons.newInstance();
			if (p == null || p.getVersion() == null){
				Main.debug("Version of '"+(p == null ? "<Null client>" : p.getName())+"' is undefined");
				return packet.setcompressedId(compressed).load(b, ClientVersion.UnderknownVersion);
			}
			else return packet.setcompressedId(compressed).load(b, p.getVersion());
		}
		catch (Exception e) {
			throw new RuntimeException("Packet error -> " + (cons == null ? "Class not found" : cons.getDeclaringClass().getName()) + " -> "+e.getMessage(),e);
		}
	}
	
	public int loadPacket(ProtocollVersion version,Protocol p, Direction d, Integer id, Class<? extends Packet> clazz) {
		//clazz = getPacketWithDefaultConstructor(clazz);
		int compressedId = calculate(version,p, d, id);
		classToId[version.ordinal()].put(clazz, compressedId);
		return compressedId;
	}
	
	
	public void registerPacket(Protocol p, Direction d, Class<? extends Packet> clazz, ProtocollId... ids) {
		//clazz = getPacketWithDefaultConstructor(clazz);
		try {
			for(ProtocollId id : ids)
				if(id != null && id.isValid())
				packetsId[loadPacket(id.getVersion(),p, d, id.getId(), clazz)] = (Constructor<? extends Packet>) clazz.getConstructor();
			/*
			if(v1_8_id != null){
			packetsId[loadPacket(ProtocollVersion.v1_7,p, d, v1_8_id, clazz)] = (Constructor<? extends Packet>) clazz.getConstructor();
			packetsId[loadPacket(ProtocollVersion.v1_8,p, d, v1_8_id, clazz)] = (Constructor<? extends Packet>) clazz.getConstructor();
			}
			if(v1_9_id != null)
			packetsId[loadPacket(ProtocollVersion.v1_9,p, d, v1_9_id, clazz)] = (Constructor<? extends Packet>) clazz.getConstructor();
			*/
		}
		catch (NoSuchMethodException | SecurityException ex) {
			System.out.println(clazz);
			ex.printStackTrace();
		}
		changed = true;
	}
	
	/*
	@SuppressWarnings({ "unchecked", "deprecation" })
	private Class<? extends Packet> getPacketWithDefaultConstructor(Class<? extends Packet> in) {
		try {
			for (Constructor<?> c : in.getConstructors())
				if (c.getParameterTypes().length == 0) return in;
			for (Constructor<?> c : in.getDeclaredConstructors())
				if (c.getParameterTypes().length == 0) return in;
		}
		catch (Exception e) {
		
		}
		try {
			Main.sendMessage("Adding default constructor to class: " + in.getName());
			ClassPool pool = IIInitialHandler.pool();
			pool.insertClassPath(new ClassClassPath(in));
			CtClass ct_in = pool.get(in.getName());
			ct_in.setName(in.getName() + "$-1");
			ct_in.addConstructor(defaultConstructor(ct_in));
			return ct_in.toClass(IIInitialHandler.getClassLoader());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return in;
	}
	
	private CtConstructor defaultConstructor(CtClass declaring) throws CannotCompileException {
		CtConstructor cons = new CtConstructor((CtClass[]) null, declaring);
		
		ConstPool cp = declaring.getClassFile2().getConstPool();
		Bytecode code = new Bytecode(cp, 1, 1);
		code.addAload(0);
		try {
			code.addInvokespecial(declaring.getSuperclass(), "<init>", "()V");
		}
		catch (NotFoundException e) {
			throw new CannotCompileException(e);
		}
		
		code.add(Bytecode.RETURN);
		
		cons.getMethodInfo2().setCodeAttribute(code.toCodeAttribute());
		return cons;
	}
	*/
	
	public void unregisterPacket(ProtocollVersion version,Protocol p, Direction d, Integer id) {
		packetsId[calculate(version,p, d, id)] = null;
		changed = true;
		
	}
	
	public int countPackets() {
		return getRegisteredPackets().size();
	}
}
