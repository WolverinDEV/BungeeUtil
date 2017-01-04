package dev.wolveringer.bungeeutil.entity.datawatcher;

public interface HumanDataWatcher extends LivingEntityDataWatcher {
	public void setSkinFlags(byte flag);

	public byte getSkinFlag();

	public void setCapeActive(boolean b);

	public boolean isCapeActive();

	public void setAbsorptionHearts(float f);

	public float getAbsorptionHearts();

	public void setScore(int score);

	public int getScore();

	@Override
	public HumanDataWatcher injektDefault();
}
