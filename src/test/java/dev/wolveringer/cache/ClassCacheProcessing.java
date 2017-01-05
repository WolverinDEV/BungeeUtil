package dev.wolveringer.cache;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import dev.wolveringer.bungeeutil.cache.UsedClassProcessing;
import lombok.AllArgsConstructor;
import lombok.ToString;

public class ClassCacheProcessing {

	@ToString
	@AllArgsConstructor
	public static class ClassCacheTestClass extends ClassCacheTestSuperClass{
		static final int staticA = 22;

		int fieldA;
		String fieldB;
		ClassCacheTestClass fieldC;
	}

	public static class ClassCacheTestSuperClass {
		int superA;
		String superB;
	}

	@Test
	public void testReproducer() {
		ClassCacheTestClass instance = new ClassCacheTestClass(1, "Hello", null);
		instance.superA = 2;
		instance.superB = "Hello world";

		UsedClassProcessing<ClassCacheTestClass> processor = new UsedClassProcessing<>(ClassCacheTestClass.class, -1);

		instance = processor.processing(instance);
		assertTrue(instance.fieldA == 0);
		assertTrue(instance.fieldB == null);
		assertTrue(instance.fieldC == null);

		assertTrue(instance.superA == 0);
		assertTrue(instance.superB == null);
	}
}
