package dev.wolveringer.bungeeutil.hastebin;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.base.Charsets;

public class HastebinDocument {
	private static final String BASE_URL = "http://hastebin.com/raw/";
	private String identifier;
	private ArrayList<String> lines = new ArrayList<>();

	public HastebinDocument(String identifier) {
		if(identifier.contains("/"))
			this.identifier = identifier.substring(identifier.lastIndexOf("/")+1);
		else
			this.identifier = identifier;
	}
	 public void load() {
		try{
			lines = new ArrayList<>(Arrays.asList(performGetRequest(new URL(BASE_URL + identifier)).split("\n")));
			if(lines.size() == 1)
				if(lines.get(0).startsWith("{")){
					try{
						JSONObject obj = new JSONObject(lines.get(0));
						if(obj.has("message"))
							throw new DocumentNotFoundException(obj.getString("message"));
					}catch (JSONException e){
						e.printStackTrace();
					}
				}
		}catch (IOException ex){
			ex.printStackTrace();
		}
	}

	private String performGetRequest(URL url) throws IOException {
		HttpURLConnection connection = createUrlConnection(url);
		InputStream inputStream = null;
		try{
			inputStream = connection.getInputStream();
			String result = IOUtils.toString(inputStream, Charsets.UTF_8);
			return result;
		}catch (IOException e){
			IOUtils.closeQuietly(inputStream);
			inputStream = connection.getErrorStream();
			if(inputStream != null){
				String result = IOUtils.toString(inputStream, Charsets.UTF_8);
				return result;
			}
			throw e;
		}finally{
			IOUtils.closeQuietly(inputStream);
		}
	}

	private HttpURLConnection createUrlConnection(URL url) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setConnectTimeout(15000);
		connection.setReadTimeout(15000);
		connection.setDoOutput(true);
		connection.setUseCaches(false);
		connection.setRequestMethod("GET");
		return connection;
	}
	public ArrayList<String> getLines() {
		return this.lines;
	}
	public String getIdentifier() {
		return this.identifier;
	}
}
