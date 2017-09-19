package dev.wolveringer.chat;

import java.io.IOException;
import java.util.Map;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;


@SuppressWarnings({"rawtypes"})
class ChatTypeAdapter extends TypeAdapter {

	final Map a;
	final ChatTypeAdapterFactory b;

	ChatTypeAdapter(ChatTypeAdapterFactory chattypeadapterfactory, Map map) {
		this.b = chattypeadapterfactory;
		this.a = map;
	}

	@Override
	public Object read(JsonReader jsonreader) {
		try{
			if(jsonreader.peek() == JsonToken.NULL){
				try{
					jsonreader.nextNull();
				}catch (IOException e){
					e.printStackTrace();
				}
				return null;
			}else{
				return this.a.get(jsonreader.nextString());
			}
		}catch (IOException e){
			e.printStackTrace();
		}
		return jsonreader;
	}

	@Override
	public void write(JsonWriter jsonwriter, Object object) {
		if(object == null){
			try{
				jsonwriter.nullValue();
			}catch (IOException e){
				e.printStackTrace();
			}
		}else{
			try{
				jsonwriter.value(ChatTypeAdapterFactory.a(this.b, object));
			}catch (IOException e){
				e.printStackTrace();
			}
		}
	}
}