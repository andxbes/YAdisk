/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes;

import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.andxbes.Disk.Disk;
import ua.andxbes.Disk.Resource;

/**
 *
 * @author Andr
 */
public class QueryIT {

    private Query query;

    public QueryIT() {

	query = new Query(Token.instance());
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
    public void getDiskInfo() {
	
	Disk disk = query.getDiskInfo();
 	   
	Logger.getLogger("Test disk testQuery").info(disk.toString());
	
    }
    
    @Test
    public void getResource() {
	
	Resource resource = query.getResource("/");
 	   
	Logger.getLogger("Test getResourceList()").info(resource.toString());
	
    }
  

}
