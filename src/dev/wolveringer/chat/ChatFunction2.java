package dev.wolveringer.chat;

import com.google.common.base.Function;

@SuppressWarnings("rawtypes")
final class ChatFunction2 implements Function {
	ChatFunction2() {}
	
	@Override
	public Object apply(Object object) {
		IChatBaseComponent obj = (IChatBaseComponent) object;
		IChatBaseComponent silber = obj.addSibling();
		silber.setChatModifier(silber.getChatModifier().getChatModifier());
		return silber;
	}
}