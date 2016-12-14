package com.sesnu.orion.dao;

import java.util.List;

import com.sesnu.orion.web.model.Shipping;



public interface ShippingDAO {
	
	public List<Shipping> list(long orderRef);
	
	public void saveOrUpdate(Shipping ship);

	public Shipping get(long id);

	public void delete(Shipping ship);
	
}