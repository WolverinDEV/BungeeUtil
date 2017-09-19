package dev.wolveringer.chat;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public interface IChatBaseComponent extends Iterable {

	List getSiblings();

	IChatBaseComponent addSibling(String s);

	IChatBaseComponent addSibling(IChatBaseComponent ichatbasecomponent);

	String getRawText();

	String getText();

	IChatBaseComponent addSibling();

	ChatModifier getChatModifier();

	IChatBaseComponent setChatModifier(ChatModifier chatmodifier);

	boolean hasClickListener();
	
	public ChatClickListener run(String s);
	
	public ArrayList<String> getClickSignature();
	
	public String toString(StringMethode m);
}