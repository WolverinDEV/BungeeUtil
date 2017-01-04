package dev.wolveringer.bungeeutil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class UtilReflection {
	/*
	private static class ClassReflectionUtils {
		private static Method methode_class_getField0;

		static {
			try {
				methode_class_getField0 = Class.class.getDeclaredMethod("getField0", String.class);
				methode_class_getField0.setAccessible(true);
			} catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		}

		public static Field getField(Class clazz,String field){
			try {
				return (Field) methode_class_getField0.invoke(clazz, field);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	*/

	public static Field getField(Class obj, String name) {
		Field f = null;
		try {
			f = obj.getDeclaredField(name);
		} catch (Exception e) {
		}
		if (f == null) {
			try {
				f = obj.getField(name);
			} catch (Exception e) {
			}
		}
		if(!f.isAccessible()) {
			f.setAccessible(true);
		}
		return f;
	}

	public static Field getField(Object obj, String name) {
		return getField(obj.getClass(), name);
	}

	public static Method getMethod(Class obj, String name,Class...args) {
		Method m = null;
		try{
			m = obj.getDeclaredMethod(name, args);
		}catch(Exception e){
		}
		if(m == null) {
			try {
				m = obj.getMethod(name, args);
			} catch (NoSuchMethodException | SecurityException e) {
			}
		}
		if(m != null) {
			if(!m.isAccessible()) {
				m.setAccessible(true);
			}
		}
		return m;
	}

	public static boolean setField(Class clazz,Object obj, String field, Object value) {
		Field f = getField(clazz, field);
		if (!f.isAccessible()) {
			f.setAccessible(true);
		}
		try {
			f.set(obj, value);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public static boolean setField(Object obj, String field, Object value) {
		Field f = getField(obj, field);
		if (!f.isAccessible()) {
			f.setAccessible(true);
		}
		try {
			f.set(obj, value);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
