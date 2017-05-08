package com.sesnu.orion.web.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sesnu.orion.dao.ItemDAO;
import com.sesnu.orion.dao.SalesPlanDAO;
import com.sesnu.orion.web.model.Item;
import com.sesnu.orion.web.model.SalesPlan;
import com.sesnu.orion.web.model.SalesView;
import com.sesnu.orion.web.utility.Util;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class SalesPlanController {

	
	@Autowired
	SalesPlanDAO salesPlanDao;
	@Autowired Util util;
	

	@RequestMapping(value = "/api/user/salesPlan/{year}/{month}", method = RequestMethod.GET)
	public @ResponseBody List<SalesView> items(@PathVariable("year") int year,
			@PathVariable("month") String month,HttpServletResponse response)
						throws IOException {
		
		if(year==0 || month.equals("latest")){
			List<SalesView> salesViews = getLatestSalesPlan();
			if(salesViews!=null && salesViews.size()>0){
				return salesViews;
			}
			response.sendError(404);
			return null;
			
		}
		
		List<SalesView> salesViews = salesPlanDao.list(year,month.trim());
		if(salesViews.size()>0){
			return salesViews;
		}
		response.sendError(404);
		return null;
	}
	
	
	@RequestMapping(value = "/api/user/salesPlan/{year}/{month}", method = RequestMethod.POST)
	public @ResponseBody List<SalesView> addItem(HttpServletResponse response,@RequestBody SalesPlan sp,
			@PathVariable("year") int year,@PathVariable("month") String month)
			throws Exception {
		JSONParser parser = new JSONParser();
		
		JSONArray months = (JSONArray) parser.parse(sp.getMonth());
		for (Object monObj : months) {
			String monthName = (String) monObj;
			sp.setUpdatedOn(Util.parseDate(new Date(),"/"));
			sp.setId(null);
			sp.setMonth(monthName);
			sp.setMon(Util.getMonth(sp.getMonth()));
			salesPlanDao.saveOrUpdate(sp);
		}
	
		List<SalesView> salesViews = salesPlanDao.list(sp.getYear(),sp.getMonth());
		if(salesViews.size()>0){
			return salesViews;
		}
		response.sendError(404);
		return null;

	}
	
	
	@RequestMapping(value = "/api/user/salesPlan/{year}/{month}", method = RequestMethod.PUT)
	public @ResponseBody List<SalesView> updateItem(HttpServletResponse response,
			@RequestBody SalesPlan salesPlan,@PathVariable("year") int year,
			@PathVariable("month") String month)
			throws Exception {
		
//		if(salesPlanDao.get(salesView.getId())==null){
//			response.sendError(400);
//			return null;
//		}
//		SalesPlan salesPlan = new SalesPlan(salesView);
		salesPlan.setUpdatedOn(Util.parseDate(new Date(),"/"));
		salesPlan.setMon(Util.getMonth(salesPlan.getMonth()));
		salesPlanDao.saveOrUpdate(salesPlan);
		
		List<SalesView> salesViews = salesPlanDao.list(year,month.trim());
		if(salesViews.size()>0){
			return salesViews;
		}else{
			salesViews = getLatestSalesPlan();
			if(salesViews!=null && salesViews.size()>0){
				return salesViews;				
			}
			response.sendError(404);
			return null;
		}

	}
	
		
	@RequestMapping(value = "api/user/salesPlan/duplicatePlan", method = RequestMethod.POST)
	public @ResponseBody List<SalesView> duplicatePlan(@RequestBody JSONObject prop,HttpServletResponse response)
			throws IOException {
		List<SalesPlan> salesPlans = salesPlanDao.getPlanByTime(Integer.parseInt(prop.get("sourceYear").toString()),
														prop.get("sourceMonth").toString());
	
		int year = Integer.parseInt(prop.get("newYear").toString());
		String month = prop.get("newMonth").toString();
		
		if(salesPlans.size()>0){
			for (SalesPlan sp : salesPlans) {
				 sp.setYear(year);
				 sp.setMonth(month);
				 sp.setMon(Util.getMonth(month));
				 sp.setId(null);
				 salesPlanDao.saveOrUpdate(sp);
			}
		}
		
		List<SalesView> salesViews = salesPlanDao.list(year,month.trim());
		if(salesViews.size()>0){
			return salesViews;
		}
		response.sendError(404);
		return null;
		
	}
	

	@RequestMapping(value = "/api/user/salesPlan/{id}/{year}/{month}", method = RequestMethod.DELETE)
	public @ResponseBody List<SalesView> deleteItem(@PathVariable("id") long id,@PathVariable("year") int year,
			@PathVariable("month") String month,HttpServletResponse response) throws Exception {
		SalesPlan sp = salesPlanDao.get(id);
		if(sp != null){
			salesPlanDao.delete(id);
			List<SalesView> salesViews = salesPlanDao.list(year,month.trim());
			if(salesViews.size()>0){
				return salesViews;
			}else{
				String latestTime = salesPlanDao.getLatest();
				year = Integer.parseInt(latestTime.split("-")[0]);
				month = latestTime.split("-")[1].toString();
				salesViews = salesPlanDao.list(year,month.trim());
				if(salesViews.size()>0){
					return salesViews;
				}else{
					response.sendError(404);
					return null;
				}
			}
		}
		response.sendError(400);
		return null;
	}
	
	@RequestMapping(value = "/api/user/salesPlan/{year}/{month}", method = RequestMethod.DELETE)
	public @ResponseBody List<SalesView> deleteSalesPlan(@PathVariable("year") int year,
			@PathVariable("month") String month,HttpServletResponse response) throws Exception {
		
		List<SalesView> sp =  salesPlanDao.list(year,month.trim());
		if(sp != null){
			salesPlanDao.deleteByTime(year, month);
			String latestTime = salesPlanDao.getLatest();
			year = Integer.parseInt(latestTime.split("-")[0]);
			month = latestTime.split("-")[1].toString();
			List<SalesView> salesViews = salesPlanDao.list(year,month.trim());
			if(salesViews.size()>0){
				return salesViews;
			}else{
				response.sendError(404);
				return null;
			}
		}
		response.sendError(400);
		return null;
	}
	
	private List<SalesView> getLatestSalesPlan(){
		String latestTime = salesPlanDao.getLatest();
		if(latestTime==null){
			return null;
		}
		int year = Integer.parseInt(latestTime.split("-")[0]);
		String month = latestTime.split("-")[1].toString();
		List<SalesView> salesViews = salesPlanDao.list(year,month.trim());
		return salesViews;
	}
}
