package dev.wolveringer.chat;

import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

@SuppressWarnings({"unused","unchecked"})
public class ChatMessage extends ChatBaseComponent {

	public static final Pattern pattern = Pattern.compile("%(?:(\\d+)\\$)?([A-Za-z%]|$)");
	List<IChatBaseComponent> siblings = Lists.newArrayList();
	private final String translationKey;
	private final Object[] values;

	public ChatMessage(String s, Object... aobject) {
		this.translationKey = s;
		this.values = aobject;
		Object[] aobject1 = aobject;
		int i = aobject.length;

		for(int j = 0;j < i;++j){
			Object object = aobject1[j];
			if(object instanceof IChatBaseComponent){
				((IChatBaseComponent) object).getChatModifier().setChatModifier(this.getChatModifier());
			}
		}
	}

	private IChatBaseComponent getText(int i){
		if(i >= this.values.length){
			throw new ChatMessageException(this, i);
		}else{
			Object object = this.values[i];
			Object object1;

			if(object instanceof IChatBaseComponent){
				object1 = object;
			}else{
				object1 = new ChatComponentText(object == null ? "null" : object.toString());
				((IChatBaseComponent) object1).getChatModifier().setChatModifier(this.getChatModifier());
			}

			return (IChatBaseComponent) object1;
		}
	}

	@Override
	public String getText() {
		StringBuilder stringbuilder = new StringBuilder();
		Iterator<IChatBaseComponent> iterator = this.siblings.iterator();

		while (iterator.hasNext())
			stringbuilder.append(iterator.next().getText());

		return stringbuilder.toString();
	}

	@Override
	public boolean equals(Object object) {
		if(this == object){
			return true;
		}else if(!(object instanceof ChatMessage)){
			return false;
		}else{
			ChatMessage chatmessage = (ChatMessage) object;

			return Arrays.equals(this.values, chatmessage.values) && this.translationKey.equals(chatmessage.translationKey) && super.equals(object);
		}
	}

	@Override
	public ChatMessage addSibling() {
		Object[] aobject = new Object[this.values.length];

		for(int i = 0;i < this.values.length;++i){
			if(this.values[i] instanceof IChatBaseComponent){
				aobject[i] = ((IChatBaseComponent) this.values[i]).addSibling();
			}else{
				aobject[i] = this.values[i];
			}
		}

		ChatMessage chatmessage = new ChatMessage(this.translationKey, aobject);

		chatmessage.setChatModifier(this.getChatModifier().clone());
		Iterator<IChatBaseComponent> iterator = this.getSiblings().iterator();

		while (iterator.hasNext())
			chatmessage.addSibling(iterator.next().addSibling());

		return chatmessage;
	}

	@Override
	public int hashCode() {
		int i = super.hashCode();

		i = 31 * i + this.translationKey.hashCode();
		i = 31 * i + Arrays.hashCode(this.values);
		return i;
	}

	public String getLanguage() {
		return this.translationKey;
	}

	@Override
	public Iterator<IChatBaseComponent> iterator() {
		return Iterators.concat(this.siblings.iterator(), this.texte.iterator());
	}

	public Object[] getData() {
		return this.values;
	}

	@Override
	public IChatBaseComponent setChatModifier(ChatModifier chatmodifier) {
		super.setChatModifier(chatmodifier);
		for(int j = 0;j < this.values.length;++j){
			if(this.values[j] instanceof IChatBaseComponent)
				((IChatBaseComponent) this.values[j]).getChatModifier().setChatModifier(this.getChatModifier());
		}
		if(false){
			Iterator<IChatBaseComponent> iterator = this.siblings.iterator();
			while (iterator.hasNext()){
				iterator.next().getChatModifier().setChatModifier(chatmodifier);
			}
		}

		return this;
	}

	@Override
	public String toString() {
		return "TranslatableComponent{key=\'" + this.translationKey + '\'' + ", args=" + Arrays.toString(this.values) + ", siblings=" + this.texte + ", style=" + this.getChatModifier() + '}';
	}

	@Override
	public String toString(StringMethode m) {
		return null;
	}
}