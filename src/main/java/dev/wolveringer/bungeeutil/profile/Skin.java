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
		this.empty = true;
	}

	protected Skin(JSONObject raw_profile) {
		try {
			if (raw_profile.has("properties")) {
				JSONArray properties = (JSONArray) raw_profile.get("properties");
				for (int i = 0; i < properties.length(); i++) { //TODO size == 1 ?????
					JSONObject property = (JSONObject) properties.get(i);
					this.name = (String) property.get("name");
					this.raw_value = (String) property.get("value");
					this.signature = (String) property.get("signature");
				}
			}
			if (this.raw_value != null) {
				this.value = new JSONObject(Base64Coder.decodeString(this.raw_value));
			} else {
				this.value = new JSONObject();
			}
			this.empty = false;
		} catch (Exception e) {
			e.printStackTrace();
			this.empty = true;
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
		this.value = new JSONObject(Base64Coder.decodeString(this.raw_value));
		if (!signature.equalsIgnoreCase("undefined")) {
			this.signature = signature;
		}
	}

	public GameProfile applay(GameProfile g) {
		if (g.getId() == null) {
			g.setId(this.getUUID());
		}
		if (g.getName() == null) {
			g.setName(this.getProfileName());
		}
		g.getProperties().clear();
		g.getProperties().put(this.name, new Property(this.name, this.raw_value, this.isSignatureRequired() ? this.signature : null));
		return g;
	}

	@Override
	public Skin clone() {
		Skin _new = new Skin();
		_new.empty = this.empty;
		_new.name = this.name;
		_new.raw_value = this.raw_value;
		_new.signature = this.signature;
		_new.value = this.value;
		return _new;
	}

	public String getCapeUrl() {
		if (this.hasCape()) {
			return this.value.getJSONObject("textures").getJSONObject("CAPE").getString("url");
		}
		return null;
	}

	public String getName() {
		return this.name;
	}

	public String getProfileName() {
		if (!this.hasProfileName()) {
			return "undef";
		}
		return this.value.getString("profileName");
	}

	public String getRawData() {
		return this.raw_value;
	}

	public String getSignature() {
		if (this.signature == null) {
			return "undefined";
		}
		return this.signature;
	}

	public String getSkinUrl() {
		if (this.hasSkin()) {
			return this.value.getJSONObject("textures").getJSONObject("SKIN").getString("url");
		}
		return null;
	}

	public UUID getUUID() {
		return UUID.fromString(this.value.getString("profileId").replaceFirst("([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]+)", "$1-$2-$3-$4-$5"));
	}

	public boolean hasCape() {
		return this.value.has("textures") && this.value.getJSONObject("textures").has("CAPE") && this.value.getJSONObject("textures").getJSONObject("CAPE").has("url");
	}

	public boolean hasProfileName() {
		return this.value.has("profileName");
	}

	public boolean hasSkin() {
		return this.value.has("textures") && this.value.getJSONObject("textures").has("SKIN") && this.value.getJSONObject("textures").getJSONObject("SKIN").has("url");
	}

	public boolean hasUUID() {
		return this.value.has("profileId");
	}

	public boolean isEmpty() {
		return this.empty;
	}

	public boolean isPublic() {
		return this.value.getBoolean("isPublic");
	}

	public boolean isSignatureRequired() {
		return this.value.has("signatureRequired") && this.value.getBoolean("signatureRequired");
	}

	public void setCape(String url) {
		this.empty = false;
		if (!this.value.has("textures")) {
			this.value.put("value", new JSONObject());
		}
		if (!this.value.getJSONObject("textures").has("CAPE")) {
			this.value.getJSONObject("textures").put("CAPE", new JSONObject());
		}
		this.value.getJSONObject("textures").getJSONObject("CAPE").put("url", url);
		this.updateRaw();
	}

	public void setProfileName(String name) {
		this.empty = false;
		this.value.put("profileName", name);
		this.updateRaw();
	}

	public void setPublic(boolean b) {
		this.empty = false;
		this.value.put("isPublic", b);
		this.updateRaw();
	}

	public void setRawData(String string) {
		this.raw_value = string;
		this.value = new JSONObject(Base64Coder.decodeString(this.raw_value));
	}

	public void setSignature(String signature) {
		this.signature = signature;
		this.empty = false;
	}

	public void setSignatureRequired(boolean flag) {
		this.empty = false;
		this.value.put("signatureRequired", flag);
		this.updateRaw();

	}

	public void setSkin(String url) {
		this.empty = false;
		if (!this.value.has("textures")) {
			this.value.put("value", new JSONObject());
		}
		if (!this.value.getJSONObject("textures").has("SKIN")) {
			this.value.getJSONObject("textures").put("SKIN", new JSONObject());
		}
		this.value.getJSONObject("textures").getJSONObject("SKIN").put("url", url);
		this.updateRaw();
	}

	public void setUUID(UUID id) {
		this.empty = false;
		this.value.put("profileId", id.toString().replaceAll("-", ""));
		this.updateRaw();
	}

	public GameProfile toGameProfile() {
		try {
			return this.applay(new GameProfile(this.getUUID(), this.getProfileName()));
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String toString() {
		return this.value.toString();
	}

	private void updateRaw() {
		this.raw_value = Base64Coder.encodeString(this.value.toString());
	}
}
