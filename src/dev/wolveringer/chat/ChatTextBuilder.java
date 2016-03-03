package dev.wolveringer.chat;

import dev.wolveringer.chat.ChatColor.ChatColorUtils;
import net.md_5.bungee.api.ChatColor;

public class ChatTextBuilder {
	IChatBaseComponent base;
	IChatBaseComponent currunt = null;

	public ChatTextBuilder() {
		base = new ChatComponentText("");
	}

	public ChatTextBuilder appand(String text) {
		if(currunt != null)
			base.addSibling(currunt);
		currunt = new ChatComponentText("");
		return this;
	}

	public ChatTextBuilder setColor(ChatColor color) {
		if(ChatColorUtils.isColor(color))
			currunt.getChatModifier().setColor(color);
		return this;
	}

	public void setFormat(ChatColor... format) {
		currunt.getChatModifier().resetColors();
		for(ChatColor c : format)
			if(!ChatColorUtils.isColor(c)){
				switch (c) {
					case BOLD:
						currunt.getChatModifier().setBold(true);
						continue;
					case ITALIC:
						currunt.getChatModifier().setItalic(true);
						continue;
					case MAGIC:
						currunt.getChatModifier().setRandom(true);
						continue;
					case STRIKETHROUGH:
						currunt.getChatModifier().setStrikethrough(true);
						continue;
					case UNDERLINE:
						currunt.getChatModifier().setUnderline(true);
						continue;
					default:
						continue;
				}
			}
	}

	public ChatTextBuilder setHoverAction(ChatHoverable hover) {
		currunt.getChatModifier().setHover(hover);
		return this;
	}

	public ChatTextBuilder setHoverAction(String hover) {
		currunt.getChatModifier().setHover(new ChatHoverable(EnumHoverAction.SHOW_TEXT, ChatSerializer.fromMessage(hover)));
		return this;
	}

	public ChatTextBuilder setHoverAction(EnumHoverAction action, String hover) {
		currunt.getChatModifier().setHover(new ChatHoverable(action, ChatSerializer.fromMessage(hover)));
		return this;
	}

	public ChatTextBuilder setHoverAction(EnumHoverAction action, IChatBaseComponent hover) {
		currunt.getChatModifier().setHover(new ChatHoverable(action, hover));
		return this;
	}
}
