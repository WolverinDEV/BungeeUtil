package dev.wolveringer.bungeeutil.packetlib;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.StringUtils;

import dev.wolveringer.bungeeutil.BungeeUtil;
import dev.wolveringer.bungeeutil.CostumPrintStream;
import dev.wolveringer.bungeeutil.packets.Packet;
import dev.wolveringer.bungeeutil.packets.creator.AbstractPacketCreator;
import dev.wolveringer.bungeeutil.packets.creator.CachedPacketCreator;
import dev.wolveringer.bungeeutil.packets.creator.NormalPacketCreator;
import dev.wolveringer.bungeeutil.player.Player;
import dev.wolveringer.bungeeutil.player.ClientVersion.BigClientVersion;
import dev.wolveringer.bungeeutil.player.connection.ProtocollVersion;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import net.md_5.bungee.protocol.Protocol;
import net.md_5.bungee.protocol.ProtocolConstants.Direction;

public class PacketRegistry {
    @Getter
    public static class ProtocollId {
        private ProtocollVersion version;
        private int id;

        public ProtocollId(BigClientVersion version, int id) {
            this.version = version.getProtocollVersion();
            this.id = id;
        }

        public ProtocollId(ProtocollVersion version, int id) {
            this.version = version;
            this.id = id;
        }

        public boolean isValid() {
            return this.id >= 0 && this.version != null && this.version != ProtocollVersion.Unsupported;
        }

        @Override
        public String toString() {
            return "ProtocollId [version=" + this.version + ", id=" + Integer.toHexString(this.id) + "]";
        }
    }

    public static final AtomicLong classInstances = new AtomicLong();
    public static final ProtocollVersion[] packetIdVersionsArray = new ProtocollVersion[]{ProtocollVersion.v1_8,
            ProtocollVersion.v1_9, ProtocollVersion.v1_9_2, ProtocollVersion.v1_9_3, ProtocollVersion.v1_9_4,
            ProtocollVersion.v1_10, ProtocollVersion.v1_11, ProtocollVersion.v1_12, ProtocollVersion.v1_12_1, ProtocollVersion.v1_12_2};

    private static AbstractPacketCreator creator;

