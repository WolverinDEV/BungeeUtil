package dev.wolveringer.bungeeutil.player.connection;

import dev.wolveringer.bungeeutil.player.ClientVersion.BigClientVersion;
import lombok.Getter;

@Getter
public enum ProtocollVersion {
	Unsupported(0, -1, false),
	v1_7(1, 5, false),
	v1_8(2, 47, true),
	v1_9(3, 107, true),
	v1_9_2(3,108, true),
	v1_9_3(3, 109, true),
	v1_9_4(3, 110, true),
	v1_10(4, 210, true),
	v1_11(5, 315, true),
	v1_12(6, 332, true);

	private int basedVersionInt;
	private BigClientVersion basedVersion;
	private boolean supported;
	private int protocollVersion;

	private ProtocollVersion(int basedVersion,int protocoll, boolean supported) {
		this.basedVersionInt = basedVersion;
		this.supported = supported;
		this.protocollVersion = protocoll;
	}

	public BigClientVersion getBasedVersion(){
		if(this.basedVersion == null) {
			this.basedVersion = BigClientVersion.values()[this.basedVersionInt];
		}
		return this.basedVersion;
	}

	public int getProtocollVersion(){
		return this.protocollVersion;
	}
}