package dev.wolveringer.bungeeutil.entity.datawatcher;

public interface LivingEntityDataWatcher extends EntityDataWatcher {

	public int getArrows();

	public float getHealth();;

	public int getParicelColor();

	public boolean hasAI();

	@Override
	public LivingEntityDataWatcher injektDefault();

	public boolean isParticelVisiable();

	public void setAI(boolean flag);

	public void setArrows(int amauth);

	public void setHealth(float h);

	public void setParicelColor(int color);

	public void setParticelVisiable(boolean flag);
}
