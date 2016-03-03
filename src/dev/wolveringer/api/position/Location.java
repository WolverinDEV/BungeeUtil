package dev.wolveringer.api.position;

import dev.wolveringer.BungeeUtil.NumberConversions;
import dev.wolveringer.BungeeUtil.Vector;

/**
 * Represents a 3-dimensional position in a world
 */
public class Location extends Potision<Double> implements Cloneable {
	/**
	 * Safely converts a double (location coordinate) to an int (block
	 * coordinate)
	 *
	 * @param loc
	 *            Precise coordinate
	 * @return Block coordinate
	 */
	public static int locToBlock(double loc) {
		return NumberConversions.floor(loc);
	}

	private float pitch;
	private float yaw;

	/**
	 * Constructs a new Location with the given coordinates
	 *
	 * @param world
	 *            The world in which this location resides
	 * @param x
	 *            The x-coordinate of this new location
	 * @param y
	 *            The y-coordinate of this new location
	 * @param z
	 *            The z-coordinate of this new location
	 */
	public Location(final double x, final double y, final double z) {
		this(x, y, z, 0, 0);
	}

	/**
	 * Constructs a new Location with the given coordinates and direction
	 *
	 * @param world
	 *            The world in which this location resides
	 * @param x
	 *            The x-coordinate of this new location
	 * @param y
	 *            The y-coordinate of this new location
	 * @param z
	 *            The z-coordinate of this new location
	 * @param yaw
	 *            The absolute rotation on the x-plane, in degrees
	 * @param pitch
	 *            The absolute rotation on the y-plane, in degrees
	 */
	public Location(final double x, final double y, final double z, final float yaw, final float pitch) {
		super(x,y,z);
		this.pitch = pitch;
		this.yaw = yaw;
	}

	/**
	 * Adds the location by another. Not world-aware.
	 *
	 * @see Vector
	 * @param x
	 *            X coordinate
	 * @param y
	 *            Y coordinate
	 * @param z
	 *            Z coordinate
	 * @return the same location
	 */
	public Location add(double x, double y, double z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}

	/**
	 * Adds the location by another.
	 *
	 * @see Vector
	 * @param vec
	 *            The other location
	 * @return the same location
	 * @throws IllegalArgumentException
	 *             for differing worlds
	 */
	public Location add(Location vec) {
		if(vec == null){
			throw new IllegalArgumentException("Cannot add Locations of differing worlds");
		}

		x += vec.x;
		y += vec.y;
		z += vec.z;
		return this;
	}

	/**
	 * Adds the location by a vector.
	 *
	 * @see Vector
	 * @param vec
	 *            Vector to use
	 * @return the same location
	 */
	public Location add(Vector vec) {
		this.x += vec.getX();
		this.y += vec.getY();
		this.z += vec.getZ();
		return this;
	}

	@Override
	public Location clone() {
		try{
			return (Location) super.clone();
		}catch (CloneNotSupportedException e){
			throw new Error(e);
		}
	}

	/**
	 * Get the distance between this location and another. The value of this
	 * method is not cached and uses a costly square-root function, so do not
	 * repeatedly call this method to get the location's magnitude. NaN will
	 * be returned if the inner result of the sqrt() function overflows, which
	 * will be caused if the distance is too long.
	 *
	 * @see Vector
	 * @param o
	 *            The other location
	 * @return the distance
	**/
	public double distance(Location o) {
		return Math.sqrt(distanceSquared(o));
	}

