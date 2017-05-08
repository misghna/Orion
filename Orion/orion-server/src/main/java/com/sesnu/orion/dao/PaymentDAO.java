package com.sesnu.orion.dao;

import java.util.List;

import com.sesnu.orion.web.model.PayView;
import com.sesnu.orion.web.model.Payment;




public interface PaymentDAO {
	
	public List<PayView> listByOrderRef(long orderRefId);

	public List<PayView> listAll();
	
	public void saveOrUpdate(Payment pay);

	public Payment get(long id);

	public void delete(Payment pay);
	
	public List getPaymentHistogram(int year,String dest);


	
}