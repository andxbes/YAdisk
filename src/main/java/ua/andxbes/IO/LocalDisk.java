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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import ua.andxbes.DiskForAll;
import ua.andxbes.DiskJsonObjects.Resource;
import ua.andxbes.query.YaDisk;

/**
 *
 * @author Andr
 */
public  class LocalDisk implements DiskForAll {

    public  final SimpleDateFormat dateFormat ;
    private final static Logger log = Logger.getLogger("IO");
    
    private static File fileRootDir;
    private Map<String, List<Resource>> mapTree;

   
    
    public static class FileWriteOrReadHolder{
          public static  final  LocalDisk HOLDER_Instance = new LocalDisk();
    }
    
    public static LocalDisk getInstance(){
          return FileWriteOrReadHolder.HOLDER_Instance;
    }
    
    private LocalDisk() {
          this("./Ya-disk");
    }
    

    private LocalDisk(String rootDir) {
	dateFormat = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss'+00:00'");
	//привести дату изменения к 00 часовому поясу 
	dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	if(fileRootDir == null)setRootDir(rootDir);
    }

    public static  final void setRootDir(String rootDir) {
	fileRootDir = new File(rootDir);
	if (!fileRootDir.exists()) {
	    fileRootDir.mkdir();
	}
    }

    /**
     * deelete said directory or file
     *
     * @param path "/rrrr.doc" -> "Ya-disk/rrrr.doc"
     */
    @Override
    public void deleteFolderOrFile(String path) {
	deletefile(new File(getPathToRootDir() + path));
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

    @Override
    public void write(String path, ReadableByteChannel i) {
	File f = new File(getPathToRootDir() + path);
	write(f, i);
    }

    public void write(File f, ReadableByteChannel i) {
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
		Logger.getLogger(LocalDisk.class.getName()).log(Level.SEVERE, null, ex);
	    } catch (IOException ex) {
		Logger.getLogger(LocalDisk.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}

    }

    @Override
    public ReadableByteChannel read(String path) throws FileNotFoundException {
	File file = new File(getPathToRootDir() + path);
	FileChannel fch = new FileInputStream(file).getChannel();
	return fch;
    }

    public static String get_Md5_Hash(ReadableByteChannel bch) {
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
	    Logger.getLogger(LocalDisk.class.getName()).log(Level.SEVERE, null, ex);
	}
	
	 //Todo срезается ноль вначале 
	int num ;
	if( (num = checsumm.length()) <32){
	     for(int i = 0 ;i<32 - num;i++){
	        checsumm = "0"+checsumm;
	     }
	}
	return checsumm;
    }

    // = =========================== 
    @Override
    public Map<String, List<Resource>> getResource() {
	if (mapTree != null) {
	    mapTree.clear();
	}
	mapTree = new HashMap<>();
	buildTree(new File(getPathToRootDir() + "/"));
	return toFormatYaDisk();
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
	//System.out.println(f);
	if (f.isDirectory() && mapTree.get(f.toString()) == null) {
	    mapTree.put(f.toString(), new ArrayList<>());
	} else if (f.isFile()) {
	    Resource resource = new Resource();

	    try {
		resource.setSize(f.length())
			.setMd5(get_Md5_Hash(new FileInputStream(f).getChannel()))
			.setName(f.getName())
			.setPath(f.getPath().replace(getPathToRootDir(), ""))
			.setSize(f.length())
			.setModified(dateFormat.format(f.lastModified()))
			.setInDisk(this).setToDisk(YaDisk.getInstance());
	    } catch (FileNotFoundException ex) {
		Logger.getLogger(LocalDisk.class.getName()).log(Level.SEVERE, null, ex);
	    }

	    if (mapTree.get(f.getParent()) == null) {
		List<Resource> l = new ArrayList<>();
		l.add(resource);
		mapTree.put(f.getParent(), l);
	    } else {
		mapTree.get(f.getParent()).add(resource);
	    }
	}
    }

    private Map<String, List<Resource>> toFormatYaDisk() {
	Map<String, List<Resource>> map = new HashMap<>();

	for (Map.Entry<String, List<Resource>> entrySet : mapTree.entrySet()) {
	    String localKey = entrySet.getKey().replace(getPathToRootDir(), "").replace("\\", "/");
	    if (localKey.isEmpty()) {
		localKey = "/";
	    }
	    map.put(localKey, entrySet.getValue());
	}

	return map;
    }

    /**
     * @return the fileRootDir
     */
    public static String getPathToRootDir() {
	return fileRootDir.getPath();
    }
}
