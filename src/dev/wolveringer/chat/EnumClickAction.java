package dev.wolveringer.chat;

import java.util.Map;

import com.google.common.collect.Maps;

@SuppressWarnings("unchecked")
public enum EnumClickAction {

	OPEN_URL("OPEN_URL", 0, "open_url", true),
	OPEN_FILE("OPEN_FILE", 1, "open_file", false),
	RUN_COMMAND("RUN_COMMAND", 2, "run_command", true),
	SUGGEST_COMMAND("SUGGEST_COMMAND", 4, "suggest_command", true),
	TWITCH_USER_INFO("TWITCH_USER_INFO", 3, "twitch_user_info", false);
	
	@SuppressWarnings("rawtypes")
	private static final Map actions = Maps.newHashMap();
	static{
		for(EnumClickAction c : values()){
			actions.put(c.getActionName(), c);
		}
	}

	public static EnumClickAction getActionFromName(String s) {
		return (EnumClickAction) actions.get(s);
	}

	private final boolean extra_data;

	private final String command;

	private EnumClickAction(String s, int i, String command, boolean extra_data) {
		this.command = command;
		this.extra_data = extra_data;
	}

	public boolean hasExtraData() {
		return this.extra_data;
	}

	public String getActionName() {
		return this.command;
	}
}