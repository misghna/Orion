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
import com.sesnu.orion.web.model.Client;
import com.sesnu.orion.web.utility.Util;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class ClientController {

	
	@Autowired
	ClientDAO clientDAO;
	@Autowired Util util;
	@Autowired OrderDAO orderDao;
	

	@RequestMapping(value = "/api/user/clients", method = RequestMethod.GET)
	public @ResponseBody List<Client> items(HttpServletResponse response) throws IOException {

		List<Client> clients= clientDAO.listAll();

		if(clients.size()>0){
			return clients;
		}
		response.sendError(404);
		return null;
	}
	

	
	@RequestMapping(value = "/api/user/client", method = RequestMethod.POST)
	public @ResponseBody List<Client> addItem(HttpServletResponse response,
			@RequestBody Client client)
			throws Exception {
		
				
		client.setId(null);
		clientDAO.saveOrUpdate(client);

		List<Client> clients = clientDAO.listAll();

		if(clients.size()>0){
			return clients;
		}
		response.sendError(404);
		return null;
		

	}
	
	
	@RequestMapping(value = "/api/user/client", method = RequestMethod.PUT)
	public @ResponseBody List<Client> updateItem(HttpServletResponse response,
			@RequestBody Client client)
			throws Exception {
		
		
		if(clientDAO.get(client.getId())==null){
			response.sendError(400);
			return null;
		}
		
		
		clientDAO.saveOrUpdate(client);
		List<Client> clients = clientDAO.listAll();

		if(clients.size()>0){
			return clients;
		}
		response.sendError(404);
		return null;

	}
	
	

	@RequestMapping(value = "/api/user/client/{id}", method = RequestMethod.DELETE)
	
	public @ResponseBody List<Client> deleteItem(@PathVariable("id") long id,
			HttpServletResponse response) throws Exception {
		Client client = clientDAO.get(id);
	
		if(client != null){
			clientDAO.delete(client);
			List<Client> clients = clientDAO.listAll();

			if(clients.size()>0){
				return clients;
			}else{
				response.sendError(404,Util.parseError("not found"));
			}
		}
		return null;
	}
	
		

}
