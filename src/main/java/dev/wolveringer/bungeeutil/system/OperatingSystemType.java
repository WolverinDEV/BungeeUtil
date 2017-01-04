package dev.wolveringer.bungeeutil.system;

import java.util.HashMap;

public class OperatingSystemType {
	public static abstract class OperatingSystem {
		public OperatingSystem() {
			types.put(this.getIdentifier(), this);
		}
		public abstract String getIdentifier();

		public boolean isLinux(){
			return this.getIdentifier().toLowerCase().contains("linux");
		}
		public boolean isMac(){
			return this.getIdentifier().toLowerCase().contains("mac");
		}
		public boolean isWindows(){
			return this.getIdentifier().toLowerCase().contains("windows");
		}
	}
	public static abstract class OperatingSystemLinux extends OperatingSystem{}

	public static abstract class OperatingSystemMac extends OperatingSystem{
		public static final OperatingSystem MAC_OS = new OperatingSystemMac() {
			@Override
			public String getIdentifier() {
				return "Mac OS";
			};
		};
		public static final OperatingSystem MAC_OS_X = new OperatingSystemMac() {
			@Override
			public String getIdentifier() {
				return "Mac OS X";
			};
		};
	}
	public static abstract class OperatingSystemWindows extends OperatingSystem{
		public static final OperatingSystem WINDOWS_95 = new OperatingSystemWindows() {
			@Override
			public String getIdentifier() {
				return "Windows 95";
			};
		};
		public static final OperatingSystem WINDOWS_98 = new OperatingSystemWindows() {
			@Override
			public String getIdentifier() {
				return "Windows 98";
			};
		};
		public static final OperatingSystem WINDOWS_ME = new OperatingSystemWindows() {
			@Override
			public String getIdentifier() {
				return "Windows Me";
			};
		};
		public static final OperatingSystem WINDOWS_NT = new OperatingSystemWindows() {
			@Override
			public String getIdentifier() {
				return "Windows NT";
			};
		};
		public static final OperatingSystem WINDOWS_2000 = new OperatingSystemWindows() {
			@Override
			public String getIdentifier() {
				return "Windows 2000";
			};
		};
		public static final OperatingSystem WINDOWS_XP = new OperatingSystemWindows() {
			@Override
			public String getIdentifier() {
				return "Windows XP";
			};
		};
		public static final OperatingSystem WINDOWS_2003 = new OperatingSystemWindows() {
			@Override
			public String getIdentifier() {
				return "Windows 2003";
			};
		};
		public static final OperatingSystem WINDOWS_CE = new OperatingSystemWindows() {
			@Override
			public String getIdentifier() {
				return "Windows CE";
			};
		};
		public static final OperatingSystem WINDOWS_7 = new OperatingSystemWindows() {
			@Override
			public String getIdentifier() {
				return "Windows 7";
			};
		};
		public static final OperatingSystem WINDOWS_8 = new OperatingSystemWindows() {
			@Override
			public String getIdentifier() {
				return "Windows 8";
			};
		};
		public static final OperatingSystem WINDOWS_10 = new OperatingSystemWindows() {
			@Override
			public String getIdentifier() {
				return "Windows 10";
			};
		};
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
	private static OperatingSystem type = null;


	public static OperatingSystem getSystemType(){
		if(type == null){
			type = types.get(System.getProperty("os.name"));
			if(type == null) {
				type = UNDEFINED;
			}
		}
		return type;
	}
	String name;

	private OperatingSystemType(String name) {
		this.name = name;
	}
}
