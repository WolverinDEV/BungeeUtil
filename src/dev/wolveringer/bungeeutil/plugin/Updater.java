package dev.wolveringer.bungeeutil.plugin;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarInputStream;

import javax.print.DocFlavor.STRING;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import org.json.JSONArray;
import org.json.JSONObject;

import dev.wolveringer.bungeeutil.BungeeUtil;
import dev.wolveringer.bungeeutil.Configuration;
import dev.wolveringer.bungeeutil.MathUtil;
import dev.wolveringer.bungeeutil.chat.ChatColorUtils;
import dev.wolveringer.bungeeutil.item.ItemStack;
import dev.wolveringer.bungeeutil.item.Material;
import dev.wolveringer.bungeeutil.item.ItemStack.Click;
import dev.wolveringer.bungeeutil.item.meta.SkullMeta;
import dev.wolveringer.bungeeutil.profile.SkinFactory;
import lombok.NonNull;

public class Updater {
	
	private String url;
	private JSONObject data;
	private long last;
	
	public Updater(String url) {
		this.url = url;
	}
	
	public boolean checkUpdate() {
		updateData();
		BungeeUtil.getInstance().sendMessage(ChatColorUtils.COLOR_CHAR + "aChecking for Plugin updates");
		if (data == null) throw new NullPointerException("HTTP Data is null. Invpoke getData() first");
		if (!isNewstVersion()) {
			installUpdate();
			BungeeCord.getInstance().stop();
			return true;
		}
		else {
			if (!isDevBuild()) BungeeUtil.getInstance().sendMessage(ChatColorUtils.COLOR_CHAR + "aNo plugin update found! Your version is alredy the newest! (" + getCurrentVersion() + ")");
			else BungeeUtil.getInstance().sendMessage(ChatColorUtils.COLOR_CHAR + "aYou plugin version is newer than the currunt public version. I think i'm a dev build... All bugs will be ignored");
		}
		return false;
	}
	
