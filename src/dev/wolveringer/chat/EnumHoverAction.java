package dev.wolveringer.chat;

import java.util.Map;

import com.google.common.collect.Maps;

@SuppressWarnings("unchecked")
public enum EnumHoverAction {
	
	SHOW_ITEM("SHOW_ITEM", 2, "show_item", true),
	SHOW_ACHIEVEMENT("SHOW_ACHIEVEMENT", 1, "show_achievement", true),
	SHOW_TEXT("SHOW_TEXT", 0, "show_text", true);
	
	@SuppressWarnings("rawtypes")
	private static final Map names = Maps.newHashMap();
	static{
		for(EnumHoverAction action : values())
			names.put(action.getActionName(), action);
	}

	public static EnumHoverAction getActionFromName(String s) {
		return (EnumHoverAction) names.get(s);
	}

	private final boolean extra_data;

	private final String command;

	private EnumHoverAction(String s, int i, String command, boolean extra_data) {
		this.command = command;
		this.extra_data = extra_data;
	}

	public boolean hasExtraData(){
		return this.extra_data;
	}

	public String getActionName(){
		return this.command;
	}
}