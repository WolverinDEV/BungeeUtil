package dev.wolveringer.Reflect.Test;

import java.util.ArrayList;

import dev.wolveringer.BungeeUtil.packets.Packet;
import dev.wolveringer.BungeeUtil.packets.PacketPlayInPosition;
import dev.wolveringer.BungeeUtil.packets.PacketPlayInPositionLook;
import dev.wolveringer.api.position.Location;
import dev.wolveringer.packet.MathHelper_v1_8;

public class Main {
    static class Friend {
        private final String name;
        public Friend(String name) {
            this.name = name;
        }
        public String getName() {
            return this.name;
        }
        public synchronized void bow(Friend bower) {
            System.out.format("%s: %s"
                + "  has bowed to me!%n", 
                this.name, bower.getName());
            bower.bowBack(this);
        }
        public synchronized void bowBack(Friend bower) {
            System.out.format("%s: %s"
                + " has bowed back to me!%n",
                this.name, bower.getName());
        }
    }
    public static void main(String[] args) {
    	/*
        final Friend alphonse = new Friend("Alphonse");
        final Friend gaston = new Friend("Gaston");
        new Thread(new Runnable() { public void run() { alphonse.bow(gaston); }  }).start();
        new Thread(new Runnable() { public void run() { gaston.bow(alphonse); } }).start();
        */
    }
   	public static Location readPosition(long l) {
   		long val = l;
   		return new Location((int)(val >> 38), (int)(val << 26 >> 52), (int)(val << 38 >> 38));
   	}

    
}