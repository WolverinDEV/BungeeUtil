package dev.wolveringer.bungeeutil.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class CachedHashMap<K,V> extends HashMap<K, V> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CachedArrayList<K> keys;
	private boolean locked = false;

	public CachedHashMap(int defautTime, TimeUnit defaultTimeUnit) {
		this.keys = new CachedArrayList<>(defautTime, defaultTimeUnit);
	}

	@Override
	public V get(Object key) {
		V out = super.get(key);
		if(!this.locked) {
			if(!this.keys.contains(key)){
				out = null;
				super.remove(key);
			}
		}
		return out;
	}

	@Override
	public V getOrDefault(Object key, V defaultValue) {
		V out;
		if((out = this.get(key)) == null) {
			return defaultValue;
		}
		return out;
	}

	@Override
	public Set<K> keySet() {
		if(!this.locked) {
			this.keys.update();
		}
		return new HashSet<>(this.keys);
	}

	public void lock() {
		this.locked = true;
		this.keys.update();
	}

	@Override
	public V put(K key, V value) {
		this.putKey(key);
		return super.put(key, value);
	}

	public V put(K key, V value, int time, TimeUnit unit) {
		this.keys.add(key, time, unit);
		return super.put(key, value);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		for(K key : m.keySet()) {
			this.putKey(key);
		}
		super.putAll(m);
	}

	private void putKey(K key){
		this.keys.remove(key);
		this.keys.add(key);
	}

	@Override
	public V remove(Object key) {
		this.keys.remove(key);
		return super.remove(key);
	}

	@Override
	public boolean remove(Object key, Object value) {
		this.keys.remove(key);
		return this.remove(key, value);
	}

	@Override
	public int size() {
		return this.keys.size();
	}

	public void unlock(){
		this.locked = false;
	}
	@Override
	public Collection<V> values() {
		for(K key : super.keySet())
		 {
			this.get(key); //Update value
		}
		return super.values();
	}
}
