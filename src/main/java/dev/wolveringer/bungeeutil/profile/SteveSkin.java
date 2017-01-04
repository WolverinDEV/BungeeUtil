package dev.wolveringer.bungeeutil.profile;

import java.util.UUID;

import org.json.JSONObject;

public class SteveSkin extends Skin{

	public SteveSkin() {
		super(new JSONObject());
	}
	
	@Override
	public GameProfile applay(GameProfile g) {
		return g;
	}
	
	@Override
	public String getCapeUrl() {
		return "";
	}
	
	@Override
	public String getProfileName() {
		return "Steve";
	}

	@Override
	public String getRawData() {
		return "undefined";
	}
	
	@Override
	public String getSignature() {
		return "undefined";
	}
	
	@Override
	public String getSkinUrl() {
		return "";
	}
	
	@Override
	public UUID getUUID() {
		return UUID.nameUUIDFromBytes("Steve".getBytes());
	}
	
	@Override
	public boolean hasSkin() {
		return false;
	}
	@Override
	public boolean hasCape() {
		return false;
	}
	@Override
	public boolean hasProfileName() {
		return true;
	}
	@Override
	public boolean hasUUID() {
		return false;
	}
	@Override
	public boolean isEmpty() {
		return true;
	}
	@Override
	public boolean isPublic() {
		return false;
	}
	@Override
	public boolean isSignatureRequired() {
		return false;
	}
	@Override
	public void setCape(String url) {}
	@Override
	public void setProfileName(String name) {}
	@Override
	public void setPublic(boolean b) {}
	@Override
	public void setRawData(String string) {}
	@Override
	public void setSignature(String signature) {}
	@Override
	public void setSignatureRequired(boolean flag) {}
	@Override
	public void setSkin(String url) {}
	@Override
	public void setUUID(UUID id) {}
	@Override
	public GameProfile toGameProfile() {
		return new GameProfile(getUUID(), getProfileName());
	}
	@Override
	public String toString() {
		return "Skin[Owner=Steve]";
	}
}