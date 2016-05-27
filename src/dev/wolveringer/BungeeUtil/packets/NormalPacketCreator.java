package dev.wolveringer.BungeeUtil.packets;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dev.wolveringer.BungeeUtil.ClientVersion;
import dev.wolveringer.BungeeUtil.ClientVersion.ProtocollVersion;
import dev.wolveringer.BungeeUtil.Main;
import dev.wolveringer.BungeeUtil.Player;
import dev.wolveringer.BungeeUtil.packets.Packet.ProtocollId;
import dev.wolveringer.packet.PacketDataSerializer;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.md_5.bungee.protocol.Protocol;
import net.md_5.bungee.protocol.ProtocolConstants.Direction;

public class NormalPacketCreator extends AbstractPacketCreator {
	@AllArgsConstructor
	@Getter
	private static class PacketHolder {
		private Constructor<? extends Packet> constuctor;

		public Packet newInstance() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			return constuctor.newInstance();
		}
	}
	
	private PacketHolder[] packetsId = new PacketHolder[((ProtocollVersion.values().length & 0x0F) << 16) | ((Protocol.values().length & 0x0F) << 12) | ((Direction.values().length & 0x0F) << 8) | 0xFF]; // Calculate max packet compressed id. (0xFF = Max ID)
	@SuppressWarnings("unchecked")
	private HashMap<Class<? extends Packet>, Integer>[] classToId = new HashMap[ProtocollVersion.values().length];
	
	private List<Class<? extends Packet>> registerPackets = new ArrayList<>();
	private boolean packetListChanged = true;
	
	public NormalPacketCreator() {
		for(int i = 0;i<classToId.length;i++)
			classToId[i] = new HashMap<>();
	}
	
	@SuppressWarnings("unchecked")
	public int getPacketId(ProtocollVersion version,Class<? extends Packet> clazz) {
		if(version == ProtocollVersion.Unsupported){
			throw new NullPointerException("Unsupported version!");
		}
		if (!clazz.getName().endsWith("$-1")) while (clazz.getName().contains("$")) {
			clazz = (Class<? extends Packet>) clazz.getSuperclass();
		}
		if (!classToId[version.ordinal()].containsKey(clazz)) //throw new NullPointerException("Packet " + clazz.getName() + " not loadet.");
			if(version.getBasedVersion().getProtocollVersion() != version)
				return getPacketId(version.getBasedVersion().getProtocollVersion(),clazz);
			else
				return -1;
		return   classToId[version.ordinal()].get(clazz);
	}
	
	public List<Class<? extends Packet>> getRegisteredPackets() {
		if (packetListChanged) {
			registerPackets.clear();
			for (int i = 0; i < packetsId.length; i++) {
				if(packetsId[i] == null)
					continue;
				Constructor<? extends Packet> constructor = packetsId[i].getConstuctor();
				if (constructor == null) continue;
				registerPackets.add(constructor.getDeclaringClass());
			}
			packetListChanged = false;
		}
		return registerPackets;
	}
	
	public Packet getPacket0(ProtocollVersion version,Protocol protocol, Direction d, Integer id, ByteBuf b, Player p) {
		int compressed = calculate(version,protocol, d, id);
		PacketHolder cons = null;
		if ((cons = packetsId[compressed]) == null) {
			if(version.getBasedVersion().getProtocollVersion() == version){ //Fallback (based version) (1.8-1.9)
				return null;
			}
			else{
				return getPacket0(version.getBasedVersion().getProtocollVersion(), protocol, d, id, b, p);
			}
		}
		if(cons.getConstuctor() == null)
			return null;
		try {
			Packet packet = cons.newInstance();
			if (p == null || p.getVersion() == null){
				Main.debug("Version of '"+(p == null ? "<Null client>" : p.getName())+"' is undefined");
				return packet.setcompressedId(compressed).load(b, ClientVersion.UnderknownVersion);
			}
			else return packet.setcompressedId(compressed).load(b, p.getVersion());
		}
		catch (Exception e) {
			throw new RuntimeException("Packet error (Version: " + (p == null ? "unknown" : p.getVersion()) + ",Readed version: "+version+", Class: " + (cons == null || cons.getConstuctor() == null ? "null" : cons.getConstuctor().getDeclaringClass().getName()) + ", Id: 0x"+Integer.toHexString(id).toUpperCase()+") -> "+e.getMessage(),e);
		}
	}
	
	public Packet getPacket1(ProtocollVersion version,ProtocollVersion orginalVersion,Protocol protocol, Direction d, Integer id, ByteBuf b, Player p) {
		return null;
	}
	
	public int loadPacket(ProtocollVersion version,Protocol p, Direction d, Integer id, Class<? extends Packet> clazz) {
		//clazz = getPacketWithDefaultConstructor(clazz);
		int compressedId = calculate(version,p, d, id);
		classToId[version.ordinal()].put(clazz, compressedId);
		return compressedId;
	}
	
	
	public void registerPacket(Protocol p, Direction d, Class<? extends Packet> clazz, ProtocollId... ids) {
		//clazz = getPacketWithDefaultConstructor(clazz);
		try {
			for(ProtocollId id : ids)
				if(id != null && id.isValid()){
					packetsId[loadPacket(id.getVersion(),p, d, id.getId(), clazz)] = new PacketHolder(clazz == null ? null : (Constructor<? extends Packet>) clazz.getConstructor());
					/*
					if(id.getVersion().getBasedVersion().getProtocollVersion() != id.getVersion()){
						int cid = loadPacket(id.getVersion().getBasedVersion().getProtocollVersion(),p, d, id.getId(), clazz);
						if(packetsId[cid] == null)
							packetsId[cid] = new PacketHolder(null);
					}
					*/
				}
		}
		catch (NoSuchMethodException | SecurityException ex) {
			System.out.println(clazz);
			ex.printStackTrace();
		}
		packetListChanged = true;
	}
	
	public void unregisterPacket(ProtocollVersion version,Protocol p, Direction d, Integer id) {
		packetsId[calculate(version,p, d, id)] = null;
		packetListChanged = true;
	}
	
	public int countPackets() {
		return getRegisteredPackets().size();
	}
	
	/**
	 * Try to create a default constructor.....
	 */
	/*
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	private static Class<? extends Packet> getPacketWithDefaultConstructor(final Class<? extends Packet> in) {
		try {
			for (Constructor<?> c : in.getConstructors())
				if (c.getParameterTypes().length == 0) return in;
			//for (Constructor<?> c : in.getDeclaredConstructors())
			//	if (c.getParameterTypes().length == 0) return in;
		}
		catch (Exception e) {
		}
		try {
			Main.sendMessage("Adding default constructor to class: " + in.getName());
			ClassPool pool = IIInitialHandler.pool();
			pool.insertClassPath(new ClassClassPath(in));
			
			
			CtClass ct_in = pool.get(in.getName());
			byte[] oldClassBytecode = ct_in.toBytecode();
			ct_in.defrost();
			//ct_in.setName(in.getName() + "$-1");
			//ct_in.setSuperclass(pool.get(in.getName()));
			//for(CtConstructor c : pool.get(in.getName()).getConstructors())
			//	ct_in.addConstructor(CtNewConstructor.copy(c, ct_in, null));
			ct_in.addConstructor(defaultConstructor(ct_in));
			
			Class oldClass = Class.forName(in.getName());
			
			ClassLoader loader = IIInitialHandler.getClassLoader();
			final byte[] data = ct_in.toBytecode();
			InstrumentationUtil.getInstrumentation().addTransformer(new DemoTransformer(oldClass.getName(), oldClass.getClassLoader(),data));
			System.out.println("X1: "+InstrumentationUtil.getInstrumentation().isModifiableClass(oldClass)+":"+InstrumentationUtil.getInstrumentation().isRedefineClassesSupported()+":"+InstrumentationUtil.getInstrumentation().isRetransformClassesSupported());
			System.out.println(oldClass);
			InstrumentationUtil.getInstrumentation().redefineClasses(new ClassDefinition(Class.forName(in.getName()), oldClassBytecode));
			
			//UtilReflection.getMethod(ClassLoader.class, "defineClass", byte[].class,int.class,int.class).invoke(loader, data,0,data.length);
			//unloadClass(IIInitialHandler.getClassLoader(), in.getName());
			
			
			return (Class<? extends Packet>) IIInitialHandler.getClassLoader().loadClass(ct_in.getName());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return in;
	}
	
	private static void unloadClass(ClassLoader loader,String clazz) throws IllegalArgumentException, IllegalAccessException{
		 Vector<Class<?>> classes = (Vector<Class<?>>) UtilReflection.getField(ClassLoader.class, "classes").get(loader);
		 for(Class c : new Vector<>(classes))
			 if(c.getName().equalsIgnoreCase(clazz)){
				 System.out.println("Removing");
				 classes.remove(c);
			 }
		 System.out.println("Loaded classes: "+classes);
	}
	
	private static CtConstructor defaultConstructor(CtClass declaring) throws CannotCompileException {
		CtConstructor cons = new CtConstructor((CtClass[]) null, declaring);
		
		ConstPool cp = declaring.getClassFile2().getConstPool();
		Bytecode code = new Bytecode(cp, 1, 1);
		code.addAload(0);
		try {
			System.out.println(declaring.getSuperclass());
			code.addInvokespecial(declaring.getSuperclass(), "<init>", "()V");//
		}
		catch (NotFoundException e) {
			throw new CannotCompileException(e);
		}
		
		code.add(Bytecode.RETURN);
		
		cons.getMethodInfo2().setCodeAttribute(code.toCodeAttribute());
		return cons;
	}
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		loadAgent();
		Class currunt = TestPacket.class;
		currunt = getPacketWithDefaultConstructor(currunt);
		System.out.println(Arrays.asList(currunt.getConstructors())+":"+(TestPacket.class.isAssignableFrom(currunt)));
		Object a = currunt.getConstructor(String.class).newInstance("Testing");
		//TestPacket b = (TestPacket) currunt.newInstance();
		//System.out.println(a.getTest()+":"+b.getTest());
	}

	public static void loadAgent() {
		InstrumentationUtil.init();
	}
	*/
}

