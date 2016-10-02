package dev.wolveringer.hastebin;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

public class HastebinPost {
	private StringBuilder text = new StringBuilder();
	private boolean changed = false;
	private String currunturl = "";
	public HastebinPost(String... text) {
		if(text.length>0)
			changed = true;
		for(String s : text)
			this.text.append(s + "\n");
	}

	public void addLine(String line){
		this.text.append(line+"\n");
		changed = true;
	}
	public String getText() {
		return text.toString();
	}
	public void setText(String text){
		changed = true;
		this.text = new StringBuilder(text);
	}
	
	public String getTextUrl(){
		if(changed){
			try{
				String out = performPostRequest(new URL("http://hastebin.com/documents"));
				JSONObject o = new JSONObject(out);
				if(!o.has("key")){
					System.err.println("Cant paste Document (Response: "+out+")");
					return "";
				}
				changed = false;
				return currunturl = "http://hastebin.com/"+o.getString("key");
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return currunturl;
	}
	
	private String performPostRequest(URL url) throws IOException {
		HttpURLConnection connection = createUrlConnection(url);

		connection.setRequestProperty("Content-Type", "text/plain");
		connection.setRequestProperty("Content-Length", String.valueOf(text.length()));

		OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
		writer.write(text.toString());
		writer.flush();

		InputStream inputStream = null;
		try{
			inputStream = connection.getInputStream();
			String result = IOUtils.toString(inputStream, Charset.forName("UTF-8"));
			return result;
		}catch (IOException e){
			IOUtils.closeQuietly(inputStream);
			inputStream = connection.getErrorStream();
			if(inputStream != null){
				String result = IOUtils.toString(inputStream, Charset.forName("UTF-8"));
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
		connection.setRequestMethod("POST");
		return connection;
	}
}
