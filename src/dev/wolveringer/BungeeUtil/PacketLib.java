package dev.wolveringer.BungeeUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import dev.wolveringer.BungeeUtil.packets.Packet;
import dev.wolveringer.BungeeUtil.packets.PacketPlayInFlying;
import dev.wolveringer.BungeeUtil.packets.PacketPlayInPosition;
import lombok.AllArgsConstructor;
import lombok.Getter;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class PacketLib {
	@AllArgsConstructor
	@Getter
	private static class PacketHandlerHolder implements Comparable<PacketHandlerHolder> {
		private PacketHandler handler;
		private int importance;
		
		@Override
		public int compareTo(PacketHandlerHolder o) {
			return Integer.compare(o.importance, importance);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((handler == null) ? 0 : handler.hashCode());
			result = prime * result + importance;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if(obj instanceof PacketHandler)
				return obj.equals(handler);
			if (getClass() != obj.getClass())
				return false;
			PacketHandlerHolder other = (PacketHandlerHolder) obj;
			if (handler == null) {
				if (other.handler != null)
					return false;
			} else if (!handler.equals(other.handler))
				return false;
			if (importance != other.importance)
				return false;
			return true;
		}
	}
	private static HashMap<Class<? extends Packet>, ArrayList<PacketHandlerHolder>> handlers = new HashMap<Class<? extends Packet>, ArrayList<PacketHandlerHolder>>() {
		private static final long serialVersionUID = 1L;

		@Override
		public ArrayList<PacketHandlerHolder> get(Object paramObject) {
			Object r = super.get(paramObject);
			if(r == null)
				try{
					super.put((Class<? extends Packet>) paramObject, new ArrayList<PacketHandlerHolder>(){
						private static final long serialVersionUID = 1L;
						public void insertSorted(PacketHandlerHolder value) {
					        super.add(value);
					        Comparable<PacketHandlerHolder> cmp = (Comparable<PacketHandlerHolder>) value;
					        for (int i = size()-1; i > 0 && cmp.compareTo(get(i-1)) < 0; i--)
					            Collections.swap(this, i, i-1);
					    }
						public boolean add(PacketHandlerHolder value) {
							insertSorted(value);
							return true;
						}
					});
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
					Main.debug("Sarching for subinstances of "+c);
					if(c != Packet.class)
						for(Class<? extends Packet> clazz : Packet.getRegisteredPackets()){
							if(c.isAssignableFrom(clazz)){
								list.add(clazz);
							}
						}
					list.add(c);
					r = (list = new ArrayList<>(new HashSet<>(list)));
					for(Class x : list)
						Main.debug("Found class instance of "+c+" -> "+x);
					super.put((Class<? extends Packet>) paramObject, r);
				}catch (Exception e){ }
			return r;
		}
	};

	public static void addHandler(PacketHandler h) {
		addHandler(h, 0);
	}

	public static void addHandler(PacketHandler h,int importance) {
		for(Class c : superclazzes.get(getPacketType(h))){
			handlers.get(c).add(new PacketHandlerHolder(h, importance));
		}
	}

	
	public static void removeHandler(PacketHandler h) {
		for(Class c : superclazzes.get(getPacketType(h))){
			handlers.get(c).remove(h);
		}
	}
	
	public static PacketHandleEvent handle(PacketHandleEvent e) {
		Class<? extends Packet> c = e.getPacket().getClass();
		for(PacketHandlerHolder h : new ArrayList<>(handlers.get(c)))
			h.handler.handle(e);
		for(PacketHandlerHolder h : new ArrayList<>(handlers.get(Packet.class)))
			h.handler.handle(e);
		return e;
	}

	private static Class getPacketType(PacketHandler s) {
		for(Type interfaces : s.getClass().getGenericInterfaces())
			if(interfaces instanceof ParameterizedType)
				for(Type c : ((ParameterizedType) interfaces).getActualTypeArguments())
					try{
						if(c.equals(Packet.class))
							continue;
						try{
							return Class.forName(c.toString().split(" ")[1]);
						}catch(ExceptionInInitializerError e){
							System.out.println("Cant find class "+c.toString().split(" ")[1]);
							throw e;
						}
					}catch (ClassNotFoundException e){
						e.printStackTrace();
					}
		return Packet.class;
	}
	
	public static void main(String[] args) {
		PacketHandler handler = new PacketHandler<PacketPlayInFlying>() {
			@Override
			public void handle(PacketHandleEvent<PacketPlayInFlying> e) {
				System.out.println("Handle "+e);
			}
		};
		System.out.println("Type: "+getPacketType(handler));
		addHandler(handler);
		handle(new PacketHandleEvent(new PacketPlayInPosition(), null));
	}
}
