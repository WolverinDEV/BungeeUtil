package dev.wolveringer.chat;

import java.util.Iterator;

import net.md_5.bungee.api.ChatColor;

public class ChatComponentText extends ChatBaseComponent{

	private final String message;

	public ChatComponentText(String s) {
		this.message = s;
	}

	@Override
	public String getText() {
		return this.message;
	}

	@Override
	public boolean equals(Object object) {
		if(this == object){
			return true;
		}else if(!(object instanceof ChatComponentText)){
			return false;
		}else{
			ChatComponentText chatcomponenttext = (ChatComponentText) object;
			return this.message.equals(chatcomponenttext.getText()) && super.equals(object);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public IChatBaseComponent addSibling() {
		ChatComponentText chatcomponenttext = new ChatComponentText(this.message);

		chatcomponenttext.setChatModifier(this.getChatModifier().clone());
		Iterator<IChatBaseComponent> iterator = this.getSiblings().iterator();

		while (iterator.hasNext()){
			chatcomponenttext.addSibling(iterator.next().addSibling());
		}

		return chatcomponenttext;
	}
	@Override
	public String toString() {
		return toString(StringMethode.LIST_VAR);
	}
	
	public String toString(StringMethode m) {
		if(m == StringMethode.LIST_VAR)
			return "TextComponent{text=\'" + this.message + '\'' + ", siblings=" + this.texte + ", style=" + this.getChatModifier() + '}';
		else if(m == StringMethode.LIST_NOT_NULL_VAR){
			StringBuilder builder = new StringBuilder();
			builder.append("TextComponent{");
			if(this.message != null  && !"".equalsIgnoreCase(this.message)){
				builder.append("text=");
				builder.append(this.message);
				builder.append(", ");
			}
			if(this.modifier != null){
				builder.append("style=");
				builder.append(this.modifier.toString(m));
				builder.append(", ");
			}
			if(this.texte != null){
				builder.append("siblings=[");
				for(IChatBaseComponent c : texte)
					builder.append(c.toString(m)+", ");
				builder.append("]");
			}
			return builder.append("}").toString().replaceAll(", \\}", "").replaceAll(", \\]", "");
		}else if(m == StringMethode.MESSAGE_COLORED){
			return ChatSerializer.toMessage(this, ""+dev.wolveringer.chat.ChatColor.ChatColorUtils.COLOR_CHAR+"f");
		}else if(m == StringMethode.MESSAGE_UNCOLORED){
			return ChatColor.stripColor(ChatSerializer.toMessage(this, ""+dev.wolveringer.chat.ChatColor.ChatColorUtils.COLOR_CHAR+"f"));
		}else if(m == StringMethode.JSON){
			return ChatSerializer.toJSONString(this);
		}
		return "";
	}
}