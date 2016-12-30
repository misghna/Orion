package com.sesnu.orion.web.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sesnu.orion.dao.ItemDAO;
import com.sesnu.orion.dao.OrderDAO;
import com.sesnu.orion.dao.SummaryDAO;
import com.sesnu.orion.dao.UserDAO;
import com.sesnu.orion.dao.OrderDAO;
import com.sesnu.orion.web.model.Item;
import com.sesnu.orion.web.model.Order;
import com.sesnu.orion.web.model.Summary;
import com.sesnu.orion.web.model.User;
import com.sesnu.orion.web.model.Order;
import com.sesnu.orion.web.model.Order;
import com.sesnu.orion.web.utility.ReportService;
import com.sesnu.orion.web.utility.Util;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class ReportController {

	
	@Autowired
	ReportService report;


	
	
	@RequestMapping(value = "/api/user/report/generateOrderAuth", method = RequestMethod.POST)
	public @ResponseBody String updateHomeColorSet(HttpServletRequest request)
			throws Exception {

		report.generateOrderAuthReport(null, null);
		return "done";
	}
	
	
}
