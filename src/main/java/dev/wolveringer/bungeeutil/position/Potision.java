package dev.wolveringer.bungeeutil.position;

public class Potision<T> {
	protected T x;
	protected T y;
	protected T z;

	public Potision(T x, T y, T z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		Potision other = (Potision) obj;
		if (this.x == null) {
			if (other.x != null) {
				return false;
			}
		}
		else if (!this.x.equals(other.x)) {
			return false;
		}
		if (this.y == null) {
			if (other.y != null) {
				return false;
			}
		}
		else if (!this.y.equals(other.y)) {
			return false;
		}
		if (this.z == null) {
			if (other.z != null) {
				return false;
			}
		}
		else if (!this.z.equals(other.z)) {
			return false;
		}
		return true;
	}

	public T getX() {
		return this.x;
	}

	public T getY() {
		return this.y;
	}

	public T getZ() {
		return this.z;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.x == null ? 0 : this.x.hashCode());
		result = prime * result + (this.y == null ? 0 : this.y.hashCode());
		result = prime * result + (this.z == null ? 0 : this.z.hashCode());
		return result;
	}

	public void setX(T x) {
		this.x = x;
	}

	public void setY(T y) {
		this.y = y;
	}

	public void setZ(T z) {
		this.z = z;
	}
}
