import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
		String encText = util.encrypText("ansebagroup2016");
        String decText = util.decryptText(encText);
        Assert.assertEquals(decText, "ansebagroup2016");
        System.out.println(encText);
    }
	
}
