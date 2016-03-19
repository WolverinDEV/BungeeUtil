package dev.wolveringer.BungeeUtil.item.itemmeta;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import dev.wolveringer.BungeeUtil.item.Item;

@SuppressWarnings({ "deprecation", "rawtypes" })
public class MetaFactory {
	private static ItemMetaProxy proxy = new ItemMetaProxy();
	public static boolean equals(ItemMeta meta, Object object) {
		if(object == null && meta == null)
			return true;
		else if(object == null || meta == null){
			return false;
		}else if(!(object instanceof ItemMeta)){
			return false;
		}
		ItemMeta im = (ItemMeta) object;
		if(!im.getLore().equals(meta.getLore())){
			return false;
		}
		if(!im.getDisplayName().equalsIgnoreCase(meta.getDisplayName())){
			return false;
		}
		return true;
	}

	public static ItemMeta getItemMeta(Item item) {
		/*
		switch (item.getType()) {
			case SKULL_ITEM:
				return createProxyInstance(SkullMeta.class,item);
			default:
				return createProxyInstance(CraftItemMeta.class,item);
		}
		*/
		switch (item.getType()) {
			case SKULL_ITEM:
				return new SkullMeta(item);
			default:
				return new CraftItemMeta(item);
		}
	}

	protected static ItemMeta createProxyInstance(Class c,Item item) {
		ProxyFactory factor = new ProxyFactory();
		factor.setSuperclass(c);
		factor.setHandler(proxy);
		try{
			return (ItemMeta) factor.create(new Class[]{Item.class}, new Object[]{item});
		}catch (NoSuchMethodException | IllegalArgumentException | InstantiationException | IllegalAccessException | InvocationTargetException e){
			e.printStackTrace();
		}
		return new CraftItemMeta(item);
	}

	protected static class ItemMetaProxy implements MethodHandler {
		@SuppressWarnings("serial")
		private static HashMap<Class, ArrayList<String>> whitelist = new HashMap<Class, ArrayList<String>>(){
			public java.util.ArrayList<String> get(Object key) {
				if(super.get(key) == null)
					super.put((Class) key, new ArrayList<String>());
				return super.get(key);
			}
		};
		public static void addWhiteList(Class clazz,String methode){
			whitelist.get(clazz).add(methode);
		}
		@Override
		public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
			if(self instanceof CraftItemMeta){
				if(thisMethod.getName().startsWith("set")){
					for(Class c : whitelist.keySet())
						if(isInstance(self, c))
							if(whitelist.get(c).contains(thisMethod.getName()))
								return proceed.invoke(self, args);
					Object resulut = proceed.invoke(self, args); //? null
					self.getClass().getDeclaredMethod("build").invoke(self);
					((CraftItemMeta)self).fireUpdate();
					return resulut;
				}
			}
			return proceed.invoke(self, args);
		}
		private boolean isInstance(Object base,Class of){
			try{
				of.cast(base);
				return true;
			}catch(Exception e){
				return false;
			}
		}
	}
}
