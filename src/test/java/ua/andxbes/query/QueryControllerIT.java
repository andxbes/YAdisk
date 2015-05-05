/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes.query;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import ua.andxbes.Token;
import ua.andxbes.fieldsForQuery.Field;
import ua.andxbes.fieldsForQuery.Fields;
import ua.andxbes.fieldsForQuery.From;
import ua.andxbes.fieldsForQuery.Limit;
import ua.andxbes.fieldsForQuery.MediaType;
import ua.andxbes.fieldsForQuery.Overwrite;
import ua.andxbes.fieldsForQuery.Path;
import ua.andxbes.fieldsForQuery.Type;

/**
 *
 * @author Andr
 */
public class QueryControllerIT {

    private final QueryController query;

    public QueryControllerIT() {

	query = new QueryController(Token.instance());
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

    private String getAnyFolder() {
	return getAnyResources(Type.DIR);
    }

    private String getAnyFile() {
	return getAnyResources(Type.FILE);
    }

    private String getAnyResources(String type) {
	String result = null;

	try {
	    Resource resource = query.getResource(new Field[]{new Path("/")});
	    Resource[] list = resource.getEmbedded().getItems();

	    for (int i = 0; i < list.length; i++) {
		if (list[i].getType().equals(type)) {
		    result = list[i].getPath();
		    break;
		}
	    }
	} catch (ConnectException | NoSuchFieldError ex) {
	    Logger.getLogger(QueryControllerIT.class.getName()).log(Level.SEVERE, null, ex);
	}

	return result;
    }

    @Test
    public void getResource() throws ConnectException {

	Resource resource = null;
	try {
	    resource = query.getResource(new Path("/"));
	} catch (NoSuchFieldError ex) {
	    Logger.getLogger(QueryControllerIT.class.getName()).log(Level.SEVERE, null, ex);
	}

	Logger.getLogger("Test getResourceList()").info(resource.toString());

    }

    @Test
    public void getLinkToDownloadIT() throws FileNotFoundException, UnsupportedEncodingException, ConnectException {
	String pathToFile = getAnyFile();
	Logger.getLogger("Test getResourceList()").info("\n" + pathToFile);

	Link link = query.getLinkToDownload(new Field[]{new Path(pathToFile)});
	Logger.getLogger("Test getResourceList()").info(link.toString());

    }

    @Test
    public void getLinkForUpload() throws FileNotFoundException, UnsupportedEncodingException, ConnectException {
	String pathToFile = getAnyFolder();
	Logger.getLogger("Test getResourceList()").info("\n" + pathToFile);

	Link link = query.getLinkToDownload(new Field[]{new Path(pathToFile)});
	Logger.getLogger("Test getResourceList()").info(link.toString());
    }

    @Test
    public void testGetFiles() {

	try {
	    ResourceList expResult = null;
	    FilesResouceList result = null;

	    result = query.getFiles(new Limit(100));//Фильтр на количество ожидаемых обьектов , по умолчанию 20

	    Logger.getLogger(this.getClass().getSimpleName()).info(result.toString());
	} catch (ConnectException ex) {
	    Logger.getLogger(QueryControllerIT.class.getName()).log(Level.SEVERE, null, ex);
	    Assert.fail();
	}

    }

    @Test
    public void testGetLastUpdatesList() {

	ResourceList expResult = null;
	LastUploadedResourceList result = null;

	try {
	    result = query.getLastUploadedList(new Field[]{new Limit(100)});//Фильтр на количество ожидаемых обьектов , по умолчанию 20  
	} catch (ConnectException ex) {
	    Logger.getLogger(QueryControllerIT.class.getName()).log(Level.SEVERE, null, ex);
	    Assert.fail();
	}

	Logger.getLogger(this.getClass().getSimpleName()).info(result.toString());
    }

    @Test
    public void testGetPublicResources() {

	ResourceList expResult = null;
	PublicResourcesList result = null;

	try {
	    result = query.getPublicResources(new Field[]{new Limit(100)});//Фильтр на количество ожидаемых обьектов , по умолчанию 20  
	} catch (ConnectException ex) {
	    Logger.getLogger(QueryControllerIT.class.getName()).log(Level.SEVERE, null, ex);
	    Assert.fail();
	}

	Logger.getLogger(this.getClass().getSimpleName()).info(result.toString());
    }

    @Test
    public void testCheck() {
	Field[] list = new Field[]{new MediaType("doc"), new Fields("111")};

	Assert.assertTrue(query.checkRequiredField(list, new Class[]{MediaType.class}));
	Assert.assertFalse(query.checkRequiredField(list, new Class[]{Limit.class}));

	Assert.assertFalse(query.checkRequiredField(list, new Class[]{Limit.class, Type.class}));
	Assert.assertTrue(query.checkRequiredField(list, new Class[]{Fields.class, MediaType.class}));

    }

    @Test
    public void postCopyIT() throws InterruptedException {

	Link link;
	try {
	    link = query.postCopy(new From(getAnyFolder()), new Path("/folderFortests/"), new Overwrite(true));

	    /* Я так понял , все тесты запускаются в разных потоках ,
	     и основной поток при закрытии ни как не ждет завершения дочерних потоков  .
	     Потому создаем еще один поток , которого будем в тесте ждать .
	     */
	    query.refrashStatusOperationId(link).join(); //waiting result of assynchronous operation

	    Logger.getLogger(QueryControllerIT.class.getName()).log(Level.INFO, "\nresult  = {0}", link.toString());
	} catch (ConnectException ex) {
	    Logger.getLogger(QueryControllerIT.class.getName()).log(Level.SEVERE, null, ex);
	    Assert.fail(ex.getMessage());
	} catch (RuntimeException e) {
	    Logger.getLogger(QueryControllerIT.class.getName()).log(Level.SEVERE, null, e);
	}

    }
    
    @Test
    public void uploadFile() throws NoSuchFieldError, FileNotFoundException, IOException{
	File f = new File("./gradlew.bat");
	String root = "/testFolder/";
	Path path = new Path(root + f.getName());
        query.putToServer(f,path,new Overwrite(true));
    
    
    }
}
