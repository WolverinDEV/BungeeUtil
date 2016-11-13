package dev.wolveringer.chat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

@SuppressWarnings({ "rawtypes" })
public abstract class ChatBaseComponent implements IChatBaseComponent {

	protected List<IChatBaseComponent> texte = Lists.newArrayList();

	protected ChatModifier modifier;

	public ChatBaseComponent() {
	}

	@Override
	public List getSiblings() {
		return this.texte;
	}

	public IChatBaseComponent addSibling(String text) {
		return this.addSibling(new ChatComponentText(text));
	}

	@Override
	public IChatBaseComponent addSibling(IChatBaseComponent ichatbasecomponent) {
		ichatbasecomponent.getChatModifier().setChatModifier(this.getChatModifier());
		this.texte.add(ichatbasecomponent);
		return this;
	}

	@Override
	public String getRawText() {
		StringBuilder stringbuilder = new StringBuilder();
		Iterator iterator = this.iterator();

		while (iterator.hasNext()){
			IChatBaseComponent ichatbasecomponent = (IChatBaseComponent) iterator.next();

			stringbuilder.append(ichatbasecomponent.getText());
		}
		return stringbuilder.toString();
	}

	@Override
	public boolean equals(Object object) {
		if(this == object){
			return true;
		}else if(!(object instanceof ChatBaseComponent)){
			return false;
		}else{
			ChatBaseComponent chatbasecomponent = (ChatBaseComponent) object;
			return this.texte.equals(chatbasecomponent.texte) && this.getChatModifier().equals(chatbasecomponent.getChatModifier());
		}
	}

	@Override
	public ChatModifier getChatModifier() {
		if(this.modifier == null){
			this.modifier = new ChatModifier();
			Iterator iterator = this.texte.iterator();

			while (iterator.hasNext()){
				IChatBaseComponent ichatbasecomponent = (IChatBaseComponent) iterator.next();

				ichatbasecomponent.getChatModifier().setChatModifier(this.modifier);
			}
		}

		return this.modifier;
	}

	@Override
	public int hashCode() {
		return 31 * this.modifier.hashCode() + this.texte.hashCode();
	}

	@Override
	public Iterator<IChatBaseComponent> iterator() {
		return Iterators.concat(Iterators.forArray(new ChatBaseComponent[] { this }), Iterators.forArray(this.texte.toArray(new IChatBaseComponent[this.texte.size()])));
	}

	@Override
	public IChatBaseComponent setChatModifier(ChatModifier chatmodifier) {
		this.modifier = chatmodifier;
		Iterator<IChatBaseComponent> iterator = this.texte.iterator();
		while (iterator.hasNext()){
			iterator.next().getChatModifier().setChatModifier(this.getChatModifier());
		}

		return this;
	}

	@Override
	public boolean hasClickListener() {
		Iterator<IChatBaseComponent> copms = iterator();
		for(IChatBaseComponent comp = copms.next();copms.hasNext();comp = copms.next()){
			if(comp.hasClickListener())
				return true;
		}
		return false;
	}

	@Override
	public ArrayList<String> getClickSignature() {
		ArrayList<String> strings = new ArrayList<String>();
		Iterator<IChatBaseComponent> copms = iterator();
		for(IChatBaseComponent comp = copms.next();copms.hasNext();comp = copms.next())
			if(comp.hasClickListener())
				strings.addAll(comp.getClickSignature());
		return strings;
	}

	@Override
	public ChatClickListener run(String s) {
		Iterator<IChatBaseComponent> copms = iterator();
		for(IChatBaseComponent comp = copms.next();copms.hasNext();comp = copms.next()){
			if(comp.hasClickListener()){
				ChatClickListener click = comp.run(s);
				if(click != null)
					return click;
			}
		}
		return null;
	}

}