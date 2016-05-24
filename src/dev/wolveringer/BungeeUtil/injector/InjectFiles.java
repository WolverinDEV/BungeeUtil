package dev.wolveringer.BungeeUtil.injector;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javassist.ClassPool;
import javassist.CtClass;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.UserConnection;
import dev.wolveringer.BungeeUtil.Main;
import dev.wolveringer.BungeeUtil.configuration.Configuration;
import dev.wolveringer.chat.ChatColor.ChatColorUtils;

public class InjectFiles {
	public static int inject() {
		try{
			int modifier = UserConnection.class.getModifiers();

			if(!Modifier.isFinal(modifier) && Modifier.isPublic(modifier)){
				return -1;
			}
			Main.sendMessage(ChatColorUtils.COLOR_CHAR+"aStarting BungeeUtil injection.");
			Main.sendMessage(ChatColorUtils.COLOR_CHAR+"aSet modifiers for class UserConnection.class to \"public\"");

			String[] names = { "net.md_5.bungee.UserConnection.class" };
			ClassPool cp = ClassPool.getDefault();
			CtClass clazz = cp.getCtClass(UserConnection.class.getName());
			clazz.setModifiers(Modifier.PUBLIC);
			ByteArrayOutputStream bout;
			DataOutputStream out = new DataOutputStream(bout = new ByteArrayOutputStream());
			clazz.getClassFile().write(out);
			InputStream[] streams = { new ByteArrayInputStream(bout.toByteArray()) };
			File bungee_file = new File(BungeeCord.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
			updateZipFile(bungee_file, names, streams);
			return 1;
		}catch (Exception e){
			e.printStackTrace();
		}
		return 0;
	}

	private static void updateZipFile(File zipFile, String[] names, InputStream[] ins) throws IOException {
		File tempFile = File.createTempFile(zipFile.getName(), null);
		if(!tempFile.delete())
			Main.sendMessage("Warn: Cant delte temp file.");
		if(tempFile.exists())
			Main.sendMessage("Warn: Temp target file alredy exist!");
		if(!zipFile.exists())
			throw new RuntimeException("Could not rename the file " + zipFile.getAbsolutePath() + " to " + tempFile.getAbsolutePath()+" (Src. not found!)");
		int renameOk = zipFile.renameTo(tempFile)?1:0;
		if(renameOk==0){
			tempFile = new File(zipFile.toString()+".copy");
			com.google.common.io.Files.copy(zipFile, tempFile);
			renameOk = 2;
			if(zipFile.delete()){
				Main.sendMessage("Warn: Src file cant delete.");
				renameOk = -1;
			}
		}
		if(renameOk == 0)
			throw new RuntimeException("Could not rename the file " + zipFile.getAbsolutePath() + " to " + tempFile.getAbsolutePath()+" (Directory read only? (Temp:[R:"+(tempFile.canRead()?1:0)+";W:"+(tempFile.canWrite()?1:0)+",D:"+(tempFile.canExecute()?1:0)+"],Src:[R:"+(zipFile.canRead()?1:0)+";W:"+(zipFile.canWrite()?1:0)+",D:"+(zipFile.canExecute()?1:0)+"]))");
		if(renameOk != 1)
			Main.sendMessage("Warn: Cant create temp file. Use .copy file");
		byte[] buf = new byte[Configuration.getLoadingBufferSize()];
		Main.sendMessage(ChatColorUtils.COLOR_CHAR+"aBuffer size: "+ChatColorUtils.COLOR_CHAR+"e"+buf.length);
		ZipInputStream zin = new ZipInputStream(new FileInputStream(tempFile));
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));

		ZipEntry entry = zin.getNextEntry();
		while (entry != null){
			String path_name = entry.getName().replaceAll("/", "\\.");
			boolean notReplace = true;
			for(String f : names)
				if(f.equals(path_name)){
					notReplace = false;
					break;
				}
			if(notReplace){
				out.putNextEntry(new ZipEntry(entry.getName()));
				int len;
				while ((len = zin.read(buf)) > 0)
					out.write(buf, 0, len);
			}
			entry = zin.getNextEntry();
		}
		zin.close();
		for(int i = 0;i < names.length;i++){
			InputStream in = ins[i];
			int index = names[i].lastIndexOf('.');
			out.putNextEntry(new ZipEntry(names[i].substring(0, index).replaceAll("\\.", "/") + names[i].substring(index)));
			int len;
			while ((len = in.read(buf)) > 0)
				out.write(buf, 0, len);
			out.closeEntry();
			in.close();
		}
		out.close();
		tempFile.delete();
		if(renameOk == -1){
			System.exit(-1);
		}
	}
}
