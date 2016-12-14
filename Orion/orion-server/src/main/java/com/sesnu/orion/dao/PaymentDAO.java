package com.sesnu.orion.dao;

import java.util.List;

import com.sesnu.orion.web.model.Payment;




public interface PaymentDAO {
	
	public List<Payment> list(long orderRef);

	public void saveOrUpdate(Payment pay);

	public Payment get(long id);

	public void delete(Payment pay);


	
}