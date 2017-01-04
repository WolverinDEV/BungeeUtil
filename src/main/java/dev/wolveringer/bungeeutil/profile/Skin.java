package dev.wolveringer.bungeeutil.profile;

import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class Skin {
	protected static Skin createEmptySkin() {
		Skin s = new Skin();
		s.name = "";
		s.raw_value = "";
		s.signature = "";
		s.value = new JSONObject();
		s.updateRaw();
		return s;
	}

	private String name = "textures";
	private String raw_value;
	private String signature;
	private boolean empty = false;

	private JSONObject value;

	private Skin() {
		empty = true;
	}

	protected Skin(JSONObject raw_profile) {
		try {
			if (raw_profile.has("properties")) {
				JSONArray properties = (JSONArray) raw_profile.get("properties");
				for (int i = 0; i < properties.length(); i++) { //TODO size == 1 ?????
					JSONObject property = (JSONObject) properties.get(i);
					name = (String) property.get("name");
					raw_value = (String) property.get("value");
					signature = (String) property.get("signature");
				}
			}
			if (raw_value != null)
				value = new JSONObject(Base64Coder.decodeString(raw_value));
			else
				value = new JSONObject();
			empty = false;
		} catch (Exception e) {
			e.printStackTrace();
			empty = true;
		}
		
	}

	protected Skin(String rawValue, String signature) {
		try {
			if (rawValue == null || rawValue.equalsIgnoreCase("undefined") || Base64Coder.decodeString(rawValue) == null) {
				rawValue = Base64Coder.encodeString("{}");
			}
		} catch (IllegalArgumentException ex) {
			rawValue = Base64Coder.encodeString("{}");
		}
		this.raw_value = rawValue;
		this.value = new JSONObject(Base64Coder.decodeString(raw_value));
		if (!signature.equalsIgnoreCase("undefined"))
			this.signature = signature;
	}

	public GameProfile applay(GameProfile g) {
		if (g.getId() == null)
			g.setId(getUUID());
		if (g.getName() == null)
			g.setName(getProfileName());
		g.getProperties().clear();
		g.getProperties().put(name, new Property(name, raw_value, isSignatureRequired() ? signature : null));
		return g;
	}

	public String getSkinUrl() {
		if (hasSkin())
			return value.getJSONObject("textures").getJSONObject("SKIN").getString("url");
		return null;
	}

	public void setSkin(String url) {
		empty = false;
		if (!value.has("textures"))
			value.put("value", new JSONObject());
		if (!value.getJSONObject("textures").has("SKIN"))
			value.getJSONObject("textures").put("SKIN", new JSONObject());
		value.getJSONObject("textures").getJSONObject("SKIN").put("url", url);
		updateRaw();
	}

	public boolean hasSkin() {
		return value.has("textures") && value.getJSONObject("textures").has("SKIN") && value.getJSONObject("textures").getJSONObject("SKIN").has("url");
	}

	public void setCape(String url) {
		empty = false;
		if (!value.has("textures"))
			value.put("value", new JSONObject());
		if (!value.getJSONObject("textures").has("CAPE"))
			value.getJSONObject("textures").put("CAPE", new JSONObject());
		value.getJSONObject("textures").getJSONObject("CAPE").put("url", url);
		updateRaw();
	}

	public String getCapeUrl() {
		if (hasCape())
			return value.getJSONObject("textures").getJSONObject("CAPE").getString("url");
		return null;
	}

	public boolean hasCape() {
		return value.has("textures") && value.getJSONObject("textures").has("CAPE") && value.getJSONObject("textures").getJSONObject("CAPE").has("url");
	}

	public UUID getUUID() {
		return UUID.fromString(value.getString("profileId").replaceFirst("([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]+)", "$1-$2-$3-$4-$5"));
	}

	public void setUUID(UUID id) {
		empty = false;
		value.put("profileId", id.toString().replaceAll("-", ""));
		updateRaw();
	}

	public boolean hasUUID() {
		return value.has("profileId");
	}

	public String getProfileName() {
		if (!hasProfileName())
			return "undef";
		return value.getString("profileName");
	}

	public boolean hasProfileName() {
		return value.has("profileName");
	}

	public void setProfileName(String name) {
		empty = false;
		value.put("profileName", name);
		updateRaw();
	}

	public boolean isPublic() {
		return value.getBoolean("isPublic");
	}

	public void setPublic(boolean b) {
		empty = false;
		value.put("isPublic", b);
		updateRaw();
	}

	public boolean isEmpty() {
		return empty;
	}

	private void updateRaw() {
		raw_value = Base64Coder.encodeString(value.toString());
	}

	public GameProfile toGameProfile() {
		try {
			return applay(new GameProfile(getUUID(), getProfileName()));
		} catch (Exception e) {
			return null;
		}
	}

	public void setSignatureRequired(boolean flag) {
		empty = false;
		value.put("signatureRequired", flag);
		updateRaw();

	}

	public boolean isSignatureRequired() {
		return value.has("signatureRequired") && value.getBoolean("signatureRequired");
	}

	public String getSignature() {
		if (signature == null)
			return "undefined";
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
		empty = false;
	}

	public void setRawData(String string) {
		this.raw_value = string;
		this.value = new JSONObject(Base64Coder.decodeString(raw_value));
	}

	public String getRawData() {
		return raw_value;
	}

	public Skin clone() {
		Skin _new = new Skin();
		_new.empty = empty;
		_new.name = name;
		_new.raw_value = raw_value;
		_new.signature = signature;
		_new.value = value;
		return _new;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return value.toString();
	}
}
