package dev.wolveringer.BungeeUtil;

import java.util.ArrayList;

import net.md_5.bungee.api.plugin.Plugin;

public class AsyncCatcher {
	private static boolean enabled = true;
	private static ArrayList<ThreadGroup> unchecked_threads= new ArrayList<ThreadGroup>();
	private static ArrayList<Plugin> unchecked_plugins = new ArrayList<Plugin>();
	public static void catchOp(String reason) {
		if(enabled){
			if(unchecked_threads.size() != 0)
				for(Plugin p : unchecked_plugins)
					if(p.getDescription().getName().contains(Thread.currentThread().getThreadGroup().getName()))
						return;
			if(unchecked_threads.size() != 0)
				if(unchecked_threads.contains(Thread.currentThread().getThreadGroup()))
					return;
			throw new IllegalStateException("Asynchronous " + reason + "!"); 
		}
	}
	
	public static void enable(Plugin plugin){
		if(!unchecked_threads.contains(Thread.currentThread().getThreadGroup()))
			throw new UnsupportedOperationException("Thread alredy unregistered!");
		unchecked_threads.add(Thread.currentThread().getThreadGroup());
		unchecked_plugins.add(plugin);
	}
	public static void disable(Plugin plugin){
		if(unchecked_threads.contains(Thread.currentThread().getThreadGroup()))
			throw new UnsupportedOperationException("Thread alredy registered!");
		unchecked_plugins.remove(plugin);
		unchecked_threads.remove(Thread.currentThread().getThreadGroup());
	}
	@Deprecated
	public static void disableAll(){
		enabled = false;
	}
}