package com.sesnu.orion.web.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.sesnu.orion.dao.DocumentDAO;
import com.sesnu.orion.dao.NotificationDAO;
import com.sesnu.orion.dao.OrderDAO;
import com.sesnu.orion.web.model.DocView;
import com.sesnu.orion.web.model.Document;
import com.sesnu.orion.web.model.Notification;
import com.sesnu.orion.web.model.OrderView;
import com.sesnu.orion.web.model.TCPResponse;
import com.sesnu.orion.web.utility.NotificationService;
import com.sesnu.orion.web.utility.Util;

/**
 * Handles requests for the application file upload requests
 */
@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class FileUploadController {

	private static final Logger logger = LoggerFactory
			.getLogger(FileUploadController.class);


	@Autowired private Util util;
	@Autowired private DocumentDAO docDao;
	@Autowired private NotificationDAO notifDao;
	
	@RequestMapping(value = "/api/user/uploadFile", method = RequestMethod.POST)
	public @ResponseBody List<DocView> uploadFileHandler(HttpServletRequest req,
			@RequestParam("file") MultipartFile file,
			@RequestParam("data") String data,
			@RequestParam("orderRef") long orderRef,
			HttpServletResponse res
			) throws ParseException, IOException {

		JSONParser parser = new JSONParser();
		JSONObject dataObj = (JSONObject) parser.parse(data);
		
		String fileName =file.getOriginalFilename();
		if (!file.isEmpty()) {
			try {
				fileName = file.getOriginalFilename();
				fileName = fileName.replace(".", "_").concat("__").concat(((Long)orderRef).toString()).concat("__").concat(Long.toString((System.currentTimeMillis())));
				byte[] bytes = file.getBytes();

				TCPResponse response = util.writeToS3(bytes,fileName);
				if(response.getCode()==200){
					Document doc = new Document(orderRef,fileName,dataObj.get("type").toString(),Util.parseDate(new Date(),"/"),dataObj.get("remark").toString());
					docDao.saveOrUpdate(doc);
					// register for notification
					Notification notif = new Notification(doc.getType(),"Document",doc.getOrderRef());			
					notifDao.saveOrUpdate(notif);
					return docDao.listByOrderRef(orderRef);
				}else{
					res.sendError(response.getCode(), "Error in storage");
				}

				return null;
			} catch (Exception e) {
				res.sendError(400, "Error in storage");
				return null;
			}
		} else {
				res.sendError(400, "Error in storage");
				return null;
		}
	}

	
	
	
}
