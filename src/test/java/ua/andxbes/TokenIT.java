package ua.andxbes;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Andr
 */
public class TokenIT {
    
    public static final Logger log = Logger.getLogger("TokenIT");
    private Token instance;
    public static  String id ="45f708d998054dd29dfc73c7e33c664d" ,
	                  pas ="e32c22762f8c44dea3d3f60626a06d36";
    
    public TokenIT() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
	
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of toString method, of class Token.
     */
    @Test
    public void testToString_TokenIsNull() {
	
	instance = new Token();
	try {
	    String result = instance.toString();
	} catch (RuntimeException e) {
	    
	    Assert.assertTrue("Отловил" + e, true);
	    return;
	}
	Assert.assertFalse("Пропустил", true);
	
    }
    
    @Test
    public void testToString_whoTokken() {
	
	instance = new Token(id,pas);
	
	Assert.assertTrue("Пройден", true);
	
    }
    
}
