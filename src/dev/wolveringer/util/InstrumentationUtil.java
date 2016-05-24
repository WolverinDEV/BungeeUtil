package dev.wolveringer.util;

import java.lang.instrument.Instrumentation;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ea.agentloader.AgentLoader;

import dev.wolveringer.BungeeUtil.Main;
import dev.wolveringer.util.apache.StringUtils;
import sun.instrument.InstrumentationImpl;

public class InstrumentationUtil {
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
	
	/** Handle to instance of Instrumentation interface. */
	private static volatile Instrumentation globalInstrumentation;
	
	@SuppressWarnings("unused")
	private static void premain(final String agentArgs, final Instrumentation inst) {
		Main.debug("Instrumentation is preloading now!");
		globalInstrumentation = inst;
	}
	
	@SuppressWarnings("unused")
	private static void agentmain(String agentArgs, Instrumentation inst) {
		globalInstrumentation = inst;
		
		String[] args = agentArgs.split(Character.toString('\u0000'));
		Main.debug("Instrumentation is loaded now!");
		Main.debug("Instrumentation loaded at " + DATE_FORMAT.format(new Date()) + ". Invoked at " + DATE_FORMAT.format(new Date(Long.parseLong(args[0]))) + ". Needed time: " + (System.currentTimeMillis() - Long.parseLong(args[0])) + "ms");
		if (globalInstrumentation instanceof InstrumentationImpl) {
			try {
				Main.debug("Instrumentation memory position 0x" + Long.toHexString(UtilReflection.getField(InstrumentationImpl.class, "mNativeAgent").getLong(inst)).toUpperCase());
			}
			catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void validateInitialisation() {
		if (globalInstrumentation == null) { throw new IllegalStateException("Agent not initialized."); }
	}
	
	/**
	 * Provide the memory size of the provided object (but not it's components).
	 * 
	 * @param object
	 *            Object whose memory size is desired.
	 * @return The size of the provided object, not counting its components
	 *         (described in Instrumentation.getObjectSize(Object)'s Javadoc as "an
	 *         implementation-specific approximation of the amount of storage consumed
	 *         by the specified object").
	 * @throws IllegalStateException
	 *             Thrown if my Instrumentation is null.
	 */
	public static long getObjectSize(final Object object) {
		validateInitialisation();
		return globalInstrumentation.getObjectSize(object);
	}
	
	public static Instrumentation getInstrumentation() {
		validateInitialisation();
		return globalInstrumentation;
	}
	
	public static void init() {
		String[] data = { String.valueOf(System.currentTimeMillis()) };
		AgentLoader.loadAgentClass(InstrumentationUtil.class.getName(), StringUtils.join(data, Character.toString('\u0000')), null, true, true, true);
	}
}
