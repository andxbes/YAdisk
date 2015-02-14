package ua.andxbes;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import ua.andxbes.ShowPage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Andr
 */
public class ShowPageIT {
    
    public ShowPageIT() {
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
     * Test of start method, of class ShowPage.
     */
    @Test
    public void testStart() throws Exception {
	

           ShowPage sp = new ShowPage();
	   sp.start2("http://yandex.ru");
	  
	  
	
    }
    
}
