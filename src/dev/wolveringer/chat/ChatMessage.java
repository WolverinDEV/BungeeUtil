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
	List<IChatBaseComponent> b = Lists.newArrayList();
	private final String d;
	private final Object[] e;
	private final Object f = new Object();

	public ChatMessage(String s, Object... aobject) {
		this.d = s;
		this.e = aobject;
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
		if(i >= this.e.length){
			throw new ChatMessageException(this, i);
		}else{
			Object object = this.e[i];
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

	protected void b(String s) {
		Matcher matcher = pattern.matcher(s);
		int i = 0;
		int j = 0;

		try{
			int k;
			for(;matcher.find(j);j = k){
				int l = matcher.start();
				k = matcher.end();
				if(l > j){
					ChatComponentText chatcomponenttext = new ChatComponentText(String.format(s.substring(j, l), new Object[0]));
					chatcomponenttext.getChatModifier().setChatModifier(this.getChatModifier());
					this.b.add(chatcomponenttext);
				}

				String s1 = matcher.group(2);
				String s2 = s.substring(l, k);

				if("%".equals(s1) && "%%".equals(s2)){
					ChatComponentText chatcomponenttext1 = new ChatComponentText("%");

					chatcomponenttext1.getChatModifier().setChatModifier(this.getChatModifier());
					this.b.add(chatcomponenttext1);
				}else{
					if(!"s".equals(s1)){
						throw new ChatMessageException(this, "Unsupported format: \'" + s2 + "\'");
					}

					String s3 = matcher.group(1);
					int i1 = s3 != null ? Integer.parseInt(s3) - 1 : i++;

					this.b.add(this.getText(i1));
				}
			}

			if(j < s.length()){
				ChatComponentText chatcomponenttext2 = new ChatComponentText(String.format(s.substring(j), new Object[0]));

				chatcomponenttext2.getChatModifier().setChatModifier(this.getChatModifier());
				this.b.add(chatcomponenttext2);
			}
		}catch (IllegalFormatException illegalformatexception){
			throw new ChatMessageException(this, illegalformatexception);
		}
	}

	@Override
	public String getText() {
		StringBuilder stringbuilder = new StringBuilder();
		Iterator<IChatBaseComponent> iterator = this.b.iterator();

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

			return Arrays.equals(this.e, chatmessage.e) && this.d.equals(chatmessage.d) && super.equals(object);
		}
	}

	@Override
	public ChatMessage addSibling() {
		Object[] aobject = new Object[this.e.length];

		for(int i = 0;i < this.e.length;++i){
			if(this.e[i] instanceof IChatBaseComponent){
				aobject[i] = ((IChatBaseComponent) this.e[i]).addSibling();
			}else{
				aobject[i] = this.e[i];
			}
		}

		ChatMessage chatmessage = new ChatMessage(this.d, aobject);

		chatmessage.setChatModifier(this.getChatModifier().clone());
		Iterator<IChatBaseComponent> iterator = this.getSiblings().iterator();

		while (iterator.hasNext())
			chatmessage.addSibling(iterator.next().addSibling());

		return chatmessage;
	}

	@Override
	public int hashCode() {
		int i = super.hashCode();

		i = 31 * i + this.d.hashCode();
		i = 31 * i + Arrays.hashCode(this.e);
		return i;
	}

	public String getLanguage() {
		return this.d;
	}

	@Override
	public Iterator<IChatBaseComponent> iterator() {
		return Iterators.concat(this.b.iterator(), this.texte.iterator());
	}

	public Object[] getData() {
		return this.e;
	}

	@Override
	public IChatBaseComponent setChatModifier(ChatModifier chatmodifier) {
		super.setChatModifier(chatmodifier);
		for(int j = 0;j < this.e.length;++j){
			if(this.e[j] instanceof IChatBaseComponent)
				((IChatBaseComponent) this.e[j]).getChatModifier().setChatModifier(this.getChatModifier());
		}
		if(false){
			Iterator<IChatBaseComponent> iterator = this.b.iterator();
			while (iterator.hasNext()){
				iterator.next().getChatModifier().setChatModifier(chatmodifier);
			}
		}

		return this;
	}

	@Override
	public String toString() {
		return "TranslatableComponent{key=\'" + this.d + '\'' + ", args=" + Arrays.toString(this.e) + ", siblings=" + this.texte + ", style=" + this.getChatModifier() + '}';
	}

	@Override
	public String toString(StringMethode m) {
		return null;
	}
}