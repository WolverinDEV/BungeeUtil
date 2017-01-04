package dev.wolveringer.bungeeutil.position;

public class BlockPosition extends Potision<Integer> {
	public BlockPosition(int x, int y, int z) {
		super(x, y, z);
	}
	public long toLong(){
		return (this.x & 67108863L) << 38L | (this.y & 4095L) << 26L | (this.z & 67108863L) << 0;
	}
}
