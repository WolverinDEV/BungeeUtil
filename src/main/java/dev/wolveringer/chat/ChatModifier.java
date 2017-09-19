package dev.wolveringer.chat;

import net.md_5.bungee.api.ChatColor;

public class ChatModifier {

	private static final ChatModifier defaultModifier = new ChatStyleRoot();

	private ChatModifier modifier;

	private ChatColor color;
	private Boolean bold = false;

	private Boolean italic = false;

	private Boolean underlined = false;

	private Boolean strikethrough = false;

	private Boolean obfuscated = false;

	private ChatClickable click;

	private ChatHoverable hover;

	public ChatModifier() {
	}

	public ChatModifier(ChatColor color, Boolean bold, Boolean italic, Boolean underlined, Boolean strikethrough, Boolean obfuscated, ChatClickable click, ChatHoverable hover) {
		this.color = color;
		this.bold = bold;
		this.italic = italic;
		this.underlined = underlined;
		this.strikethrough = strikethrough;
		this.obfuscated = obfuscated;
		this.click = click;
		this.hover = hover;
	}

	public ChatColor getColor() {
		return this.color == null ? this.getModifier().getColor() : this.color;
	}

	public ChatModifier setChatModifier(ChatModifier chatmodifier) {
		this.modifier = chatmodifier;
		return this;
	}

	public boolean isBold() {
		return this.bold == null ? this.getModifier().isBold() : this.bold.booleanValue();
	}

	public boolean isItalic() {
		return this.italic == null ? this.getModifier().isItalic() : this.italic.booleanValue();
	}

	@Override
	public ChatModifier clone() {
		ChatModifier chatmodifier = new ChatModifier();
		chatmodifier.bold = this.bold;
		chatmodifier.italic = this.italic;
		chatmodifier.strikethrough = this.strikethrough;
		chatmodifier.underlined = this.underlined;
		chatmodifier.obfuscated = this.obfuscated;
		chatmodifier.color = this.color;
		chatmodifier.click = this.click;
		chatmodifier.hover = this.hover;
		chatmodifier.modifier = this.modifier;
		return chatmodifier;
	}

	public boolean isStrikethrough() {
		return this.strikethrough == null ? this.getModifier().isStrikethrough() : this.strikethrough.booleanValue();
	}

	public boolean isUnderlined() {
		return this.underlined == null ? this.getModifier().isUnderlined() : this.underlined.booleanValue();
	}

	@Override
	public boolean equals(Object object) {
		if(this == object){
			return true;
		}else if(!(object instanceof ChatModifier)){
			return false;
		}else{
			ChatModifier chatmodifier = (ChatModifier) object;
			boolean flag;

			if(this.isBold() == chatmodifier.isBold() && this.getColor() == chatmodifier.getColor() && this.isItalic() == chatmodifier.isItalic() && this.isObfuscated() == chatmodifier.isObfuscated() && this.isStrikethrough() == chatmodifier.isStrikethrough() && this.isUnderlined() == chatmodifier.isUnderlined()){
				label56: {
					if(this.getClick() != null){
						if(!this.getClick().equals(chatmodifier.getClick())){
							break label56;
						}
					}else if(chatmodifier.getClick() != null){
						break label56;
					}

					if(this.getHover() != null){
						if(!this.getHover().equals(chatmodifier.getHover())){
							break label56;
						}
					}else if(chatmodifier.getHover() != null){
						break label56;
					}

					flag = true;
					return flag;
				}
			}

			flag = false;
			return flag;
		}
	}

	public boolean isObfuscated() {
		return this.obfuscated == null ? this.getModifier().isObfuscated() : this.obfuscated.booleanValue();
	}

	public boolean hasOwnStyle() {
		return this.bold == null && this.italic == null && this.strikethrough == null && this.underlined == null && this.obfuscated == null && this.color == null && this.click == null && this.hover == null;
	}

	public ChatClickable getClick() {
		return this.click == null ? this.getModifier().getClick() : this.click;
	}

