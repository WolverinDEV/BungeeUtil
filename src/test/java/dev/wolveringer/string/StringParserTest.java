package dev.wolveringer.string;

import org.junit.Test;

import dev.wolveringer.bungeeutil.plugin.StringParser;

public class StringParserTest {

	@Test
	public void test() {
		StringParser parser = new StringParser();
		parser.registerString("test", new StringParser.CostumString("X${test}(message=\"Hello world xD\")Y", "A test message", null));
		System.out.println("Message: "+parser.getString("start: ${test}(message=\"Hello world xD\") :end", 5));
	}

}
