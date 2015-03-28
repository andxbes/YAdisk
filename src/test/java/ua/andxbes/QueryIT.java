/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes;

import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.andxbes.Disk.Disk;
import ua.andxbes.Disk.Link;
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

	Resource resource = null;
	try {
	    resource = query.getResource("/");
	} catch (FileNotFoundException ex) {
	    Logger.getLogger(QueryIT.class.getName()).log(Level.SEVERE, null, ex);
	}

	Logger.getLogger("Test getResourceList()").info(resource.toString());

    }
    
 

    @Test 
    public void getLinkToDownloadIT() throws FileNotFoundException {
//TODO не работает с русскими документами 
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
    
     @Test 
    public void getLinkToDownloadIT_With_Unicode() throws FileNotFoundException {
                //TODO не работает с русскими документами 
	Resource resource = query.getResource("/");
	Resource[] list = resource.getEmbedded().getItems();
	String pathToFile = null;
	for (int i = 0; i < list.length; i++) {
                 if(list[i].getType().equals("file")){
		     pathToFile = list[i].getPath();//последним есть документ с русским названием 
		   
		 }
	}

	pathToFile = pathToFile.split(":")[1];
	
	Logger.getLogger("Test getResourceList()").info("\n"+pathToFile);
	
	
	Link link = query.getLinkToDownload(pathToFile);
	

	Logger.getLogger("Test getResourceList()").info(link.toString());

    }

}
