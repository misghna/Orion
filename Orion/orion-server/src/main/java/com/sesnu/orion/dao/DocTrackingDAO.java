package com.sesnu.orion.dao;

import java.util.List;

import com.sesnu.orion.web.model.Bid;
import com.sesnu.orion.web.model.BidView;
import com.sesnu.orion.web.model.DocTracking;
import com.sesnu.orion.web.model.DocTrackingView;
import com.sesnu.orion.web.model.DuLicense;
import com.sesnu.orion.web.model.DuLicenseView;




public interface DocTrackingDAO {
	
	public List<DocTrackingView> listAll();

	public List<DocTrackingView> listByOrderId(long orderRef);
	
	public DocTracking get(long id);
	
	public void saveOrUpdate(DocTracking docTrack);
	
	public void delete(DocTracking 	docTrack);




	
}