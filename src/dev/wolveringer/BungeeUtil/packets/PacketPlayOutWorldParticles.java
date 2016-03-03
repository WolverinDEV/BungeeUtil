package dev.wolveringer.BungeeUtil.packets;

import java.util.ArrayList;
import java.util.Arrays;

import dev.wolveringer.BungeeUtil.Vector;
import dev.wolveringer.BungeeUtil.ClientVersion.BigClientVersion;
import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayOut;
import dev.wolveringer.api.particel.Particle;
import dev.wolveringer.api.position.Location;
import dev.wolveringer.packet.PacketDataSerializer;

public class PacketPlayOutWorldParticles extends Packet implements PacketPlayOut {

	private int count;
	private float data;
	private Location loc;
	private Particle name;
	private Vector vector;

	//1.8
	boolean far;
	int[] p_data;

	public PacketPlayOutWorldParticles() {
		super(0x2A);
	}

	public PacketPlayOutWorldParticles(Particle s, Location loc, Vector v, float f6, int i, int... pdata) {
		super(0x2A);
		this.name = s;
		this.loc = loc;
		this.vector = v;
		this.data = f6;
		this.count = i;
		this.p_data = pdata;
	}

	public PacketPlayOutWorldParticles(Particle s, Location loc, Vector v, float f6, int i, boolean far, int... pdata) {
		super(0x2A);
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

	public Particle getParticle() {
		return name;
	}

	public Vector getVector() {
		return vector;
	}

	@Override
	public void read(PacketDataSerializer packetdataserializer) {
		if(getVersion().getBigVersion() == BigClientVersion.v1_7){
			name = Particle.fromName(packetdataserializer.readString(64));
			loc = new Location(packetdataserializer.readFloat(), packetdataserializer.readFloat(), packetdataserializer.readFloat());
			vector = new Vector(packetdataserializer.readFloat(), packetdataserializer.readFloat(), packetdataserializer.readFloat());
			this.data = packetdataserializer.readFloat();
			this.count = packetdataserializer.readInt();
		}else if(getVersion().getBigVersion() == BigClientVersion.v1_8 || getVersion().getBigVersion() == BigClientVersion.v1_9){
			this.name = Particle.fromId(packetdataserializer.readInt());
			this.far = packetdataserializer.readBoolean();
			loc = new Location(packetdataserializer.readFloat(), packetdataserializer.readFloat(), packetdataserializer.readFloat());
			vector = new Vector(packetdataserializer.readFloat(), packetdataserializer.readFloat(), packetdataserializer.readFloat());
			this.data = packetdataserializer.readFloat();
			this.count = packetdataserializer.readInt();
			ArrayList<Integer> a = new ArrayList<Integer>();
			while (packetdataserializer.readableBytes() > 0){
				a.add(packetdataserializer.readVarInt());
			}
			this.p_data = new int[a.size()];
			int pos = 0;
			for(Integer i : a){
				p_data[pos] = i;
				pos++;
			}
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

	public void setParticle(Particle name) {
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
		if(getVersion().getBigVersion() == BigClientVersion.v1_7){
			packetdataserializer.writeString(this.name.getName());
			packetdataserializer.writeFloat(new Float(this.loc.getX()));
			packetdataserializer.writeFloat(new Float(this.loc.getY()));
			packetdataserializer.writeFloat(new Float(this.loc.getZ()));
			packetdataserializer.writeFloat(new Float(this.vector.getX()));
			packetdataserializer.writeFloat(new Float(this.vector.getY()));
			packetdataserializer.writeFloat(new Float(this.vector.getZ()));
			packetdataserializer.writeFloat(data);
			packetdataserializer.writeInt(count);
		}else if(getVersion().getBigVersion() == BigClientVersion.v1_8 || getVersion().getBigVersion() == BigClientVersion.v1_9){
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
			for(Integer o : p_data)
				packetdataserializer.writeVarInt((int) o);
		}
	}

	public boolean isJ() {
		return far;
	}

	public void setJ(boolean j) {
		this.far = j;
	}

	public int[] getMetaData() {
		return p_data;
	}

	public void setMetaData(int[] p_data) {
		this.p_data = p_data;
	}

}