    static {
        try {
            Enumeration<URL> mappingFiles = PacketRegistry.class.getClassLoader().getResources("PacketMapping.data");
            if (!mappingFiles.hasMoreElements()) throw new Exception("Cant resolve packet mapping file");

            InputStream is = mappingFiles.nextElement().openStream();
            DataInputStream dis = new DataInputStream(is);

            String line = null;
            while ((line = dis.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                String[] elements = line.split(" -> ");
                if (elements[1].equalsIgnoreCase("UNDEFINED")) continue; //Protocoll
                if (elements[2].equalsIgnoreCase("UNDEFINED")) continue; //Direction

                String className = elements[0].trim();
                Class<? extends Packet> clazz = (Class<? extends Packet>) Class.forName(className);
                Protocol protocoll = Protocol.valueOf(elements[1].trim());
                Direction direction = Direction.valueOf(elements[2].trim());
                ProtocollId[] protocollIds = new ProtocollId[packetIdVersionsArray.length];

                String[] protocollIdsString = elements[3].split(" ");
                for (int i = 0; i < Math.min(protocollIdsString.length, protocollIds.length); i++) {
                    if (!protocollIdsString[i].trim().equalsIgnoreCase("~NAN"))
                        protocollIds[i] = new ProtocollId(packetIdVersionsArray[i], Integer.decode(protocollIdsString[i].trim()));
                }

                registerPacket(protocoll, direction, clazz, protocollIds);
            }

            is.close();
        } catch (Exception e) {
            e.printStackTrace();
            BungeeUtil.getInstance().sendMessage("§cCant load packet mapping! Packet registry failed to setup!");
        }
    }

    public static int calculate(ProtocollVersion version, Protocol p, Direction d, Integer id) {
        return getCreator().calculate(version, p, d, id);
    }

    public static int countPackets() {
        return getCreator().countPackets();
    }

    public static AbstractPacketCreator getCreator() {
        if (creator == null) {
            if (System.getProperty("bungeeutil.packet.no_cache") != null) {
                creator = new NormalPacketCreator();
                if (BungeeUtil.getInstance() != null)
                    BungeeUtil.getInstance().sendMessage("§6Dont use CachedPacketCreator!");
            } else {
                if (BungeeUtil.getInstance() != null)
                    BungeeUtil.getInstance().sendMessage("§aUsing CachedPacketCreator!");
                creator = new CachedPacketCreator(new NormalPacketCreator(), Integer.getInteger("bungeeutil.packet.cache_threads", Runtime.getRuntime().availableProcessors() * 2));
            }
        }
        return creator;
    }

    public static Direction getDirection(int base) {
        return getCreator().getDirection(base);
    }

    public static Direction getDirection(Class<? extends Packet> clazz) {
        return getCreator().getDirection(clazz);
    }

    public static Packet getPacket(ProtocollVersion version, Protocol s, Direction d, ByteBuf b, Player p) {
        return getCreator().getPacket(version, s, d, b, p);
    }

    public static int getPacketId(int base) {
        return getCreator().getPacketId(base);
    }

    public static int getCompressedId(ProtocollVersion version, Class<? extends Packet> clazz) {
        return getCreator().getPacketId(version, clazz);
    }

    public static Protocol getProtocoll(int base) {
        return getCreator().getProtocoll(base);
    }

    public static Protocol getProtocoll(Class<? extends Packet> clazz) {
        return getCreator().getProtocoll(clazz);
    }

    public static List<Class<? extends Packet>> getRegisteredPackets() {
        return getCreator().getRegisteredPackets();
    }

    public static void listPackets() {
        getCreator().listPackets();
    }

    public static void listPackets(CostumPrintStream out) {
        getCreator().listPackets(out);
    }

    public static int loadPacket(ProtocollVersion version, Protocol p, Direction d, Integer id, Class<? extends Packet> clazz) {
        return getCreator().loadPacket(version, p, d, id, clazz);
    }

    public static void registerPacket(Protocol p, Direction d, Class<? extends Packet> clazz, ProtocollId... ids) {
        getCreator().registerPacket(p, d, clazz, ids);
    }

    public static void unregisterPacket(ProtocollVersion version, Protocol p, Direction d, Integer id) {
        getCreator().unregisterPacket(version, p, d, id);
    }

    //Generate Mapping file for 1.8-1.12.1
    public static void main(String[] args) {
        List<Class<? extends Packet>> packets = getCreator().getRegisteredPackets();

        List<String> lines = new ArrayList<>();
        lines.add("#<Class> -> <protocoll> -> <direction> -> <1.8> <1.9> <1.9.2> <1.9.3> <1.9.4> <1.10> <1.11> <1.12> <1.12.1> <1.12.2>");
        lines.add("");

        int classNameWidth = packets.stream().map(e -> e.getName().toString().length()).max(Integer::compare).orElse(20);
        int protocollNameWidth = Arrays.stream(Protocol.values()).map(e -> e.toString().length()).max(Integer::compare).orElse(10);

        for (Class<? extends Packet> clazz : packets) {
            StringBuilder line = new StringBuilder();
            line.append(StringUtils.rightPad(clazz.getName().toString(), classNameWidth));
            line.append(" -> ");

            Protocol protocoll = getCreator().getProtocoll(clazz);
            String sprotocoll = protocoll == null ? "UNDEFINED" : protocoll.toString();
            line.append(StringUtils.rightPad(sprotocoll, protocollNameWidth) + " -> ");

            //TO_CLIENT
            //TO_SERVER
            //UNDEFINED
            Direction direction = getDirection(clazz);
            if (direction == null) {
                line.append("UNDEFINED");
            } else {
                line.append(direction.toString());
            }
            line.append(" -> ");

            for (ProtocollVersion ver : packetIdVersionsArray) {
                int id = getPacketId(getCompressedId(ver, clazz));
                //Create mapping vor 1.12
                /*
				if(ver == ProtocollVersion.v1_12){
					id = getPacketId(getCompressedId(ProtocollVersion.v1_11, clazz));
					if(id < 0 || id == 0xFF){
						line.append("~NAN");
						line.append(" ");
						continue;
					}
					
					if(protocoll == Protocol.GAME){
						if(direction == Direction.TO_SERVER){
							if(id >= 0x01 && id <= 0x15){
								id += 1;
							} else if(id == 0x16){
								id += 2;
							} else {
								id += 3;
							}
						} else if(direction == Direction.TO_CLIENT){
							if(id == 0x28)
								id = 0x25;
							else if(id >= 0x25 && id <= 0x27)
								id += 1;
							else if(id >= 0x30 && id <= 0x34)
								id += 1;
							else if(id >= 0x35 && id <= 0x49)
								id += 2;
							else if(id >= 0x4A)
								id += 3;
						}
					}
				}
				*/
                //Create mapping vor 1.12.1
				/*
				if(ver == ProtocollVersion.v1_12_1){
					id = getPacketId(getCompressedId(ProtocollVersion.v1_12, clazz));
					if(id < 0 || id == 0xFF){
						line.append("~NAN");
						line.append(" ");
						continue;
					}
					if(protocoll == Protocol.GAME){
						if(direction == Direction.TO_SERVER){
							if(id >= 0x02) id -= 1;
						} else if(direction == Direction.TO_CLIENT){
							if(id >= 0x2B && id <= 0x12) id += 1;
						}
					}
				}
				*/
                //Create mapping vor 1.12.2 (No changes so far)
                if (ver == ProtocollVersion.v1_12_2) {
                    id = getPacketId(getCompressedId(ProtocollVersion.v1_12_1, clazz));
                }

                if (id < 0 || id == 0xFF) {
                    line.append("~NAN");
                } else {
                    String hex = Integer.toHexString(id);
                    if (hex.length() == 1) hex = "0" + hex;
                    hex = "0x" + StringUtils.upperCase(hex);
                    line.append(hex);
                }
                line.append(" ");
            }

            lines.add(line.toString());
        }

        lines.forEach(System.out::println);
    }
}
