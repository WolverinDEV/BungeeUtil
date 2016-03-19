package dev.wolveringer.NPC;

import dev.wolveringer.BungeeUtil.ClientVersion.BigClientVersion;
import dev.wolveringer.api.datawatcher.DataWatcher;
import dev.wolveringer.api.datawatcher.HumanDataWatcher;

public class NPCData implements HumanDataWatcher{
	private HumanDataWatcher v1_8;
	private HumanDataWatcher v1_9;
	
	public NPCData() {
		v1_8 = DataWatcher.createDataWatcher(BigClientVersion.v1_8).getSpecialDataWatcher(HumanDataWatcher.class);
		v1_9 = DataWatcher.createDataWatcher(BigClientVersion.v1_9).getSpecialDataWatcher(HumanDataWatcher.class);
	}
	
	public boolean isSneaking() {
		return v1_8.isSneaking();
	}
	public void setSkinFlags(byte flag) {
		v1_8.setSkinFlags(flag);
		v1_9.setSkinFlags(flag);
	}
	public void setHealth(float h) {
		v1_8.setHealth(h);
		v1_9.setHealth(h);
	}
	public void setSneaking(boolean flag) {
		v1_8.setSneaking(flag);
		v1_9.setSneaking(flag);
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
	public void setCapeActive(boolean b) {
		v1_8.setCapeActive(b);
	}
	public void setArrows(int amauth) {
		v1_8.setArrows(amauth);
		v1_9.setArrows(amauth);
	}
	public void setSprinting(boolean flag) {
		v1_8.setSprinting(flag);
		v1_9.setSprinting(flag);
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
	public void setParicelColor(int color) {
		v1_8.setParicelColor(color);
		v1_9.setParicelColor(color);
	}
	public void setAbsorptionHearts(float f) {
		v1_8.setAbsorptionHearts(f);
		v1_9.setAbsorptionHearts(f);
	}
	public void setInvisible(boolean flag) {
		v1_8.setInvisible(flag);
		v1_9.setInvisible(flag);
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
	public void setParticelVisiable(boolean flag) {
		v1_8.setParticelVisiable(flag);
		v1_9.setParticelVisiable(flag);
	}
	public void setScore(int score) {
		v1_8.setScore(score);
		v1_9.setScore(score);
	}
	public int getScore() {
		return v1_8.getScore();
	}
	public boolean isParticelVisiable() {
		return v1_8.isParticelVisiable();
	}
	public void setOnFire(boolean fire) {
		v1_8.setOnFire(fire);
		v1_9.setOnFire(fire);
	}
	public HumanDataWatcher injektDefault() {
		v1_9.injektDefault();
		return v1_8.injektDefault();
	}
	public void setAI(boolean flag) {
		v1_8.setAI(flag);
	}
	public boolean isBlocking() {
		return v1_8.isBlocking();
	}
	public void setBlocking(boolean block) {
		v1_8.setBlocking(block);
		v1_9.setBlocking(block);
	}
	public boolean hasAI() {
		return v1_8.hasAI();
	}
	public boolean isGlowing() {
		return v1_8.isGlowing();
	}
	public void setGlowing(boolean glow) {
		v1_8.setGlowing(glow);
		v1_9.setGlowing(glow);
	}
	public boolean isElytra() {
		return v1_9.isElytra();
	}
	public void setElytra(boolean elytra) {
		v1_9.setElytra(elytra);
	}
	public int getAir() {
		return v1_8.getAir();
	}
	public void setAir(int air) {
		v1_8.setAir(air);
		v1_9.setAir(air);
	}
	public void setCostumName(String name) {
		v1_8.setCostumName(name);
		v1_9.setCostumName(name);
	}
	public String getCostumName() {
		return v1_8.getCostumName();
	}
	public boolean isCostumNameVisiable() {
		return v1_8.isCostumNameVisiable();
	}
	public void setCostumNameVisiable(boolean flag) {
		v1_8.setCostumNameVisiable(flag);
		v1_9.setCostumNameVisiable(flag);
	}
	public boolean isSlient() {
		return v1_8.isSlient();
	}
	public void setSlient(boolean flag) {
		v1_8.setSlient(flag);
		v1_9.setSlient(flag);
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

	public DataWatcher getWatcher(BigClientVersion bigVersion) {
		if(bigVersion == BigClientVersion.v1_9){
			return v1_9.getWatcher();	
		}
		else
			return v1_8.getWatcher();
	}
}
