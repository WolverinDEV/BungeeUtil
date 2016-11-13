package dev.wolveringer.bungeeutil.packets;

import java.util.ArrayList;
import java.util.Arrays;

import dev.wolveringer.BungeeUtil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import dev.wolveringer.bungeeutil.particel.ParticleEffect;
import dev.wolveringer.bungeeutil.player.ClientVersion.BigClientVersion;
import dev.wolveringer.bungeeutil.position.Location;
import dev.wolveringer.bungeeutil.position.Vector;

public class PacketPlayOutWorldParticles extends Packet implements PacketPlayOut {
	
	private int count;
	private float data;
	private Location loc;
	private ParticleEffect name;
	private Vector vector;
	
	// 1.8
	boolean far;
	int[] p_data;
	
	public PacketPlayOutWorldParticles() {
		super(0x2A);
	}
	
	public PacketPlayOutWorldParticles(ParticleEffect s, Location loc, Vector v, float f6, int i, int... pdata) {
		this.name = s;
		this.loc = loc;
		this.vector = v;
		this.data = f6;
		this.count = i;
		this.p_data = pdata;
	}
	
	public PacketPlayOutWorldParticles(ParticleEffect s, Location loc, Vector v, float f6, int i, boolean far, int... pdata) {
		this.name = s;
		this.loc = loc;
		this.vector = v;
		this.data = f6;
		this.count = i;
		this.p_data = pdata;
		this.far = far;
	}
	
	public int getCount() {
		return count;
	}
	
	public float getData() {
		return data;
	}
	
	public Location getLoc() {
		return loc;
	}
	
	public ParticleEffect getParticle() {
		return name;
	}
	
	public Vector getVector() {
		return vector;
	}
	
	@Override
	public void read(PacketDataSerializer packetdataserializer) {
		this.name = ParticleEffect.fromId(packetdataserializer.readInt());
		this.far = packetdataserializer.readBoolean();
		loc = new Location(packetdataserializer.readFloat(), packetdataserializer.readFloat(), packetdataserializer.readFloat());
		vector = new Vector(packetdataserializer.readFloat(), packetdataserializer.readFloat(), packetdataserializer.readFloat());
		this.data = packetdataserializer.readFloat();
		this.count = packetdataserializer.readInt();
		ArrayList<Integer> a = new ArrayList<Integer>();
		while (packetdataserializer.readableBytes() > 0) {
			a.add(packetdataserializer.readVarInt());
		}
		this.p_data = new int[a.size()];
		int pos = 0;
		for (Integer i : a) {
			p_data[pos] = i;
			pos++;
		}
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
	public void setData(float data) {
		this.data = data;
	}
	
	public void setLoc(Location loc) {
		this.loc = loc;
	}
	
	public void setParticle(ParticleEffect name) {
		this.name = name;
	}
	
	public void setVector(Vector vector) {
		this.vector = vector;
	}
	
	@Override
	public String toString() {
		return "PacketPlayOutWorldParticles [count=" + count + ", data=" + data + ", loc=" + loc + ", name=" + name + ", vector=" + vector + ", j=" + far + ", p_data=" + Arrays.toString(p_data) + "]";
	}
	
	@Override
	public void write(PacketDataSerializer packetdataserializer) {
		packetdataserializer.writeInt(this.name.getId());
		packetdataserializer.writeBoolean(this.far);
		packetdataserializer.writeFloat(new Float(this.loc.getX()));
		packetdataserializer.writeFloat(new Float(this.loc.getY()));
		packetdataserializer.writeFloat(new Float(this.loc.getZ()));
		packetdataserializer.writeFloat(new Float(this.vector.getX()));
		packetdataserializer.writeFloat(new Float(this.vector.getY()));
		packetdataserializer.writeFloat(new Float(this.vector.getZ()));
		packetdataserializer.writeFloat(data);
		packetdataserializer.writeInt(count);
		for (Integer o : p_data)
			packetdataserializer.writeVarInt((int) o);
	}
	
	public boolean isFar() {
		return far;
	}
	
	public void setFar(boolean j) {
		this.far = j;
	}
	
	public int[] getMetaData() {
		return p_data;
	}
	
	public void setMetaData(int[] p_data) {
		this.p_data = p_data;
	}
	
}
