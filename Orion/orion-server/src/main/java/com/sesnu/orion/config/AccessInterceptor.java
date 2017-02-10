package com.sesnu.orion.config;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.EmptyInterceptor;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.sesnu.orion.dao.MiscSettingDAO;
import com.sesnu.orion.web.model.User;
import com.sesnu.orion.web.utility.ConfigFile;


public class AccessInterceptor extends EmptyInterceptor {
    private static final long serialVersionUID = -7610980115231672096L;

    private static final Logger logger = Logger.getLogger(AccessInterceptor.class.getName());
  
    final List<String> protectedTables = Arrays.asList(new String[]{"orders","order_view","summary_view","shipping_view","container_view","doc_view","du_license_view","pay_view","bid_view","doc_track_view","du_license","doc_tracking","bid","payment","shipping","status_view","container","approvals_view","approvals","doc"});
  
    
    @Override
    public String onPrepareStatement(String sql) {
    	String tableName = getTableName(sql.toLowerCase());
    	System.out.println(tableName);
    	if(tableName==null){ 
    		return sql;
    	}
    	if(!protectedTables.contains(tableName) || sql.indexOf("select id from orders")>-1){
    		System.out.println("table not found in array");
    		return sql;
    	}
		
		if(new ConfigFile().getProp().get("devMode").equals("true"))return sql;
		
		RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
		 
		User user = (User) attributes.getAttribute("user",0);
    	
		if(user.getRole().equals("Admin"))return sql;
            
        List<Long> allowedOrderRefs = (List<Long>) attributes.getAttribute("grantedIds", 0);
        
        if(allowedOrderRefs==null){
        	allowedOrderRefs = new ArrayList<Long>();
        	allowedOrderRefs.add(0l);
        }
        String parsedSql= insertId(sql.toLowerCase(),allowedOrderRefs,tableName);
        System.out.println("parsed sql " + parsedSql);
        return parsedSql;
    }
    
    
    private static String getTableName(String sql){ //apply only for select statment
    	if(sql.indexOf("from")>0 && sql.indexOf("select")>-1){
	    	String tn= sql.substring(sql.indexOf("from"),sql.length());
	    	tn = tn.trim().split(" ")[1].toLowerCase();
	    	return tn;
    	}
    	return null;
    }
    
    private static String insertId(String sql,List<Long> allowedOrderRefs,String tableName){
    	String key = "order_ref";
    	if(tableName.equals("orders") || tableName.equals("order_view")){
    		key="id";
    	}
    	String inClause = allowedOrderRefs.toString().replace("[","(").replace("]", ")");
    	if(sql.indexOf("where")>0){
    		String p1 = sql.substring(0,sql.indexOf("where") + 5);
    		String p2 = sql.substring(sql.indexOf("where") + 5,sql.length());
    		return p1 + " " + key + " in " + inClause + (p2.trim().isEmpty()? "": " and " + p2);
    	}else{
    		String tnm = getTableName(sql);
    		String p1 = sql.substring(0,sql.indexOf(tnm) + tnm.length());
    		String p2 = sql.substring(sql.indexOf(tnm) + tnm.length(),sql.length());
    		return p1 + " where " + key + " in " + inClause  + (p2.trim().isEmpty()? "": " " + p2);
    	}

    }
    
    
//  List<String> securedTables = null;
//  if(attributes.getAttribute("tables", 0)==null){
//  	securedTables = miscDao.getTableNamesWithOrderRef();
//  	attributes.setAttribute("tables", securedTables,0);
//  }else{
//  	securedTables= (List<String>) attributes.getAttribute("tables", 0);
//  }


   
}
