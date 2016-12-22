package com.sesnu.orion.dao;

import java.util.List;

import com.sesnu.orion.web.model.Shipping;
import com.sesnu.orion.web.model.ShippingView;



public interface ShippingDAO {
	
	public List<ShippingView> listByOrderId(long orderRef);
	
	public List<ShippingView> listAll();
	
	public void saveOrUpdate(Shipping ship);

	public Shipping get(long id);

	public void delete(Shipping ship);
	
}