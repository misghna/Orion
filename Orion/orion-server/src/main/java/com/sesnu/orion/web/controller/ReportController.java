package com.sesnu.orion.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sesnu.orion.dao.BidDAO;
import com.sesnu.orion.dao.PaymentDAO;
import com.sesnu.orion.web.model.Approval;
import com.sesnu.orion.web.model.Bid;
import com.sesnu.orion.web.model.Payment;
import com.sesnu.orion.web.service.ReportService;
import com.sesnu.orion.web.utility.ConfigFile;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class ReportController {

	
	@Autowired ReportService report;
	@Autowired BidDAO bidDao;
	@Autowired PaymentDAO payDao;
	@Autowired ConfigFile config;

	
	
	@RequestMapping(value = "/api/user/report/preview", method = RequestMethod.POST)
	public @ResponseBody String generateOrderAuthPreview(HttpServletRequest request,@RequestBody Approval app)
			throws Exception {
		String preview =null;
		if(app.getType().equals("Order Authorization")){
			preview = report.generateOrderAuthReport(app,"preview");
		}else if(app.getType().equals("Payment")){
			preview = report.generatePayAuthReport(app,"preview");									
		}else if(app.getType().equals("Exporter Margin")){
			preview = report.generateExporterAprvReport(app,"preview");									
		}
		
		return preview;
	}
	

	
}
