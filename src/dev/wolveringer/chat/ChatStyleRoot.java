package dev.wolveringer.chat;

import net.md_5.bungee.api.ChatColor;

final class ChatStyleRoot extends ChatModifier {

	ChatStyleRoot() {
	}

	@Override
	public ChatModifier setHover(ChatHoverable chathoverable) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ChatModifier setChatModifier(ChatModifier chatmodifier) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ChatModifier clone() {
		return this;
	}

	@Override
	public ChatModifier getChatModifier() {
		return super.getChatModifier();
	}

	@Override
	public ChatClickable getClick() {
		return null;
	}

	@Override
	public ChatColor getColor() {
		return ChatColor.WHITE;
	}

	@Override
	public ChatHoverable getHover() {
		return null;
	}

	@Override
	public ChatModifier setBold(Boolean obool) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ChatModifier setChatClickable(ChatClickable chatclickable) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ChatModifier setColor(ChatColor enumchatformat) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ChatModifier setItalic(Boolean obool) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ChatModifier setRandom(Boolean obool) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ChatModifier setStrikethrough(Boolean obool) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ChatModifier setUnderline(Boolean obool) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isBold() {
		return false;
	}

	@Override
	public boolean isItalic() {
		return false;
	}

	@Override
	public boolean isObfuscated() {
		return false;
	}

	@Override
	public boolean isStrikethrough() {
		return false;
	}

	@Override
	public boolean isUnderlined() {
		return false;
	}

	@Override
	public String toString() {
		return "Style.ROOT";
	}
}