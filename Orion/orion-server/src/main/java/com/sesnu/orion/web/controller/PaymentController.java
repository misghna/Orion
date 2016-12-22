package com.sesnu.orion.web.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sesnu.orion.dao.PaymentDAO;
import com.sesnu.orion.web.model.PayView;
import com.sesnu.orion.web.model.Payment;
import com.sesnu.orion.web.utility.Util;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class PaymentController {

	
	@Autowired
	PaymentDAO payDao;

	@Autowired Util util;
	

	@RequestMapping(value = "/api/user/pay/{orderRef}", method = RequestMethod.GET)
	public @ResponseBody List<PayView> items(@PathVariable("orderRef") String orderRef,
			HttpServletResponse response) throws IOException {

		List<PayView> pays =null;
		if(orderRef.equals("all")){
			 pays = payDao.listAll();
		}else{
		 pays = payDao.listByOrderRef(Long.parseLong(orderRef));
		}
		if(pays.size()>0){
			return pays;
		}
		response.sendError(404);
		return null;
	}
	
	
	@RequestMapping(value = "/api/user/pay", method = RequestMethod.POST)
	public @ResponseBody List<PayView> addItem(HttpServletResponse response,@RequestBody Payment pay)
			throws Exception {
		
		pay.setUpdatedOn(Util.parseDate(new Date(),"/"));
		pay.setId(null);
		payDao.saveOrUpdate(pay);
		
		List<PayView> pays = payDao.listByOrderRef(pay.getOrderRef());
		if(pays.size()>0){
			return pays;
		}
		response.sendError(404);
		return null;

	}
	
	
	@RequestMapping(value = "/api/user/pay", method = RequestMethod.PUT)
	public @ResponseBody List<PayView> updateItem(HttpServletResponse response,
			@RequestBody Payment pay)
			throws Exception {
		
		if(payDao.get(pay.getId())==null){
			response.sendError(400);
			return null;
		}
		pay.setUpdatedOn(Util.parseDate(new Date(),"/"));
		payDao.saveOrUpdate(pay);
		
		List<PayView> Payments = payDao.listByOrderRef(pay.getOrderRef());
		if(Payments.size()>0){
			return Payments;
		}
		response.sendError(404);
		return null;

	}
	
	

	@RequestMapping(value = "/api/user/pay/{id}", method = RequestMethod.DELETE)
	
	public @ResponseBody List<PayView> deleteItem(@PathVariable("id") long id,
			HttpServletResponse response) throws Exception {
		Payment pay = payDao.get(id);
		long orderRef= pay.getOrderRef();
		if(pay != null){
			payDao.delete(pay);
			List<PayView> pays = payDao.listByOrderRef(orderRef);
			if(pays.size()>0){
				return pays;
			}
		}
		response.sendError(400);
		return null;
	}
	
		

}
