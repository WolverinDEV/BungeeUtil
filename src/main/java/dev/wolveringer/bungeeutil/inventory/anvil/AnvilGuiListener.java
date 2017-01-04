package dev.wolveringer.bungeeutil.inventory.anvil;

public interface AnvilGuiListener {
	public void onClose(AnvilGui guy);
	/**
	 * @param cuy
	 * @param message
	 * This methode will run when a player entered the message
	 */
	public void onConfirmInput(AnvilGui guy, String message);
	/**
	 *
	 * @param guy
	 * @param newMessage
	 *
	 * this event will be triggered when a player changes the displayname
	 */
	public void onMessageChange(AnvilGui guy, String newMessage);
}
