package dev.wolveringer.maps;

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
		return add(e, defautTime, defaultTimeUnit);
	}

	@Override
	public void add(int index, E element) {
		add(index, element, defautTime, defaultTimeUnit);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		return addAll(c, defautTime, defaultTimeUnit);
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		return addAll(index, c, defautTime, defaultTimeUnit);
	}

	public boolean add(E e, int time, TimeUnit unit) {
		if (time == 0)
			return false;
		boolean add = super.add(e);
		long t;
		times.put(e, t = System.currentTimeMillis() + unit.toMillis(time));
		if (nextUpdate > t)
			nextUpdate = t;
		return add;
	}

	public void add(int index, E e, int time, TimeUnit unit) {
		if (time == 0)
			return;
		super.add(index, e);
		long t;
		times.put(e, t = System.currentTimeMillis() + unit.toMillis(time));
		if (nextUpdate > t)
			nextUpdate = t;
	}

	public boolean addAll(int index, Collection<? extends E> c, int time, TimeUnit unit) {
		if (time == 0)
			return false;
		for (E e : c) {
			long t;
			times.put(e, t = System.currentTimeMillis() + unit.toMillis(time));
			if (nextUpdate > t)
				nextUpdate = t;
		}
		return super.addAll(index, c);
	}

	public boolean addAll(Collection<? extends E> c, int time, TimeUnit unit) {
		if (time == 0)
			return false;
		for (E e : c) {
			long t;
			times.put(e, t = System.currentTimeMillis() + unit.toMillis(time));
			if (nextUpdate > t)
				nextUpdate = t;
		}
		return super.addAll(c);
	}

	@Override
	public E remove(int index) {
		E obj = super.remove(index);
		long t = times.remove(obj);
		if (t == nextUpdate)
			updateTimes();
		return obj;
	}

	@Override
	public E get(int index) {
		update();
		return super.get(index);
	}

	@Override
	public int size() {
		update();
		return super.size();
	}

	@Override
	public Iterator<E> iterator() {
		update();
		return super.iterator();
	}

	@Override
	public void clear() {
		nextUpdate = Long.MAX_VALUE;
		times.clear();
		super.clear();
	}

	public void update() {
		if (System.currentTimeMillis() > nextUpdate)
			updateTimes();
	}

	@Override
	public boolean remove(Object o) {
		if(!times.containsKey(o))
			return false;
		long r = times.get(o);
		times.remove(o);
		if (r != 0L) {
			if (nextUpdate == r)
				update();
		}
		return super.remove(o);
	}
	
	public void resetTime(E element){
		times.put(element, System.currentTimeMillis() + defaultTimeUnit.toMillis(defautTime));
	}

	@Override
	public boolean contains(Object o) {
		update();
		return super.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		update();
		return super.containsAll(c);
	}

	@Override
	public int indexOf(Object o) {
		update();
		return super.indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		update();
		return super.isEmpty();
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		update();
		return super.subList(fromIndex, toIndex);
	}

	@Override
	public int lastIndexOf(Object o) {
		update();
		return super.lastIndexOf(o);
	}

	@Override
	public E set(int index, E element) {
		return set(index, element,defautTime,defaultTimeUnit);
	}
	
	@Override
	public <T> T[] toArray(T[] a) {
		update();
		return super.toArray(a);
	}
	
	@Override
	public Object[] toArray() {
		update();
		return super.toArray();
	}
	public E set(int index, E element, int time, TimeUnit unit) {
		update();
		long l;
		times.put(element, l = unit.toMillis(time));
		if (l < nextUpdate)
			nextUpdate = l;
		E old = super.set(index, element);

		long r = times.get(old);
		times.remove(old);
		if (r != 0L) {
			if (nextUpdate == r)
				update();
		}
		return old;
	}

	private void updateTimes() {
		long min = Long.MAX_VALUE;
		long time = System.currentTimeMillis();
		HashMap<E, Long> ctimes  = new HashMap<>(times);
		for (E e : ctimes.keySet()) {
			long l = ctimes.get(e);
			if (time > l){
				boolean alowed = true;
				for(UnloadListener<E> listener : new ArrayList<>(listener))
					if(listener != null)
						if(!listener.canUnload(e))
							alowed = false;
				if(alowed)
					super.remove(e);
				else{
					resetTime(e);
					l = ctimes.get(e);
				}
			}
			else if (l < min)
				min = l;
		}
		nextUpdate = min;
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
	
	public void addUnloadListener(UnloadListener<E> listener){
		this.listener.add(listener);
	}
	public void removeUnloadListener(UnloadListener<E> listener){
		this.listener.remove(listener);
	}
}
