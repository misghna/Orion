package com.sesnu.orion.dao;

import java.util.List;

import com.sesnu.orion.web.model.Container;
import com.sesnu.orion.web.model.ContainerView;



public interface ContainerDAO {
	
	public List<ContainerView> listByOrderId(long orderRef);
	
	public List<ContainerView> listAll();
	
	public void saveOrUpdate(Container ship);

	public Container get(long id);

	public void delete(Container ship);
	
}