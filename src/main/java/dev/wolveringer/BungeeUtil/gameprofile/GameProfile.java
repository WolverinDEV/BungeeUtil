package dev.wolveringer.BungeeUtil.gameprofile;

import java.util.UUID;

public class GameProfile {
	private UUID id;
	private String name;
	private PropertyMap properties = new PropertyMap();
	private boolean legacy;

	public GameProfile(UUID id, String name) {
		if((id == null) && ("".equalsIgnoreCase(name)||name==null))
			throw new IllegalArgumentException("Name and ID cannot both be blank");
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
		return (this.id != null) && getName().isEmpty();
	}

	@Override
	public String toString() {
		return "GameProfile [id=" + id + ", name=" + name + ", properties=" + properties + ", legacy=" + legacy + "]";
	}

	public boolean isLegacy() {
		return this.legacy;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public void setProperties(PropertyMap properties) {
		this.properties = properties;
	}
}
