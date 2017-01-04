package dev.wolveringer.bungeeutil.entity.datawatcher;

public interface EntityDataWatcher{

	public int getAir();

	public String getCostumName();

	public DataWatcher getWatcher();

	public boolean hasGravity();

	public EntityDataWatcher injektDefault();

	public boolean isBlocking();

	public boolean isCostumNameVisiable();

	public boolean isElytra();

	public boolean isGlowing();

	public boolean isInvisible();

	/**
	 *  Minecrat 1.9
	 */

	public boolean isOnFire();

	public boolean isSlient();

	public boolean isSneaking();

	public boolean isSprinting();

	public void setAir(int air);

	public void setApplayGravity(boolean flag);

	public void setBlocking(boolean block);

	public void setCostumName(String name);

	public void setCostumNameVisiable(boolean flag);

	public void setElytra(boolean elytra);

	public void setGlowing(boolean glow);

	public void setInvisible(boolean flag);

	public void setOnFire(boolean fire);
	public void setSlient(boolean flag);


	public void setSneaking(boolean flag);

	public void setSprinting(boolean flag);
}
