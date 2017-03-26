package dev.wolveringer.string;

import java.util.HashMap;

import org.junit.Test;

import dev.wolveringer.bungeeutil.plugin.StringParser;
import dev.wolveringer.bungeeutil.plugin.StringParser.CostumString;

public class StringParserTest {

	@Test
	public void test() {
		StringParser parser = new StringParser();
		
		CostumString downloadString = new CostumString("https://github.com/WolverinDEV/BungeeUtil/raw/jars/buildedJars/standalone/BungeeUtil-%version%.jar", "The basic download url", new HashMap<>());
		downloadString.getDefaults().put("version", "unknown");
		
		parser.registerString("download.github", downloadString);
		System.out.println("Message: "+parser.getString("${download.github}(version=2.4.2)", 5));
	}

}
