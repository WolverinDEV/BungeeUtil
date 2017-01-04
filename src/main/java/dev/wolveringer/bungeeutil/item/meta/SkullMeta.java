package dev.wolveringer.bungeeutil.item.meta;

import java.util.UUID;

import dev.wolveringer.bungeeutil.BungeeUtil;
import dev.wolveringer.bungeeutil.OperationCalback;
import dev.wolveringer.bungeeutil.item.Item;
import dev.wolveringer.bungeeutil.profile.GameProfile;
import dev.wolveringer.bungeeutil.profile.GameProfileSerializer;
import dev.wolveringer.bungeeutil.profile.Skin;
import dev.wolveringer.bungeeutil.profile.SkinFactory;
import dev.wolveringer.bungeeutil.profile.UUIDFetcher;
import dev.wolveringer.nbt.NBTTagCompound;
import net.md_5.bungee.BungeeCord;

public class SkullMeta extends CraftItemMeta {

	static{
		MetaFactory.ItemMetaProxy.addWhiteList(SkullMeta.class, "setSkin");
	}

	GameProfile p = new GameProfile(UUID.randomUUID(), "");
	UUID owner;

	public SkullMeta(Item i) {
		super(i);
		if(this.getTag().hasKey("SkullOwner")) {
			this.p = GameProfileSerializer.deserialize(this.getTag().getCompound("SkullOwner"));
		}
	}

	@Override
	protected void build() {
		if(this.p != null) {
			this.getTag().set("SkullOwner", GameProfileSerializer.serialize(new NBTTagCompound(), this.p));
		}
		this.fireUpdate();
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(!super.equals(obj)) {
			return false;
		}
		if(this.getClass() != obj.getClass()) {
			return false;
		}
		SkullMeta other = (SkullMeta) obj;
		if(this.owner == null){
			if(other.owner != null) {
				return false;
			}
		}else if(!this.owner.equals(other.owner)) {
			return false;
		}
		if(this.p == null){
			if(other.p != null) {
				return false;
			}
		}else if(!this.p.equals(other.p)) {
			return false;
		}
		return super.equals(obj);
	}

	public GameProfile getProfile() {
		return this.p;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (this.owner == null ? 0 : this.owner.hashCode());
		result = prime * result + (this.p == null ? 0 : this.p.hashCode());
		return result;
	}

	public void setProfile(GameProfile p) {
		this.p = p;
		this.build();
	}

	public void setSkin(Skin s) {
		s.applay(this.p);
		this.p.setName(this.p.getName().equalsIgnoreCase("") ? s.getProfileName() : this.p.getName());
		this.build();
	}

	public void setSkin(final String owner) {
		BungeeCord.getInstance().getScheduler().runAsync(BungeeUtil.getPluginInstance(), () -> {
			try{
				SkullMeta.this.setSkin(UUIDFetcher.getUUIDOf(owner));
			}catch (Exception e){
				e.printStackTrace();
			}
		});
	}

	@SuppressWarnings("unchecked")
	public void setSkin(UUID owner) {
		this.owner = owner;
		if(this.p == null) {
			this.p = new GameProfile(owner, "");
		}
		this.p.setId(owner);
		SkinFactory.getSkin(owner, (OperationCalback<Skin>) response -> SkullMeta.this.setSkin(response));
		this.build();
	}

	public void setSkullOwner(String name) {
		if(this.p == null) {
			this.p = new GameProfile(UUID.randomUUID(), name);
		}
		this.p.setName(name);
		this.build();
	}

	@Override
	public String toString() {
		return "SkullMeta@" + System.identityHashCode(this) + "[p=" + this.p + ", owner=" + this.owner + "]";
	}
}
