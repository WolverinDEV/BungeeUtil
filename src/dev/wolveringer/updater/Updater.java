package dev.wolveringer.updater;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
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
		Main.sendMessage(ChatColorUtils.COLOR_CHAR + "aChecking for Plugin updates");
		if (data == null) throw new NullPointerException("HTTP Data is null. Invpoke getData() first");
		JSONObject plugins = data.getJSONObject("plugins");
		if (plugins.has(Main.getMain().getDescription().getName())) {
			if (check(Main.getMain().getDescription().getName(), plugins.getJSONObject(Main.getMain().getDescription().getName()))) {
				BungeeCord.getInstance().stop();
				return true;
			}
			else {
				if (Main.getMain().getDescription().getVersion().equalsIgnoreCase(plugins.getJSONObject(Main.getMain().getDescription().getName()).getString("version"))) Main.sendMessage(ChatColorUtils.COLOR_CHAR + "aNo plugin update found! Your version is alredy the newest! (" + plugins.getJSONObject(Main.getMain().getDescription().getName()).getString("version") + ")");
				else Main.sendMessage(ChatColorUtils.COLOR_CHAR + "aYou plugin version is newer than the currunt public version. I think i'm a dev build... All bugs will be ignored");
			}
		}
		else Main.sendMessage(ChatColorUtils.COLOR_CHAR + "cPlugin not found!");
		return false;
	}
	
	public String getNewestVersion() {
		updateData();
		if (data.has(Main.getMain().getDescription().getName())) { return data.getJSONObject(Main.getMain().getDescription().getName()).getString("version"); }
		return "underknown";
	}
	
	public boolean isNewstVersion() {
		updateData();
		return !checkVersion(Main.getMain().getDescription().getVersion(), getNewestVersion());
	}
	
	private boolean check(String name, JSONObject plugin) {
		String url = plugin.getString("url");
		String version = plugin.getString("version");
		File f = new File(Main.getMain().getDataFolder().getAbsoluteFile().getAbsolutePath() + ".jar");
		return checkVersion(plugin, ProxyServer.getInstance().getPluginManager().getPlugin(name), version, url, f, name);
	}
	
	private void editChangeLog(JSONObject obj, String name, String version) {
		JSONArray changes = obj.getJSONArray("changeLog");
		for (int i = 0; i < changes.length(); i++) {
			JSONObject ver = changes.getJSONObject(i);
			if (ver.getString("version").equalsIgnoreCase(version)) {
				Configuration.setVersionFeature(Arrays.asList(ver.getString("changes").split("<br>")));
			}
		}
	}
	
	private boolean checkVersion(JSONObject root, Plugin plugin, String version, String url2, File f, String name) {
		if (checkVersion(plugin.getDescription().getVersion(), version)) {
			if (Configuration.isUpdaterActive()) {
				Main.sendMessage(ChatColorUtils.COLOR_CHAR + "aUpdating the plugin" + ChatColorUtils.COLOR_CHAR + "6" + name + " " + ChatColorUtils.COLOR_CHAR + "afrom version " + ChatColorUtils.COLOR_CHAR + "e" + plugin.getDescription().getVersion() + " " + ChatColorUtils.COLOR_CHAR + "ato version " + ChatColorUtils.COLOR_CHAR + "1" + version + ChatColorUtils.COLOR_CHAR + "a ab.");
				editChangeLog(root, name, version);
				downloadPlugin(url2, f, name);
			}
			else {
				Main.sendMessage(ChatColorUtils.COLOR_CHAR + "aBungeeUtils found an update. New version: " + ChatColorUtils.COLOR_CHAR + "6" + version + ChatColorUtils.COLOR_CHAR + "a.");
				return false;
			}
			return true;
		}
		return false;
	}
	
	private boolean checkVersion(String version1, String version2) {
		return Long.parseLong(version1.replaceAll("\\.", "")) > Long.parseLong(version2.replaceAll("\\.", ""));
	}
	
	private void downloadPlugin(String url, File f, String name) {
		Main.sendMessage(ChatColorUtils.COLOR_CHAR + "aStarting downloading " + ChatColorUtils.COLOR_CHAR + "6" + name);
		try {
			Main.setInformation(ChatColorUtils.COLOR_CHAR + "aDownloading update " + ChatColorUtils.COLOR_CHAR + "7[" + ChatColorUtils.COLOR_CHAR + "e000%" + ChatColorUtils.COLOR_CHAR + "7]");
			BufferedInputStream in = null;
			FileOutputStream fout = null;
			try {
				URLConnection com = new URL(url).openConnection();
				int fileLength = com.getContentLength();
				in = new BufferedInputStream(com.getInputStream());
				File df;
				if (f.exists()) {
					fout = new FileOutputStream(df = new File(f.toString() + ".download"));
					df.deleteOnExit();
				}
				else fout = new FileOutputStream(df = f);
				final byte data[] = new byte[1024];
				int count;
				int readed = 0;
				while (true) {
					count = in.read(data, 0, 1024);
					if (count == -1) break;
					fout.write(data, 0, count);
					readed += count;
					String p = "000" + MathUtil.calculatePercent(readed, fileLength);
					p = p.substring(0, p.indexOf("."));
					p = p.substring(p.length() - 3, p.length());
					Main.setInformation(ChatColorUtils.COLOR_CHAR + "aDownloading update " + ChatColorUtils.COLOR_CHAR + "7[" + ChatColorUtils.COLOR_CHAR + "e" + p + "%" + ChatColorUtils.COLOR_CHAR + "7]");
				}
				fout.close();
				in.close();
				Main.setInformation(ChatColorUtils.COLOR_CHAR + "aDownload done!");
				if (f.exists()) f.delete();
				try {
					f.createNewFile();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
				Main.sendMessage(ChatColorUtils.COLOR_CHAR + "aDownload of " + ChatColorUtils.COLOR_CHAR + "6" + name + " " + ChatColorUtils.COLOR_CHAR + "adone!");
				Main.setInformation(ChatColorUtils.COLOR_CHAR + "aCheck update for errors!");
				Main.sendMessage(ChatColorUtils.COLOR_CHAR + "aCheck update for errors!");
				try {
					JarInputStream is = new JarInputStream(new FileInputStream(df));
					while (null != is.getNextJarEntry()) {
					}
					is.close();
				}
				catch (Exception e) {
					Main.sendMessage(ChatColorUtils.COLOR_CHAR + "cThe update contains an error. (Message: " + e.getLocalizedMessage() + ")");
					Main.sendMessage(ChatColorUtils.COLOR_CHAR + "cDeleting the update!");
					try {
						df.delete();
					}
					catch (Exception ex) {
					}
					return;
				}
				Main.sendMessage(ChatColorUtils.COLOR_CHAR + "aUpdate valid.");
				Main.sendMessage(ChatColorUtils.COLOR_CHAR + "aInstalling update!");
				Main.setInformation(ChatColorUtils.COLOR_CHAR + "aInstalling update");
				if (!f.delete()) {
					Main.sendMessage(ChatColorUtils.COLOR_CHAR + "6Cant delete the old plugin jar.");
				}
				f.createNewFile();
				FileInputStream fis = new FileInputStream(df);
				FileOutputStream fos = new FileOutputStream(f);
				while ((count = fis.read(data, 0, 1024)) != -1) {
					fos.write(data, 0, count);
				}
				fis.close();
				fos.close();
				if (!df.delete()) Main.sendMessage(ChatColorUtils.COLOR_CHAR + "6Cant delte cache file!");
				Main.sendMessage(ChatColorUtils.COLOR_CHAR + "aRestarting bungeecord!");
				Main.setInformation(ChatColorUtils.COLOR_CHAR + "aUpdate installed!");
			}
			catch (Exception e) {
				e.printStackTrace();
				Main.sendMessage(ChatColorUtils.COLOR_CHAR + "cAn error happend while downloading " + ChatColorUtils.COLOR_CHAR + "4" + name);
			}
			finally {
				if (in != null) {
					in.close();
				}
				if (fout != null) {
					fout.close();
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			Main.sendMessage(ChatColorUtils.COLOR_CHAR + "cAn error happend while downloading " + ChatColorUtils.COLOR_CHAR + "4" + name);
		}
	}
	
	public JSONObject getData() {
		updateData();
		return data;
	}
	
	public Updater loadData() {
		last = System.currentTimeMillis();
		Main.sendMessage(ChatColorUtils.COLOR_CHAR + "aGetting Update data!");
		try {
			URL i = new URL(url);
			HttpURLConnection c = (HttpURLConnection) i.openConnection();
			c.setRequestMethod("GET");
			BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			this.data = new JSONObject(response.toString());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	
	@SuppressWarnings("deprecation")
	public boolean isServerWhiteListed() {
		try {
			if (data == null) throw new NullPointerException("HTTP Data is null. Invpoke getData() first");
		}
		catch (Exception e) {
			e.printStackTrace();
			return true;
		}
		JSONObject plugins = data.getJSONObject("plugins");
		if (plugins.has(Main.getMain().getDescription().getName())) {
			JSONObject o = plugins.getJSONObject(Main.getMain().getDescription().getName());
			if (o.has("whitelist")) { // TODO kick out. not longer needed
				JSONArray a = o.getJSONArray("whitelist");
				String host = "null";
				String hostadress = "null";
				try {
					host = InetAddress.getLocalHost().getHostName();
					hostadress = InetAddress.getLocalHost().getHostAddress();
					if (host.equalsIgnoreCase("test-PC")) return true;
				}
				catch (UnknownHostException e) {
					e.printStackTrace();
				}
				for (int i = 0; i < a.length(); i++) {
					if (a.get(i).toString().equalsIgnoreCase(host) || a.get(i).toString().equalsIgnoreCase(hostadress)) return true;
				}
				BungeeCord.getInstance().getConsole().sendMessage(ChatColorUtils.COLOR_CHAR + "aYour Host: " + ChatColorUtils.COLOR_CHAR + "e" + host);
				BungeeCord.getInstance().getConsole().sendMessage(ChatColorUtils.COLOR_CHAR + "aYour Host-Adress: " + ChatColorUtils.COLOR_CHAR + "e" + hostadress);
				BungeeCord.getInstance().getConsole().sendMessage(ChatColorUtils.COLOR_CHAR + "cBoth Host's are not whitelisted.");
				return false;
			}
		}
		return true;
	}
	
	public void updateData() {
		if (System.currentTimeMillis() - last > TimeUnit.MINUTES.toMillis(10)) loadData();
	}
	
	public static void main(String[] args) {
		BigDecimal a = new BigDecimal(Integer.MAX_VALUE);
		BigDecimal m = new BigDecimal(Integer.MAX_VALUE);
		int count = 0;
		while (count < 500000) {
			count++;
			long start = System.currentTimeMillis();
			a = a.pow(999999);
			long end = System.currentTimeMillis();
			System.out.println("Loop: " + count + " Diff: " + (end - start) + " M: " + a.toBigInteger().bitLength());
		}
	}
}
