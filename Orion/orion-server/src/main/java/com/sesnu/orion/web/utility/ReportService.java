package com.sesnu.orion.web.utility;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lowagie.text.DocumentException;
import com.sesnu.orion.web.model.Bid;
import com.sesnu.orion.web.model.Order;

@Component
public class ReportService {

	
	@Autowired private ConfigFile conf;
	@Autowired private Util util;
	
	
	public String generateOrderAuthReport(Order order, Bid bid) 
			throws DocumentException, IOException{
		
		String orginalFile = conf.getFile("orderAuth.html");
		// process the file
		
		String pdfFilePath = util.convertToPdf(orginalFile);
		return pdfFilePath;
	}
	
	
	
}
