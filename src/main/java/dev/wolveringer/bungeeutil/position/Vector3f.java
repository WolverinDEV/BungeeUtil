package dev.wolveringer.bungeeutil.position;

import dev.wolveringer.nbt.NBTTagFloat;
import dev.wolveringer.nbt.NBTTagList;

public class Vector3f extends Potision<Float>{
	public Vector3f(float x, float y, float z) {
		super(x, y, z);
	}

	public Vector3f(NBTTagList list) {
		this(list.getFloat(0),list.getFloat(1),list.getFloat(2));
	}

	public NBTTagList toNBTTagList() {
		NBTTagList localNBTTagList = new NBTTagList();
		localNBTTagList.add(new NBTTagFloat(this.x));
		localNBTTagList.add(new NBTTagFloat(this.y));
		localNBTTagList.add(new NBTTagFloat(this.z));
		return localNBTTagList;
	}

	public boolean equals(Object paramObject) {
		if(!(paramObject instanceof Vector3f)){
			return false;
		}
		Vector3f localVector3f = (Vector3f) paramObject;
		return (this.x == localVector3f.x) && (this.y == localVector3f.y) && (this.z == localVector3f.z);
	}

	@Override
	public void setX(Float x) {
		throw new UnsupportedOperationException();
	}
	@Override
	public void setY(Float y) {
		throw new UnsupportedOperationException();
	}
	@Override
	public void setZ(Float z) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String toString() {
		return "Vector3f [x=" + x + ", y=" + y + ", z=" + z + "]";
	}
}
