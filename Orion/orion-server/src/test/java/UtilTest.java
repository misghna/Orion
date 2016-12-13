import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.sesnu.orion.web.utility.Util;

import junit.framework.Assert;

public class UtilTest {

	 Util util;
	 
	@Before
    public void runOnceBeforeClass() {
    	util = new Util();
    }
	
	@Test
    public void EncrypTest() {
		String encText = util.encrypText("aEe/cgcnzgBPt7aeh7JeW2mJm2pi+vvsP/Ib4eJ9");
        String decText = util.decryptText(encText);
        Assert.assertEquals(decText, "aEe/cgcnzgBPt7aeh7JeW2mJm2pi+vvsP/Ib4eJ9");
        System.out.println(encText);
    }
	
	
	@Test
    public void DecryptTest() {
        String decText = util.decryptText("dcbbbcb67a85a28cbf71adc70d224b2004dc40cda93d623875378a5fe20205921b630220f68e92880fc4d0e98a2ee6a3");
     //   Assert.assertEquals(decText, "aEe/cgcnzgBPt7aeh7JeW2mJm2pi+vvsP/Ib4eJ9");
        System.out.println(decText);
        
        decText = util.decryptText("944a25f992d036c6dc05db082dbbfa50cc2a5f42e20fccee970b50f4c3e575f2f05826e7605af4c3949f1abbe15fcebaad1f017587e24f9bd9c7b493786340b7");
        //   Assert.assertEquals(decText, "aEe/cgcnzgBPt7aeh7JeW2mJm2pi+vvsP/Ib4eJ9");
           System.out.println(decText);
    }
	
	@Test
    public void sendSmsTest() {
		//util.sendText("hello msghe", "+14083077700");

		System.out.println(util.generateString(6));
	}


	
	
}
