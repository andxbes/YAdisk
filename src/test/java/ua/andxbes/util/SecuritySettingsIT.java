/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes.util;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Andr
 */
public class SecuritySettingsIT {
    
    public SecuritySettingsIT() {
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
     * Test of encrypt method, of class SecuritySettings.
     */
    @Test
    public void testEncrypt() throws IOException {
	String st = "encrypt";
         String temp  = SecuritySettings.encrypt(st);
	 Logger.getLogger(SecuritySettingsIT.class.getName()).log(Level.INFO, " \"{0}\" =  {1}",new Object[]{st,temp});
	 
	 temp = SecuritySettings.decrypt(temp);
	 Logger.getLogger(SecuritySettingsIT.class.getName()).log(Level.INFO, " \"{0}\" =  {1}",new Object[]{st,temp});
	
	 assertEquals(temp, st);
	
    }

  

    
}
