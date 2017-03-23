package com.sesnu.orion.dao;

import java.util.List;

import com.sesnu.orion.web.model.Bid;
import com.sesnu.orion.web.model.BidView;
import com.sesnu.orion.web.model.AddressBook;
import com.sesnu.orion.web.model.DuLicense;
import com.sesnu.orion.web.model.DuLicenseView;




public interface AddressBookDAO {
	
	public List<AddressBook> listAll();
		
	public List<AddressBook> listAllFWAgents();
	
	public List<AddressBook> listAllDest();
	
	public AddressBook getByName(String name);
	
	public List<AddressBook> getByType(String name);
	
	public AddressBook get(long id);
	
	public void saveOrUpdate(AddressBook client);
	
	public void delete(AddressBook client);

	
}