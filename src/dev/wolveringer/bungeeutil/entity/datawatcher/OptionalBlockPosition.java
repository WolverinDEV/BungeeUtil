package dev.wolveringer.bungeeutil.entity.datawatcher;

import dev.wolveringer.bungeeutil.position.BlockPosition;

public class OptionalBlockPosition {
	private BlockPosition position;

	public OptionalBlockPosition(BlockPosition position) {
		super();
		this.position = position;
	}

	public BlockPosition getPosition() {
		return position;
	}

	public void setPosition(BlockPosition position) {
		this.position = position;
	}
	
	
}
