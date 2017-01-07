package dev.wolveringer.bungeeutil.plugin.updater;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import dev.wolveringer.bungeeutil.BungeeUtil;
import dev.wolveringer.bungeeutil.plugin.Main;
import dev.wolveringer.bungeeutil.plugin.StringParser;
import lombok.Getter;
import lombok.ToString;
import net.md_5.bungee.api.ChatColor;

public class UpdaterV2 implements Updater{
	@SuppressWarnings("null")
	private static List<String> getStringList(StringParser parser, JSONArray array){
		List<String> out = new ArrayList<>();
		if(array != null)
			for(Object o : array)
				if(o instanceof String)
					out.add(parser == null ? parser.getString((String) o, 256) : (String) o);
		return out;
	}
	
	@ToString
	@Getter
	static class DownloadData {
		private String url;
		private String sha1;
		private String md5;
		
		private StringParser stringParser;
		
		public DownloadData(StringParser stringParser, JSONObject data) {
			this.stringParser = stringParser;
			this.url = stringParser.getString(data.getString("url"), 256);
			this.sha1 = stringParser.getString(data.getString("sha1"), 256);
			this.md5 = stringParser.getString(data.getString("md5"), 256);
		}
	}
	
	@ToString
	@Getter
	static class VersionsData {
		private Version version;
		
		private DownloadData download;
		
		private List<String> changelog;
		private List<String> bugs;
		
		private StringParser stringParser;
		
		public VersionsData(Version version, StringParser stringParser, JSONObject data) {
			this.version = version;
			this.stringParser = stringParser;
			this.download = new DownloadData(stringParser, data.getJSONObject("download"));
			this.changelog = getStringList(stringParser, data.has("changelog") ? data.getJSONArray("changelog") : null);
			this.bugs = getStringList(stringParser, data.getJSONArray("bugs"));
		}
	}
	
	private String remoteURL;
	private JSONObject updateData;
	@SuppressWarnings("unused")
	private long lastRefresh;

	private VersionsData newestVersion = null;
	private List<VersionsData> avariableVersions = new ArrayList<>();
	
	private StringParser stringParser = null;
	
	public UpdaterV2(String url) {
		this.remoteURL = url;
	}

	@Override
	public boolean loadData() {
		this.lastRefresh = System.currentTimeMillis();
		if(BungeeUtil.getInstance() != null) {
			BungeeUtil.getInstance().sendMessage(ChatColor.GREEN + "Fetching update data.");
		}
		try {
			URL url = new URL(this.remoteURL);
			InputStream is = url.openStream();
			String response = IOUtils.toString(is);
			is.close();
			
			if(!response.isEmpty()){
				this.updateData = new JSONObject(response);
				if(this.updateData.getInt("configVersion") != 2){
					BungeeUtil.debug("Invalid updater version. (Neded: "+this.updateData.getInt("configVersion")+". Given: 2)");
					this.updateData = null;
					return false;
				}
				
				this.stringParser = new StringParser();
				try {
					this.stringParser.parseStrings(this.updateData.getJSONObject("strings"));
				}catch (Exception e) {
					BungeeUtil.debug("Cant parse strings! . Exception:");
					BungeeUtil.debug(e);
					return false;
				}
				
				JSONObject versions = this.updateData.getJSONObject("history");
				for(String version : versions.keySet()){
					try {
						String pversion = stringParser.getString(version, 255);
						this.avariableVersions.add(new VersionsData(new Version(pversion), this.stringParser, versions.getJSONObject(version)));
					}catch (Exception e) {
						BungeeUtil.debug("Cant parse version "+version+". Exception:");
						BungeeUtil.debug(e);
					}
				}
				
				this.newestVersion = getVersionsData(new Version(stringParser.getString(this.updateData.getString("CurrentVersion"))));
				if(this.newestVersion == null){
					BungeeUtil.debug("Cant find newest versions data (Version: "+this.updateData.getString("CurrentVersion")+")");
					return false;
				}
				
				return true;
			}
			this.updateData = null;
			return false;
		}
		catch (Exception e) {
			BungeeUtil.debug(e);
		}
		return false;
	}
	
	@Override
	public boolean isValid() {
		return this.avariableVersions != null && this.newestVersion != null && stringParser != null;
	}
	
	public VersionsData getVersionsData(Version version){
		for(VersionsData data : this.avariableVersions)
			if(data.version.compareTo(version) == 0)
				return data;
		return null;
	}
	
	public Version getOwnVersion(){
		return Main.getMain() == null ? new Version(System.getProperty("updater.version","unknown")) : new Version(Main.getMain().getDescription().getVersion());
	}
	
	@Override
	public Version getNewestVersion() {
		return isValid() ? this.newestVersion.getVersion() : null;
	}
	
	@Override
	public boolean isOfficialBuild() { //TODO check checksum
		return true;
	}
	
	@Override
	public List<String> getBugs(Version version) {
		VersionsData data = getVersionsData(version);
		if(data == null)
			return new ArrayList<>();
		return data.getBugs();
	}
	
	@Override
	public List<String> getChangeNotes(Version version) {
		VersionsData data = getVersionsData(version);
		if(data == null)
			return new ArrayList<>();
		return data.getChangelog();
	}
	
	@Override
	public List<String> getMOTD(Version version) {
		ArrayList<String> out = new ArrayList<>();
		int importance = -1;
		if(this.updateData.has("motd")){
			for(Object obj : this.updateData.getJSONArray("motd")){
				JSONObject e = (JSONObject) obj;
				String pattern = e.getString("versionPattern");
				if(this.getOwnVersion().getVersion().replaceAll(pattern, "").isEmpty()){
					int eimportance = e.getInt("importance");
					if(eimportance > importance){
						importance = eimportance;
						obj = getStringList(this.stringParser, e.getJSONArray("message"));
					}
				}
			}
		}
		return out;
	}
	
