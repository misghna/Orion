package com.sesnu.orion.web.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sesnu.orion.dao.MiscSettingDAO;
import com.sesnu.orion.dao.PortFeeDAO;
import com.sesnu.orion.web.model.MiscSetting;
import com.sesnu.orion.web.model.PortFee;
import com.sesnu.orion.web.utility.Util;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class MiscSettingController {

	
	@Autowired
	MiscSettingDAO miscSettingDao;

	@Autowired Util util;
	

	@RequestMapping(value = "/api/user/miscSetting", method = RequestMethod.GET)
	public @ResponseBody List<MiscSetting> items(HttpServletResponse response) throws IOException {

		List<MiscSetting> miscSettings = miscSettingDao.listAll();
		if(miscSettings.size()>0){
			return miscSettings;
		}
		response.sendError(404);
		return null;
	}

	@RequestMapping(value = "/api/user/miscSetting/ports", method = RequestMethod.GET)
	public @ResponseBody List<String> ports(HttpServletResponse response) throws IOException {

		MiscSetting miscSetting = miscSettingDao.getByName("Ports");
		if(miscSetting !=null){
			return Arrays.asList(miscSetting.getValue().split(","));
		}
		response.sendError(404);
		return null;
	}
	
	@RequestMapping(value = "/api/user/miscSetting/{name}", method = RequestMethod.GET)
	public @ResponseBody List<String> terminals(HttpServletResponse response,@PathVariable("name") String name) throws IOException {
		
		MiscSetting miscSetting = miscSettingDao.getByName(StringUtils.capitalize(name));
		if(miscSetting!=null){
			return Arrays.asList(miscSetting.getValue().split(","));
		}
		response.sendError(400,Util.parseError("Bad Property name"));
		return null;
	}
	
//	@RequestMapping(value = "/api/user/miscSetting/importers", method = RequestMethod.GET)
//	public @ResponseBody List<String> importers(HttpServletResponse response) throws IOException {
//
//		MiscSetting miscSetting = miscSettingDao.getByName("Importers");
//		if(miscSetting!=null){
//			return Arrays.asList(miscSetting.getValue().split(","));
//		}
//		response.sendError(404);
//		return null;
//	}
	
	@RequestMapping(value = "/api/user/miscSetting", method = RequestMethod.PUT)
	public @ResponseBody List<MiscSetting> updateItem(HttpServletResponse response,
			@RequestBody MiscSetting miscSetting)
			throws Exception {
		
		
		if(miscSettingDao.get(miscSetting.getId())==null){
			response.sendError(400,"Setting doesnt exist");
			return null;
		}
				
		miscSetting.setUpdatedOn(Util.parseDate(new Date(),"/"));
		miscSettingDao.saveOrUpdate(miscSetting);
		
		List<MiscSetting> settings = miscSettingDao.listAll();
		if(settings.size()>0){
			return settings;
		}
		response.sendError(404);
		return null;

	}
	

	
			

}