	/**
	 * Get the squared distance between this location and another.
	 *
	 * @see Vector
	 * @param o
	 *            The other location
	 * @return the distance
	 * @throws IllegalArgumentException
	 *             for differing worlds
	 */
	public double distanceSquared(Location o) {
		if(o == null){
			throw new IllegalArgumentException("Cannot measure distance to a null location");
		}

		return NumberConversions.square(x - o.x) + NumberConversions.square(y - o.y) + NumberConversions.square(z - o.z);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null){
			return false;
		}
		if(getClass() != obj.getClass()){
			return false;
		}
		final Location other = (Location) obj;
		if(Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x)){
			return false;
		}
		if(Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y)){
			return false;
		}
		if(Double.doubleToLongBits(this.z) != Double.doubleToLongBits(other.z)){
			return false;
		}
		if(Float.floatToIntBits(this.pitch) != Float.floatToIntBits(other.pitch)){
			return false;
		}
		if(Float.floatToIntBits(this.yaw) != Float.floatToIntBits(other.yaw)){
			return false;
		}
		return true;
	}

	/**
	 * Gets the floored value of the X component, indicating the block that
	 * this location is contained with.
	 *
	 * @return block X
	 */
	public int getBlockX() {
		return locToBlock(x);
	}

	/**
	 * Gets the floored value of the Y component, indicating the block that
	 * this location is contained with.
	 *
	 * @return block y
	 */
	public int getBlockY() {
		return locToBlock(y);
	}

	/**
	 * Gets the floored value of the Z component, indicating the block that
	 * this location is contained with.
	 *
	 * @return block z
	 */
	public int getBlockZ() {
		return locToBlock(z);
	}

	/**
	 * Gets a unit-vector pointing in the direction that this Location is
	 * facing.
	 *
	 * @return a vector pointing the direction of this location's
	 *         {@link #getPitch() pitch} and {@link #getYaw() yaw}
	 */
	public Vector getDirection() {
		Vector vector = new Vector();

		double rotX = this.getYaw();
		double rotY = this.getPitch();

		vector.setY(-Math.sin(Math.toRadians(rotY)));

		double xz = Math.cos(Math.toRadians(rotY));

		vector.setX(-xz * Math.sin(Math.toRadians(rotX)));
		vector.setZ(xz * Math.cos(Math.toRadians(rotX)));

		return vector;
	}

	/**
	 * Gets the pitch of this location, measured in degrees.
	 * <ul>
	 * <li>A pitch of 0 represents level forward facing.
	 * <li>A pitch of 90 represents downward facing, or negative y direction.
	 * <li>A pitch of -90 represents upward facing, or positive y direction.
	 * <ul>
	 * Increasing pitch values the equivalent of looking down.
	 *
	 * @return the incline's pitch
	 */
	public float getPitch() {
		return pitch;
	}

	/**
	 * Gets the yaw of this location, measured in degrees.
	 * <ul>
	 * <li>A yaw of 0 or 360 represents the positive z direction.
	 * <li>A yaw of 180 represents the negative z direction.
	 * <li>A yaw of 90 represents the negative x direction.
	 * <li>A yaw of 270 represents the positive x direction.
	 * </ul>
	 * Increasing yaw values are the equivalent of turning to your
	 * right-facing, increasing the scale of the next respective axis, and
	 * decreasing the scale of the previous axis.
	 *
	 * @return the rotation's yaw
	 */
	public float getYaw() {
		return yaw;
	}

	/**
	 * Gets the magnitude of the location, defined as sqrt(x^2+y^2+z^2). The
	 * value of this method is not cached and uses a costly square-root
	 * function, so do not repeatedly call this method to get the location's
	 * magnitude. NaN will be returned if the inner result of the sqrt()
	 * function overflows, which will be caused if the length is too long. Not
	 * world-aware and orientation independent.
	 *
	 * @see Vector
	 * @return the magnitude
	 */
	public double length() {
		return Math.sqrt(NumberConversions.square(x) + NumberConversions.square(y) + NumberConversions.square(z));
	}

	/**
	 * Gets the magnitude of the location squared. Not world-aware and
	 * orientation independent.
	 *
	 * @see Vector
	 * @return the magnitude
	 */
	public double lengthSquared() {
		return NumberConversions.square(x) + NumberConversions.square(y) + NumberConversions.square(z);
	}

	/**
	 * Performs scalar multiplication, multiplying all components with a
	 * scalar. Not world-aware.
	 *
	 * @param m
	 *            The factor
	 * @see Vector
	 * @return the same location
	 */
	public Location multiply(double m) {
		x *= m;
		y *= m;
		z *= m;
		return this;
	}

	/**
	 * @param m
	 *            The factor
	 * @see Vector
	 * @return the same location
	 */
	public Location dividide(double m) {
		x /= m;
		y /= m;
		z /= m;
		return this;
	}
	
	/**
	 * Sets the {@link #getYaw() yaw} and {@link #getPitch() pitch} to point
	 * in the direction of the vector.
	 */
	public Location setDirection(Vector vector) {
		/*
		 * Sin = Opp / Hyp
		 * Cos = Adj / Hyp
		 * Tan = Opp / Adj
		 * x = -Opp
		 * z = Adj
		 */
		final double _2PI = 2 * Math.PI;
		final double x = vector.getX();
		final double z = vector.getZ();

		if(x == 0 && z == 0){
			pitch = vector.getY() > 0 ? -90 : 90;
			return this;
		}

		double theta = Math.atan2(-x, z);
		yaw = (float) Math.toDegrees((theta + _2PI) % _2PI);

		double x2 = NumberConversions.square(x);
		double z2 = NumberConversions.square(z);
		double xz = Math.sqrt(x2 + z2);
		pitch = (float) Math.toDegrees(Math.atan(-vector.getY() / xz));

		return this;
	}

	/**
	 * Sets the pitch of this location, measured in degrees.
	 * <ul>
	 * <li>A pitch of 0 represents level forward facing.
	 * <li>A pitch of 90 represents downward facing, or negative y direction.
	 * <li>A pitch of -90 represents upward facing, or positive y direction.
	 * <ul>
	 * Increasing pitch values the equivalent of looking down.
	 *
	 * @param pitch
	 *            new incline's pitch
	 */
	public Location setPitch(float pitch) {
		this.pitch = pitch;
		return this;
	}

	/**
	 * Sets the yaw of this location, measured in degrees.
	 * <ul>
	 * <li>A yaw of 0 or 360 represents the positive z direction.
	 * <li>A yaw of 180 represents the negative z direction.
	 * <li>A yaw of 90 represents the negative x direction.
	 * <li>A yaw of 270 represents the positive x direction.
	 * </ul>
	 * Increasing yaw values are the equivalent of turning to your
	 * right-facing, increasing the scale of the next respective axis, and
	 * decreasing the scale of the previous axis.
	 *
	 * @param yaw
	 *            new rotation's yaw
	 */
	public Location setYaw(float yaw) {
		this.yaw = yaw;
		return this;
	}

	/**
	 * Subtracts the location by another. Not world-aware and
	 * orientation independent.
	 *
	 * @see Vector
	 * @param x
	 *            X coordinate
	 * @param y
	 *            Y coordinate
	 * @param z
	 *            Z coordinate
	 * @return the same location
	 */
	public Location subtract(double x, double y, double z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		return this;
	}

	/**
	 * Subtracts the location by another.
	 *
	 * @see Vector
	 * @param vec
	 *            The other location
	 * @return the same location
	 * @throws IllegalArgumentException
	 *             for differing worlds
	 */
	public Location subtract(Location vec) {
		if(vec == null){
			throw new IllegalArgumentException("Cannot add Locations of differing worlds");
		}

		x -= vec.x;
		y -= vec.y;
		z -= vec.z;
		return this;
	}

	/**
	 * Subtracts the location by a vector.
	 *
	 * @see Vector
	 * @param vec
	 *            The vector to use
	 * @return the same location
	 */
	public Location subtract(Vector vec) {
		this.x -= vec.getX();
		this.y -= vec.getY();
		this.z -= vec.getZ();
		return this;
	}

	@Override
	public String toString() {
		return "Location{x=" + x + ",y=" + y + ",z=" + z + ",pitch=" + pitch + ",yaw=" + yaw + '}';
	}

	/**
	 * Constructs a new {@link Vector} based on this Location
	 *
	 * @return New Vector containing the coordinates represented by this
	 *         Location
	 */
	public Vector toVector() {
		return new Vector(x, y, z);
	}

	/**
	 * Zero this location's components. Not world-aware.
	 *
	 * @see Vector
	 * @return the same location
	 */
	public Location zero() {
		x = 0D;
		y = 0D;
		z = 0D;
		return this;
	}
	
	@Override
	public int hashCode() {
		int hash = 3;

		hash = 19 * hash + (int) (Double.doubleToLongBits(this.x) ^ Double.doubleToLongBits(this.x) >>> 32);
		hash = 19 * hash + (int) (Double.doubleToLongBits(this.y) ^ Double.doubleToLongBits(this.y) >>> 32);
		hash = 19 * hash + (int) (Double.doubleToLongBits(this.z) ^ Double.doubleToLongBits(this.z) >>> 32);
		hash = 19 * hash + Float.floatToIntBits(this.pitch);
		hash = 19 * hash + Float.floatToIntBits(this.yaw);
		return hash;
	}
}