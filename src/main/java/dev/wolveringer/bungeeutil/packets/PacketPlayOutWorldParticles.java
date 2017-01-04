package dev.wolveringer.bungeeutil.packets;

import java.util.ArrayList;
import java.util.Arrays;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import dev.wolveringer.bungeeutil.particel.ParticleEffect;
import dev.wolveringer.bungeeutil.position.Location;
import dev.wolveringer.bungeeutil.position.Vector;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PacketPlayOutWorldParticles extends Packet implements PacketPlayOut {

	private int count;
	private float data;
	private Location loc;
	private ParticleEffect name;
	private Vector vector;

	// 1.8
	private boolean far;
	private int[] particelData;

	public PacketPlayOutWorldParticles(ParticleEffect s, Location loc, Vector v, float f6, int i, boolean far, int... pdata) {
		this.name = s;
		this.loc = loc;
		this.vector = v;
		this.data = f6;
		this.count = i;
		this.particelData = pdata;
		this.far = far;
	}

	public PacketPlayOutWorldParticles(ParticleEffect s, Location loc, Vector v, float f6, int i, int... pdata) {
		this.name = s;
		this.loc = loc;
		this.vector = v;
		this.data = f6;
		this.count = i;
		this.particelData = pdata;
	}

	@Override
	public void read(PacketDataSerializer packetdataserializer) {
		this.name = ParticleEffect.fromId(packetdataserializer.readInt());
		this.far = packetdataserializer.readBoolean();
		this.loc = new Location(packetdataserializer.readFloat(), packetdataserializer.readFloat(), packetdataserializer.readFloat());
		this.vector = new Vector(packetdataserializer.readFloat(), packetdataserializer.readFloat(), packetdataserializer.readFloat());
		this.data = packetdataserializer.readFloat();
		this.count = packetdataserializer.readInt();
		ArrayList<Integer> a = new ArrayList<Integer>();
		while (packetdataserializer.readableBytes() > 0) {
			a.add(packetdataserializer.readVarInt());
		}
		this.particelData = new int[a.size()];
		int pos = 0;
		for (Integer i : a) {
			this.particelData[pos] = i;
			pos++;
		}
	}


	@Override
	public String toString() {
		return "PacketPlayOutWorldParticles [count=" + this.count + ", data=" + this.data + ", loc=" + this.loc + ", name=" + this.name + ", vector=" + this.vector + ", j=" + this.far + ", p_data=" + Arrays.toString(this.particelData) + "]";
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
		packetdataserializer.writeFloat(this.data);
		packetdataserializer.writeInt(this.count);
		for (Integer o : this.particelData) {
			packetdataserializer.writeVarInt(o);
		}
	}
}
