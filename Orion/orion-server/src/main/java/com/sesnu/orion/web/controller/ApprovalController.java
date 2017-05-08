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
import com.sesnu.orion.dao.NotificationDAO;
import com.sesnu.orion.dao.OrderDAO;
import com.sesnu.orion.dao.PaymentDAO;
import com.sesnu.orion.dao.UserDAO;
import com.sesnu.orion.web.model.Approval;
import com.sesnu.orion.web.model.ApprovalView;
import com.sesnu.orion.web.model.Bid;
import com.sesnu.orion.web.model.Notification;
import com.sesnu.orion.web.model.Order;
import com.sesnu.orion.web.model.OrderView;
import com.sesnu.orion.web.model.Payment;
import com.sesnu.orion.web.model.User;
import com.sesnu.orion.web.service.NotificationService;
import com.sesnu.orion.web.service.ReportService;
import com.sesnu.orion.web.utility.ConfigFile;
import com.sesnu.orion.web.utility.Util;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class ApprovalController {

	
	@Autowired ApprovalDAO aprvDao;
	@Autowired Util util;
	@Autowired private ConfigFile conf;
	@Autowired private UserDAO userDao;
	@Autowired private BidDAO bidDao;
	@Autowired private PaymentDAO payDao;
	@Autowired private ReportService repoService;
	@Autowired private NotificationDAO notifDao;
	@Autowired private OrderDAO orderDao;
	
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
		else if(aprvls.size()>0 && aprvls.get(0).getApprover().equals(aprv.getApprover()) && !aprvls.get(0).getStatus().equals("Void")){
			response.sendError(400,Util.parseError("Approval request already exists"));
			return null;
		}
		
		User user= getActiveUser(request);

		aprv.setRequestedBy(user.getFullname());
		aprv.setStatus("pending");
		aprv.setRequestedOn(new Date());
		aprvDao.saveOrUpdate(aprv);
				
		if(aprv.getType().trim().equals("Order Authorization")){
			sendReqNotif(user,aprv,"Order Authorization");
			Bid bid = bidDao.get(aprv.getForId());
			bid.setApproval("Pending Approval");
			bidDao.saveOrUpdate(bid);
			response.sendError(200,"ok");
			return "success";
		}else if(aprv.getType().trim().equals("Exporter Margin")){
			sendReqNotif(user,aprv,"Exporter Margin");
			Order order = orderDao.getOrder(aprv.getForId());
			order.setStatus("Pending Approval");
			orderDao.saveOrUpdate(order);
			response.sendError(200,"ok");
			return "success";
		}else{
			sendReqNotif(user,aprv,"Payment(" + aprv.getForName() + ")");
			Payment pay = payDao.get(aprv.getForId());
			pay.setStatus("Pending Approval");
			payDao.saveOrUpdate(pay);
			
			response.sendError(200,"ok");
			return "success";
		}

	}
	
	private void sendReqNotif(User requester,Approval aprv,String type){
		User approver= userDao.getUserByName(aprv.getApprover());
		Order order = orderDao.getOrder(aprv.getOrderRef());
		StringBuilder msg = new StringBuilder();
		msg.append("Hello  "+approver.getFullname()+",\n\n ");
		msg.append(requester.getFullname() + " is requesting your approval for " + type + "\n");
		msg.append("this request is with regards to Order ref " + order.getInvNo() + " of " + order.getItem() + "(" + order.getBrand() + ")");
		String emailPostFix = ("\n\n Do not replay to this email, this is an automated message.\n\n Thank you!!");
		util.sendText(msg.toString(), approver.getPhone());
		Util.sendMail("Approval Request", approver.getEmail() ,msg.toString() + emailPostFix);
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
		
		if(aprv.getStatus().indexOf("Void")>-1){
			response.sendError(400,Util.parseError("This request is void, please create new request"));
			return null;
		}
		
		User user= getActiveUser(request);	
		if(!aprv.getApprover().equals(user.getFullname())){
			response.sendError(400,Util.parseError("Only " + aprv.getApprover() + " can approve this request."));
			return null;
		}
		
		List<ApprovalView> aprList = aprvDao.listByOrderRef(aprv.getOrderRef());
		for (ApprovalView ap : aprList) {
			if(ap.getStatus().equals("Approved") && ap.getType().equals(aprv.getType()) && 
					(ap.getType().equals("Order Authorization") || ap.getType().equals("Exporter Margin"))){
				response.sendError(400,Util.parseError("A identical request has been already approved, check for duplicates"));
				return null;
			}
			
		}
		
		
		aprv.setStatus("Approved");
		aprv.setApprovedOn(new Date());
		aprvDao.saveOrUpdate(aprv);
		
		if(aprv.getType().equals("Order Authorization")){
		//	sendAprvNotif(user,aprv,"Order Authorization");
			Bid bid = bidDao.get(aprv.getForId());
			bid.setApproval("Approved");
			bidDao.saveOrUpdate(bid);
			repoService.generateOrderAuthReport(aprv,"actual");
			
			return "success";
		}else if(aprv.getType().equals("Exporter Margin")){
		//	sendAprvNotif(user,aprv,"Exporter Margin");
			Order order = orderDao.getOrder(aprv.getForId());
			order.setStatus("Approved");
			orderDao.saveOrUpdate(order);
			repoService.generateExporterAprvReport(aprv,"actual");			
			return "success";
		}else{
		//	sendAprvNotif(user,aprv,"Payment(" + aprv.getForName() + ")");
			Payment pay = payDao.get(aprv.getForId());
			pay.setStatus("Approved");
			payDao.saveOrUpdate(pay);
	
			// register for notification
			Notification notif = new Notification(pay.getName(),"Payment",pay.getOrderRef());			
			notifDao.saveOrUpdate(notif);
			
			repoService.generatePayAuthReport(aprv, "actual");
			return "success";
		}
		
	}
	
	
//	private void sendAprvNotif(User requester,Approval aprv,String type){
//		User approver= userDao.getUserByName(aprv.getApprover());
//		Order order = orderDao.getOrder(aprv.getOrderRef());
//		StringBuilder msg = new StringBuilder();
//		msg.append("Hello  "+requester.getFullname()+",\n\n ");
//		msg.append(approver.getFullname() + " has approved your request for " + type + "\n");
//		msg.append("this approval is with regards to Order ref " + order.getInvNo() + " of " + order.getItem() + "(" + order.getBrand() + ")");
//		String emailPostFix = ("\n\n Do not replay to this email, this is an automated message.\n\n Thank you!!");
//		util.sendText(msg.toString(), approver.getPhone());
//		Util.sendMail("Approval Request", approver.getEmail() ,msg.toString() + emailPostFix);
//	}
	
	
	@RequestMapping(value = "/api/user/approval/void/{id}", method = RequestMethod.PUT)
	public @ResponseBody List<ApprovalView> updateItem(HttpServletResponse response,
			@PathVariable("id") long id)
			throws Exception {
		
		Approval ap = aprvDao.get(id);
		if(ap==null){
			response.sendError(400,"bad request");
			return null;
		}
		ap.setStatus("Void");
		
		aprvDao.saveOrUpdate(ap);
		if(ap.getType().equals("Exporter Margin")){
			Order order = orderDao.getOrder(ap.getForId());
			order.setStatus("Unsigned");
			orderDao.saveOrUpdate(order);
		}
		
		List<ApprovalView> aprvls = aprvDao.listAll();
		if(aprvls.size()>0){
			return aprvls;
		}
		response.sendError(404);
		return null;

	}
	
		

}
