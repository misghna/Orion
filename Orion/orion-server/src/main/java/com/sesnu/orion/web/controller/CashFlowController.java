package com.sesnu.orion.web.controller;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.ClientProtocolException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sesnu.orion.dao.BidDAO;
import com.sesnu.orion.dao.ItemDAO;
import com.sesnu.orion.dao.OrderDAO;
import com.sesnu.orion.dao.PaymentDAO;
import com.sesnu.orion.dao.SalesPlanDAO;
import com.sesnu.orion.dao.ShippingDAO;
import com.sesnu.orion.dao.StatusDAO;
import com.sesnu.orion.dao.SummaryDAO;
import com.sesnu.orion.dao.UserDAO;
import com.sesnu.orion.dao.OrderDAO;
import com.sesnu.orion.web.model.Item;
import com.sesnu.orion.web.model.Order;
import com.sesnu.orion.web.model.OrderStat;
import com.sesnu.orion.web.model.OrderView;
import com.sesnu.orion.web.model.Required;
import com.sesnu.orion.web.model.Status;
import com.sesnu.orion.web.model.Summary;
import com.sesnu.orion.web.model.User;
import com.sesnu.orion.web.model.Order;
import com.sesnu.orion.web.model.Order;
import com.sesnu.orion.web.utility.Util;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class CashFlowController {

	static List<String> MUST_DOCS = Arrays.asList(new String []{"Order Initiated","Proforma Invoice","Supplier Selected",
																"Order Authorization","Commercial Invoice","Item Shipped"});
   
	JSONObject exchangeRates = new JSONObject();
	
	@Autowired
	SalesPlanDAO salesPlanDao;
	@Autowired Util util;
	@Autowired OrderDAO orderDao;
	@Autowired PaymentDAO payDao;
	@Autowired BidDAO bidao;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/user/cashFlow", method = RequestMethod.POST)
	public @ResponseBody JSONArray cashFlow(HttpServletResponse response,
			@RequestBody JSONObject param) throws IOException {

		Integer year = Integer.parseInt(param.get("year").toString());
		Boolean cummulative = param.get("format").toString().equals("Cummulative");
		String baseCurr =  param.get("currency").toString();
		
		List estimateList =  salesPlanDao.estimatedCashFlow(year, param.get("dest").toString());
		
		List actualList =  payDao.getPaymentHistogram(year, param.get("dest").toString());
		
		List ItemCostList =  bidao.getItemCostHistogram(year, param.get("dest").toString());
		
		try{
			fetchExchangeRates(estimateList,actualList,ItemCostList, baseCurr);
		}catch(Exception e){
			response.sendError(500, "error when getting exchange rates");
			return null;
		}
		
		JSONArray result = new JSONArray();
		
		JSONObject estimate = new JSONObject();
		estimate.put("data", parseCashFlow(estimateList,cummulative,baseCurr));
		estimate.put("label", "Estimate");
		result.add(estimate);
		
		

		JSONObject actual = new JSONObject();
		actual.put("data", parseCashFlow(actualList,cummulative,baseCurr));
		actual.put("label", "Actual");
		result.add(actual);
		
		JSONObject itemCost = new JSONObject();
		itemCost.put("data", parseCashFlow(ItemCostList,cummulative,baseCurr));
		itemCost.put("label", "ItemCost");
		result.add(itemCost);
		
		return result;
	}
	

	
	private JSONArray getEmpty(){
		JSONArray ja = new JSONArray();
		for (int i = 0; i < 12; i++) {
			ja.add(0);
		}
		return ja;
	}
	
	private void fetchExchangeRates(List estimateList, List actualList,List itemCostList,String baseCurr)
			throws ClientProtocolException, IOException, ParseException {
		
		Set<String> currencies = new HashSet<String>();
		
		if(estimateList!=null){
			for (Object obj : estimateList) {
				Object[] records = (Object[]) obj;
				String currency = (String) records[2];
				if(!baseCurr.equals(currency)){
					currencies.add(currency);
				}
			}
		}
		
		if(actualList!=null){
			for (Object obj : actualList) {
				Object[] records = (Object[]) obj;
				String currency = (String) records[2];
				if(!baseCurr.equals(currency)){
					currencies.add(currency);
				}
			}
		}
		
		if(itemCostList!=null){
			for (Object obj : itemCostList) {
				Object[] records = (Object[]) obj;
				String currency = (String) records[2];
				if(!baseCurr.equals(currency)){
					currencies.add(currency);
				}
			}
		}
		
		if(currencies.size()>0){
			exchangeRates = util.getExchangeRates(baseCurr, currencies);
		}
		
	}


	private JSONArray parseCashFlow(List list,boolean cummlative,String baseCurr){
		if(list==null || list.size()==0){
			return getEmpty();
		}
		double totalVal = 0;
		JSONArray result = new JSONArray();
		for (int month=1 ;  month<13 ; month++) {
			double val = proccessMonth(list,month,baseCurr);
			totalVal += val;
			if(cummlative){
				result.add(Math.round(totalVal));
			}else{
				result.add(Math.round(val));
			}
		}	
		return result;
	}
	
	private double proccessMonth(List list,Integer month,String baseCurr){
		double val = 0;
		if(list!=null){
			for (Object object : list) {
				Object[] records = (Object[]) object;
					Integer aMonth = Integer.parseInt(records[1].toString());
					String currency = (String) records[2];
					double amount = Double.parseDouble(records[0].toString());
					if(!currency.equals(baseCurr)){
						double exchangeRate = Double.parseDouble(exchangeRates.get(baseCurr.concat(currency)).toString());
						amount =  amount/exchangeRate;
					}
					if(aMonth.equals(month)){
						val += amount;
					}
			}
		}
		return val;
	}
	
	
}
