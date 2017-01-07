package dev.wolveringer.bungeeutil.plugin.updater;

import java.util.List;

public interface Updater {
	public boolean loadData();
	public boolean isValid();
	
	public Version getOwnVersion();
	public Version getNewestVersion();
	public List<Version> getVersionsBehind();
	public boolean isOfficialBuild();
	
	public List<String> getChangeNotes(Version version);
	public List<String> getBugs(Version version);
	public List<String> getMOTD(Version version);
	
	public static enum UpdateState {
		ALREDY_UP_TO_DATE,
		SUCCESSFULL,
		FAILED_NO_DATA,
		FAILED_DOWNLOAD,
		FAILED_FILE,
		FAILED_CHECKSUM,
		FAILED_UNKNOWN;
	}
	
	public boolean hasUpdate();
	public UpdateState update();
	public UpdateState updateTo(Version target);
}
