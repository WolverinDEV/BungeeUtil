package dev.wolveringer.bungeeutil.hastebin;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class HastebinPost {
	private StringBuilder text = new StringBuilder();
	private boolean changed = false;
	private String currunturl = "";
	public HastebinPost(String... text) {
		if(text.length>0) {
			this.changed = true;
		}
		for(String s : text) {
			this.text.append(s + "\n");
		}
	}

	public void addLine(String line){
		this.text.append(line+"\n");
		this.changed = true;
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
	public String getText() {
		return this.text.toString();
	}

	public String getTextUrl(){
		if(this.changed){
			String out = "unknwon";
			try{
				out = this.performPostRequest(new URL("http://hastebin.com/documents"));
				JSONObject o = new JSONObject(out);
				if(!o.has("key")){
					System.err.println("Cant paste Document (Response: "+out+")");
					return "";
				}
				this.changed = false;
				return this.currunturl = "http://hastebin.com/"+o.getString("key");
			} catch(JSONException e){
				System.err.println("Cant parse json '"+out+"'");
				e.printStackTrace();
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		return this.currunturl;
	}

	private String performPostRequest(URL url) throws IOException {
		HttpURLConnection connection = this.createUrlConnection(url);

		connection.setRequestProperty("Content-Type", "text/plain");
		connection.setRequestProperty("Content-Length", String.valueOf(this.text.length()));

		OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
		writer.write(this.text.toString());
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

	public void setText(String text){
		this.changed = true;
		this.text = new StringBuilder(text);
	}
}
