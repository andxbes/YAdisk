/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.andxbes.DiskJsonObjects.Disk;
import ua.andxbes.DiskJsonObjects.FilesResouceList;
import ua.andxbes.DiskJsonObjects.Link;
import ua.andxbes.DiskJsonObjects.Resource;
import ua.andxbes.DiskJsonObjects.ResourceList;
import ua.andxbes.fieldsForQuery.Field;

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

	Resource resource = null;
	try {
	    resource = query.getResource("/");
	} catch (NoSuchFieldError ex) {
	    Logger.getLogger(QueryIT.class.getName()).log(Level.SEVERE, null, ex);
	}

	Logger.getLogger("Test getResourceList()").info(resource.toString());

    }
    

    @Test 
    public void getLinkToDownloadIT() throws FileNotFoundException {
	Resource resource = query.getResource("/");
	Resource[] list = resource.getEmbedded().getItems();
	String pathToFile = null;
	for (int i = 0; i < list.length; i++) {
                 if(list[i].getType().equals("file")){
		     pathToFile = list[i].getPath();
		     break; 
		 }
	}

	pathToFile = pathToFile.split(":")[1];
	
	Logger.getLogger("Test getResourceList()").info("\n"+pathToFile);
	
	
	Link link = query.getLinkToDownload(pathToFile);
	

	Logger.getLogger("Test getResourceList()").info(link.toString());

    }

//    /**
//     * Test of getDiskInfo method, of class Query.
//     */
//    @Test
//    public void testGetDiskInfo() {
//	System.out.println("getDiskInfo");
//	Query instance = null;
//	Disk expResult = null;
//	Disk result = instance.getDiskInfo();
//	assertEquals(expResult, result);
//	// TODO review the generated test code and remove the default call to fail.
//	fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getResource method, of class Query.
//     */
//    @Test
//    public void testGetResource() {
//	System.out.println("getResource");
//	String path = "";
//	Query instance = null;
//	Resource expResult = null;
//	Resource result = instance.getResource(path);
//	assertEquals(expResult, result);
//	// TODO review the generated test code and remove the default call to fail.
//	fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getLinkToDownload method, of class Query.
//     */
//    @Test
//    public void testGetLinkToDownload() {
//	System.out.println("getLinkToDownload");
//	String path = "";
//	Query instance = null;
//	Link expResult = null;
//	Link result = instance.getLinkToDownload(path);
//	assertEquals(expResult, result);
//	// TODO review the generated test code and remove the default call to fail.
//	fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getFiles method, of class Query.
//     */
    @Test
    public void testGetFiles() {
	
	ResourceList expResult = null;
	FilesResouceList result = query.getFiles(new ArrayList<Field>());
	Logger.getLogger(this.getClass().getSimpleName()).info(result.toString());
	
    }
    
    

}
