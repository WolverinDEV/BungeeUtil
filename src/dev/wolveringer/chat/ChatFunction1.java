package dev.wolveringer.chat;

import com.google.common.base.Function;

@SuppressWarnings({ "rawtypes" })
final class ChatFunction1 implements Function {

	ChatFunction1() {}

	@Override
	public Object apply(Object object) {
		return ((IChatBaseComponent) object).iterator();
	}
}