/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes.IO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andr
 */
public class FileWriteOrRead {

    private final static Logger log = Logger.getLogger("IO");
    final static String ROOT = "./Ya-disk";
    
    public FileWriteOrRead() {
	File file = new File(ROOT);
	if (!file.exists()) {
	    file.mkdir();
	}
    }
    
//    public void deletefile(String path){
//         File f = new File(ROOT + path);
//	 log.info(f.getPath());
//	 f.delete();
//    }
    
    public void write(String path, InputStream i) {
	
	
	File f = new File(ROOT + path);
	if(!f.exists()){
	 createFolder(path);
	}
	//checkExistFile(path);
	
	ReadableByteChannel inputStrCh = Channels.newChannel(i);	
	ByteBuffer buf = ByteBuffer.allocate(1024);
	try (FileChannel file = new FileOutputStream(f).getChannel()) {
	    while (inputStrCh.read(buf) != -1) {
		buf.flip();
	        file.write(buf);
	        buf.clear();
	    }
	} catch (FileNotFoundException ex) {
	    Logger.getLogger(FileWriteOrRead.class.getName()).log(Level.SEVERE, null, ex);
	} catch (IOException ex) {
	    Logger.getLogger(FileWriteOrRead.class.getName()).log(Level.SEVERE, null, ex);
	}
	
    }
    
    private void createFolder(String path){
        String pathsplit[] = path.split("/");
	String fileName = pathsplit[pathsplit.length-1];
	String folderPath = path.replace(fileName,"");
	System.out.println(folderPath);
	File f = new File(ROOT+folderPath);
	f.mkdirs();
    }
    
}
