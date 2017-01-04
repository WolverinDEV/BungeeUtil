package dev.wolveringer.bungeeutil.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class CachedHashMap<K,V> extends HashMap<K, V> {
	private CachedArrayList<K> keys;
	private boolean locked = false;
	
	public CachedHashMap(int defautTime, TimeUnit defaultTimeUnit) {
		keys = new CachedArrayList<>(defautTime, defaultTimeUnit);
	}
	
	@Override
	public V put(K key, V value) {
		putKey(key);
		return super.put(key, value);
	}
	
	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		for(K key : m.keySet())
			putKey(key);
		super.putAll(m);
	}
	
	@Override
	public V get(Object key) {
		V out = super.get(key);
		if(!locked)
			if(!keys.contains(key)){
				out = null;
				super.remove(key);
			}
		return out;
	}
	
	public V getOrDefault(Object key, V defaultValue) {
		V out;
		if((out = get(key)) == null)
			return defaultValue;
		return out;
	}
	
	@Override
	public V remove(Object key) {
		keys.remove(key);
		return super.remove(key);
	}
	
	public boolean remove(Object key, Object value) {
		keys.remove(key);
		return remove(key, value);
	}
	
	private void putKey(K key){
		keys.remove(key);
		keys.add(key);
	}

	public V put(K key, V value, int time, TimeUnit unit) {
		keys.add(key, time, unit);
		return super.put(key, value);
	}
	
	@Override
	public int size() {
		return keys.size();
	}
	
	@Override
	public Set<K> keySet() {
		if(!locked)
			keys.update();
		return new HashSet<>(keys);
	}
	
	@Override
	public Collection<V> values() {
		for(K key : super.keySet())
			get(key); //Update value
		return super.values();
	}

	public void lock() {
		locked = true;
		keys.update();
	}
	public void unlock(){
		locked = false;
	}
}
