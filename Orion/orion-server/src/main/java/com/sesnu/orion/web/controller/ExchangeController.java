package com.sesnu.orion.web.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sesnu.orion.dao.ExchangeDAO;
import com.sesnu.orion.web.model.Currency;
import com.sesnu.orion.web.model.Exchange;
import com.sesnu.orion.web.utility.Util;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class ExchangeController {

	
	@Autowired
	ExchangeDAO exchangeDao;
	
	@Autowired Util util;
	

	@RequestMapping(value = "/api/user/exchange", method = RequestMethod.GET)
	public @ResponseBody List<Exchange> exchange(HttpServletResponse response) throws IOException {

		List<Exchange> exchange = exchangeDao.listAll();

		if(exchange.size()>0){
			return exchange;
		}
		response.sendError(404);
		return null;
	}
	
	
	@RequestMapping(value = "/api/user/currency", method = RequestMethod.GET)
	public @ResponseBody List<Currency> currency(HttpServletResponse response) throws IOException {

		List<Currency> currency = exchangeDao.listAllCurrencies();

		if(currency.size()>0){
			return currency;
		}
		response.sendError(404);
		return null;
	}

	@RequestMapping(value = "/api/user/exchange", method = RequestMethod.POST)
	public @ResponseBody List<Exchange> addItem(HttpServletResponse response,@RequestBody Exchange curr)
			throws Exception {
		
		Exchange portFee1 = exchangeDao.getByCurrency(curr);
		if(portFee1!=null){
			response.sendError(400,Util.parseError("Currency exchange rate already exists, edit the existing instead of adding new"));
			return null;
		}
		
		Currency cur = exchangeDao.getByAbrev(curr.getFromCurrency());
		if(cur==null){
			response.sendError(400,Util.parseError("Invalid currency abbrevation in From, please select from list"));
			return null;
		}
		
		cur = exchangeDao.getByAbrev(curr.getToCurrency());
		if(cur==null){
			response.sendError(400,Util.parseError("Invalid currency abbrevation in To, please select from list"));
			return null;
		}
		
		curr.setUpdatedOn(Util.parseDate(new Date(),"/"));
		curr.setId(null);
		exchangeDao.saveOrUpdate(curr);
		
		List<Exchange> curencies = exchangeDao.listAll();
		if(curencies.size()>0){
			return curencies;
		}
		response.sendError(404);
		return null;	

	}
	
	
	@RequestMapping(value = "/api/user/exchange", method = RequestMethod.PUT)
	public @ResponseBody List<Exchange> updateItem(HttpServletResponse response,
			@RequestBody Exchange curr)
			throws Exception {
		
		
		if(exchangeDao.get(curr.getId())==null){
			response.sendError(400);
			return null;
		}
				
		curr.setUpdatedOn(Util.parseDate(new Date(),"/"));
		exchangeDao.saveOrUpdate(curr);
		
		List<Exchange> currencies = exchangeDao.listAll();
		if(currencies.size()>0){
			return currencies;
		}
		response.sendError(404);
		return null;
	}
	
	@RequestMapping(value = "/api/user/exchange/{id}", method = RequestMethod.DELETE)
	public @ResponseBody List<Exchange> updateItem(HttpServletResponse response,@PathVariable("id") long id)
			throws Exception {
		
		Exchange curr = exchangeDao.get(id);
		if(curr==null){
			response.sendError(404);
			return null;
		}
				
		exchangeDao.delete(curr);
		
		List<Exchange> currencies = exchangeDao.listAll();
		if(currencies.size()>0){
			return currencies;
		}
		response.sendError(404);
		return null;

	}
	
			

}