class TestPacket extends Packet{
	String test = "undefined";
	
	public TestPacket(String test) {
		this.test = test;
	}
	
	@Override
	public void read(PacketDataSerializer s) {
		
	}

	@Override
	public void write(PacketDataSerializer s) {
		
	}
	public String getTest() {
		return test;
	}
}
class DemoTransformer implements ClassFileTransformer {
    /** The internal form class name of the class to transform */
    protected String className;
    /** The class loader of the class */
    protected ClassLoader classLoader;
    private byte[] newData;
    /**
     * Creates a new DemoTransformer
     * @param className The binary class name of the class to transform
     * @param classLoader The class loader of the class
     */
    public DemoTransformer(String className, ClassLoader classLoader,byte[] newData) {
        this.className = className.replace('.', '/');
        this.classLoader = classLoader;
        this.newData = newData;
    }

    /**
     * {@inheritDoc}
     * @see java.lang.instrument.ClassFileTransformer#transform(java.lang.ClassLoader, java.lang.String, java.lang.Class, java.security.ProtectionDomain, byte[])
     */
    @Override
    public byte[] transform(ClassLoader loader, String className,
            Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
            byte[] classfileBuffer) throws IllegalClassFormatException {
        if(className.equals(this.className) && loader.equals(classLoader)) {
        	System.out.println("Redefining");
            return newData;
        }
        return classfileBuffer;
    }

}
