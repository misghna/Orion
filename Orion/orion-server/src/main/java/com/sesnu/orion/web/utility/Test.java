package com.sesnu.orion.web.utility;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.client.ClientProtocolException;
import org.json.simple.parser.ParseException;

public class Test {

	public static void main(String[] args) throws ClientProtocolException, IOException, ParseException {
		Set currencies = new HashSet<String>();
		currencies.add("GBP");
		currencies.add("EUR");
		currencies.add("AOA");
		
		
		Util util = new Util();
		
		System.out.println(util.getExchangeRates("USD", currencies));

	}

}
