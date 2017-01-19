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

import com.sesnu.orion.dao.TerminalDAO;
import com.sesnu.orion.web.model.Terminal;
import com.sesnu.orion.web.utility.Util;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class TerminalController {

	
	@Autowired
	TerminalDAO terminalDao;

	@Autowired Util util;
	

	@RequestMapping(value = "/api/user/terminal", method = RequestMethod.GET)
	public @ResponseBody List<Terminal> items(HttpServletResponse response) throws IOException {

		List<Terminal> terminals = terminalDao.listAll();

		if(terminals.size()>0){
			return terminals;
		}
		response.sendError(404);
		return null;
	}
	
	
	@RequestMapping(value = "/api/user/terminal", method = RequestMethod.POST)
	public @ResponseBody List<Terminal> addItem(HttpServletResponse response,@RequestBody Terminal terminal)
			throws Exception {
		
		terminal.setId(null);
		terminalDao.saveOrUpdate(terminal);
		
		List<Terminal> terminals = terminalDao.listAll();
		if(terminals.size()>0){
			return terminals;
		}
		response.sendError(404);
		return null;
		

	}
	
	
	@RequestMapping(value = "/api/user/terminal", method = RequestMethod.PUT)
	public @ResponseBody List<Terminal> updateItem(HttpServletResponse response,
			@RequestBody Terminal terminal)
			throws Exception {

		terminalDao.saveOrUpdate(terminal);
		
		List<Terminal> terminals = terminalDao.listAll();
		if(terminals.size()>0){
			return terminals;
		}
		response.sendError(404);
		return null;

	}
	
	@RequestMapping(value = "/api/user/terminal/{id}", method = RequestMethod.DELETE)
	public @ResponseBody List<Terminal> updateItem(HttpServletResponse response,@PathVariable("id") long id)
			throws Exception {
		
		Terminal terminal = terminalDao.get(id);
		if(terminal==null){
			response.sendError(400);
			return null;
		}
				
		terminalDao.delete(terminal);
		
		List<Terminal> terminals = terminalDao.listAll();
		if(terminals.size()>0){
			return terminals;
		}
		response.sendError(404);
		return null;

	}
	
			

}
