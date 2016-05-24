package dev.wolveringer.util;

import java.lang.instrument.Instrumentation;

public class Test {
	public static void main(String[] args) {
		InstrumentationUtil.init();
		System.out.println(InstrumentationUtil.getObjectSize(Instrumentation.class));;
	}
}
