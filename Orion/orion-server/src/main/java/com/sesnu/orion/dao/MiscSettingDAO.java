package com.sesnu.orion.dao;

import java.util.List;

import com.sesnu.orion.web.model.MiscSetting;



public interface MiscSettingDAO {
	
	
	public List<MiscSetting> listAll();
		
	public void saveOrUpdate(MiscSetting ship);

	public MiscSetting get(long id);

	public void delete(MiscSetting setting);

	public List<MiscSetting> getByNameType(String name, String type);
	
//	public List<Currency> getByNameType(String name, String type);
	
}