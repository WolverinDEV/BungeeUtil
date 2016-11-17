package dev.wolveringer.bungeeutil.profile;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;

import com.google.common.base.Charsets;

public class SkinRequest {
	public String performGetRequest(URL url) throws IOException {
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

	protected HttpURLConnection createUrlConnection(URL url) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setConnectTimeout(2000);
		connection.setReadTimeout(2000);
		connection.setUseCaches(false);
		connection.setRequestMethod("GET");
		connection.setDefaultUseCaches(false);
		connection.addRequestProperty("User-Agent", "Mozilla/5.0");
		connection.addRequestProperty("Cache-Control", "no-cache, no-store, must-revalidate");
		connection.addRequestProperty("Pragma", "no-cache");
		return connection;
	}
}
