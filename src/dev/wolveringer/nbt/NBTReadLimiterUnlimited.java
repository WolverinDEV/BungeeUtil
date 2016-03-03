package dev.wolveringer.nbt;

final class NBTReadLimiterUnlimited extends NBTReadLimiter {
	NBTReadLimiterUnlimited(long i) {
		super(i);
	}

	@Override
	public void readBytes(long i) {
	}
}
