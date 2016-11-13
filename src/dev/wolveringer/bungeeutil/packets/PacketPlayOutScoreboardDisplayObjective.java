package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.BungeeUtil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;

public class PacketPlayOutScoreboardDisplayObjective extends Packet implements PacketPlayOut{
	public static enum Position {
		LIST(0),
		SIDEBAR(1),
		BELOW_NAME(2);
		
		private int i;
		private Position(int i) {
			this.i = i;
		}
		public static Position getPosition(int i){
			for(Position p : values())
				if(p.i == i)
					return p;
			return null;
		}
		public int getInt(){
			return i;
		}
	}
	String name;
	Position p;
	
	public PacketPlayOutScoreboardDisplayObjective() {
		super(0x3D);
	}
	
	public PacketPlayOutScoreboardDisplayObjective(String name,Position p) {
		this();
		this.name = name;
		this.p = p;
	}
	
	@Override
	public void read(PacketDataSerializer s) {
		p = Position.getPosition(s.readByte());
		name = s.readString(-1);
	}
	@Override
	public void write(PacketDataSerializer s) {
		s.writeByte(p.i);
		s.writeString(name);
	}
	
	public String getName() {
		return this.name;
	}
	public Position getPosition() {
		return this.p;
	}
}
