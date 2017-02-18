package com.sesnu.orion.dao;

import java.util.List;

import com.sesnu.orion.web.model.AccessHistory;
import com.sesnu.orion.web.model.Location;



public interface AccessHistoryDAO {
	
	public List<AccessHistory> listByUserId(long userId);

	public void save(AccessHistory access);
	
	public void update(AccessHistory access);
	
	public List<AccessHistory> getUnCoordinated();
	
	public AccessHistory get(long id);
	
	public void delete(AccessHistory access);

	public List<AccessHistory> listAll();
	
	public List<Location> listAllLocations();

	public void saveLocation(Location loc);
		
	public List<Location> listFailedLocations();
	
	public List<String> listNewIps();
	
}