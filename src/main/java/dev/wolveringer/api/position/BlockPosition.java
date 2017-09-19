package dev.wolveringer.api.position;

public class BlockPosition extends Potision<Integer> {
	public BlockPosition(int x, int y, int z) {
		super(x, y, z);
	}
	public long toLong(){
		return (x & 67108863L) << 38L | (y & 4095L) << 26L | (z & 67108863L) << 0;
	}
}
