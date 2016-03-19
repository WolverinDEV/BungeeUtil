package dev.wolveringer.api.datawatcher;

public interface LivingEntityDataWatcher extends EntityDataWatcher {

	public void setHealth(float h);

	public float getHealth();;

	public void setArrows(int amauth);

	public int getArrows();

	public void setParicelColor(int color);
	
	public int getParicelColor();
	
	public void setParticelVisiable(boolean flag);
	
	public boolean isParticelVisiable();

	public void setAI(boolean flag);
	
	public boolean hasAI();

	@Override
	public LivingEntityDataWatcher injektDefault();
}
