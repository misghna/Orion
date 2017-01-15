package com.sesnu.orion.dao;

import java.util.List;

import com.sesnu.orion.web.model.Bid;
import com.sesnu.orion.web.model.BidView;
import com.sesnu.orion.web.model.Client;
import com.sesnu.orion.web.model.DuLicense;
import com.sesnu.orion.web.model.DuLicenseView;




public interface ClientDAO {
	
	public List<Client> listAll();
	
	public Client get(long id);
	
	public void saveOrUpdate(Client client);
	
	public void delete(Client client);

	
}