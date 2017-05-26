package dev.wolveringer.bungeeutil.entity.player;

import com.google.common.base.Function;

import dev.wolveringer.bungeeutil.entity.datawatcher.DataWatcher;
import dev.wolveringer.bungeeutil.entity.datawatcher.HumanEntityDataWatcher;
import dev.wolveringer.bungeeutil.player.ClientVersion.BigClientVersion;

public class MultiVersionPlayerDatawatcher implements HumanEntityDataWatcher {
	//TODO mutch better?
	private HumanEntityDataWatcher v1_8;
	private HumanEntityDataWatcher v1_9;
	private HumanEntityDataWatcher v1_10;
	private HumanEntityDataWatcher v1_11;
	private HumanEntityDataWatcher v1_12;
	
	public MultiVersionPlayerDatawatcher() {
		this.v1_8 = DataWatcher.createDataWatcher(BigClientVersion.v1_8).getSpecialDataWatcher(HumanEntityDataWatcher.class);
		this.v1_9 = DataWatcher.createDataWatcher(BigClientVersion.v1_9).getSpecialDataWatcher(HumanEntityDataWatcher.class);
		this.v1_10 = DataWatcher.createDataWatcher(BigClientVersion.v1_10).getSpecialDataWatcher(HumanEntityDataWatcher.class);
		this.v1_11 = DataWatcher.createDataWatcher(BigClientVersion.v1_11).getSpecialDataWatcher(HumanEntityDataWatcher.class);
		this.v1_12 = DataWatcher.createDataWatcher(BigClientVersion.v1_12).getSpecialDataWatcher(HumanEntityDataWatcher.class);
	}

	private void applay(Function<HumanEntityDataWatcher, Void> c) {
		c.apply(this.v1_8);
		c.apply(this.v1_9);
		c.apply(this.v1_10);
		c.apply(this.v1_11);
		c.apply(this.v1_12);
	}

	@Override
	public float getAbsorptionHearts() {
		return this.v1_8.getAbsorptionHearts();
	}

	@Override
	public int getAir() {
		return this.v1_8.getAir();
	}

	@Override
	public int getArrows() {
		return this.v1_8.getArrows();
	}

	@Override
	public String getCostumName() {
		return this.v1_8.getCostumName();
	}

	@Override
	public float getHealth() {
		return this.v1_8.getHealth();
	}

	@Override
	public int getParicelColor() {
		return this.v1_8.getParicelColor();
	}

	@Override
	public int getScore() {
		return this.v1_8.getScore();
	}

	@Override
	public byte getSkinFlag() {
		return this.v1_8.getSkinFlag();
	}
	
	@Override
	public DataWatcher getWatcher() {
		return null;
	}

	public DataWatcher getWatcher(BigClientVersion bigVersion) {
		switch (bigVersion) {
		case v1_12:
			return this.v1_12.getWatcher();
		case v1_11:
			return this.v1_11.getWatcher();
		case v1_10:
			return this.v1_10.getWatcher();
		case v1_9:
			return this.v1_9.getWatcher();
		case v1_8:
			return this.v1_8.getWatcher();
		default:
			break;
		}
		return null;
	}

	@Override
	public boolean hasAI() {
		return this.v1_8.hasAI();
	}

	@Override
	public boolean hasGravity() {
		return this.v1_9.hasGravity();
	}

	@Override
	public HumanEntityDataWatcher injektDefault() {
		this.v1_9.injektDefault();
		this.v1_10.injektDefault();
		this.v1_11.injektDefault();
		this.v1_12.injektDefault();
		return this.v1_8.injektDefault();
	}

	@Override
	public boolean isBlocking() {
		return this.v1_8.isBlocking();
	}

	@Override
	public boolean isCapeActive() {
		return this.v1_8.isCapeActive();
	}

	@Override
	public boolean isCostumNameVisiable() {
		return this.v1_8.isCostumNameVisiable();
	}

