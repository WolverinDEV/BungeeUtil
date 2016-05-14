package dev.wolveringer.BungeeUtil;

import lombok.Getter;

public enum ClientVersion {
	UnderknownVersion(-1, BigClientVersion.UnderknownVersion,false),
	v1_7_1(4, BigClientVersion.v1_7,false),
	v1_7_2(4, BigClientVersion.v1_7,false),
	v1_7_3(4, BigClientVersion.v1_7,false),
	v1_7_4(4, BigClientVersion.v1_7,false),
	v1_7_5(4, BigClientVersion.v1_7,false),
	v1_7_6(5, BigClientVersion.v1_7,false),
	v1_7_7(5, BigClientVersion.v1_7,false),
	v1_7_8(5, BigClientVersion.v1_7,false),
	v1_7_9(5, BigClientVersion.v1_7,false),
	v1_7_10(5, BigClientVersion.v1_7,false),
	v1_8_0(47, BigClientVersion.v1_8,true),
	v1_8_1(47, BigClientVersion.v1_8,true),
	v1_8_2(47, BigClientVersion.v1_8,true), // 1.8.2-1.8.10 //Unchanged
	v1_8_3(47, BigClientVersion.v1_8,true),
	v1_8_4(47, BigClientVersion.v1_8,true),
	v1_8_5(47, BigClientVersion.v1_8,true),
	v1_8_6(47, BigClientVersion.v1_8,true),
	v1_8_7(47, BigClientVersion.v1_8,true),
	v1_8_8(47, BigClientVersion.v1_8,true),
	v1_8_9(47, BigClientVersion.v1_8,true),
	v1_8_10(47, BigClientVersion.v1_8,true),
	//Skip snapshots
	v1_9_0(107,BigClientVersion.v1_9,true),
	v1_9_1(107,BigClientVersion.v1_9,true),
	v1_9_2(108,BigClientVersion.v1_9,true),
	v1_9_3(109,BigClientVersion.v1_9,true),
	v1_9_4(110,BigClientVersion.v1_9,true);
	private int v;
	BigClientVersion bv;
	@Getter
	private boolean supported = false;
	
	private ClientVersion(int v, BigClientVersion bv,boolean supported) {
		this.v = v;
		this.bv = bv;
		this.supported = supported;
	}

	public int getVersion() {
		return v;
	}

	public BigClientVersion getBigVersion() {
		return bv;
	}

	public static enum BigClientVersion {
		UnderknownVersion, v1_7, v1_8, v1_9;
	}

	public static ClientVersion fromProtocoll(int protocolVersion) {
		for(ClientVersion v : ClientVersion.values())
			if(v.getVersion() == protocolVersion)
				return v;
		return null;
	}
}
