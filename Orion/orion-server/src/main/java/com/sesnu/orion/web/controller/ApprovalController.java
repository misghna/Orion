package com.sesnu.orion.web.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sesnu.orion.dao.ApprovalDAO;
import com.sesnu.orion.dao.BidDAO;
import com.sesnu.orion.dao.PaymentDAO;
import com.sesnu.orion.dao.UserDAO;
import com.sesnu.orion.web.model.Approval;
import com.sesnu.orion.web.model.ApprovalView;
import com.sesnu.orion.web.model.Bid;
import com.sesnu.orion.web.model.Payment;
import com.sesnu.orion.web.model.User;
import com.sesnu.orion.web.utility.ConfigFile;
import com.sesnu.orion.web.utility.Util;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class ApprovalController {

	
	@Autowired ApprovalDAO aprvDao;
	@Autowired Util util;
	@Autowired private ConfigFile conf;
	@Autowired UserDAO userDao;
	@Autowired BidDAO bidDao;
	@Autowired PaymentDAO payDao;
	

	@RequestMapping(value = "/api/user/approval/{orderRef}", method = RequestMethod.GET)
	public @ResponseBody List<ApprovalView> byId(@PathVariable("orderRef") String orderRef,
			HttpServletResponse response) throws IOException {

		List<ApprovalView> pays =null;
		if(orderRef.equals("all")){
			 pays = aprvDao.listAll();
		}else{
		 pays = aprvDao.listByOrderRef(Long.parseLong(orderRef));
		}
		if(pays.size()>0){
			return pays;
		}
		response.sendError(404);
		return null;
	}
	
	
	@RequestMapping(value = "/api/user/approval/name/{name}", method = RequestMethod.GET)
	public @ResponseBody List<ApprovalView> byName(@PathVariable("name") String name,
			HttpServletResponse response) throws IOException {

		List<ApprovalView> aprvls = aprvDao.listByApproverName(name);

		if(aprvls.size()>0){
			return aprvls;
		}
		response.sendError(404);
		return null;
	}
	
	
	@RequestMapping(value = "/api/user/approval", method = RequestMethod.POST)
	public @ResponseBody String addItem(HttpServletResponse response,
			HttpServletRequest request, @RequestBody Approval aprv)
			throws Exception {
		
		if(aprv.getOrderRef()==0){
			response.sendError(400, Util.parseError("bad data"));
			return null;
		}
		
		List<ApprovalView> aprvls = aprvDao.listByTypeId(aprv.getType(), aprv.getForId());
		
		if(aprvls.size()>0 && aprvls.get(0).getStatus().equals("Approved")){
			response.sendError(400, Util.parseError("Already Approved"));
			return null;
		}
		else if(aprvls.size()>0 && aprvls.get(0).getApprover().equals(aprv.getApprover())){
			response.sendError(400,Util.parseError("Approval request already exists"));
			return null;
		}
		
		User user= getActiveUser(request);

		aprv.setRequestedBy(user.getFullname());
		aprv.setStatus("pending");
		aprv.setRequestedOn(new Date());
		aprvDao.saveOrUpdate(aprv);
		
		if(aprv.getType().trim().equals("Order Authorization")){
			Bid bid = bidDao.get(aprv.getForId());
			bid.setApproval("Pending Approval");
			bidDao.saveOrUpdate(bid);
			response.sendError(200,"ok");
			return "success";
		}else{
			Payment pay = payDao.get(aprv.getForId());
			pay.setStatus("Pending Approval");
			payDao.saveOrUpdate(pay);
			response.sendError(200,"ok");
			return "success";
		}

	}
	
	private User getActiveUser(HttpServletRequest request){
		User user=null;
		HttpSession session = request.getSession();			
		if(request.getSession().getAttribute("user")!=null){			
			 user = (User) session.getAttribute("user");		
		}else if(conf.getProp().get("devMode").equals("true"))	{
			 user = util.getDevUser(userDao);
		}
		return user;
	}
	
	
	@RequestMapping(value = "/api/user/approve", method = RequestMethod.PUT)
	public @ResponseBody String updateItem(HttpServletResponse response,
			@RequestBody Approval aprv,HttpServletRequest request)
			throws Exception {
		
		if(aprvDao.get(aprv.getId())==null){
			response.sendError(400,Util.parseError("bad data"));
			return null;
		}
		
		if(aprv.getStatus().indexOf("Approved")>-1 || aprv.getStatus().indexOf("Paid")>-1){
			response.sendError(400,Util.parseError("This request is already approved"));
			return null;
		}
		
		User user= getActiveUser(request);	
		if(!aprv.getApprover().equals(user.getFullname())){
			response.sendError(403,Util.parseError("Only " + aprv.getApprover() + " can approve this request."));
			return null;
		}
		
		aprv.setStatus("Approved");
		aprv.setApprovedOn(new Date());
		aprvDao.saveOrUpdate(aprv);
		
		if(aprv.getType().equals("Order Authorization")){
			Bid bid = bidDao.get(aprv.getForId());
			bid.setApproval("Approved");
			bidDao.saveOrUpdate(bid);
			return "success";
		}else{
			Payment pay = payDao.get(aprv.getForId());
			pay.setStatus("Approved");
			payDao.saveOrUpdate(pay);
			return "success";
		}
		
	}
	
//	@RequestMapping(value = "/api/user/approval", method = RequestMethod.PUT)
//	public @ResponseBody List<ApprovalView> updateItem(HttpServletResponse response,
//			@RequestBody Approval aprv)
//			throws Exception {
//		
//		if(aprvDao.get(aprv.getId())==null){
//			response.sendError(400);
//			return null;
//		}
//		aprvDao.saveOrUpdate(aprv);
//		
//		List<ApprovalView> aprvls = aprvDao.listByOrderRef(aprv.getOrderRef());
//		if(aprvls.size()>0){
//			return aprvls;
//		}
//		response.sendError(404);
//		return null;
//
//	}
//	
//	
//
//	@RequestMapping(value = "/api/user/approval/{id}", method = RequestMethod.DELETE)
//	
//	public @ResponseBody List<ApprovalView> deleteItem(@PathVariable("id") long id,
//			HttpServletResponse response) throws Exception {
//		
//		Approval aprv = aprvDao.get(id);
//		if(aprv != null){
//			aprvDao.delete(aprv);
//			List<ApprovalView> pays = aprvDao.listByOrderRef(aprv.getOrderRef());
//			if(pays.size()>0){
//				return pays;
//			}
//		}
//		response.sendError(400);
//		return null;
//	}
	
		

}
