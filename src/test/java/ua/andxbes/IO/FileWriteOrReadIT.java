/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes.IO;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.rmi.ConnectException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.andxbes.DiskJsonObjects.Link;
import ua.andxbes.DiskJsonObjects.Resource;
import ua.andxbes.Token;
import ua.andxbes.fieldsForQuery.Path;
import ua.andxbes.query.QueryController;

/**
 *
 * @author Andr
 */
public class FileWriteOrReadIT {
    
    Logger log = Logger.getLogger("TestIO");
    FileWriteOrRead file;
    
    public FileWriteOrReadIT() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
	file = new FileWriteOrRead();
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void isCreatedFolderforWork() {
	File f = new File("./Ya-disk/");
	Assert.assertTrue(f.exists());
    }
    
    @Test
    public void writeFile() throws FileNotFoundException, IOException, InterruptedException {
	
	String text = "Привет Мир";
	ReadableByteChannel rbch = Channels.newChannel(new ByteArrayInputStream(text.getBytes()));
	file.write("/yahu/ggg.txt", rbch);
	
	String path = "/yahu/dd/gg.txt";
	
	rbch = Channels.newChannel(new ByteArrayInputStream(text.getBytes()));
	file.write(path, rbch);
	BufferedReader br = new BufferedReader(new FileReader(FileWriteOrRead.ROOT + path));
	String readtext = br.readLine();
	br.close();
	log.info(readtext);
	file.deleteFolderOrFile("/yahu");
	Assert.assertTrue(text.equals(readtext));
	
    }
    
    @Test
    public void testGenerateMd5Hash() throws NoSuchFieldError, ConnectException, FileNotFoundException {
	String name_Of_File_In_The_Disk = "/eclipse.7z";
	//String name_Of_File_In_The_Disk ="/Вопросы на собеседованиях.doc";
	QueryController qc = new QueryController(Token.instance());
	
	Link link = qc.getLinkToDownload(new Path(name_Of_File_In_The_Disk));
	Resource r = qc.getResource(new Path(name_Of_File_In_The_Disk));
	
	log.log(Level.INFO, " resource = {0}", r.toString());
	log.log(Level.INFO, " link = {0}", link.toString());
	
	File ffff = new File(FileWriteOrRead.ROOT + name_Of_File_In_The_Disk);
	System.out.println(ffff + "  " + ffff.exists());
	
	if (!ffff.exists()) {
	    ReadableByteChannel rbch = qc.downloadFile(link);
	    file.write(r.getPath(), rbch);
	}
	
	String yadiskMd5 = r.getMd5();
	String myDiskMD5 = file.get_Md5_Hash(file.readFile(name_Of_File_In_The_Disk));
	
	log.log(Level.INFO, " in Yadisk = \n{0}\n, in your disk \n{1}\n", new Object[]{yadiskMd5, myDiskMD5});
	Assert.assertTrue(yadiskMd5.equals(myDiskMD5));
    }
    
    @Test    
    public void getTree() {
	StringBuilder sb = new StringBuilder();
	Map<String, List<Resource>> map = file.getMapOfFile();
	for (Map.Entry<String, List<Resource>> entrySet : map.entrySet()) {
	    String key = entrySet.getKey();
	    
	    List<Resource> value = entrySet.getValue();
	    sb.append("\n\n").append(key);
	    sb.append("\n--------------------------------------------------------\n");
	    for (Resource value1 : value) {
		sb.append(value1.toString());		
	    }
	    
	}
	log.info(sb.toString());
    }
    
}
