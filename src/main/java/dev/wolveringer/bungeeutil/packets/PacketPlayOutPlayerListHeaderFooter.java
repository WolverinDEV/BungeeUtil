package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.ByteString;
import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PacketPlayOutPlayerListHeaderFooter extends Packet implements PacketPlayOut {
	private ByteString header;
	private ByteString footer;

	public PacketPlayOutPlayerListHeaderFooter(BaseComponent header,BaseComponent footer) {
		this.setHeader(header);
		this.setFooter(footer);
	}
	public PacketPlayOutPlayerListHeaderFooter(String header,String footer) {
		this.setHeader(TextComponent.fromLegacyText(header));
		this.setFooter(TextComponent.fromLegacyText(footer));
	}

	public BaseComponent getFooter() {
		return ComponentSerializer.parse(this.footer.getString())[0];
	}

	public BaseComponent getHeader() {
		return ComponentSerializer.parse(this.header.getString())[0];
	}

	@Override
	public void read(PacketDataSerializer s) {
		this.header = s.readStringBytes();
		this.footer = s.readStringBytes();
	}

	public void setFooter(BaseComponent... footer) {
		this.footer = new ByteString(ComponentSerializer.toString(footer));
	}

	public void setHeader(BaseComponent... header) {
		this.header = new ByteString(ComponentSerializer.toString(header));
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeStringBytes(this.header);
		s.writeStringBytes(this.footer);
	}
}
