package dev.wolveringer.chat;

import java.util.UUID;

public class ChatClickable {

	private EnumClickAction action;
	private String value;
	private transient String command_signature;
	private transient ChatClickListener listener = null;

	public ChatClickable(EnumClickAction action) {
		this.action = action;
	}
	public ChatClickable(EnumClickAction action, String data) {
		this(action);
		this.value = data;
	}
	public ChatClickable(EnumClickAction enumclickaction, String s,ChatClickListener listener) {
		this(enumclickaction,s);
		this.command_signature = UUID.randomUUID().toString() + System.currentTimeMillis();
	}

	public EnumClickAction getAction() {
		return this.action;
	}

	public String getValue() {
		return this.value;
	}
	
	@Override
	public int hashCode() {
		int i = this.action.hashCode();

		i = 31 * i + (this.value != null ? this.value.hashCode() : 0);
		return i;
	}

	@Override
	public String toString() {
		if(!hasListener())
			return "ClickEvent{action=" + this.action + ", value=\'" + this.value + '\'' + '}';
		else
			return "ClickEvent{action=" + this.action + ", value=\'" + this.value + '\'' +", CommandSignature=\'"+ command_signature +"\'"+'}';
	}

	public String getCommandSignature() {
		return command_signature;
	}

	public boolean hasListener() {
		return listener != null && command_signature != null && !"".equalsIgnoreCase(command_signature);
	}

	public ChatClickListener getListener() {
		return listener;
	}
}