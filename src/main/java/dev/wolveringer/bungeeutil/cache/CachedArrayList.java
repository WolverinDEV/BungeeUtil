package dev.wolveringer.bungeeutil.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CachedArrayList<E> extends ArrayList<E> {
	public static interface UnloadListener<E> {
		public boolean canUnload(E element);
	}
	private static final long serialVersionUID = 1L;

	private long nextUpdate = Long.MAX_VALUE;

	private int defautTime;
	private TimeUnit defaultTimeUnit = TimeUnit.MILLISECONDS;

	private HashMap<E, Long> times = new HashMap<>();

	private ArrayList<UnloadListener<E>> listener = new ArrayList<>();

	public CachedArrayList(int defautTime, TimeUnit defaultTimeUnit) {
		this.defautTime = defautTime;
		this.defaultTimeUnit = defaultTimeUnit;
	}

	@Override
	public boolean add(E e) {
		return this.add(e, this.defautTime, this.defaultTimeUnit);
	}

	public boolean add(E e, int time, TimeUnit unit) {
		if (time == 0) {
			return false;
		}
		boolean add = super.add(e);
		long t;
		this.times.put(e, t = System.currentTimeMillis() + unit.toMillis(time));
		if (this.nextUpdate > t) {
			this.nextUpdate = t;
		}
		return add;
	}

	@Override
	public void add(int index, E element) {
		this.add(index, element, this.defautTime, this.defaultTimeUnit);
	}

	public void add(int index, E e, int time, TimeUnit unit) {
		if (time == 0) {
			return;
		}
		super.add(index, e);
		long t;
		this.times.put(e, t = System.currentTimeMillis() + unit.toMillis(time));
		if (this.nextUpdate > t) {
			this.nextUpdate = t;
		}
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		return this.addAll(c, this.defautTime, this.defaultTimeUnit);
	}

	public boolean addAll(Collection<? extends E> c, int time, TimeUnit unit) {
		if (time == 0) {
			return false;
		}
		for (E e : c) {
			long t;
			this.times.put(e, t = System.currentTimeMillis() + unit.toMillis(time));
			if (this.nextUpdate > t) {
				this.nextUpdate = t;
			}
		}
		return super.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		return this.addAll(index, c, this.defautTime, this.defaultTimeUnit);
	}

	public boolean addAll(int index, Collection<? extends E> c, int time, TimeUnit unit) {
		if (time == 0) {
			return false;
		}
		for (E e : c) {
			long t;
			this.times.put(e, t = System.currentTimeMillis() + unit.toMillis(time));
			if (this.nextUpdate > t) {
				this.nextUpdate = t;
			}
		}
		return super.addAll(index, c);
	}

	public void addUnloadListener(UnloadListener<E> listener){
		this.listener.add(listener);
	}

	@Override
	public void clear() {
		this.nextUpdate = Long.MAX_VALUE;
		this.times.clear();
		super.clear();
	}

	@Override
	public boolean contains(Object o) {
		this.update();
		return super.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		this.update();
		return super.containsAll(c);
	}

	@Override
	public E get(int index) {
		this.update();
		return super.get(index);
	}

	@Override
	public int indexOf(Object o) {
		this.update();
		return super.indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		this.update();
		return super.isEmpty();
	}

	@Override
	public Iterator<E> iterator() {
		this.update();
		return super.iterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		this.update();
		return super.lastIndexOf(o);
	}

	@Override
	public E remove(int index) {
		E obj = super.remove(index);
		long t = this.times.remove(obj);
		if (t == this.nextUpdate) {
			this.updateTimes();
		}
		return obj;
	}

	@Override
	public boolean remove(Object o) {
		if(!this.times.containsKey(o)) {
			return false;
		}
		long r = this.times.get(o);
		this.times.remove(o);
		if (r != 0L) {
			if (this.nextUpdate == r) {
				this.update();
			}
		}
		return super.remove(o);
	}

	public void removeUnloadListener(UnloadListener<E> listener){
		this.listener.remove(listener);
	}

	public void resetTime(E element){
		this.times.put(element, System.currentTimeMillis() + this.defaultTimeUnit.toMillis(this.defautTime));
	}

	@Override
	public E set(int index, E element) {
		return this.set(index, element,this.defautTime,this.defaultTimeUnit);
	}

	public E set(int index, E element, int time, TimeUnit unit) {
		this.update();
		long l;
		this.times.put(element, l = unit.toMillis(time));
		if (l < this.nextUpdate) {
			this.nextUpdate = l;
		}
		E old = super.set(index, element);

		long r = this.times.get(old);
		this.times.remove(old);
		if (r != 0L) {
			if (this.nextUpdate == r) {
				this.update();
			}
		}
		return old;
	}

	@Override
	public int size() {
		this.update();
		return super.size();
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		this.update();
		return super.subList(fromIndex, toIndex);
	}
	@Override
	public Object[] toArray() {
		this.update();
		return super.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		this.update();
		return super.toArray(a);
	}

	@Override
	public String toString() {
		return super.toString();
	}

	public void update() {
		update(false);
	}
	
	public void update(boolean force) {
		if (force || System.currentTimeMillis() > this.nextUpdate) {
			this.updateTimes();
		}
	}
	
	private void updateTimes() {
		long min = Long.MAX_VALUE;
		long time = System.currentTimeMillis();
		HashMap<E, Long> ctimes  = new HashMap<>(this.times);
		for (E e : ctimes.keySet()) {
			long l = ctimes.get(e);
			if (time > l){
				boolean alowed = true;
				for(UnloadListener<E> listener : new ArrayList<>(this.listener)) {
					if(listener != null) {
						if(!listener.canUnload(e)) {
							alowed = false;
						}
					}
				}
				if(alowed) {
					super.remove(e);
				} else{
					this.resetTime(e);
					l = ctimes.get(e);
				}
			}
			else if (l < min) {
				min = l;
			}
		}
		this.nextUpdate = min;
	}
}