	@Override
	public int hashCode() {
		int i = this.color.hashCode();

		i = 31 * i + this.bold.hashCode();
		i = 31 * i + this.italic.hashCode();
		i = 31 * i + this.underlined.hashCode();
		i = 31 * i + this.strikethrough.hashCode();
		i = 31 * i + this.obfuscated.hashCode();
		i = 31 * i + this.click.hashCode();
		i = 31 * i + this.hover.hashCode();
		return i;
	}

	public ChatHoverable getHover() {
		return this.hover == null ? this.getModifier().getHover() : this.hover;
	}

	public ChatModifier getChatModifier() {
		ChatModifier chatmodifier = new ChatModifier();
		chatmodifier.setBold(Boolean.valueOf(this.isBold()));
		chatmodifier.setItalic(Boolean.valueOf(this.isItalic()));
		chatmodifier.setStrikethrough(Boolean.valueOf(this.isStrikethrough()));
		chatmodifier.setUnderline(Boolean.valueOf(this.isUnderlined()));
		chatmodifier.setRandom(Boolean.valueOf(this.isObfuscated()));
		chatmodifier.setColor(this.getColor());
		chatmodifier.setChatClickable(this.getClick());
		chatmodifier.setHover(this.getHover());
		return chatmodifier;
	}

	private ChatModifier getModifier() {
		return this.modifier == null ? defaultModifier : this.modifier;
	}

	public ChatModifier setBold(Boolean obool) {
		this.bold = obool;
		return this;
	}

	public ChatModifier setChatClickable(ChatClickable chatclickable) {
		this.click = chatclickable;
		return this;
	}

	public ChatModifier setHover(ChatHoverable hover) {
		this.hover = hover;
		return this;
	}

	public ChatModifier setColor(ChatColor enumchatformat) {
		this.color = enumchatformat;
		return this;
	}

	public ChatModifier setItalic(Boolean obool) {
		this.italic = obool;
		return this;
	}

	public ChatModifier setRandom(Boolean obool) {
		this.obfuscated = obool;
		return this;
	}

	public ChatModifier setStrikethrough(Boolean obool) {
		this.strikethrough = obool;
		return this;
	}

	public ChatModifier setUnderline(Boolean obool) {
		this.underlined = obool;
		return this;
	}

	public void resetColors() {
		bold = null;
		color = null;
		italic = null;
		obfuscated = null;
		strikethrough = null;
		underlined = null;
	}
	
	@Override
	public String toString() {
		return "Style{hasParent=" + (this.modifier != null) + ", color=" + this.color + ", bold=" + this.bold + ", italic=" + this.italic + ", underlined=" + this.underlined + ", strikethrough=" + this.strikethrough + ", obfuscated=" + this.obfuscated + ", clickEvent=" + this.getClick() + ", hoverEvent=" + this.getHover() + '}';
	}

	public String toString(StringMethode m) {
		if(m == StringMethode.LIST_NOT_NULL_VAR){
			StringBuilder builder = new StringBuilder();
			builder.append("Style");
			builder.append("{");
			builder.append("hasParent=");
			builder.append(this.modifier != null);
			builder.append(", ");
			if(this.color != null){
				builder.append("color=");
				builder.append(this.color);
				builder.append(", ");
			}
			if(this.bold != null){
				builder.append("bold=");
				builder.append(this.bold);
				builder.append(", ");
			}
			if(this.italic != null){
				builder.append("italic=");
				builder.append(this.italic);
				builder.append(", ");
			}
			if(this.underlined != null){
				builder.append("underlined=");
				builder.append(this.underlined);
				builder.append(", ");
			}
			if(this.strikethrough != null){
				builder.append("strikethrough=");
				builder.append(this.strikethrough);
				builder.append(", ");
			}
			if(this.obfuscated != null){
				builder.append("obfuscated=");
				builder.append(this.obfuscated);
				builder.append(", ");
			}
			if(this.click != null){
				builder.append("clickEvent=");
				builder.append(this.click);
				builder.append(", ");
			}
			if(this.hover != null){
				builder.append("hoverEvent=");
				builder.append(this.hover);
			}
			builder.append("}");
			return builder.toString();
		}
		return toString();
	}
}