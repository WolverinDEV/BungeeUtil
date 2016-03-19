package dev.wolveringer.updater;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarInputStream;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import org.json.JSONArray;
import org.json.JSONObject;

import dev.wolveringer.BungeeUtil.Main;
import dev.wolveringer.BungeeUtil.configuration.Configuration;
import dev.wolveringer.chat.ChatColor.ChatColorUtils;
import dev.wolveringer.util.MathUtil;

public class Updater {
	
	private String url;
	private JSONObject data;
	private long last;
	
	public Updater(String url) {
		this.url = url;
	}

	public boolean check() {
		updateData();
		Main.sendMessage(ChatColorUtils.COLOR_CHAR+"aChecking for Plugin updates");
		if(data == null)
			throw new NullPointerException("HTTP Data is null. Invpoke getData() first");
		JSONObject plugins = data.getJSONObject("plugins");
		if(plugins.has(Main.getMain().getDescription().getName())){
			if(check(Main.getMain().getDescription().getName(), plugins.getJSONObject(Main.getMain().getDescription().getName()))){
				BungeeCord.getInstance().stop();
				return true;
			}else{
				if(Main.getMain().getDescription().getVersion().equalsIgnoreCase(plugins.getJSONObject(Main.getMain().getDescription().getName()).getString("version")))
					Main.sendMessage(ChatColorUtils.COLOR_CHAR+"aNo plugin update found! Your version is alredy the newest! ("+plugins.getJSONObject(Main.getMain().getDescription().getName()).getString("version")+")");
				else
					Main.sendMessage(ChatColorUtils.COLOR_CHAR+"aYou plugin version is newer than the currunt public version. I think i´m a dev build... All bugs will be ignored");
			}
			}else
				Main.sendMessage(ChatColorUtils.COLOR_CHAR+"cPlugin not found!");
		return false;
	}

	public String getNewestVersion(){
		updateData();
		if(data.has(Main.getMain().getDescription().getName())){
			return data.getJSONObject(Main.getMain().getDescription().getName()).getString("version");
		}
		return "underknown";
	}
	public boolean isNewstVersion(){
		updateData();
		return !checkVersion(Main.getMain().getDescription().getVersion(), getNewestVersion());
	}
	
	private boolean check(String name, JSONObject plugin) {
		String url = plugin.getString("url");
		String version = plugin.getString("version");
		File f = new File(Main.getMain().getDataFolder().getAbsoluteFile().getAbsolutePath() + ".jar");
		return checkVersion(plugin,ProxyServer.getInstance().getPluginManager().getPlugin(name), version, url, f, name);
	}
	
	private void editChangeLog(JSONObject obj,String name,String version){
		JSONArray changes = obj.getJSONArray("changeLog");
		for(int i = 0;i<changes.length();i++){
			JSONObject ver = changes.getJSONObject(i);
			if(ver.getString("version").equalsIgnoreCase(version)){
				Configuration.setVersionFeature(Arrays.asList(ver.getString("changes").split("<br>")));
			}
		}
	}

	private boolean checkVersion(JSONObject root,Plugin plugin, String version, String url2, File f, String name) {
		if(checkVersion(plugin.getDescription().getVersion(), version)){
			Main.sendMessage(ChatColorUtils.COLOR_CHAR+"aUpdate das Plugin "+ChatColorUtils.COLOR_CHAR+"6" + name + " "+ChatColorUtils.COLOR_CHAR+"avon der Verion "+ChatColorUtils.COLOR_CHAR+"e" + plugin.getDescription().getVersion() + " "+ChatColorUtils.COLOR_CHAR+"aauf die Version "+ChatColorUtils.COLOR_CHAR+"1" + version + ChatColorUtils.COLOR_CHAR+"a ab.");
			editChangeLog(root,name,version);
			downloadPlugin(url2, f, name);
			return true;
		}
		return false;
	}

	private boolean checkVersion(String version1, String version2) {
		String[] v1 = version1.split("\\.");
		String[] v2 = version2.split("\\.");
		for(int i = 0;i < Collections.max(Arrays.asList(v1.length, v2.length));i++){
			if(i + 1 < v1.length && i + 1 >= v2.length)
				return !isNotZero(Arrays.copyOfRange(v1, i + 1, v1.length));
			else if(i + 1 < v2.length && i + 1 >= v1.length)
				return isNotZero(Arrays.copyOfRange(v2, i + 1, v2.length));
			else if(Integer.parseInt(v1[i]) < Integer.parseInt(v2[i]))
				return true;
		}
		return false;
	}

	private boolean isNotZero(String[] x) {
		for(String s : x)
			if(Integer.parseInt(s) != 0)
				return true;
		return false;
	}

