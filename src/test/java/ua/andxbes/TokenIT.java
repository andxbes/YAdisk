/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes;

import java.util.logging.Level;
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
    
    
    @Test
    public void token() {
	System.out.println("token");
	
	//Token instance = new Token();
	Token instance = Token.instance();
	
	Logger.getLogger(TokenIT.class.getName()).log(Level.INFO, "message", instance);
	
	 try {
	      Assert.assertTrue(instance.toString()!= null);
	 } catch (RuntimeException e) {
	     Assert.fail(e.toString());
	 }

    }
    @Test
    public void saveAndrestoreFields(){
          
	
    
    }
    
}
