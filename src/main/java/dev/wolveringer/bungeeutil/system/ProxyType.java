package dev.wolveringer.bungeeutil.system;

public enum ProxyType {
	BUNGEECORD,
	WATERFALL;

	private static ProxyType type;

	public static ProxyType getType(){
		resolveType:
		if(type == null){
			try{
				Class.forName("io.github.waterfallmc.waterfall.conf.WaterfallConfiguration"); //The Waterfall configuration. Not in BungeeCord
				type = ProxyType.WATERFALL;
				break resolveType;
			}catch(ClassNotFoundException e){}
			type = ProxyType.BUNGEECORD;
		}
		return type;
	}
}
