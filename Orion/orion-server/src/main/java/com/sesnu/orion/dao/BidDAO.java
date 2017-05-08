package com.sesnu.orion.dao;

import java.util.List;

import com.sesnu.orion.web.model.Bid;
import com.sesnu.orion.web.model.BidView;




public interface BidDAO {
	
	public List<BidView> list(long orderRef);

	public void saveOrUpdate(Bid bid);
	
	public Bid getBidWinner(long orderRef);
	
	public List<Bid> setBidWinner(long bidId);

	public Bid get(long id);
	
	public void delete(Bid bid);

	public List<BidView> listAll();



	
}