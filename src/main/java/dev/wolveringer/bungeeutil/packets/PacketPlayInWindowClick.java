package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.item.Item;
import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayIn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PacketPlayInWindowClick extends Packet implements PacketPlayIn {

	public static class Mode {
		//public static final class Normal {
		public static final int NORMAL_LEFT_CLICK = getInt(0, 0);

		public static final int NORMAL_RIGHT_CLICK = getInt(0, 1);

		public static final int NORMAL_MIDDLE_CLICK = getInt(3, 2);

		public static final int NORMAL_DOUBLE_CLICK = getInt(6, 0);
//}

		//public static final class Shift {
		public static final int SHIFT_LEFT_CLICK = getInt(1, 0);

		public static final int SHIFT_RIGHT_CLICK = getInt(1, 1);
//}

		//public static final class Key {
		public static final int KEY_1 = getInt(2, 0);

		public static final int KEY_2 = getInt(2, 1);
			public static final int KEY_3 = getInt(2, 2);
			public static final int KEY_4 = getInt(2, 3);
			public static final int KEY_5 = getInt(2, 4);

		public static final int KEY_6 = getInt(2, 5);
			public static final int KEY_7 = getInt(2, 6);

		public static final int KEY_8 = getInt(2, 7);
			public static final int KEY_9 = getInt(2, 8);
		//}
			//public static final class Drop {
			public static final int DROP_ITEM = getInt(4, 0);
			public static final int DROP_ITEM_STACK = getInt(4, 1);
		//}
			//public static final class Drag {
			public static final int DRAG_START_LEFT = getInt(5, 0);
			public static final int DRAG_ADD_LEFT = getInt(5, 1);
			public static final int DRAG_END_LEFT = getInt(5, 2);
			public static final int DRAG_START_RIGHT = getInt(5, 4);
			public static final int DRAG_ADD_RIGHT = getInt(5, 5);

		public static final int DRAG_END_RIGHT = getInt(5, 6);
		//}
			private static int getInt(int mode, int button) {
				return (mode << 4) + button;
			}

		private static int getMode(int mode) {
				return mode >> 4;
			}
			public static boolean inDrop(int mode) {
				return getMode(mode) == 4;
			}
			public static boolean isDrag(int mode) {
				return getMode(mode) == 5;
			}
			public static boolean isKey(int mode) {
				return getMode(mode) == 2;
			}
			public static boolean isNormalClick(int mode) {
				return getMode(mode) == 0 || getMode(mode) == 3 || getMode(mode) == 6;
			}
			public static boolean isShiftClick(int mode) {
				return getMode(mode) == 1;
			}
	}

	private short actionNumber;
	private Item item;
	private int mode = 0;
	private int slot;
	private int window;

	@Override
	public void read(PacketDataSerializer s) {
		this.window = s.readByte();
		this.slot = s.readShort();
		this.mode += s.readByte();
		this.actionNumber = s.readShort();
		this.mode += s.readByte() << 4;
		this.item = s.readItem();
	}

	@Override
	public String toString() {
		return "PacketPlayInWindowClick [actionNumber=" + this.actionNumber + ", item=" + this.item + ", shift=" + this.mode + ", slot=" + this.slot + ", window=" + this.window + "]";
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeByte(this.window);
		s.writeShort(this.slot);
		s.writeByte(this.mode & 0x0F);
		s.writeShort(this.actionNumber);
		s.writeByte(this.mode >> 4);
		s.writeItem(this.item);
	}
}
