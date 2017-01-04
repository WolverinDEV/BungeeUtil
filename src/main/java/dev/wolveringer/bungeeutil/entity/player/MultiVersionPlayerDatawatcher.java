package dev.wolveringer.bungeeutil.entity.player;

import com.google.common.base.Function;

import dev.wolveringer.bungeeutil.entity.datawatcher.DataWatcher;
import dev.wolveringer.bungeeutil.entity.datawatcher.HumanDataWatcher;
import dev.wolveringer.bungeeutil.player.ClientVersion.BigClientVersion;

public class MultiVersionPlayerDatawatcher implements HumanDataWatcher {
	private HumanDataWatcher v1_8;
	private HumanDataWatcher v1_9;
	private HumanDataWatcher v1_10;
	
	public MultiVersionPlayerDatawatcher() {
		v1_8 = DataWatcher.createDataWatcher(BigClientVersion.v1_8).getSpecialDataWatcher(HumanDataWatcher.class);
		v1_9 = DataWatcher.createDataWatcher(BigClientVersion.v1_9).getSpecialDataWatcher(HumanDataWatcher.class);
		v1_10 = DataWatcher.createDataWatcher(BigClientVersion.v1_10).getSpecialDataWatcher(HumanDataWatcher.class);
	}
	
	public boolean isSneaking() {
		return v1_8.isSneaking();
	}
	
	public void setSkinFlags(final byte flag) {
		applay(new Function<HumanDataWatcher, Void>() {
			@Override
			public Void apply(HumanDataWatcher arg0) {
				arg0.setSkinFlags(flag);
				return null;
			}
		});
	}
	
	public void setHealth(final float h) {
		applay(new Function<HumanDataWatcher, Void>() {
			@Override
			public Void apply(HumanDataWatcher arg0) {
				arg0.setHealth(h);
				return null;
			}
		});
	}
	
	public void setSneaking(final boolean flag) {
		applay(new Function<HumanDataWatcher, Void>() {
			@Override
			public Void apply(HumanDataWatcher arg0) {
				arg0.setSneaking(flag);
				return null;
			}
		});
	}
	
	public byte getSkinFlag() {
		return v1_8.getSkinFlag();
	}
	
	public float getHealth() {
		return v1_8.getHealth();
	}
	
	public boolean isSprinting() {
		return v1_8.isSprinting();
	}
	
	public void setCapeActive(final boolean b) {
		applay(new Function<HumanDataWatcher, Void>() {
			@Override
			public Void apply(HumanDataWatcher arg0) {
				arg0.setCapeActive(b);
				return null;
			}
		});
	}
	
	public void setArrows(final int amauth) {
		applay(new Function<HumanDataWatcher, Void>() {
			@Override
			public Void apply(HumanDataWatcher arg0) {
				arg0.setArrows(amauth);
				return null;
			}
		});
	}
	
	public void setSprinting(final boolean flag) {
		applay(new Function<HumanDataWatcher, Void>() {
			@Override
			public Void apply(HumanDataWatcher arg0) {
				arg0.setSprinting(flag);
				return null;
			}
		});
	}
	
	public boolean isCapeActive() {
		return v1_8.isCapeActive();
	}
	
	public int getArrows() {
		return v1_8.getArrows();
	}
	
	public boolean isInvisible() {
		return v1_8.isInvisible();
	}
	
	public void setParicelColor(final int color) {
		applay(new Function<HumanDataWatcher, Void>() {
			@Override
			public Void apply(HumanDataWatcher arg0) {
				arg0.setParicelColor(color);
				return null;
			}
		});
	}
	
	public void setAbsorptionHearts(final float f) {
		applay(new Function<HumanDataWatcher, Void>() {
			@Override
			public Void apply(HumanDataWatcher arg0) {
				arg0.setAbsorptionHearts(f);
				return null;
			}
		});
	}
	
	public void setInvisible(final boolean flag) {
		applay(new Function<HumanDataWatcher, Void>() {
			@Override
			public Void apply(HumanDataWatcher arg0) {
				arg0.setInvisible(flag);
				return null;
			}
		});
	}
	
	public float getAbsorptionHearts() {
		return v1_8.getAbsorptionHearts();
	}
	
	public int getParicelColor() {
		return v1_8.getParicelColor();
	}
	
	public boolean isOnFire() {
		return v1_8.isOnFire();
	}
	
