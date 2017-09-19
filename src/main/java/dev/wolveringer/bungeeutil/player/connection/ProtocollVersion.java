package dev.wolveringer.bungeeutil.player.connection;

import dev.wolveringer.bungeeutil.player.ClientVersion.BigClientVersion;
import lombok.Getter;

@Getter
public enum ProtocollVersion {
	Unsupported(0, -1, false, true),
	v1_7(1, 5, false, true),
	v1_8(2, 47, true, true),
	v1_9(3, 107, true, true),
	v1_9_2(3,108, true, true),
	v1_9_3(3, 109, true, false),
	v1_9_4(3, 110, true, false),
	v1_10(4, 210, true, true),
	v1_11(5, 315, true, true),
	v1_12(6, 335, true, true),
	v1_12_1(6, 338, true, false),
	v1_12_2(6, 340, true, false);

	private int basedVersionInt;
	private BigClientVersion basedVersion;
	private boolean supported;
	private int protocollVersion;
	private boolean baseVersionFallback;
	
	private ProtocollVersion(int basedVersion,int protocoll, boolean supported, boolean useBaseVersionAsFallback) {
		this.basedVersionInt = basedVersion;
		this.supported = supported;
		this.protocollVersion = protocoll;
		this.baseVersionFallback = useBaseVersionAsFallback;
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