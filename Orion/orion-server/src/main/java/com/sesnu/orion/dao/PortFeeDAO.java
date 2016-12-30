package com.sesnu.orion.dao;

import java.util.List;

import com.sesnu.orion.web.model.PortFee;



public interface PortFeeDAO {
	
	
	public List<PortFee> listAll();
	
	public List<String> listAllAgency();
	
	public void saveOrUpdate(PortFee ship);

	public PortFee get(long id);

	public void delete(PortFee ship);

	public PortFee getByName(String agency);
	
}