	public void setParticelVisiable(final boolean flag) {
		applay(new Function<HumanDataWatcher, Void>() {
			@Override
			public Void apply(HumanDataWatcher arg0) {
				arg0.setParticelVisiable(flag);
				return null;
			}
		});
	}
	
	public void setScore(final int score) {
		applay(new Function<HumanDataWatcher, Void>() {
			@Override
			public Void apply(HumanDataWatcher arg0) {
				arg0.setScore(score);
				return null;
			}
		});
	}
	
	public int getScore() {
		return v1_8.getScore();
	}
	
	public boolean isParticelVisiable() {
		return v1_8.isParticelVisiable();
	}
	
	public void setOnFire(final boolean fire) {
		applay(new Function<HumanDataWatcher, Void>() {
			@Override
			public Void apply(HumanDataWatcher arg0) {
				arg0.setOnFire(fire);
				return null;
			}
		});
	}
	
	public HumanDataWatcher injektDefault() {
		v1_9.injektDefault();
		v1_10.injektDefault();
		return v1_8.injektDefault();
	}
	
	public void setAI(final boolean flag) {
		v1_8.setAI(flag);
	}
	
	public boolean isBlocking() {
		return v1_8.isBlocking();
	}
	
	public void setBlocking(final boolean block) {
		applay(new Function<HumanDataWatcher, Void>() {
			@Override
			public Void apply(HumanDataWatcher arg0) {
				arg0.setBlocking(block);
				return null;
			}
		});
	}
	
	public boolean hasAI() {
		return v1_8.hasAI();
	}
	
	public boolean isGlowing() {
		return v1_8.isGlowing();
	}
	
	public void setGlowing(final boolean glow) {
		applay(new Function<HumanDataWatcher, Void>() {
			@Override
			public Void apply(HumanDataWatcher arg0) {
				arg0.setGlowing(glow);
				return null;
			}
		});
	}
	
	public boolean isElytra() {
		return v1_9.isElytra();
	}
	
	public void setElytra(final boolean elytra) {
		v1_9.setElytra(elytra);
	}
	
	public int getAir() {
		return v1_8.getAir();
	}
	
	public void setAir(final int air) {
		applay(new Function<HumanDataWatcher, Void>() {
			@Override
			public Void apply(HumanDataWatcher arg0) {
				arg0.setAir(air);
				return null;
			}
		});
	}
	
	public void setCostumName(final String name) {
		applay(new Function<HumanDataWatcher, Void>() {
			@Override
			public Void apply(HumanDataWatcher arg0) {
				arg0.setCostumName(name);
				return null;
			}
		});
	}
	
	public String getCostumName() {
		return v1_8.getCostumName();
	}
	
	public boolean isCostumNameVisiable() {
		return v1_8.isCostumNameVisiable();
	}
	
	public void setCostumNameVisiable(final boolean flag) {
		applay(new Function<HumanDataWatcher, Void>() {
			@Override
			public Void apply(HumanDataWatcher arg0) {
				arg0.setCostumNameVisiable(flag);
				return null;
			}
		});
	}
	
	public boolean isSlient() {
		return v1_8.isSlient();
	}
	
	public void setSlient(final boolean flag) {
		applay(new Function<HumanDataWatcher, Void>() {
			@Override
			public Void apply(HumanDataWatcher arg0) {
				arg0.setSlient(flag);
				return null;
			}
		});
	}
	
	@Override
	public void setApplayGravity(final boolean flag) {
		applay(new Function<HumanDataWatcher, Void>() {
			@Override
			public Void apply(HumanDataWatcher arg0) {
				arg0.setApplayGravity(flag);
				return null;
			}
		});
	}
	
	public DataWatcher getWatcher() {
		return null;
	}
	
	public HumanDataWatcher getV1_8() {
		return v1_8;
	}
	
	public HumanDataWatcher getV1_9() {
		return v1_9;
	}
	
	public HumanDataWatcher getV1_10() {
		return v1_10;
	}
	
	public DataWatcher getWatcher(BigClientVersion bigVersion) {
		if (bigVersion == BigClientVersion.v1_10) return v1_10.getWatcher();
		else if (bigVersion == BigClientVersion.v1_9) return v1_9.getWatcher();
		else return v1_8.getWatcher();
	}
	
	@Override
	public boolean hasGravity() {
		return v1_9.hasGravity();
	}
	
	private void applay(Function<HumanDataWatcher, Void> c) {
		c.apply(v1_8);
		c.apply(v1_9);
		c.apply(v1_10);
	}
}
