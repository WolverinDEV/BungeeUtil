package dev.wolveringer.api.gui;

public interface AnvilGuiListener {
	/**
	 * 
	 * @param guy
	 * @param newMessage
	 * 
	 * this event will be triggered when a player changes the displayname
	 */
	public void onMessageChange(AnvilGui guy, String newMessage);
	/**
	 * @param cuy
	 * @param message
	 * This methode will run when a player entered the message
	 */
	public void onConfirmInput(AnvilGui guy, String message);
	public void onClose(AnvilGui guy);
}