	@Override
	public boolean hasUpdate() {
		return getOwnVersion().compareTo(getNewestVersion()) < 0;
	}
	
	@Override
	public List<Version> getVersionsBehind() {
		List<Version> out = new ArrayList<>();
		
		for(VersionsData data : this.avariableVersions)
			if(data.getVersion().compareTo(this.getOwnVersion()) > 0)
				out.add(data.getVersion());
		
		Collections.sort(out, new Comparator<Version>() {
			@Override
			public int compare(Version o1, Version o2) {
				return o1.compareTo(o2);
			}
		});
		
		return out;
	}
	
	@Override
	public UpdateState update() {
		return updateTo(getNewestVersion());
	}
	
	@Override
	public UpdateState updateTo(Version target) {
		VersionsData data = getVersionsData(target);
		if(data == null)
			return UpdateState.FAILED_NO_DATA;
		
		BungeeUtil.getInstance().sendMessage(ChatColor.GREEN + "Updating from "+this.getOwnVersion().getVersion()+" to "+this.getNewestVersion().getVersion());
		
		File foulderPath = new File(Main.getMain().getDataFolder(), "updater");
		foulderPath.mkdirs();
		
		File downloadedData = new File(foulderPath, "BungeeUtils-v"+target.getVersion()+".download");
		
		if(downloadedData.exists()){
			BungeeUtil.getInstance().sendMessage(ChatColor.GOLD+"Found alredy downloaded version.");
			BungeeUtil.getInstance().sendMessage(ChatColor.GREEN+"Verifying file.");
			String md5 = "unknown";
			try {
				FileInputStream fis = new FileInputStream(downloadedData);
				md5 = DatatypeConverter.printHexBinary(DigestUtils.md5(fis));
				fis.close();
			}catch (Exception e) {
				BungeeUtil.debug("Cant create md5 checksum!");
				BungeeUtil.debug(e);
			}
			if(!md5.equalsIgnoreCase(data.getDownload().getMd5())){ //Only delete if not the mewest
				BungeeUtil.getInstance().sendMessage(ChatColor.GOLD+"Verifying failed. Deleting old files");
				
				if(!downloadedData.delete()){
					BungeeUtil.getInstance().sendMessage(ChatColor.RED+"Cant delete old files. Abort!");
					BungeeUtil.debug("Cant delete target file ("+downloadedData.getAbsolutePath()+")");
					return UpdateState.FAILED_FILE;
				}
				if(downloadedData.exists()){
					BungeeUtil.getInstance().sendMessage(ChatColor.RED+"Cant delete old files. Abort!");
					BungeeUtil.debug("Cant delete target file ("+downloadedData.getAbsolutePath()+")");
					return UpdateState.FAILED_FILE;
				}
			} else BungeeUtil.getInstance().sendMessage(ChatColor.GREEN+"Verifyed old file. File is valid. Using this file.");
		}
		
		if(!downloadedData.exists()){ //Download the update
			try {
				BungeeUtil.getInstance().sendMessage(ChatColor.GREEN + "Starting to download the update ("+data.getDownload().getUrl()+") to "+downloadedData.getAbsolutePath());
				URL targetURL = new URL(data.getDownload().getUrl());
				InputStream is = targetURL.openStream();
				FileOutputStream os = new FileOutputStream(downloadedData);
				IOUtils.copy(is, os);
				os.close();
				is.close();
				
				BungeeUtil.getInstance().sendMessage(ChatColor.GREEN + "Verifying download!");
				is = new FileInputStream(downloadedData);
				String md5 = DatatypeConverter.printHexBinary(DigestUtils.md5(is));
				is.close();
				
				if(!md5.equalsIgnoreCase(data.getDownload().getMd5())){
					BungeeUtil.getInstance().sendMessage(ChatColor.RED + "Verify failed! Invalid checksum.");
					BungeeUtil.debug("Downloaded MD5 check sum isnt equal to given. (File: "+md5+", Given: "+data.getDownload().getMd5()+")");
					return UpdateState.FAILED_CHECKSUM;
				}
				BungeeUtil.getInstance().sendMessage(ChatColor.GREEN + "Download done.");
			}catch (Exception e) {
				BungeeUtil.getInstance().sendMessage(ChatColor.RED + "An error happend while downloading the update.");
				BungeeUtil.debug("Cant download update. Exception:");
				BungeeUtil.debug(e);
				return UpdateState.FAILED_DOWNLOAD;
			}
		}
		
		File ownFile = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile());
		BungeeUtil.getInstance().sendMessage(ChatColor.GREEN + "Copy update to current file.");
		
		if(!ownFile.delete())
			BungeeUtil.debug("Cant delete own file.");
		
		try {
			FileOutputStream fos = new FileOutputStream(ownFile);
			FileInputStream fis = new FileInputStream(downloadedData);
			IOUtils.copy(fis, fos);
			fos.close();
			fis.close();
			BungeeUtil.getInstance().sendMessage(ChatColor.GREEN + "Files copied.");
			BungeeUtil.getInstance().sendMessage(ChatColor.GREEN + "Update done!");
			return UpdateState.SUCCESSFULL;
		}catch (Exception e) {
			BungeeUtil.getInstance().sendMessage(ChatColor.RED + "Cant copy update to the current file. Abort!");
			BungeeUtil.debug("Cant download update. Exception:");
			BungeeUtil.debug(e);
			return UpdateState.FAILED_FILE;
		}
	}
}
