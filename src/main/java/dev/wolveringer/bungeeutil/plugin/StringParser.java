package dev.wolveringer.bungeeutil.plugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class StringParser {
	@AllArgsConstructor
	@NoArgsConstructor
	@Getter
	public static class CostumString {
		@Setter
		private String value;
		@Setter
		private String description;
		private HashMap<String, String> defaults = new HashMap<>();
		
		public CostumString(JSONObject object) {
			this.value = object.getString("value");
			this.description = object.has("description") ? object.getString("description") : "";
			if(object.has("defaults")){
				for(Iterator<String> it = object.getJSONObject("defaults").keys(); it.hasNext();){
					String key = it.next();
					defaults.put(key, object.getJSONObject("defaults").getString(key));
				}
			}
		}
		
		public String get(Map<String, String> values){
			HashSet<Entry<String, String>> keys = new HashSet<>(values.entrySet());
			if(defaults != null)
				keys.addAll(defaults.entrySet());
			
			String out = value;
			for(Entry<String, String> e : keys)
				out = out.replace("%"+e.getKey()+"%", e.getValue());
			return out;
		}
	}
	
	private HashMap<String, CostumString> strings = new HashMap<>();
	
	public void registerString(String key, CostumString str){
		strings.put(key, str);
	}
	
	public void parseStrings(JSONObject strings){
		for(String key : strings.keySet())
			this.strings.put(key, new CostumString(strings.getJSONObject(key)));
	}
	
	public String getString(String key, Map<String, String> values){
		if(!strings.containsKey(key))
			return "!"+key+"!";
		return strings.get(key).get(values);
	}
	
	public String getString(String original){
		Matcher m = Pattern.compile("\\$\\{([a-z-A-Z0-9_\\.]+)\\}").matcher(original);
		
		String out = "";
		int lindex = 0;
		while (m.find()) {
			out += original.substring(lindex, m.start());
			lindex = m.end();
			
			String key = m.group(1);
			String next = original.substring(m.end());
			
			int index = -1;
			HashMap<String, String> values = new HashMap<>();
			String current = null;
			boolean inbrace = false;
			
			while (index+1 < next.length()) {
				index++;
				if(!inbrace && next.charAt(index) == '(' && current == null){
					current = "";
					continue;
				}
				if(!inbrace && next.charAt(index) == ' ' && current.isEmpty())
					continue;
				
				if(!inbrace && (next.charAt(index) == ',' || next.charAt(index) == ')')){
					int eindex = current.indexOf('=');
					if(eindex == -1)
						values.put(current, "");
					else
						values.put(current.substring(0, eindex), current.substring(eindex+1));
					current = "";
					if(next.charAt(index) == ')')
						break;
					continue;
				}
				if(next.charAt(index) == '"'){
					inbrace = !inbrace;
					continue;
				}
				current += next.charAt(index);
			}
			lindex += index + 1;
			if(!current.isEmpty())
				throw new RuntimeException("Invalid arguments in: "+next);
			
			out += getString(key, values);
		}
		if(lindex < original.length())
			out += original.substring(lindex);
		return out;
	}
	
	public String getString(String original, int maxDeep){
		String last = "";
		String current = original;
		int deep = 0;
		while (last != (current = getString(current)) && deep++ < maxDeep) {
			last = current;
		}
		return current;
	}
}
