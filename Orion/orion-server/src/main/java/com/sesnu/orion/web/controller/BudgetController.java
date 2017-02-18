package com.sesnu.orion.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sesnu.orion.dao.BudgetDAO;
import com.sesnu.orion.dao.ItemDAO;
import com.sesnu.orion.dao.SalesPlanDAO;
import com.sesnu.orion.web.model.Item;
import com.sesnu.orion.web.model.OrderView;
import com.sesnu.orion.web.model.SalesPlan;
import com.sesnu.orion.web.model.SalesView;
import com.sesnu.orion.web.service.EstimatorService;
import com.sesnu.orion.web.model.Bid;
import com.sesnu.orion.web.model.Budget;
import com.sesnu.orion.web.model.Estimate;
import com.sesnu.orion.web.utility.Util;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class BudgetController {

	
	@Autowired BudgetDAO badgetDao;
	@Autowired SalesPlanDAO salesPlanDao;;
	@Autowired Util util;
	@Autowired EstimatorService estService;
	@Autowired ItemDAO itemDao;
	

	@RequestMapping(value = "/api/user/budget/{year}/{month}", method = RequestMethod.GET)
	public @ResponseBody List<Budget> items(@PathVariable("year") int year,
			@PathVariable("month") String month,HttpServletResponse response)
						throws IOException {
		List<SalesView> salesViews =null;
		if(year==0 || month.equals("latest")){
			salesViews = getLatestSalesPlan();
		}else{
			salesViews = salesPlanDao.list(year,month.trim());
		}
		
		if(salesViews.size()<0){
			response.sendError(404,Util.parseError("Sales Plan not found"));
			return null;
		}
		
		List<Budget> budgets = new ArrayList<Budget>();
		for (SalesView sp : salesViews) {
			budgets.add(generateBudget(sp));
		}
		
		return budgets;
	}
	
	
	private Budget generateBudget(SalesView sp){
		Budget budget = new Budget(sp);
		OrderView order = new OrderView(sp);
		Bid bid = new Bid();
		bid.setTotalBid(sp.getPckPerCont()*sp.getContQnt()*sp.getCif());
		bid.setCurrency("USD");
	    Item item = itemDao.get(sp.getItemId());
		Estimate est = estService.totalEstimate(order, null, bid, item);
		
		JSONObject detail = est.getDetails();		
		budget.setBromangol(Double.parseDouble(((JSONObject)detail.get("Bromangol")).get("Total").toString()));
		budget.setShippingAgency(Double.parseDouble(((JSONObject)detail.get("Shipping Agency")).get("Total").toString()));
		budget.setLicense(Double.parseDouble(((JSONObject)detail.get("License")).get("Total").toString()));
		budget.setCustoms(Double.parseDouble(((JSONObject)detail.get("Customs")).get("Total").toString()));	
		budget.setTransport(Double.parseDouble(((JSONObject)detail.get("Transport")).get("Total").toString()));		
		budget.setTerminal(Double.parseDouble(((JSONObject)detail.get("Terminal")).get("Total").toString()));		
		budget.setPort(Double.parseDouble(((JSONObject)detail.get("Port")).get("Total").toString()));		
		budget.setCertQuality(Double.parseDouble(((JSONObject)detail.get("Certificate Of Quality")).get("Total").toString()));		
		budget.setCertHealth(Double.parseDouble(((JSONObject)detail.get("Agriculture Phyto.")).get("Total").toString()));		
		budget.setLocalPhytosanitary(Double.parseDouble(((JSONObject)detail.get("Phytosanitary")).get("Total").toString()));	
		budget.setForwardAgency(Double.parseDouble(((JSONObject)detail.get("Forwarding Agent")).get("Total").toString()));

		JSONObject summary = est.getSummary();
		budget.setTotalCif(Double.parseDouble(summary.get("TotalCIFUSD").toString()));
		budget.setTotalFees(Double.parseDouble(summary.get("TotalFees").toString()));
		budget.setTotalCost(Double.parseDouble(summary.get("TotalCost").toString()));
		budget.setCostPerPack(Double.parseDouble(summary.get("CostPerPack").toString()));
		budget.setPriceAt12(Double.parseDouble(summary.get("pricePerPack").toString()));
		budget.setPriceAt12usd(Double.parseDouble(summary.get("pricePerPackUsd").toString()));
		
		budget.setUpdatedOn(Util.parseDate(new Date()));
		
		return budget;
		
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
