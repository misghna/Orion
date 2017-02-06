package com.sesnu.orion.web.controller;

import java.io.IOException;
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

import com.sesnu.orion.dao.ClientDAO;
import com.sesnu.orion.dao.OrderDAO;
import com.sesnu.orion.web.model.AddressBook;
import com.sesnu.orion.web.utility.Util;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class ClientController {

	
	@Autowired
	ClientDAO clientDAO;
	@Autowired Util util;
	@Autowired OrderDAO orderDao;
	

	@RequestMapping(value = "/api/user/addressBook/all", method = RequestMethod.GET)
	public @ResponseBody List<AddressBook> items(HttpServletResponse response) throws IOException {

		List<AddressBook> clients= clientDAO.listAll();

		if(clients.size()>0){
			return clients;
		}
		response.sendError(404);
		return null;
	}
	

	@RequestMapping(value = "/api/user/addressBook/fwAgents", method = RequestMethod.GET)
	public @ResponseBody List<AddressBook> fwAgents(HttpServletResponse response) throws IOException {

		List<AddressBook> clients= clientDAO.listAllFWAgents();

		if(clients.size()>0){
			return clients;
		}
		response.sendError(404);
		return null;
	}
	
	@RequestMapping(value = "/api/user/addressBook/destinations", method = RequestMethod.GET)
	public @ResponseBody List<AddressBook> destinations(HttpServletResponse response) throws IOException {

		List<AddressBook> clients= clientDAO.listAllDest();

		if(clients.size()>0){
			return clients;
		}
		response.sendError(404);
		return null;
	}
	
	
	@RequestMapping(value = "/api/user/addressBook", method = RequestMethod.POST)
	public @ResponseBody List<AddressBook> addItem(HttpServletResponse response,
			@RequestBody AddressBook client)
			throws Exception {
		
				
		client.setId(null);
		clientDAO.saveOrUpdate(client);

		List<AddressBook> clients = clientDAO.listAll();

		if(clients.size()>0){
			return clients;
		}
		response.sendError(404);
		return null;
		

	}
	
	
	@RequestMapping(value = "/api/user/addressBook", method = RequestMethod.PUT)
	public @ResponseBody List<AddressBook> updateItem(HttpServletResponse response,
			@RequestBody AddressBook client)
			throws Exception {
		
		
		if(clientDAO.get(client.getId())==null){
			response.sendError(400);
			return null;
		}
		
		
		clientDAO.saveOrUpdate(client);
		List<AddressBook> clients = clientDAO.listAll();

		if(clients.size()>0){
			return clients;
		}
		response.sendError(404);
		return null;

	}
	
	

	@RequestMapping(value = "/api/user/addressBook/{id}", method = RequestMethod.DELETE)
	
	public @ResponseBody List<AddressBook> deleteItem(@PathVariable("id") long id,
			HttpServletResponse response) throws Exception {
		AddressBook client = clientDAO.get(id);
	
		if(client != null){
			clientDAO.delete(client);
			List<AddressBook> clients = clientDAO.listAll();

			if(clients.size()>0){
				return clients;
			}else{
				response.sendError(404,Util.parseError("not found"));
			}
		}
		return null;
	}
	
		

}
