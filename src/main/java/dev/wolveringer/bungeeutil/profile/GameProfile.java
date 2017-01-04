package dev.wolveringer.bungeeutil.profile;

import java.util.UUID;

public class GameProfile {
	private UUID id;
	private String name;
	private PropertyMap properties = new PropertyMap();
	private boolean legacy;

	public GameProfile(UUID id, String name) {
		if(id == null && ("".equalsIgnoreCase(name)||name==null)) {
			throw new IllegalArgumentException("Name and ID cannot both be blank");
		}
		this.id = id;
		this.name = name;
	}

	public UUID getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public PropertyMap getProperties() {
		return this.properties;
	}

	public boolean isComplete() {
		return this.id != null && this.getName().isEmpty();
	}

	public boolean isLegacy() {
		return this.legacy;
	}

	public void setId(UUID id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setProperties(PropertyMap properties) {
		this.properties = properties;
	}
	@Override
	public String toString() {
		return "GameProfile [id=" + this.id + ", name=" + this.name + ", properties=" + this.properties + ", legacy=" + this.legacy + "]";
	}
}
