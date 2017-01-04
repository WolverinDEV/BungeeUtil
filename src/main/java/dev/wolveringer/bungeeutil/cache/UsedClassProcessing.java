package dev.wolveringer.bungeeutil.cache;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UsedClassProcessing<T> {
	Class<T> clazz;
	List<FieldProcessing<T>> processors = new ArrayList<>();
	boolean breakOnFieldFail = true;

	public UsedClassProcessing(Class<T> clazz, int superDeep) {
		this.clazz = clazz;

		Class<?> current = clazz;
		int deep = 0;
		while (current != null && !(current == Object.class) && (deep < superDeep || superDeep == -1)) {

			List<Field> fields = new ArrayList<>();
			fields.addAll(Arrays.asList(current.getFields()));
			fields.addAll(Arrays.asList(current.getDeclaredFields()));
			for(Field f : fields){
				if(!Modifier.isFinal(f.getModifiers()) && !Modifier.isStatic(f.getModifiers())) {
					//TODO specific type
					this.processors.add(FieldProcessing.getProcessor(clazz, f));
				}
			}

			deep--;
			current = current.getSuperclass();
		}
	}

	public T processing(T instance){
		for(FieldProcessing<T> proc : this.processors) {
			if(!proc.processing(instance) && this.breakOnFieldFail) {
				return null;
			}
		}
		return instance;
	}
}
