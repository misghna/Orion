package com.sesnu.orion.web.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sesnu.orion.dao.DocTrackingDAO;
import com.sesnu.orion.dao.OrderDAO;
import com.sesnu.orion.web.model.DocTracking;
import com.sesnu.orion.web.model.DocTrackingView;
import com.sesnu.orion.web.model.OrderView;
import com.sesnu.orion.web.service.TrackingService;
import com.sesnu.orion.web.utility.Util;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class DocTrackController {

	
	@Autowired
	DocTrackingDAO docTrackDAO;
	@Autowired Util util;
	@Autowired OrderDAO orderDao;
	@Autowired TrackingService trackService;

	@RequestMapping(value = "/api/user/docTrack/{orderRef}", method = RequestMethod.GET)
	public @ResponseBody List<DocTrackingView> docs(@PathVariable("orderRef") String orderRef,
			HttpServletResponse response) throws IOException {

		List<DocTrackingView> docs2 = null;
		if(orderRef.equals("all")){
			docs2= docTrackDAO.listAll();
		}else{
			docs2= docTrackDAO.listByOrderId(Long.parseLong(orderRef));
		}
		if(docs2.size()>0){
			return docs2;
		}
		response.sendError(404);
		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/user/docTrack/track/{docId}", method = RequestMethod.GET)
	public @ResponseBody String docTrack(@PathVariable("docId") long docId,
			HttpServletResponse response) throws IOException, ParseException {
		
		DocTracking docTrack = docTrackDAO.get(docId);
		JSONObject errorObj = new JSONObject();

		errorObj.put("type", "HTML");
		if(docTrack.getTrackingId()==null || docTrack.getCourier()==null){
			return "please enter courier name and tracking id, and try again!";
		}
		
		switch(docTrack.getCourier()){
			case "DHL":
				return trackService.TrackHDL(docTrack.getTrackingId());
			case "FedEx":
				errorObj.put("data", "Tracking system for this courier not set");
			case "TNT":
				errorObj.put("data","Tracking system for this courier not set");
			case "Other":
				errorObj.put("data","Unknown courier");
			 default :
				 errorObj.put("data", "Proper courier Name not set");			
		}
		return errorObj.toString();
	}

	
	@RequestMapping(value = "/api/user/docTrack/{state}", method = RequestMethod.POST)
	public @ResponseBody List<DocTrackingView> addItem(HttpServletResponse response,
			@RequestBody DocTracking lic,@PathVariable("state") String state)
			throws Exception {
		
		OrderView order = orderDao.get(lic.getOrderRef());
		if(order==null){
			response.sendError(400,Util.parseError("Invalid Inv No, please select from the list"));
			return null;
		}
		
		
		lic.setId(null);
		docTrackDAO.saveOrUpdate(lic);

		List<DocTrackingView> docs2=null;
		if(state.equals("all")){
			docs2 = docTrackDAO.listAll();
		}else{
			docs2 = docTrackDAO.listByOrderId(lic.getOrderRef());
		}
		if(docs2.size()>0){
			return docs2;
		}
		response.sendError(404);
		return null;
		

	}

	
	@RequestMapping(value = "/api/user/docTrack/{state}", method = RequestMethod.PUT)
	public @ResponseBody List<DocTrackingView> updateItem(HttpServletResponse response,
			@PathVariable("state") String state,
			@RequestBody DocTracking lic2)
			throws Exception {
		
		
		if(docTrackDAO.get(lic2.getId())==null){
			response.sendError(400);
			return null;
		}
		
		docTrackDAO.saveOrUpdate(lic2);
		List<DocTrackingView> docs2=null;
	
		if(state.equals("all")){
			docs2 = docTrackDAO.listAll();
		}else{
			docs2 = docTrackDAO.listByOrderId(lic2.getOrderRef());
		}
		if(docs2.size()>0){
			return docs2;
		}
		response.sendError(404);
		return null;

	}
	
	

	@RequestMapping(value = "/api/user/docTrack/{id}/{state}", method = RequestMethod.DELETE)
	
	public @ResponseBody List<DocTrackingView> deleteItem(@PathVariable("id") long id,@PathVariable("state") String state,
			HttpServletResponse response) throws Exception {
		DocTracking lic2 = docTrackDAO.get(id);
	
		if(lic2 != null){
			docTrackDAO.delete(lic2);
			List<DocTrackingView> docs2=null;			
		
			if(state.equals("all")){
				docs2 = docTrackDAO.listAll();
			}else{
				docs2 = docTrackDAO.listByOrderId(lic2.getOrderRef());
			}
			if(docs2.size()>0){
				return docs2;
			}else{
				response.sendError(404,Util.parseError("not found"));
			}
		}
		response.sendError(400,Util.parseError("Shipment not found"));
		return null;
	}
	
	
	
		

}
