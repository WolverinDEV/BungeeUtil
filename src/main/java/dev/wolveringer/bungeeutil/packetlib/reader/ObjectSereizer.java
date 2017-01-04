package dev.wolveringer.bungeeutil.packetlib.reader;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class ObjectSereizer<T> {
	Class<T> clazz;

	@SuppressWarnings("unchecked")
	public ObjectSereizer() {
		Type[] types = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments();
		this.clazz = (Class<T>) types[0];
	}

	public Class<T> getType(){
		return this.clazz;
	}

	public abstract void write(T obj,PacketDataSerializer serelizer);
}
