package com.sesnu.orion.dao;

import java.util.List;

import com.sesnu.orion.web.model.Bid;
import com.sesnu.orion.web.model.BidView;
import com.sesnu.orion.web.model.DuLicense;
import com.sesnu.orion.web.model.DuLicenseView;




public interface DuLicenseDAO {
	
	public List<DuLicenseView> listAll();

	public List<DuLicenseView> listByOrderId(long orderRef);
	
	public DuLicense get(long id);
	
	public void saveOrUpdate(DuLicense bid);
	
	public void delete(DuLicense bid);

//	public List<DuLicense> listAll();



	
}