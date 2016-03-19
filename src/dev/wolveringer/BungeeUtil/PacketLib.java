package dev.wolveringer.BungeeUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import dev.wolveringer.BungeeUtil.packets.Packet;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class PacketLib {
	private static HashMap<Class<? extends Packet>, ArrayList<PacketHandler>> handlers = new HashMap<Class<? extends Packet>, ArrayList<PacketHandler>>() {
		private static final long serialVersionUID = 1L;

		@Override
		public ArrayList<PacketHandler> get(Object paramObject) {
			Object r = super.get(paramObject);
			if(r == null)
				try{
					super.put((Class<? extends Packet>) paramObject, new ArrayList<PacketHandler>());
				}catch (Exception e){
				}
			return super.get(paramObject);
		}
	};

	@SuppressWarnings("serial")
	private static HashMap<Class<? extends Packet>, ArrayList<Class<? extends Packet>>> superclazzes = new HashMap<Class<? extends Packet>, ArrayList<Class<? extends Packet>>>() {
		@Override
		public ArrayList<Class<? extends Packet>> get(Object paramObject) {
			ArrayList<Class<? extends Packet>> r = super.get(paramObject);
			if(r == null)
				try{
					ArrayList<Class<? extends Packet>> list = new ArrayList<Class<? extends Packet>>();
					Class c = (Class) paramObject;
					if(c == Packet.class){
						list.add(c);
					}else
						for(Class<? extends Packet> clazz : Packet.getRegisteredPackets())
							if(clazz.isAssignableFrom(c))
								if(clazz != Packet.class)
									list.add(clazz);
					super.put((Class<? extends Packet>) paramObject, r = list);
				}catch (Exception e){ }
			return r;
		}
	};

	public static void addHandler(PacketHandler h) {
		for(Class c : superclazzes.get(getPacketType(h))){
			handlers.get(c).add(h);
		}
	}

	public static void removeHandler(PacketHandler h) {
		for(Class c : superclazzes.get(getPacketType(h)))
			handlers.get(c).remove(h);
	}
	
	public static PacketHandleEvent handle(PacketHandleEvent e) {
		Class<? extends Packet> c = e.getPacket().getClass();
		for(PacketHandler h : new ArrayList<>(handlers.get(c)))
			h.handle(e);
		for(PacketHandler h : new ArrayList<>(handlers.get(Packet.class)))
			h.handle(e);
		return e;
	}

	private static Class getPacketType(PacketHandler s) {
		for(Type interfaces : s.getClass().getGenericInterfaces())
			if(interfaces instanceof ParameterizedType)
				for(Type c : ((ParameterizedType) interfaces).getActualTypeArguments())
					try{
						if(c.equals(Packet.class))
							continue;
						return Class.forName(c.toString().split(" ")[1]);
					}catch (ClassNotFoundException e){
						e.printStackTrace();
					}
		return Packet.class;
	}
}
