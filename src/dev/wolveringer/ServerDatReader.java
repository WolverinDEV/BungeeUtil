package dev.wolveringer;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.UUID;

import dev.wolveringer.nbt.NBTBase;
import dev.wolveringer.nbt.NBTCompressedStreamTools;
import dev.wolveringer.nbt.NBTTagCompound;
import dev.wolveringer.nbt.NBTTagList;
import dev.wolveringer.nbt.NBTTagString;
import net.md_5.bungee.api.ChatColor;

public class ServerDatReader {
	public static void main(String[] args) throws Exception {
		File f = new File("/home/wolverindev/.minecraft/servers.dat");
		FileInputStream in = new FileInputStream(f);
		NBTTagCompound base = NBTCompressedStreamTools.read(new DataInputStream(new BufferedInputStream(in)));
		NBTTagList list = base.getList("servers");
		for(NBTBase comp : list.asList()){
			NBTTagCompound c = (NBTTagCompound) comp;
			c.set("name", new NBTTagString("§a§l§n"+reset(c.getString("name"))));
			System.out.println(comp);
		}
		FileOutputStream out = new FileOutputStream(f);
		write(base, out);
		out.close();
	}
	
	public static String reset(String  in){
		return ChatColor.stripColor(in);
	}
	
	public static void write(NBTTagCompound nbttagcompound, OutputStream outputstream) throws Exception {
		DataOutputStream dataoutputstream = new DataOutputStream(outputstream);
		try{
			NBTCompressedStreamTools.write(nbttagcompound,(DataOutput) dataoutputstream);
		}finally{
			try{
				dataoutputstream.close();
			}catch (java.io.IOException e){
				e.printStackTrace();
			}
		}
	}
}
