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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import ua.andxbes.DiskJsonObjects.Resource;

/**
 *
 * @author Andr
 */
public class FileWriteOrRead {

    private final static Logger log = Logger.getLogger("IO");
    final static String ROOT = "./Ya-disk";
    private File file;
    private Map<String, List<Resource>> mapTree;

    public FileWriteOrRead() {
	file = new File(ROOT);
	if (!file.exists()) {
	    file.mkdir();
	}
    }

    /**
     * deelete said directory or file
     *
     * @param path "/rrrr.doc" -> "Ya-disk/rrrr.doc"
     */
    public void deleteFolderOrFile(String path) {
	deletefile(new File(ROOT + path));
    }

    private void deletefile(File path) {
	if (path.isDirectory()) {
	    for (File f : path.listFiles()) {
		if (f.isDirectory()) {
		    deletefile(f);
		} else {
		    f.delete();
		}
	    }
	}
	path.delete();
    }

    public void write(String path, ReadableByteChannel i) {

	File f = new File(ROOT + path);
	if (!f.exists()) {
	    //File folders = new File(f.getParent());
	    File folders = f.getParentFile();
	    folders.mkdirs();
	}
	if (i != null) {
	    ByteBuffer buf = ByteBuffer.allocate(1024);
	    try (FileChannel file2 = new FileOutputStream(f).getChannel()) {
		while (i.read(buf) != -1) {
		    buf.flip();
		    file2.write(buf);
		    buf.clear();
		}
	    } catch (FileNotFoundException ex) {
		Logger.getLogger(FileWriteOrRead.class.getName()).log(Level.SEVERE, null, ex);
	    } catch (IOException ex) {
		Logger.getLogger(FileWriteOrRead.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}

    }

    public ReadableByteChannel readFile(String path) throws FileNotFoundException {
	file = new File(ROOT + path);
	FileChannel fch = new FileInputStream(file).getChannel();
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

    // = =========================== 

    public Map<String, List<Resource>> getMapOfFile() {
	mapTree = new HashMap<>();
	buildTree(new File(ROOT + "/"));
	return mapTree;
    }

    private void buildTree(File f) {
	if (f.isDirectory()) {
	    for (File fl : f.listFiles()) {
		if (fl.isDirectory()) {
		    buildTree(fl);
		} else {
		    addToMapTree(fl);
		}
	    }
	}
	addToMapTree(f);
    }

    private void addToMapTree(File f) {
	System.out.println(f);
	if (f.isDirectory() && mapTree.get(f.toString()) == null) {
	    mapTree.put(f.toString(), new ArrayList<>());
	} else if (f.isFile()) {
	    Resource r = new Resource();

	    try {
		r.setSize(f.length()).setMd5(get_Md5_Hash(new FileInputStream(f).getChannel())).
			setName(f.getName()).setPath(f.getPath().replace(ROOT, "")).setSize(f.length());
		// добавить дату создания 
	    } catch (FileNotFoundException ex) {
		Logger.getLogger(FileWriteOrRead.class.getName()).log(Level.SEVERE, null, ex);
	    }
	    
	    if (mapTree.get(f.getParent()) == null) {
		List<Resource> l = new ArrayList<>();
		l.add(r);
		mapTree.put(f.getParent(), l);
	    } else {
		mapTree.get(f.getParent()).add(r);
	    }
	}
    }
    //сортировать файлы по дате обновления 
}
