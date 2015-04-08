/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.rmi.ConnectException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.andxbes.DiskJsonObjects.Disk;
import ua.andxbes.DiskJsonObjects.FilesResouceList;
import ua.andxbes.DiskJsonObjects.LastUploadedResourceList;
import ua.andxbes.DiskJsonObjects.Link;
import ua.andxbes.DiskJsonObjects.PublicResourcesList;
import ua.andxbes.DiskJsonObjects.Resource;
import ua.andxbes.DiskJsonObjects.ResourceList;
import ua.andxbes.fieldsForQuery.Field;
import ua.andxbes.fieldsForQuery.Fields;
import ua.andxbes.fieldsForQuery.Limit;
import ua.andxbes.fieldsForQuery.MediaType;
import ua.andxbes.fieldsForQuery.Path;
import ua.andxbes.fieldsForQuery.Type;

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
    public void getDiskInfo() throws ConnectException {

	Disk disk = query.getDiskInfo();

	Logger.getLogger("Test disk testQuery").info(disk.toString());

    }

    @Test
    public void getResource() throws ConnectException {

	Resource resource = null;
	try {
	    resource = query.getResource(new Field[]{new Path("/")});
	} catch (NoSuchFieldError ex) {
	    Logger.getLogger(QueryIT.class.getName()).log(Level.SEVERE, null, ex);
	}

	Logger.getLogger("Test getResourceList()").info(resource.toString());

    }

    @Test
    public void getLinkToDownloadIT() throws FileNotFoundException, UnsupportedEncodingException, ConnectException {
	
	
	Resource resource = query.getResource(new Field[]{new Path("/")});
	Resource[] list = resource.getEmbedded().getItems();
	String pathToFile = null;
	
	for (int i = 0; i < list.length; i++) {
	    if (list[i].getType().equals("file")) {
		pathToFile = list[i].getPath();
		break;
	    }
	}

	pathToFile = pathToFile.split(":")[1];
	Logger.getLogger("Test getResourceList()").info("\n" + pathToFile);

	Link link = query.getLinkToDownload(new Field[]{new Path(pathToFile)});
	Logger.getLogger("Test getResourceList()").info(link.toString());

    }

    @Test
    public void testGetFiles() {

	ResourceList expResult = null;
	FilesResouceList result = null;
	
	try {
	    result = query.getFiles(new Field[]{new Limit(100)});//Фильтр на количество ожидаемых обьектов , по умолчанию 20  
	} catch ( ConnectException ex) {
	    Logger.getLogger(QueryIT.class.getName()).log(Level.SEVERE, null, ex);
	}
	Logger.getLogger(this.getClass().getSimpleName()).info(result.toString());

    }
    
    @Test
    public void testGetLastUpdatesList() {

	ResourceList expResult = null;
	LastUploadedResourceList result = null;
	
	try {
	    result = query.getLastUploadedList(new Field[]{new Limit(100)});//Фильтр на количество ожидаемых обьектов , по умолчанию 20  
	} catch ( ConnectException ex) {
	    Logger.getLogger(QueryIT.class.getName()).log(Level.SEVERE, null, ex);
	}
	Logger.getLogger(this.getClass().getSimpleName()).info(result.toString());
    }
    
      @Test
    public void testGetPublicResources() {

	ResourceList expResult = null;
	  PublicResourcesList result = null;
	
	try {
	    result = query.getPublicResources(new Field[]{new Limit(100)});//Фильтр на количество ожидаемых обьектов , по умолчанию 20  
	} catch ( ConnectException ex) {
	    Logger.getLogger(QueryIT.class.getName()).log(Level.SEVERE, null, ex);
	}
	Logger.getLogger(this.getClass().getSimpleName()).info(result.toString());
    }

    @Test
    public void testCheck() {
	Field[] list = new Field[]{new MediaType("doc"),new Fields("111")};

	Assert.assertTrue(query.checkRequiredField(list, new Class[]{MediaType.class}));
	Assert.assertFalse(query.checkRequiredField(list, new Class[]{Limit.class}));

	Assert.assertFalse(query.checkRequiredField(list, new Class[]{Limit.class, Type.class}));
	Assert.assertTrue(query.checkRequiredField(list, new Class[]{Fields.class, MediaType.class}));

    }

}
