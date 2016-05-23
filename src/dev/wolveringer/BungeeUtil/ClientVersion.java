package dev.wolveringer.BungeeUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;

public enum ClientVersion {
	UnderknownVersion(-1, BigClientVersion.UnderknownVersion, ProtocollVersion.Unsupported),
	v1_7_1(4, BigClientVersion.v1_7, ProtocollVersion.v1_7),
	v1_7_2(4, BigClientVersion.v1_7, ProtocollVersion.v1_7),
	v1_7_3(4, BigClientVersion.v1_7, ProtocollVersion.v1_7),
	v1_7_4(4, BigClientVersion.v1_7, ProtocollVersion.v1_7),
	v1_7_5(4, BigClientVersion.v1_7, ProtocollVersion.v1_7),
	v1_7_6(5, BigClientVersion.v1_7, ProtocollVersion.v1_7),
	v1_7_7(5, BigClientVersion.v1_7, ProtocollVersion.v1_7),
	v1_7_8(5, BigClientVersion.v1_7, ProtocollVersion.v1_7),
	v1_7_9(5, BigClientVersion.v1_7, ProtocollVersion.v1_7),
	v1_7_10(5, BigClientVersion.v1_7, ProtocollVersion.v1_7),
	v1_8_0(47, BigClientVersion.v1_8, ProtocollVersion.v1_8),
	v1_8_1(47, BigClientVersion.v1_8, ProtocollVersion.v1_8),
	v1_8_2(47, BigClientVersion.v1_8, ProtocollVersion.v1_8), // 1.8.2-1.8.10
	                                                          // //Unchanged
	v1_8_3(47, BigClientVersion.v1_8, ProtocollVersion.v1_8),
	v1_8_4(47, BigClientVersion.v1_8, ProtocollVersion.v1_8),
	v1_8_5(47, BigClientVersion.v1_8, ProtocollVersion.v1_8),
	v1_8_6(47, BigClientVersion.v1_8, ProtocollVersion.v1_8),
	v1_8_7(47, BigClientVersion.v1_8, ProtocollVersion.v1_8),
	v1_8_8(47, BigClientVersion.v1_8, ProtocollVersion.v1_8),
	v1_8_9(47, BigClientVersion.v1_8, ProtocollVersion.v1_8),
	v1_8_10(47, BigClientVersion.v1_8, ProtocollVersion.v1_8),
	// Skip snapshots
	v1_9_0(107, BigClientVersion.v1_9, ProtocollVersion.v1_9),
	v1_9_1(107, BigClientVersion.v1_9, ProtocollVersion.v1_9),
	v1_9_2(108, BigClientVersion.v1_9, ProtocollVersion.v1_9_2),
	v1_9_3(109, BigClientVersion.v1_9, ProtocollVersion.v1_9_3),
	v1_9_4(110, BigClientVersion.v1_9, ProtocollVersion.v1_9_4);
	
	@Getter
	private int version;
	private BigClientVersion bigClientVersion;
	@Getter
	private ProtocollVersion protocollVersion;
	
	private ClientVersion(int v, BigClientVersion bv, ProtocollVersion protocol) {
		this.version = v;
		this.bigClientVersion = bv;
		this.protocollVersion = protocol;
	}
	
	public BigClientVersion getBigVersion() {
		return bigClientVersion;
	}
	
	public static enum BigClientVersion {
		UnderknownVersion(ProtocollVersion.Unsupported),
		v1_7(ProtocollVersion.v1_7),
		v1_8(ProtocollVersion.v1_8),
		v1_9(ProtocollVersion.v1_9);
		
		@Getter
		private ProtocollVersion protocollVersion;
		
		private BigClientVersion(ProtocollVersion basedVersion) {
			this.protocollVersion = basedVersion;
		}
	}
	
	@AllArgsConstructor
	@Getter
	public static enum ProtocollVersion {
		Unsupported(BigClientVersion.UnderknownVersion, false),
		v1_7(BigClientVersion.v1_7, false),
		v1_8(BigClientVersion.v1_8, true),
		v1_9(BigClientVersion.v1_9, true),
		v1_9_2(BigClientVersion.v1_9, true),
		v1_9_3(BigClientVersion.v1_9, true),
		v1_9_4(BigClientVersion.v1_9, true);
		
		private BigClientVersion basedVersion;
		private boolean supported;
	}
	
	public static ClientVersion fromProtocoll(int protocolVersion) {
		for (ClientVersion v : ClientVersion.values())
			if (v.getVersion() == protocolVersion) return v;
		return null;
	}
}
