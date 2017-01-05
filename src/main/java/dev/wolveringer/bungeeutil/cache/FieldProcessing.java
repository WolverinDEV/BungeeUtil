package dev.wolveringer.bungeeutil.cache;

import java.lang.reflect.Field;

public class FieldProcessing<T> {
	public static class CallableFieldProcessing<T> extends FieldProcessing<T> {
		final FieldProcessor<T> proc;
		public CallableFieldProcessing(Class<T> clazz, Field field,FieldProcessor<T> proc) {
			super(clazz, field);
			this.proc = proc;
		}

		@Override
		public boolean processing(T instance) {
			try {
				this.proc.process(instance);
			}catch(Exception e){
				e.printStackTrace();
				return false;
			}
			return true;
		}
	}

	@FunctionalInterface
	public static interface FieldProcessor<T> {
		public boolean process(T obj) throws Exception;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> FieldProcessing<T> getProcessor(Class<T> clazz, Field field){
		if(field.getType().isAssignableFrom(Long.class) || field.getType().isAssignableFrom(long.class)) {
			return new CallableFieldProcessing(clazz, field, (obj)->{
				field.setLong(obj, 0l);
				return true;
			});
		}
		if(field.getType().isAssignableFrom(Integer.class) || field.getType().isAssignableFrom(int.class)) {
			return new CallableFieldProcessing(clazz, field, (obj)->{
				field.setInt(obj, 0);
				return true;
			});
		}
		if(field.getType().isAssignableFrom(Short.class) || field.getType().isAssignableFrom(short.class)) {
			return new CallableFieldProcessing(clazz, field, (obj)->{
				field.setShort(obj, (short) 0);
				return true;
			});
		}
		if(field.getType().isAssignableFrom(Byte.class) || field.getType().isAssignableFrom(byte.class)) {
			return new CallableFieldProcessing(clazz, field, (obj)->{
				field.setByte(obj, (byte) 0);
				return true;
			});
		}
		if(field.getType().isAssignableFrom(Double.class) || field.getType().isAssignableFrom(double.class)) {
			return new CallableFieldProcessing(clazz, field, (obj)->{
				field.setDouble(obj, 0d);
				return true;
			});
		}
		if(field.getType().isAssignableFrom(Float.class) || field.getType().isAssignableFrom(float.class)) {
			return new CallableFieldProcessing(clazz, field, (obj)->{
				field.setFloat(obj, 0f);
				return true;
			});
		}
		if(field.getType().isAssignableFrom(Boolean.class) || field.getType().isAssignableFrom(boolean.class)) {
			return new CallableFieldProcessing(clazz, field, (obj)->{
				field.setBoolean(obj, false);
				return true;
			});
		}
		return new CallableFieldProcessing(clazz, field, (obj)->{
			field.set(obj, null);
			return true;
		});
	}

	final Class<T> clazz;



	final Field field;

	public FieldProcessing(Class<T> clazz, Field field) {
		this.clazz = clazz;
		this.field = field;
		field.setAccessible(true);
	}

	public boolean processing(T instance) {
		try { this.field.set(instance, null); }catch(Exception e){e.printStackTrace(); return false; }
		return true;
	}
}