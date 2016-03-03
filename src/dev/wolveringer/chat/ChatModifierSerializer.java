package dev.wolveringer.chat;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import net.md_5.bungee.api.ChatColor;

@SuppressWarnings({ "rawtypes" })
public class ChatModifierSerializer implements JsonDeserializer, JsonSerializer {

	public ChatModifierSerializer() {
	}

	public JsonElement serialize(Object obj, Type type, JsonSerializationContext jsonserializationcontext) {
		if(!(obj instanceof ChatModifier))
			throw new IllegalStateException();
		ChatModifier modifier = (ChatModifier) obj;
		if(modifier.hasOwnStyle()){
			return null;
		}else{
			JsonObject json = new JsonObject();

			if(modifier.isBold() == true){
				json.addProperty("bold", modifier.isBold());
			}

			if(modifier.isItalic() == true){
				json.addProperty("italic", modifier.isItalic());
			}

			if(modifier.isUnderlined() == true){
				json.addProperty("underlined", modifier.isUnderlined());
			}

			if(modifier.isStrikethrough() == true){
				json.addProperty("strikethrough", modifier.isStrikethrough());
			}

			if(modifier.isObfuscated() == true){
				json.addProperty("obfuscated", modifier.isObfuscated());
			}

			if(modifier.getColor() != null){
				json.add("color", jsonserializationcontext.serialize(modifier.getColor()));
			}

			JsonObject actionJson;

			if(modifier.getClick() != null){
				actionJson = new JsonObject();
				actionJson.addProperty("action", modifier.getClick().getAction().getActionName());
				actionJson.addProperty("value", modifier.getClick().getValue());
				json.add("clickEvent", actionJson);
			}

			if(modifier.getHover() != null){
				actionJson = new JsonObject();
				actionJson.addProperty("action", modifier.getHover().getAction().getActionName());
				actionJson.add("value", jsonserializationcontext.serialize(modifier.getHover().getValue()));
				json.add("hoverEvent", actionJson);
			}

			return json;
		}
	}

	public ChatModifier deserialize(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) {
		if(jsonelement.isJsonObject()){
			ChatModifier chatmodifier = new ChatModifier();
			JsonObject jsonobject = jsonelement.getAsJsonObject();

			if(jsonobject == null){
				return null;
			}else{
				if(jsonobject.has("bold")){
					chatmodifier.setBold(jsonobject.get("bold").getAsBoolean());
				}

				if(jsonobject.has("italic")){
					chatmodifier.setItalic(jsonobject.get("italic").getAsBoolean());
				}

				if(jsonobject.has("underlined")){
					chatmodifier.setUnderline(jsonobject.get("underlined").getAsBoolean());
				}

				if(jsonobject.has("strikethrough")){
					chatmodifier.setStrikethrough(jsonobject.get("strikethrough").getAsBoolean());
				}

				if(jsonobject.has("obfuscated")){
					chatmodifier.setRandom(jsonobject.get("obfuscated").getAsBoolean());
				}

				if(jsonobject.has("color")){
					chatmodifier.setColor((ChatColor) jsondeserializationcontext.deserialize(jsonobject.get("color"), ChatColor.class));
				}

				JsonObject jsonobject1;
				JsonPrimitive jsonprimitive;

				if(jsonobject.has("clickEvent")){
					jsonobject1 = jsonobject.getAsJsonObject("clickEvent");
					if(jsonobject1 != null){
						jsonprimitive = jsonobject1.getAsJsonPrimitive("action");
						EnumClickAction enumclickaction = jsonprimitive == null ? null : EnumClickAction.getActionFromName(jsonprimitive.getAsString());
						JsonPrimitive jsonprimitive1 = jsonobject1.getAsJsonPrimitive("value");
						String s = jsonprimitive1 == null ? null : jsonprimitive1.getAsString();

						if(enumclickaction != null && s != null && enumclickaction.hasExtraData()){
							chatmodifier.setChatClickable(new ChatClickable(enumclickaction, s));
						}
					}
				}

				if(jsonobject.has("hoverEvent")){
					jsonobject1 = jsonobject.getAsJsonObject("hoverEvent");
					if(jsonobject1 != null){
						jsonprimitive = jsonobject1.getAsJsonPrimitive("action");
						EnumHoverAction enumhoveraction = jsonprimitive == null ? null : EnumHoverAction.getActionFromName(jsonprimitive.getAsString());
						IChatBaseComponent ichatbasecomponent = (IChatBaseComponent) jsondeserializationcontext.deserialize(jsonobject1.get("value"), IChatBaseComponent.class);

						if(enumhoveraction != null && ichatbasecomponent != null && enumhoveraction.hasExtraData()){
							chatmodifier.setHover(new ChatHoverable(enumhoveraction, ichatbasecomponent));
						}
					}
				}

				return chatmodifier;
			}
		}else{
			return null;
		}
	}
}