package com.sesnu.orion.web.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sesnu.orion.dao.InvFormatDAO;
import com.sesnu.orion.dao.OrderDAO;
import com.sesnu.orion.web.model.Invoice;
import com.sesnu.orion.web.model.InvoiceFormat;
import com.sesnu.orion.web.model.Order;
import com.sesnu.orion.web.model.OrderView;
import com.sesnu.orion.web.service.InvoiceEnum;
import com.sesnu.orion.web.service.InvoiceProcessor;
import com.sesnu.orion.web.utility.Util;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class InvoiceController {

	
	@Autowired InvFormatDAO invFrmtDao;
	@Autowired OrderDAO orderDao;
	@Autowired Util util;
	@Autowired InvoiceProcessor invProcessor;
	@Autowired InvFormatDAO invFormat;

	@RequestMapping(value = "/api/user/invoiceFormat", method = RequestMethod.GET)
	public @ResponseBody List<InvoiceFormat> items(HttpServletResponse response) throws IOException {

		List<InvoiceFormat> formats= invFrmtDao.listAll();

		if(formats.size()>0){
			return trimFormatData(formats);
		}
		response.sendError(404);
		return null;
	}
	
	private List<InvoiceFormat> trimFormatData(List<InvoiceFormat> formats){
		for (InvoiceFormat invoiceFormat : formats) {
			if(invoiceFormat.getFormat()!=null && invoiceFormat.getFormat().length() > 20){
				invoiceFormat.setFormat(invoiceFormat.getFormat().substring(0, 20) + " . . .");
			}
		}
		return formats;
	}
	
	@RequestMapping(value = "/api/user/invoiceFormat/format/{id}", method = RequestMethod.GET)
	public @ResponseBody String items(HttpServletResponse response,@PathVariable("id") long id) throws IOException {

		InvoiceFormat format= invFrmtDao.get(id);

		if(format!=null && format.getFormat()!=null){
			return invProcessor.processPreviewInvoice(format, format.getInvoiceType());
		}
		response.sendError(404);
		return null;
	}
	

	@RequestMapping(value = "/api/user/invoiceFormat/{invNo}", method = RequestMethod.GET)
	public @ResponseBody String format(HttpServletResponse response,@PathVariable("id") long id) throws IOException {

		InvoiceFormat format= invFrmtDao.get(id);

		if(format!=null && format.getFormat()!=null){
			return format.getFormat();
		}
		response.sendError(404);
		return null;
	}
	
	@RequestMapping(value = "/api/user/invoice/get", method = RequestMethod.POST)
	public @ResponseBody String getInvoice(HttpServletResponse response,@RequestBody JSONObject param) throws IOException {
		String invType = param.get("invType").toString();
		String invNo = param.get("invNo").toString();
		Invoice inv = invFormat.getInvoice(invNo.trim(),invType.trim());
		if(inv!=null){
			return inv.getInvoice();
		}
		response.sendError(404);
		return null;
	}
	
	@RequestMapping(value = "/api/user/invoice", method = RequestMethod.POST)
	public @ResponseBody String addInvoice(HttpServletResponse response,@RequestBody JSONObject param
							) throws IOException {
		
		if(!param.containsKey("invType") || !param.containsKey("exporter")) {
			response.sendError(400,Util.parseError("Bad parameters"));
			return null;
		}
		// check if specific exists 
		String type = param.get("invType").toString();
		InvoiceFormat format= invFrmtDao.get(type,param.get("exporter").toString());

		//else get the Generic
		if(format==null || format.getFormat()==null){ 
			format= invFrmtDao.get(param.get("invType").toString(),"Generic");
		}

		if(format!=null && format.getFormat()!=null){
			type = type.toUpperCase().replaceAll(" ", "_");
			String invoice = invProcessor.processInvoice(format,param); 
			saveInvoice(param.get("invNo").toString(),param.get("invType").toString(),invoice);			
			return invoice;
		}		
		response.sendError(404);
		return null;
	}
	
	private void saveInvoice(String invNo, String invType,String invoice){
		Invoice inv = invFormat.getInvoice(invNo,invType);
		if (inv==null){
			inv = new Invoice(invNo,invType,invoice,Util.parseDate(new Date()));			
		}else{
			inv.setInvoice(invoice);
			inv.setUpdatedOn(Util.parseDate(new Date()));			
		}
		invFormat.saveInvoice(inv);
	}
	
	
	@RequestMapping(value = "/api/user/invoiceFormat", method = RequestMethod.POST)
	public @ResponseBody List<InvoiceFormat> addItem(HttpServletResponse response,@RequestBody InvoiceFormat invFrmt)
			throws Exception {
		

		invFrmt.setUpdatedOn(Util.parseDate(new Date(),"/"));
		invFrmt.setId(null);
		invFrmtDao.saveOrUpdate(invFrmt);
		
		List<InvoiceFormat> invFrmts = invFrmtDao.listAll();

		if(invFrmts.size()>0){
			return trimFormatData(invFrmts);
		}
		response.sendError(404);
		return null;
		

	}
	

	@RequestMapping(value = "/api/user/invoiceFormat", method = RequestMethod.PUT)
	public @ResponseBody List<InvoiceFormat> updateItem(HttpServletResponse response,
			@RequestBody InvoiceFormat invFrmt)
			throws Exception {
		
		
		if(invFrmtDao.get(invFrmt.getId())==null){
			response.sendError(400,Util.parseError("Bad data, try again"));
			return null;
		}
		
		if(invFrmt.getFormat().length()<25){
			List<InvoiceFormat> invFrmts = invFrmtDao.listAll();
			if(invFrmts.size()>0){
				return trimFormatData(invFrmts);
			}
		}
		

		invFrmt.setUpdatedOn(Util.parseDate(new Date(),"/"));
		invFrmtDao.saveOrUpdate(invFrmt);
		
		List<InvoiceFormat> invFrmts = invFrmtDao.listAll();

		if(invFrmts.size()>0){
			return trimFormatData(invFrmts);
		}
		response.sendError(404);
		return null;

	}
	
	
	@RequestMapping(value = "/api/user/invoice", method = RequestMethod.PUT)
	public @ResponseBody String updateItem(HttpServletResponse response,
			@RequestBody JSONObject inv)
			throws Exception {
		saveInvoice(inv.get("invNo").toString(),inv.get("invType").toString(),inv.get("htmlContent").toString());		
		Invoice invoice = invFormat.getInvoice(inv.get("invNo").toString(),inv.get("invType").toString());
		if(invoice !=null){
			return invoice.getInvoice();
		}
		response.sendError(400);
		return null;
	}
	
	

	@RequestMapping(value = "/api/user/invoiceFormat/{id}", method = RequestMethod.DELETE)
	
	public @ResponseBody List<InvoiceFormat> deleteItem(@PathVariable("id") long id,
			HttpServletResponse response) throws Exception {
		
		InvoiceFormat invFrmt = invFrmtDao.get(id);
		if(invFrmt != null){
			invFrmtDao.delete(invFrmt);
			List<InvoiceFormat> invFrmts = invFrmtDao.listAll();
			if(invFrmts.size()>0){
				return trimFormatData(invFrmts);
			}
			response.sendError(404);
		}
		
		return null;
	}
	
		

}
