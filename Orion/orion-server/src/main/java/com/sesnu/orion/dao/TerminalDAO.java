package com.sesnu.orion.dao;

import java.util.List;

import com.sesnu.orion.web.model.Bid;
import com.sesnu.orion.web.model.BidView;
import com.sesnu.orion.web.model.Terminal;




public interface TerminalDAO {
	

	public void saveOrUpdate(Terminal term);

	public Terminal get(long id);
	
	public Terminal getByName(String name);
	
	public void delete(Terminal term);

	public List<Terminal> listAll();



	
}