	private void downloadPlugin(String url, File f, String name) {
		Main.sendMessage(ChatColorUtils.COLOR_CHAR+"aStarte Download von dem Plugin "+ChatColorUtils.COLOR_CHAR+"6" + name);
		try{
			Main.setInformation("§aDownloading update §7[§e000%§7]");
			BufferedInputStream in = null;
			FileOutputStream fout = null;
			try{
				URLConnection com = new URL(url).openConnection();
				int fileLength = com.getContentLength();
				in = new BufferedInputStream(com.getInputStream());
				File df;
				if(f.exists()){
					fout = new FileOutputStream(df = new File(f.toString()+".download"));
					df.deleteOnExit();
				}else
					fout = new FileOutputStream(df = f);
				final byte data[] = new byte[1024];
				int count;
				int readed = 0;
				while (true){
					count = in.read(data, 0, 1024);
					if(count == -1)
						break;
					fout.write(data, 0, count);
					readed+=count;
					String p = "000"+MathUtil.calculatePercent(readed, fileLength);
					p = p.substring(0, p.indexOf("."));
					p = p.substring(p.length()-3, p.length());
					Main.setInformation("§aDownloading update §7[§e"+p+"%§7]");
				}
				fout.close();
				in.close();
				Main.setInformation("§aDownload done!");
				if(f.exists())
					f.delete();
				try{
					f.createNewFile();
				}catch (IOException e){
					e.printStackTrace();
				}
				Main.sendMessage(ChatColorUtils.COLOR_CHAR+"aDownloaden von dem Plugin "+ChatColorUtils.COLOR_CHAR+"6" + name + " "+ChatColorUtils.COLOR_CHAR+"aabgeshlossen");
				Main.setInformation("§aCheck update for errors!");
				Main.sendMessage(ChatColorUtils.COLOR_CHAR+"aTeste update auf fehler...");
				try{
					JarInputStream is = new JarInputStream(new FileInputStream(df));
					while(null != is.getNextJarEntry()) {}
					is.close();
				}catch(Exception e){
					Main.sendMessage(ChatColorUtils.COLOR_CHAR+"cError beim überprüfen des Updates (Message: "+e.getLocalizedMessage()+")");
					Main.sendMessage(ChatColorUtils.COLOR_CHAR+"cLösche das update");
					try{
						df.delete();
					}catch(Exception ex){}
					Main.sendMessage(ChatColorUtils.COLOR_CHAR+"cUpdate rückgängig gemacht.");
					Main.sendMessage(ChatColorUtils.COLOR_CHAR+"aRestarte den BungeeCord");
					return;
				}
				Main.sendMessage(ChatColorUtils.COLOR_CHAR+"aTeste erfolgreich abgeschlossen.");
				Main.sendMessage(ChatColorUtils.COLOR_CHAR+"aUpdate plugin");
				Main.setInformation("§aInstalling update");
				if(!f.delete()){
					Main.sendMessage(ChatColorUtils.COLOR_CHAR+"6Konnte das alte plugin nicht löschen. Überschreibe das alte Plugin.");
				}
				f.createNewFile();
				FileInputStream fis = new FileInputStream(df);
				FileOutputStream fos = new FileOutputStream(f);
				while ((count = fis.read(data, 0, 1024)) != -1){
					fos.write(data, 0, count);
				}
				fis.close();
				fos.close();
				if(!df.delete())
					Main.sendMessage(ChatColorUtils.COLOR_CHAR+"6Konnte junk Files nicht löschen..");
				Main.sendMessage(ChatColorUtils.COLOR_CHAR+"aRestarte den BungeeCord");
				Main.setInformation("§aUpdate installed!");
			}catch (Exception e){
				e.printStackTrace();
				Main.sendMessage(ChatColorUtils.COLOR_CHAR+"cError beim Downloaden vom Plugin "+ChatColorUtils.COLOR_CHAR+"4" + name);
			}finally{
				if(in != null){
					in.close();
				}
				if(fout != null){
					fout.close();
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			Main.sendMessage(ChatColorUtils.COLOR_CHAR+"cError beim Downloaden vom Plugin "+ChatColorUtils.COLOR_CHAR+"4" + name);
		}
	}

	public JSONObject getData() {
		updateData();
		return data;
	}

	public Updater loadData() {
		last = System.currentTimeMillis();
		Main.sendMessage(ChatColorUtils.COLOR_CHAR+"aGetting Update data!");
		try{
			URL i = new URL(url);
			HttpURLConnection c = (HttpURLConnection) i.openConnection();
			c.setRequestMethod("GET");
			BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null){
				response.append(inputLine);
			}
			in.close();
			this.data = new JSONObject(response.toString());
		}catch (Exception e){
			e.printStackTrace();
		}
		return this;
	}

	@SuppressWarnings("deprecation")
	public boolean whiteList() {
		try{
			if(data == null)
				throw new NullPointerException("HTTP Data is null. Invpoke getData() first");
		}catch (Exception e){
			e.printStackTrace();
			return true;
		}
		JSONObject plugins = data.getJSONObject("plugins");
		if(plugins.has(Main.getMain().getDescription().getName())){
			JSONObject o = plugins.getJSONObject(Main.getMain().getDescription().getName());
			if(o.has("whitelist")){ //TODO kick out. not longer needed
				JSONArray a = o.getJSONArray("whitelist");
				String host = "null";
				String hostadress = "null";
				try{
					host = InetAddress.getLocalHost().getHostName();
					hostadress = InetAddress.getLocalHost().getHostAddress();
					if(host.equalsIgnoreCase("test-PC"))
						return true;
				}catch (UnknownHostException e){
					e.printStackTrace();
				}
				for(int i = 0;i < a.length();i++){
					if(a.get(i).toString().equalsIgnoreCase(host) || a.get(i).toString().equalsIgnoreCase(hostadress))
						return true;
				}
				BungeeCord.getInstance().getConsole().sendMessage(ChatColorUtils.COLOR_CHAR+"aYour Host: "+ChatColorUtils.COLOR_CHAR+"e" + host);
				BungeeCord.getInstance().getConsole().sendMessage(ChatColorUtils.COLOR_CHAR+"aYour Host-Adress: "+ChatColorUtils.COLOR_CHAR+"e" + hostadress);
				BungeeCord.getInstance().getConsole().sendMessage(ChatColorUtils.COLOR_CHAR+"cBoth Host's are not whitelisted.");
				return false;
			}
		}
		return true;
	}
	
	public void updateData(){
		if(System.currentTimeMillis()-last>TimeUnit.MINUTES.toMillis(10))
			loadData();
	}
}