	public void installUpdate(){
		File ownFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getFile());
		downloadUpdate(data.getString("Download"), ownFile);
		Configuration.setLastVersion(getCurrentVersion());
	}
	
	public String getCurrentVersion(){
		return Main.getMain().getDescription().getVersion().split("-")[0];
	}
	
	public String getNewestVersion() {
		updateData();
		return data.getString("CurrentVersion");
	}
	
	public boolean isNewstVersion() {
		int minBounds = Math.min(getNewestVersion().length(), getCurrentVersion().length());
		if(minBounds == 0)
			return true;
		Long a = Long.parseLong(getNewestVersion().substring(0, minBounds).replaceAll("\\.", ""));
		Long b = Long.parseLong(getCurrentVersion().substring(0, minBounds).replaceAll("\\.", ""));
		if(a == b)
			return getNewestVersion().length() > getCurrentVersion().length();
		return a <= b;
	}
	
	public boolean isDevBuild(){
		return false;
	}
	
	/**
	 * 
	 * @param url
	 * @param targetFile
	 * @return errormask
	 * errors:
	 * 0: Create new file exception
	 * 1: Invalid jar
	 * 2: cant delete invalid jar
	 * 3: Download IO error
	 * 3: Finaly error
	 */
	private int downloadUpdate(String url, File targetFile) {
		BigInteger errorMask = new BigInteger("0");
		errorMask.setBit(8);
		BungeeUtil.getInstance().sendMessage(ChatColorUtils.COLOR_CHAR + "aUpdating from "+getCurrentVersion()+" to "+getNewestVersion());
		BungeeUtil.getInstance().sendMessage(ChatColorUtils.COLOR_CHAR + "aStarting to download the update ("+url+") to "+targetFile.getAbsolutePath());
		programm:
		try {
			BungeeUtil.getInstance().setInformation(ChatColorUtils.COLOR_CHAR + "aDownloading update " + ChatColorUtils.COLOR_CHAR + "7[" + ChatColorUtils.COLOR_CHAR + "e000%" + ChatColorUtils.COLOR_CHAR + "7]");
			BufferedInputStream in = null;
			FileOutputStream fout = null;
			try {
				URLConnection com = new URL(url).openConnection();
				int fileLength = com.getContentLength();
				in = new BufferedInputStream(com.getInputStream());
				File df;
				if (targetFile.exists()) {
					BungeeUtil.getInstance().setInformation(ChatColorUtils.COLOR_CHAR + "aUsing .download file ("+targetFile.getPath() + "BungeeUtil.download)!");
					fout = new FileOutputStream(df = new File(targetFile.getPath() + "BungeeUtil.download"));
				}
				else fout = new FileOutputStream(df = targetFile);
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
					BungeeUtil.getInstance().setInformation(ChatColorUtils.COLOR_CHAR + "aDownloading update " + ChatColorUtils.COLOR_CHAR + "7[" + ChatColorUtils.COLOR_CHAR + "e" + p + "%" + ChatColorUtils.COLOR_CHAR + "7]");
				}
				fout.close();
				in.close();
				BungeeUtil.getInstance().setInformation(ChatColorUtils.COLOR_CHAR + "aDownload done!");
				BungeeUtil.getInstance().sendMessage(ChatColorUtils.COLOR_CHAR + "aUpdate downloaded!");
				BungeeUtil.getInstance().setInformation(ChatColorUtils.COLOR_CHAR + "aCheck update for errors!");
				BungeeUtil.getInstance().sendMessage(ChatColorUtils.COLOR_CHAR + "aCheck update for errors!");
				try {
					JarInputStream is = new JarInputStream(new FileInputStream(df));
					while (null != is.getNextJarEntry()) {
					}
					is.close();
				}
				catch (Exception e) {
					errorMask.setBit(1);
					BungeeUtil.getInstance().sendMessage(ChatColorUtils.COLOR_CHAR + "cThe update contains an error. (Message: " + e.getLocalizedMessage() + ")");
					BungeeUtil.getInstance().sendMessage(ChatColorUtils.COLOR_CHAR + "cDeleting the update!");
					try {
						df.delete();
					}
					catch (Exception ex) {
						errorMask.setBit(2);
					}
					break programm;
				}
				BungeeUtil.getInstance().sendMessage(ChatColorUtils.COLOR_CHAR + "aUpdate valid.");
				BungeeUtil.getInstance().sendMessage(ChatColorUtils.COLOR_CHAR + "aInstalling update!");
				BungeeUtil.getInstance().setInformation(ChatColorUtils.COLOR_CHAR + "aInstalling update");
				if (!targetFile.equals(df) && !targetFile.delete()) {
					BungeeUtil.getInstance().sendMessage(ChatColorUtils.COLOR_CHAR + "6Cant delete the old plugin jar.");
				}
				boolean deleteOld = !targetFile.equals(df);
				if(!targetFile.createNewFile()){
					deleteOld = false;
					BungeeUtil.getInstance().sendMessage(ChatColorUtils.COLOR_CHAR + "6Cant create new jar.");
				}
				FileInputStream fis = new FileInputStream(df);
				FileOutputStream fos = new FileOutputStream(targetFile);
				while ((count = fis.read(data, 0, 1024)) != -1) {
					fos.write(data, 0, count);
				}
				fis.close();
				fos.close();
				if (deleteOld && !df.delete()) BungeeUtil.getInstance().sendMessage(ChatColorUtils.COLOR_CHAR + "6Cant delte cache file!");
				BungeeUtil.getInstance().sendMessage(ChatColorUtils.COLOR_CHAR + "aRestarting bungeecord!");
				BungeeUtil.getInstance().setInformation(ChatColorUtils.COLOR_CHAR + "aUpdate installed!");
			}
			catch (Exception e) {
				errorMask.setBit(3);
				e.printStackTrace();
				BungeeUtil.getInstance().sendMessage(ChatColorUtils.COLOR_CHAR + "cAn error happend while downloading the update");
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
			errorMask.setBit(4);
			e.printStackTrace();
			BungeeUtil.getInstance().sendMessage(ChatColorUtils.COLOR_CHAR + "cAn error happend while downloading the update");
		}
		return errorMask.intValue();
	}
	
	public JSONObject getData() {
		updateData();
		return data;
	}
	
	public Updater loadData() {
		last = System.currentTimeMillis();
		BungeeUtil.getInstance().sendMessage(ChatColorUtils.COLOR_CHAR + "aLoading update data!");
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
			BungeeUtil.debug(e);
		}
		return this;
	}
	
	/*
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
	*/
	
	public HashMap<String, List<String>> createChanges(@NonNull String lastVersion){
		HashMap<String, List<String>> out = new HashMap<>();
		if(data != null){
			JSONArray changelogArray = data.getJSONArray("Changelog");
			Iterator<Object> objects = changelogArray.iterator();
			while (objects.hasNext()) {
				JSONObject object = (JSONObject) objects.next();
				String version; 
				System.out.print((Long.parseLong((version = object.getString("Verion")).replaceAll("\\.", "")) > Long.parseLong(lastVersion.replaceAll("\\.", "")))+"-"+(Long.parseLong((version = object.getString("Verion")).replaceAll("\\.", "")) +":"+ Long.parseLong(getCurrentVersion().replaceAll("\\.", ""))));
				if(Long.parseLong((version = object.getString("Verion")).replaceAll("\\.", "")) > Long.parseLong(lastVersion.replaceAll("\\.", "")) && Long.parseLong((version = object.getString("Verion")).replaceAll("\\.", "")) <= Long.parseLong(getCurrentVersion().replaceAll("\\.", ""))){
					ArrayList<String> changes = new ArrayList<>();
					Iterator<Object> message = object.getJSONArray("Changed").iterator();
					while (message.hasNext()) {
						changes.add((String) message.next());
					}
					out.put(version, changes);
				}
			}
		}
		else
			out.put("error", Arrays.asList("§cCant featch versions data.","Make shure you have an valid internet connection."));
		return out;
	}
	
	public void updateData() {
		if (System.currentTimeMillis() - last > TimeUnit.MINUTES.toMillis(10)) loadData();
	}
}
