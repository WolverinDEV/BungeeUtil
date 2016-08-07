package dev.wolveringer.BungeeUtil.OperatingSystem;

import java.util.HashMap;

public class OperatingSystemType {
	public static abstract class OperatingSystem {
		public OperatingSystem() {
			types.put(getIdentifier(), this);
		}
		public abstract String getIdentifier();
		
		public boolean isWindows(){
			return getIdentifier().toLowerCase().contains("windows");
		}
		public boolean isLinux(){
			return getIdentifier().toLowerCase().contains("linux");
		}
		public boolean isMac(){
			return getIdentifier().toLowerCase().contains("mac");
		}
	}
	private static HashMap<String, OperatingSystem> types = new HashMap<String, OperatingSystemType.OperatingSystem>();
	
	public static final OperatingSystemWindows WINDOWS = new OperatingSystemWindows() {
		@Override
		public String getIdentifier() {
			return "Windows";
		}
	};
	public static final OperatingSystemLinux LINUX = new OperatingSystemLinux() {
		@Override
		public String getIdentifier() {
			return "Linux";
		}
	};
	public static final OperatingSystemMac MAC = new OperatingSystemMac() {
		@Override
		public String getIdentifier() {
			return "Mac";
		}
	};
	public static final OperatingSystem UNDEFINED = new OperatingSystem() {
		@Override
		public String getIdentifier() {
			return "undefined";
		}
	};
	String name;
	
	private OperatingSystemType(String name) {
		this.name = name;
	}
	
	private static OperatingSystem type = null;
	public static OperatingSystem getSystemType(){
		if(type == null){
			type = types.get(System.getProperty("os.name"));
			if(type == null)
				type = UNDEFINED;
		}
		return type;
	}
	
	
	public static abstract class OperatingSystemWindows extends OperatingSystem{
		public static final OperatingSystem WINDOWS_95 = new OperatingSystemWindows() {
			public String getIdentifier() {
				return "Windows 95";
			};
		};
		public static final OperatingSystem WINDOWS_98 = new OperatingSystemWindows() {
			public String getIdentifier() {
				return "Windows 98";
			};
		};
		public static final OperatingSystem WINDOWS_ME = new OperatingSystemWindows() {
			public String getIdentifier() {
				return "Windows Me";
			};
		};
		public static final OperatingSystem WINDOWS_NT = new OperatingSystemWindows() {
			public String getIdentifier() {
				return "Windows NT";
			};
		};
		public static final OperatingSystem WINDOWS_2000 = new OperatingSystemWindows() {
			public String getIdentifier() {
				return "Windows 2000";
			};
		};
		public static final OperatingSystem WINDOWS_XP = new OperatingSystemWindows() {
			public String getIdentifier() {
				return "Windows XP";
			};
		};
		public static final OperatingSystem WINDOWS_2003 = new OperatingSystemWindows() {
			public String getIdentifier() {
				return "Windows 2003";
			};
		};
		public static final OperatingSystem WINDOWS_CE = new OperatingSystemWindows() {
			public String getIdentifier() {
				return "Windows CE";
			};
		};
		public static final OperatingSystem WINDOWS_7 = new OperatingSystemWindows() {
			public String getIdentifier() {
				return "Windows 7";
			};
		};
		public static final OperatingSystem WINDOWS_8 = new OperatingSystemWindows() {
			public String getIdentifier() {
				return "Windows 8";
			};
		};
		public static final OperatingSystem WINDOWS_10 = new OperatingSystemWindows() {
			public String getIdentifier() {
				return "Windows 10";
			};
		};
	}
	public static abstract class OperatingSystemLinux extends OperatingSystem{}
	
	public static abstract class OperatingSystemMac extends OperatingSystem{
		public static final OperatingSystem MAC_OS = new OperatingSystemMac() {
			public String getIdentifier() {
				return "Mac OS";
			};
		};
		public static final OperatingSystem MAC_OS_X = new OperatingSystemMac() {
			public String getIdentifier() {
				return "Mac OS X";
			};
		};
	}
}
