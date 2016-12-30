package com.sesnu.orion.dao;

import java.util.List;

import com.sesnu.orion.web.model.Bid;




public interface BidDAO {
	
	public List<Bid> list(long orderRef);

	public void saveOrUpdate(Bid bid);
	
	public List<Bid> getBidWinner(long orderRef);
	
	public List<Bid> setBidWinner(long bidId);

	public Bid get(long id);
	
	public void delete(Bid bid);

	public List<Bid> listAll();



	
}