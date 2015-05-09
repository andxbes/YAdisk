/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes.IO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andr
 */
public class FileWriteOrRead {

    private final static Logger log = Logger.getLogger("IO");
    final static String ROOT = "./Ya-disk";
    private File file;

    public FileWriteOrRead() {
	file = new File(ROOT);
	if (!file.exists()) {
	    file.mkdir();
	}
    }

    public void deleteFolderOrFile(String path) {
	file = new File(ROOT + path);
	deletefile(file);
    }

    private void deletefile(File path) {
	if (path.isDirectory()) {
	    for (File f : path.listFiles()) {
		if (f.isDirectory()) {
		    deletefile(f);
		}
		f.delete();
	    }
	}
	path.delete();
    }

    public void write(String path, ReadableByteChannel i) {

	File f = new File(ROOT + path);
	System.out.println(f + " , parent = " + f.getParent());

	if (!f.exists()) {
	    File folders = new File(f.getParent());
	    folders.mkdirs();
	}

	ByteBuffer buf = ByteBuffer.allocate(1024);
	try (FileChannel file = new FileOutputStream(f).getChannel()) {
	    while (i.read(buf) != -1) {
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

    public File readFile() {
	File f = new File(ROOT);
	return f;
    }

    public ReadableByteChannel readFile(String path) {
	FileChannel fch = null;
	try {
	    file = new File(ROOT + path);
	    fch = new FileInputStream(file).getChannel();
	} catch (FileNotFoundException ex) {
	    Logger.getLogger(FileWriteOrRead.class.getName()).log(Level.SEVERE, null, ex);
	}
	return fch;
    }

    public String get_Md5_Hash(ReadableByteChannel bch) {
	String checsumm = null;
	MessageDigest md5;
	try {
	    md5 = MessageDigest.getInstance("md5");
	    md5.reset();
	    ByteBuffer bb = ByteBuffer.allocate(1024);
	    while (bch.read(bb) != -1) {
		bb.flip();
		md5.update(bb);
		bb.clear();
	    }
	    byte massageDigest[] = md5.digest();
	    checsumm = new BigInteger(1, massageDigest).toString(16);
	} catch (IOException | NoSuchAlgorithmException ex) {
	    Logger.getLogger(FileWriteOrRead.class.getName()).log(Level.SEVERE, null, ex);
	}
	return checsumm;
    }
    
    

    //сортировать файлы по дате обновления 
}
