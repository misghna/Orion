package com.sesnu.orion.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;

import com.sesnu.orion.dao.DocHandoverDAO;
import com.sesnu.orion.web.utility.GmailInbox;
import com.sesnu.orion.web.utility.NotificationService;

@Scope("prototype")
public class CronJob {
	
	@Autowired GmailInbox gmailInbox;	
	@Autowired DocHandoverDAO docDao;
	@Autowired private NotificationService notifService;
	

 //   private static final Logger logger = Logger.getLogger(CronJob.class.getName());

	
	@Scheduled(cron="5 * * * * ?")
    public void process() throws InterruptedException
    {
	
		try{
		// workaround for the multiple instances, needs to be fixed
        Long delay = (long) (Math.random()*30000);
        Thread.sleep(delay);
     
     // check pending verifications
        gmailInbox.checkConfirmation();
        
        //send notification if any
        notifService.checkNotification();
        
		}catch(Exception e){
			// add logger .. to be done
		}
        
               
    }
}
