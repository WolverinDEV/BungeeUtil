package dev.wolveringer.chat;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map.Entry;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import dev.wolveringer.chat.ChatColor.ChatColorUtils;

@SuppressWarnings("rawtypes")
public class ChatSerializer implements JsonDeserializer, JsonSerializer {

	private static final Gson deserelizer;

	static{
		GsonBuilder gsonbuilder = new GsonBuilder();
		gsonbuilder.registerTypeHierarchyAdapter(IChatBaseComponent.class, new ChatSerializer());
		gsonbuilder.registerTypeHierarchyAdapter(ChatModifier.class, new ChatModifierSerializer());
		gsonbuilder.registerTypeAdapterFactory(new ChatTypeAdapterFactory());
		deserelizer = gsonbuilder.create();
	}

	public static IChatBaseComponent toIChatBaseComponent(BaseComponent comp) {
		return fromJSON(ComponentSerializer.toString(comp));
	}

	public static String toJSONString(IChatBaseComponent ichatbasecomponent) {
		return deserelizer.toJson(ichatbasecomponent);
	}

	public static IChatBaseComponent fromJSON(String s) {
		return deserelizer.fromJson(s, IChatBaseComponent.class);
	}

	ChatSerializer() {
	}

	public static String toMessage(IChatBaseComponent component) {
		return toMessage(component, new ChatModifier());
	}

	public static String toMessage(IChatBaseComponent component, String last) {
		return toMessage(component, ChatColorUtils.getLastModifierColors(last));
	}

	public static String toMessage(IChatBaseComponent component, ChatModifier defaultModifier) {
		return toMessage(component, defaultModifier, 0);
	}

	@SuppressWarnings("unchecked")
	public static String toMessage(IChatBaseComponent component, ChatModifier defaultModifier, int start) {
		if(component == null)
			return "";
		StringBuilder out = new StringBuilder();
		String first = null;
		int count = 0;
		for(IChatBaseComponent c : (Iterable<IChatBaseComponent>) component){
			if(count < start){
				count++;
				continue;
			}
			if(first == null)
				first = c.getText();
			ChatModifier modi = c.getChatModifier();
			if(modi == null)
				modi = defaultModifier;
			out.append(modi.getColor() == null ? defaultModifier.getColor() == null ? ChatColor.WHITE : defaultModifier.getColor() : modi.getColor());
			if(modi.isBold())
				out.append(ChatColor.BOLD);
			if(modi.isItalic())
				out.append(ChatColor.ITALIC);
			if(modi.isUnderlined())
				out.append(ChatColor.UNDERLINE);
			if(modi.isBold())
				out.append(ChatColor.STRIKETHROUGH);
			if(modi.isObfuscated())
				out.append(ChatColor.MAGIC);
			out.append(c.getText());
		}
		String o = out.toString();
		if(defaultModifier.getColor() != null)
			o = o.replaceFirst("^(" + defaultModifier.getColor() + ")*", "");
		return o;
	}

	public static IChatBaseComponent fromMessage(String s) {
		ChatComponentText comp = new ChatComponentText("");
		if(!s.startsWith(""+dev.wolveringer.chat.ChatColor.ChatColorUtils.COLOR_CHAR+""))
			s = ""+dev.wolveringer.chat.ChatColor.ChatColorUtils.COLOR_CHAR+"f" + s;
		ChatColor color = ChatColor.WHITE;
		boolean bold = false;
		boolean italic = false;
		boolean underlined = false;
		boolean strikethrough = false;
		boolean obfuscated = false;
		String[] all = s.split(""+dev.wolveringer.chat.ChatColor.ChatColorUtils.COLOR_CHAR+"");
		for(int i = 0;i < all.length;i++){
			if(i + 1 < all.length){
				ChatColor c = ChatColor.getByChar(all[i + 1].charAt(0));
				if(c == ChatColor.BOLD)
					bold = true;
				else if(c == ChatColor.ITALIC)
					italic = true;
				else if(c == ChatColor.UNDERLINE)
					underlined = true;
				else if(c == ChatColor.STRIKETHROUGH)
					strikethrough = true;
				else if(c == ChatColor.MAGIC)
					obfuscated = true;
				else if(c == ChatColor.RESET){
					bold = false;
					italic = false;
					underlined = false;
					strikethrough = false;
					obfuscated = false;
					color = ChatColor.WHITE;
				}else{
					bold = false;
					italic = false;
					underlined = false;
					strikethrough = false;
					obfuscated = false;
					color = c;
				}
				String msg = all[i + 1].substring(1);
				if(msg.equalsIgnoreCase(""))
					continue;
				ChatComponentText sibling = new ChatComponentText(msg);
				sibling.getChatModifier().setColor(color);
				sibling.getChatModifier().setBold(bold);
				sibling.getChatModifier().setItalic(italic);
				sibling.getChatModifier().setRandom(obfuscated);
				sibling.getChatModifier().setUnderline(underlined);
				sibling.getChatModifier().setStrikethrough(strikethrough);
				comp.addSibling(sibling);
			}
		}
		return comp;
	}

	@Override
	public JsonElement serialize(Object arg0, Type arg1, JsonSerializationContext arg2) {
		return this.serialize((IChatBaseComponent) arg0, arg1, arg2);
	}

