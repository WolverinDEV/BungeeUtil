package dev.wolveringer.updater;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import org.json.JSONArray;
import org.json.JSONObject;

import dev.wolveringer.BungeeUtil.Main;

public class Updater {
	String url;
	JSONObject data;
	long last;
	public Updater(String url) {
		this.url = url;
	}

	public boolean check() {
		updateData();
		Main.sendMessage("§aChecking for Plugin updates");
		if(data == null)
			throw new NullPointerException("HTTP Data is null. Invpoke getData() first");
		JSONObject plugins = data.getJSONObject("plugins");
		if(plugins.has(Main.getMain().getDescription().getName())){
			if(check(Main.getMain().getDescription().getName(), plugins.getJSONObject(Main.getMain().getDescription().getName()))){
				Main.sendMessage("§aRestart for install");
				BungeeCord.getInstance().stop();
				return true;
			}else
				Main.sendMessage("§aNo plugin update found! Your version is alredy the newest! ("+plugins.getJSONObject(Main.getMain().getDescription().getName()).getString("version")+")");
		}else
			Main.sendMessage("§cPlugin not found!");
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
		if(ProxyServer.getInstance().getPluginManager().getPlugin(name) == null){
			if(!f.exists()){
				downloadPlugin(url, f, name);
				return true;
			}else{
				Main.sendMessage("§eDas Plugin §6" + name + " §eist nicht geladen.\n§cDie Version konnte nicht §berpr§ft werden.");
				return false;
			}
		}else
			return checkVersion(ProxyServer.getInstance().getPluginManager().getPlugin(name), version, url, f, name);
	}

	private boolean checkVersion(Plugin plugin, String version, String url2, File f, String name) {
		if(checkVersion(plugin.getDescription().getVersion(), version)){
			Main.sendMessage("§aUpdate das Plugin §6" + name + " §avon der Verion §e" + plugin.getDescription().getVersion() + " §aauf die Version §1" + version + "§a ab.");
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

	public void downloadPlugin(String url, File f, String name) {
		Main.sendMessage("§aStarte Download von dem Plugin §6" + name);
		if(f.exists())
			f.delete();
		try{
			f.createNewFile();
		}catch (IOException e){
			e.printStackTrace();
		}
		try{
			BufferedInputStream in = null;
			FileOutputStream fout = null;
			try{
				in = new BufferedInputStream(new URL(url).openStream());
				fout = new FileOutputStream(f);
				final byte data[] = new byte[1024];
				int count;
				while ((count = in.read(data, 0, 1024)) != -1){
					fout.write(data, 0, count);
				}
				Main.sendMessage("§aDownloaden von dem Plugin §6" + name + " §adone");
			}catch (Exception e){
				e.printStackTrace();
				Main.sendMessage("§cError beim Downloaden vom Plugin §4" + name);
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
			Main.sendMessage("§cError beim Downloaden vom Plugin §4" + name);
		}
	}

	public JSONObject getData() {
		updateData();
		return data;
	}

	public Updater loadData() {
		last = System.currentTimeMillis();
		Main.sendMessage("§aGetting Update data!");
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
			if(o.has("whitelist")){
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
				BungeeCord.getInstance().getConsole().sendMessage("§aYour Host: §e" + host);
				BungeeCord.getInstance().getConsole().sendMessage("§aYour Host-Adress: §e" + hostadress);
				BungeeCord.getInstance().getConsole().sendMessage("§cBoth Host's are not whitelisted.");
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
