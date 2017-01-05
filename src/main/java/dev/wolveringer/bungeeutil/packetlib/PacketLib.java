package dev.wolveringer.bungeeutil.packetlib;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import dev.wolveringer.bungeeutil.BungeeUtil;
import dev.wolveringer.bungeeutil.packets.Packet;
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
			return Integer.compare(o.importance, this.importance);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if(obj instanceof PacketHandler) {
				return obj.equals(this.handler);
			}
			if (this.getClass() != obj.getClass()) {
				return false;
			}
			PacketHandlerHolder other = (PacketHandlerHolder) obj;
			if (this.handler == null) {
				if (other.handler != null) {
					return false;
				}
			} else if (!this.handler.equals(other.handler)) {
				return false;
			}
			if (this.importance != other.importance) {
				return false;
			}
			return true;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (this.handler == null ? 0 : this.handler.hashCode());
			//result = prime * result + importance;
			return result;
		}
	}

	private static HashMap<Class<? extends Packet>, ArrayList<PacketHandlerHolder>> handlers = new HashMap<Class<? extends Packet>, ArrayList<PacketHandlerHolder>>() {
		private static final long serialVersionUID = 1L;

		@Override
		public ArrayList<PacketHandlerHolder> get(Object paramObject) {
			Object r = super.get(paramObject);
			if(r == null) {
				try{
					super.put((Class<? extends Packet>) paramObject, new ArrayList<PacketHandlerHolder>(){
						private static final long serialVersionUID = 1L;
						@Override
						public boolean add(PacketHandlerHolder value) {
							this.insertSorted(value);
							return true;
						}
						public void insertSorted(PacketHandlerHolder value) {
					        super.add(value);
					        Comparable<PacketHandlerHolder> cmp = value;
					        for (int i = this.size()-1; i > 0 && cmp.compareTo(this.get(i-1)) < 0; i--) {
								Collections.swap(this, i, i-1);
							}
					    }
					});
				}catch (Exception e){
				}
			}
			return super.get(paramObject);
		}
	};

	private static HashMap<Class<? extends Packet>, ArrayList<Class<? extends Packet>>> superclazzes = new HashMap<Class<? extends Packet>, ArrayList<Class<? extends Packet>>>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public ArrayList<Class<? extends Packet>> get(Object paramObject) {
			ArrayList<Class<? extends Packet>> r = super.get(paramObject);
			if(r == null) {
				try{
					ArrayList<Class<? extends Packet>> list = new ArrayList<Class<? extends Packet>>();
					Class c = (Class) paramObject;
					BungeeUtil.getInstance();
					BungeeUtil.debug("Sarching for subinstances of "+c);
					if(c != Packet.class) {
						for(Class<? extends Packet> clazz : Packet.getRegisteredPackets()){
							if(c.isAssignableFrom(clazz)){
								list.add(clazz);
							}
						}
					}
					list.add(c);
					r = list = new ArrayList<>(new HashSet<>(list));
					for(Class x : list) {
						BungeeUtil.getInstance();
						BungeeUtil.debug("Found class instance of "+c+" -> "+x);
					}
					super.put((Class<? extends Packet>) paramObject, r);
				}catch (Exception e){ }
			}
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


	private static Class getPacketType(PacketHandler s) {
		for(Type interfaces : s.getClass().getGenericInterfaces()) {
			if(interfaces instanceof ParameterizedType) {
				for(Type c : ((ParameterizedType) interfaces).getActualTypeArguments()) {
					try{
						if(c.equals(Packet.class)) {
							continue;
						}
						try{
							return Class.forName(c.toString().split(" ")[1]);
						}catch(ExceptionInInitializerError e){
							System.out.println("Cant find class "+c.toString().split(" ")[1]);
							throw e;
						}
					}catch (ClassNotFoundException e){
						e.printStackTrace();
					}
				}
			}
		}
		return Packet.class;
	}

	public static PacketHandleEvent handlePacket(PacketHandleEvent e) {
		Class<? extends Packet> c = e.getPacket().getClass();
		for(PacketHandlerHolder h : new ArrayList<>(handlers.get(c))) {
			h.handler.handle(e);
		}
		for(PacketHandlerHolder h : new ArrayList<>(handlers.get(Packet.class))) {
			h.handler.handle(e);
		}
		return e;
	}

	public static void removeHandler(PacketHandler h) {
		for(Class c : superclazzes.get(getPacketType(h))){
			for(PacketHandlerHolder h1 : new ArrayList<>(handlers.get(c))) {
				if(h1 != null && h1.getHandler() != null && h1.getHandler().equals(h)) {
					handlers.get(c).remove(h1);
				}
			}
		}
		for(PacketHandlerHolder h1 : handlers.get(Packet.class)) {
			if(h1 != null && h1.getHandler() != null && h1.getHandler().equals(h)) {
				handlers.get(Packet.class).remove(h1);
			}
		}
	}
}
