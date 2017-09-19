package dev.wolveringer.chat;

import java.util.HashMap;
import java.util.Locale;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;


@SuppressWarnings({"rawtypes", "unchecked"})
public class ChatTypeAdapterFactory implements TypeAdapterFactory {

	static String a(ChatTypeAdapterFactory chattypeadapterfactory, Object object) {
		return chattypeadapterfactory.a(object);
	}

	public ChatTypeAdapterFactory() {
	}

	private String a(Object object) {
		return object instanceof Enum ? ((Enum) object).name().toLowerCase(Locale.US) : object.toString().toLowerCase(Locale.US);
	}

	@Override
	public TypeAdapter create(Gson gson, TypeToken typetoken) {
		Class oclass = typetoken.getRawType();
		if(!oclass.isEnum()){
			return null;
		}else{
			HashMap hashmap = new HashMap();
			Object[] aobject = oclass.getEnumConstants();
			int i = aobject.length;

			for(int j = 0;j < i;++j){
				Object object = aobject[j];

				hashmap.put(this.a(object), object);
			}

			return new ChatTypeAdapter(this, hashmap);
		}
	}
}