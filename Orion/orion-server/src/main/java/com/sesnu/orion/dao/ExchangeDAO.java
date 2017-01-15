package com.sesnu.orion.dao;

import java.util.List;

import com.sesnu.orion.web.model.Currency;
import com.sesnu.orion.web.model.Exchange;



public interface ExchangeDAO {
	
	
	public List<Exchange> listAll();
		
	public void saveOrUpdate(Exchange curr);

	public Exchange get(long id);
	
	public Double getAvg(String type, String from, String to);

	public void delete(Exchange curr);

	public Exchange getByCurrency(Exchange curr);
	
	public List<Exchange> getByTypeName(String type, String name);
	
	public Exchange get(String name, String type, String from, String to);
	
	public Currency getByAbrev(String Abrev);
	
	public List<Currency> listAllCurrencies();
	
}