	@Override
	public boolean isElytra() {
		return this.v1_9.isElytra();
	}

	@Override
	public boolean isGlowing() {
		return this.v1_8.isGlowing();
	}

	@Override
	public boolean isInvisible() {
		return this.v1_8.isInvisible();
	}

	@Override
	public boolean isOnFire() {
		return this.v1_8.isOnFire();
	}

	@Override
	public boolean isParticelVisiable() {
		return this.v1_8.isParticelVisiable();
	}

	@Override
	public boolean isSlient() {
		return this.v1_8.isSlient();
	}

	@Override
	public boolean isSneaking() {
		return this.v1_8.isSneaking();
	}

	@Override
	public boolean isSprinting() {
		return this.v1_8.isSprinting();
	}

	@Override
	public void setAbsorptionHearts(final float f) {
		this.applay(arg0 -> {
			arg0.setAbsorptionHearts(f);
			return null;
		});
	}

	@Override
	public void setAI(final boolean flag) {
		this.v1_8.setAI(flag);
	}

	@Override
	public void setAir(final int air) {
		this.applay(arg0 -> {
			arg0.setAir(air);
			return null;
		});
	}

	@Override
	public void setApplayGravity(final boolean flag) {
		this.applay(arg0 -> {
			arg0.setApplayGravity(flag);
			return null;
		});
	}

	@Override
	public void setArrows(final int amauth) {
		this.applay(arg0 -> {
			arg0.setArrows(amauth);
			return null;
		});
	}

	@Override
	public void setBlocking(final boolean block) {
		this.applay(arg0 -> {
			arg0.setBlocking(block);
			return null;
		});
	}

	@Override
	public void setCapeActive(final boolean b) {
		this.applay(arg0 -> {
			arg0.setCapeActive(b);
			return null;
		});
	}

	@Override
	public void setCostumName(final String name) {
		this.applay(arg0 -> {
			arg0.setCostumName(name);
			return null;
		});
	}

	@Override
	public void setCostumNameVisiable(final boolean flag) {
		this.applay(arg0 -> {
			arg0.setCostumNameVisiable(flag);
			return null;
		});
	}

	@Override
	public void setElytra(final boolean elytra) {
		this.v1_9.setElytra(elytra);
	}

	@Override
	public void setGlowing(final boolean glow) {
		this.applay(arg0 -> {
			arg0.setGlowing(glow);
			return null;
		});
	}

	@Override
	public void setHealth(final float h) {
		this.applay(arg0 -> {
			arg0.setHealth(h);
			return null;
		});
	}

	@Override
	public void setInvisible(final boolean flag) {
		this.applay(arg0 -> {
			arg0.setInvisible(flag);
			return null;
		});
	}

	@Override
	public void setOnFire(final boolean fire) {
		this.applay(arg0 -> {
			arg0.setOnFire(fire);
			return null;
		});
	}

	@Override
	public void setParicelColor(final int color) {
		this.applay(arg0 -> {
			arg0.setParicelColor(color);
			return null;
		});
	}

	@Override
	public void setParticelVisiable(final boolean flag) {
		this.applay(arg0 -> {
			arg0.setParticelVisiable(flag);
			return null;
		});
	}

	@Override
	public void setScore(final int score) {
		this.applay(arg0 -> {
			arg0.setScore(score);
			return null;
		});
	}

	@Override
	public void setSkinFlags(final byte flag) {
		this.applay(arg0 -> {
			arg0.setSkinFlags(flag);
			return null;
		});
	}

	@Override
	public void setSlient(final boolean flag) {
		this.applay(arg0 -> {
			arg0.setSlient(flag);
			return null;
		});
	}

	@Override
	public void setSneaking(final boolean flag) {
		this.applay(arg0 -> {
			arg0.setSneaking(flag);
			return null;
		});
	}

	@Override
	public void setSprinting(final boolean flag) {
		this.applay(arg0 -> {
			arg0.setSprinting(flag);
			return null;
		});
	}
}
