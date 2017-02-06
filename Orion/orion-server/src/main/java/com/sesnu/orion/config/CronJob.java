package com.sesnu.orion.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;

import com.sesnu.orion.dao.DocHandoverDAO;
import com.sesnu.orion.web.utility.GmailInbox;

@Scope("prototype")
public class CronJob {
	
	@Autowired GmailInbox gmailInbox;
	
	@Autowired DocHandoverDAO docDao;
	
	@Scheduled(cron="5 * * * * ?")
    public void process() throws InterruptedException
    {
			 
        Long delay = (long) (Math.random()*30000);
        Thread.sleep(delay);
        gmailInbox.checkConfirmation();
               
    }
}
