/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes.IO;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
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
import ua.andxbes.fieldsForQuery.Overwrite;
import ua.andxbes.fieldsForQuery.Path;
import ua.andxbes.query.YaDisk;

/**
 *
 * @author Andr
 */
public class LocalDiskIT  {

    private static final Logger log = Logger.getLogger("TestIO");
    private LocalDisk instance;

    public LocalDiskIT() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
	LocalDisk.setRootDir("./Ya-disk");
	instance = LocalDisk.getInstance();
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
	instance.write("/yahu/ggg.txt", rbch);

	String path = "/yahu/dd/gg.txt";

	rbch = Channels.newChannel(new ByteArrayInputStream(text.getBytes()));
	instance.write(path, rbch);
	BufferedReader br = new BufferedReader(new FileReader(instance.getPathToRootDir() + path));
	String readtext = br.readLine();
	br.close();
	log.info(readtext);
	instance.deleteFolderOrFile("/yahu");
	Assert.assertTrue(text.equals(readtext));

    }

    @Test
    public void testGenerateMd5Hash() throws NoSuchFieldError, ConnectException, FileNotFoundException {
	// create file 
	File f = new File(instance.getPathToRootDir() + "/testFolder/test.txt");
	if (!f.exists()) {
	    instance.write(f, Channels.newChannel(new ByteArrayInputStream("Hello Word!!!".getBytes())));
	}
	//                                 /test.txt
	String name_Of_File_In_The_Disk = f.getPath().replace(instance.getPathToRootDir(), "");
	log.log(Level.INFO, "file name {0}", name_Of_File_In_The_Disk);
	YaDisk qc = YaDisk.getInstance();

	Link link = qc.getLinkForUpload(new Path(name_Of_File_In_The_Disk),new Overwrite(true));
	try {
	    // todo заменить на собственный метод чтения 
	    FileChannel fc = new FileInputStream(f).getChannel();
	    qc.putFileToServer(fc, new Path(name_Of_File_In_The_Disk),new Overwrite(true));
	    
	    Resource r = qc.getResource(new Path(name_Of_File_In_The_Disk));


	String yadiskMd5 = r.getMd5();
	String myDiskMD5 = LocalDisk.get_Md5_Hash(instance.read(name_Of_File_In_The_Disk));

	log.log(Level.INFO, " in Yadisk = \n{0}\n, in your disk \n{1}\n", new Object[]{yadiskMd5, myDiskMD5});
	Assert.assertTrue(yadiskMd5.equals(myDiskMD5));
	} catch (IOException ex) {
	    Logger.getLogger(LocalDiskIT.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    @Test
    public void getTree() {
	StringBuilder sb = new StringBuilder();
	Map<String, List<Resource>> map = instance.getResource();
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
    
    //@Test
    public void deleteFile(){
	String path = "\\wwq\\Новый текстовый документ.txt";
           instance.deleteFolderOrFile(path);
    
    
    }

}
