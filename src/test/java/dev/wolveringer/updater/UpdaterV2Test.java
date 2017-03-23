package dev.wolveringer.updater;

//import static org.junit.Assert.*;

import org.junit.Test;

public class UpdaterV2Test {

	//Dont do this test. It need a valid connection
	@Test
	public void test() {
		System.setProperty("updater.version", "2.1.0");
		
		/*
		UpdaterV2 updater = new UpdaterV2("https://raw.githubusercontent.com/WolverinDEV/BungeeUtil/jars/BungeeUtil.json");
		if(!updater.loadData())
			fail("Cant load update data");
		if(!updater.isValid())
			fail("Updater isnt valid!");
		
		System.out.println("Current Version: "+updater.getOwnVersion().getVersion());
		System.out.println("Newest Version: "+updater.getNewestVersion().getVersion());
		System.out.println("Versions between: "+updater.getVersionsBehind());
		System.out.println("Newest data: "+updater.getVersionsData(updater.getNewestVersion()));
		*/
	}

}
