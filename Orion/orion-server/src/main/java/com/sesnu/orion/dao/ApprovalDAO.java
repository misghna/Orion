package com.sesnu.orion.dao;

import java.util.List;

import com.sesnu.orion.web.model.Approval;
import com.sesnu.orion.web.model.ApprovalView;



public interface ApprovalDAO {
	
	public List<ApprovalView> listByOrderRef(long orderRef);
	
	public List<ApprovalView> listAll();
	
	public List<ApprovalView> listByApproverName(String name);
	
	public void saveOrUpdate(Approval ship);

	public Approval get(long id);

	public void delete(Approval ship);
	
	public List<ApprovalView> listByTypeId(String type,long forId);
	
}