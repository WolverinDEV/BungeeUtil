package dev.wolveringer.packet;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class ObjectSereizer<T> {
	Class<T> clazz;

	@SuppressWarnings("unchecked")
	public ObjectSereizer() {
		Type[] types = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
		clazz = (Class<T>) types[0];
	}
	
	public Class<T> getType(){
		return clazz;
	}
	
	public abstract void write(T obj,PacketDataSerializer serelizer);
}
