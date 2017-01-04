package dev.wolveringer.bungeeutil.entity.datawatcher;

public interface EntityDataWatcher{
	
	public boolean isSneaking();

	public void setSneaking(boolean flag);

	public boolean isSprinting();

	public void setSprinting(boolean flag);

	public boolean isInvisible();

	public void setInvisible(boolean flag);
	
	/**
	 *  Minecrat 1.9
	 */
	
	public boolean isOnFire();
	
	public void setOnFire(boolean fire);
	
	public boolean isBlocking();
	
	public void setBlocking(boolean block); 
	
	public boolean isGlowing();
	
	public void setGlowing(boolean glow); 
	
	public boolean isElytra();
	
	public void setElytra(boolean elytra); 
	
	public int getAir();
	
	public void setAir(int air);
	
	public void setCostumName(String name);
	
	public String getCostumName();
	
	public boolean isCostumNameVisiable();
	
	public void setCostumNameVisiable(boolean flag);
	
	public boolean isSlient();
	
	public void setSlient(boolean flag);
	
	public boolean hasGravity();
	public void setApplayGravity(boolean flag);
	
	
	public DataWatcher getWatcher();
	
	public EntityDataWatcher injektDefault();
}
