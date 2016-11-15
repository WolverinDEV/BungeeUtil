package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.ByteString;
import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

@AllArgsConstructor
@NoArgsConstructor
public class PacketPlayOutPlayerListHeaderFooter extends Packet implements PacketPlayOut {
	private ByteString header;
	private ByteString footer;

	public PacketPlayOutPlayerListHeaderFooter(String header,String footer) {
		setHeader(TextComponent.fromLegacyText(header));
		setFooter(TextComponent.fromLegacyText(footer));
	}
	public PacketPlayOutPlayerListHeaderFooter(BaseComponent header,BaseComponent footer) {
		setHeader(header);
		setFooter(footer);
	}

	@Override
	public void read(PacketDataSerializer s) {
		header = s.readStringBytes();
		footer = s.readStringBytes();
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeStringBytes(header);
		s.writeStringBytes(footer);
	}

	public BaseComponent getHeader() {
		return ComponentSerializer.parse(header.getString())[0];
	}

	public BaseComponent getFooter() {
		return ComponentSerializer.parse(footer.getString())[0];
	}

	public void setHeader(BaseComponent... header) {
		this.header = new ByteString(ComponentSerializer.toString(header));
	}

	public void setFooter(BaseComponent... footer) {
		this.footer = new ByteString(ComponentSerializer.toString(footer));
	}
}