	@Override
	public Object deserialize(JsonElement in_obj, Type type, JsonDeserializationContext context) throws JsonParseException {
		if(in_obj.isJsonPrimitive()){
			return new ChatComponentText(in_obj.getAsString());
		}else if(!in_obj.isJsonObject()){
			if(in_obj.isJsonArray()){
				JsonArray in = in_obj.getAsJsonArray();
				IChatBaseComponent ichatbasecomponent = null;
				Iterator<JsonElement> iterator = in.iterator();
				while (iterator.hasNext()){
					JsonElement jsonelement1 = iterator.next();
					IChatBaseComponent ichatbasecomponent1 = (IChatBaseComponent) this.deserialize(jsonelement1, jsonelement1.getClass(), context);

					if(ichatbasecomponent == null){
						ichatbasecomponent = ichatbasecomponent1;
					}else{
						ichatbasecomponent.addSibling(ichatbasecomponent1);
					}
				}

				return ichatbasecomponent;
			}else{
				throw new JsonParseException("Don\'t know how to turn " + in_obj.toString() + " into a Component");
			}
		}else{
			JsonObject jsonobject = in_obj.getAsJsonObject();
			Object object;

			if(jsonobject.has("text")){
				object = new ChatComponentText(jsonobject.get("text").getAsString());
			}else{
				if(!jsonobject.has("translate")){
					throw new JsonParseException("Don\'t know how to turn " + in_obj.toString() + " into a Component");
				}

				String s = jsonobject.get("translate").getAsString();

				if(jsonobject.has("with")){
					JsonArray jsonarray1 = jsonobject.getAsJsonArray("with");
					Object[] aobject = new Object[jsonarray1.size()];

					for(int i = 0;i < aobject.length;++i){
						aobject[i] = this.deserialize(jsonarray1.get(i), type, context);
						if(aobject[i] instanceof ChatComponentText){
							ChatComponentText chatcomponenttext = (ChatComponentText) aobject[i];

							if(chatcomponenttext.getChatModifier().hasOwnStyle() && chatcomponenttext.getSiblings().isEmpty()){
								aobject[i] = chatcomponenttext.getRawText();
							}
						}
					}

					object = new ChatMessage(s, aobject);
				}else{
					object = new ChatMessage(s, new Object[0]);
				}
			}

			if(jsonobject.has("extra")){
				JsonArray jsonarray2 = jsonobject.getAsJsonArray("extra");

				if(jsonarray2.size() <= 0){
					throw new JsonParseException("Unexpected empty array of components");
				}

				for(int j = 0;j < jsonarray2.size();++j){
					((IChatBaseComponent) object).addSibling((IChatBaseComponent) this.deserialize(jsonarray2.get(j), type, context));
				}
			}

			((IChatBaseComponent) object).setChatModifier((ChatModifier) context.deserialize(in_obj, ChatModifier.class));
			return (IChatBaseComponent) object;
		}
	}

	@SuppressWarnings("unchecked")
	public JsonElement serialize(IChatBaseComponent in, Type type, JsonSerializationContext context) {
		if(in instanceof ChatComponentText && in.getChatModifier().hasOwnStyle() && in.getSiblings().isEmpty()){
			return new JsonPrimitive(((ChatComponentText) in).getRawText());
		}else{
			JsonObject out = new JsonObject();

			if(!in.getChatModifier().hasOwnStyle()){
				this.a(in.getChatModifier(), out, context);
			}

			if(!in.getSiblings().isEmpty()){
				JsonArray siblings = new JsonArray();
				Iterator<IChatBaseComponent> iterator = in.getSiblings().iterator();

				while (iterator.hasNext()){
					IChatBaseComponent ichatbasecomponent1 = iterator.next();
					siblings.add(this.serialize(ichatbasecomponent1, ichatbasecomponent1.getClass(), context));
				}
				out.add("extra", siblings);
				if("".equalsIgnoreCase(in.getText()))
					out.addProperty("text", "");
				else
					out.addProperty("text", in.getText());
			}else if(in instanceof ChatComponentText){
				out.addProperty("text", ((ChatComponentText) in).getRawText());
			}else{
				if(!(in instanceof ChatMessage))
					throw new IllegalArgumentException("Don\'t know how to serialize " + in + " as a Component");

				ChatMessage message = (ChatMessage) in;

				out.addProperty("translate", message.getLanguage());
				if(message.getData() != null && message.getData().length > 0){
					JsonArray jsonarray1 = new JsonArray();
					Object[] aobject = message.getData();
					int i = aobject.length;

					for(int j = 0;j < i;++j){
						Object object = aobject[j];

						if(object instanceof IChatBaseComponent){
							jsonarray1.add(this.serialize((IChatBaseComponent) object, (Type) object.getClass(), context));
						}else{
							jsonarray1.add(new JsonPrimitive(String.valueOf(object)));
						}
					}

					out.add("with", jsonarray1);
				}
			}

			return out;
		}
	}

	private void a(ChatModifier chatmodifier, JsonObject jsonobject, JsonSerializationContext jsonserializationcontext) {
		JsonElement jsonelement = jsonserializationcontext.serialize(chatmodifier);
		if(jsonelement.isJsonObject()){
			JsonObject jsonobject1 = (JsonObject) jsonelement;
			Iterator iterator = jsonobject1.entrySet().iterator();

			while (iterator.hasNext()){
				Entry entry = (Entry) iterator.next();

				jsonobject.add((String) entry.getKey(), (JsonElement) entry.getValue());
			}
		}
	}

	public static String toString(IChatBaseComponent comp, StringMethode m) {
		return ((ChatBaseComponent) comp).toString(m).replaceAll(", \\}", "").replaceAll(", \\]", "");
	}
}