package dev.wolveringer.bungeeutil.entity.datawatcher;

import java.util.UUID;

public class OptionalUUID {
	private UUID uuid;

	public OptionalUUID(UUID uuid) {
		super();
		this.uuid = uuid;
	}

	public UUID getUuid() {
		return this.uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
}
