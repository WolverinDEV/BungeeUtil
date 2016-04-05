package dev.wolveringer.BungeeUtil;

public enum ClientVersion {
	UnderknownVersion(-1, BigClientVersion.UnderknownVersion),
	v1_7_1(4, BigClientVersion.v1_7),
	v1_7_2(4, BigClientVersion.v1_7),
	v1_7_3(4, BigClientVersion.v1_7),
	v1_7_4(4, BigClientVersion.v1_7),
	v1_7_5(4, BigClientVersion.v1_7),
	v1_7_6(5, BigClientVersion.v1_7),
	v1_7_7(5, BigClientVersion.v1_7),
	v1_7_8(5, BigClientVersion.v1_7),
	v1_7_9(5, BigClientVersion.v1_7),
	v1_7_10(5, BigClientVersion.v1_7),
	v1_8_0(47, BigClientVersion.v1_8),
	v1_8_1(47, BigClientVersion.v1_8),
	v1_8_2(47, BigClientVersion.v1_8), // 1.8.2-1.8.10 //Unchanged
	v1_8_3(47, BigClientVersion.v1_8),
	v1_8_4(47, BigClientVersion.v1_8),
	v1_8_5(47, BigClientVersion.v1_8),
	v1_8_6(47, BigClientVersion.v1_8),
	v1_8_7(47, BigClientVersion.v1_8),
	v1_8_8(47, BigClientVersion.v1_8),
	v1_8_9(47, BigClientVersion.v1_8),
	v1_8_10(47, BigClientVersion.v1_8),
	//Skip snapshots
	v1_9_0(107,BigClientVersion.v1_9),
	v1_9_1(107,BigClientVersion.v1_9),
	v1_9_2(108,BigClientVersion.v1_9),
	v1_9_3(109,BigClientVersion.v1_9);
	private int v;
	BigClientVersion bv;

	private ClientVersion(int v, BigClientVersion bv) {
		this.v = v;
		this.bv = bv;
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
