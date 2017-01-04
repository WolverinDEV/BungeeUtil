package dev.wolveringer.bungeeutil.entity.datawatcher;

public interface HumanDataWatcher extends LivingEntityDataWatcher {
	public float getAbsorptionHearts();

	public int getScore();

	public byte getSkinFlag();

	@Override
	public HumanDataWatcher injektDefault();

	public boolean isCapeActive();

	public void setAbsorptionHearts(float f);

	public void setCapeActive(boolean b);

	public void setScore(int score);

	public void setSkinFlags(byte flag);
}
