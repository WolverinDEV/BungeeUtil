package dev.wolveringer.api.position;

public class Potision<T> {
	protected T x;
	protected T y;
	protected T z;

	public Potision(T x, T y, T z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public T getX() {
		return x;
	}

	public void setX(T x) {
		this.x = x;
	}

	public T getY() {
		return y;
	}

	public void setY(T y) {
		this.y = y;
	}

	public T getZ() {
		return z;
	}

	public void setZ(T z) {
		this.z = z;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((x == null) ? 0 : x.hashCode());
		result = prime * result + ((y == null) ? 0 : y.hashCode());
		result = prime * result + ((z == null) ? 0 : z.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Potision other = (Potision) obj;
		if (x == null) {
			if (other.x != null) return false;
		}
		else if (!x.equals(other.x)) return false;
		if (y == null) {
			if (other.y != null) return false;
		}
		else if (!y.equals(other.y)) return false;
		if (z == null) {
			if (other.z != null) return false;
		}
		else if (!z.equals(other.z)) return false;
		return true;
	}
}
