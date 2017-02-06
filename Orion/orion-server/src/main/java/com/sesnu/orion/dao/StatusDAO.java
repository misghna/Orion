package com.sesnu.orion.dao;

import java.util.List;

import com.sesnu.orion.web.model.Bid;
import com.sesnu.orion.web.model.OrderStat;
import com.sesnu.orion.web.model.Required;
import com.sesnu.orion.web.model.Status;




public interface StatusDAO {
	
	public List<Status> list(long orderRef);
	
	public List<Status> list(long orderRef,String type);
	
	public List<OrderStat> listOrderStat(long orderRef);
	
	public List<String> listName(long orderRef, String type);
	
	public List<Required> listRequired(String typeType, String type);

	public List<Required> listAllRequired();
	
	public List<Required> listRequiredByType(String type);
	
	
	
}