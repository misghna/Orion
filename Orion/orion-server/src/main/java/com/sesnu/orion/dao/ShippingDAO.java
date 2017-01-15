package com.sesnu.orion.dao;

import java.math.BigInteger;
import java.util.List;

import com.sesnu.orion.web.model.Shipping;
import com.sesnu.orion.web.model.ShippingView;



public interface ShippingDAO {
	
	public List<ShippingView> listByOrderId(long orderRef);
	
	public List<ShippingView> listAll();
	
	public List<ShippingView> listByBL(String bl);

	public List<BigInteger> inTransitList();
	
	public List<BigInteger> inPortList();
	
	public List<BigInteger> inTerminalList();
	
	public void saveOrUpdate(Shipping ship);

	public Shipping get(long id);

	public void delete(Shipping ship);
	
	public BigInteger InTransitCount(long itemId);
	
	public BigInteger InPortCount(long itemId);
	
	public BigInteger InTerminalCount(long itemId);
	
}