/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes;

import java.util.concurrent.ExecutionException;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Andr
 */
public class SynchronizerIT {
    final static   Logger log = Logger.getLogger("SynchronazerIT");
    Synchronizer instance;
    public SynchronizerIT() {
    }
    
    @BeforeClass
    public static void setUpClass() {
	for (Handler h : Logger.getLogger("").getHandlers()) {
	    h.setLevel( Level.ALL );
	}
	log.setLevel(Level.ALL);
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
	 instance = new Synchronizer();
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testCheckCorrecT() {
	String inputStr ="./dfdfdf/";
	String inputStr2 =".\\dfdfdf\\";
	String result  = Synchronizer.checkCorrect(inputStr);
	String result2  = Synchronizer.checkCorrect(inputStr2);
	
	log.log(Level.INFO, "{0} {1}", new Object[]{result, result2});
	Assert.assertTrue(result.equals("./dfdfdf") & result2.equals(".\\dfdfdf"));
    }
    
    @Test
    public void testSomeMethod() throws InterruptedException, ExecutionException {
	instance.buildTree();
	instance.sync();
    }
    
}
