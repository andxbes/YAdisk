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
public class FileWriteOrReadIT {
    
    Logger log  = Logger.getLogger("TestIO");
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
    public void writeFile() throws FileNotFoundException, IOException {
	
	String text = "Привет Мир";
	
	file.write("/yahu/ggg.txt", new ByteArrayInputStream(text.getBytes()));
	
	String path ="/yahu/dd/gg.txt";
	file.write(path, new ByteArrayInputStream(text.getBytes()));
	String readtext = new BufferedReader(new FileReader(FileWriteOrRead.ROOT+ path)).readLine();
	log.info(readtext);
	//file.deletefile( "/yahu");
	Assert.assertTrue(text.equals(readtext));
	
    }
    
    
    
}
