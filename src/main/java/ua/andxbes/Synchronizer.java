/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import ua.andxbes.DiskJsonObjects.Resource;
import ua.andxbes.IO.LocalDisk;
import ua.andxbes.query.YaDisk;

/**
 *
 * @author Andr
 */
public class Synchronizer {

    private final static Logger log = Logger.getLogger("Synchronizer");
    private final ExecutorService threads = Executors.newCachedThreadPool();
    private Map<String, List<Resource>> localTreeMap;
    private Map<String, List<Resource>> remoteTreeMap;
    private Map<String, String> exResource;
    private YaDisk remoteDisk;
    private final String pathToFilesStory = "./filesStory";
    private LocalDisk localDisk;

    public Synchronizer() {
	this("./Ya-disk");
    }

    /**
     *
     * @param localePathToRootDir Like as './rootFolder'
     */
    public Synchronizer(String localePathToRootDir) {
	localePathToRootDir = checkCorrect(localePathToRootDir);
	LocalDisk.setRootDir(localePathToRootDir);
	localDisk = LocalDisk.getInstance();
	remoteDisk = YaDisk.getInstance();
	exResource = loadStoryOfFiles();
    }

    protected static String checkCorrect(String localePathToRootDir) {
	if (localePathToRootDir.endsWith("/") || localePathToRootDir.endsWith("\\")) {
	    localePathToRootDir = localePathToRootDir.substring(0, localePathToRootDir.length() - 1);
	}
	return localePathToRootDir;
    }

    public void buildTree() throws InterruptedException, ExecutionException {
	Future<Map<String, List<Resource>>> fRemoteTreeMap = threads.submit(() -> remoteDisk.getResource());
	Future<Map<String, List<Resource>>> fLocalTreeMap = threads.submit(() -> localDisk.getResource());
	remoteTreeMap = fRemoteTreeMap.get();
	localTreeMap = fLocalTreeMap.get();
    }

    public void sync() throws InterruptedException, ExecutionException {
	compareDisks();
	saveStoryOfFiles();
    }
//============================== logic =========================================
    // сдесь нужно будет заменить  выполнение задачь на  их планировку 

    protected void compareDisks() throws InterruptedException, ExecutionException {
	buildTree();
	if (localTreeMap.size() > 0) {
	    for (Map.Entry<String, List<Resource>> localEntry : localTreeMap.entrySet()) {
		String localKey = localEntry.getKey();
		List<Resource> localValue = localEntry.getValue();

		List<Resource> remoteValue;
		if ((remoteValue = remoteTreeMap.get(localKey)) == null) {

		    System.out.println("copy all " + localKey);
		    copyAll(localValue, localDisk, remoteDisk);
		} else {
		    compareResources(localValue, remoteValue);
		}

	    }
	}

	//нужно только скопировать недостающие директории 
	if (remoteTreeMap.size() > 0) {
	    for (Map.Entry<String, List<Resource>> remoteEntry : remoteTreeMap.entrySet()) {
		String remoteKey = remoteEntry.getKey();
		List<Resource> remoteValue = remoteEntry.getValue();

		if (localTreeMap.get(remoteKey) == null) {
		    System.out.println("copy all  revers" + remoteKey);
		    copyAll(remoteValue, remoteDisk, localDisk);
		}

	    }
	}

    }

    protected void compareResources(List<Resource> localList, List<Resource> remoteList) {
	for (Resource local : localList) {
	    boolean isCoincedenceValue = false;
	    for (Resource remote : remoteList) {

		if (local.getName().equals(remote.getName())) {
		    isCoincedenceValue = true;
		    if (!local.getMd5().equals(remote.getMd5())) {
			replace(local, remote);
		    }
		}

	    }
	    if (!isCoincedenceValue) {
		copyFile(local, localDisk, remoteDisk);
	    }
	}

	for (Resource remote : remoteList) {
	    boolean isCoincedenceValue = false;
	    for (Resource local : localList) {
		if (remote.getName().equals(local.getName())) {
		    isCoincedenceValue = true;
		}
	    }
	    if (!isCoincedenceValue) {
		copyFile(remote, remoteDisk, localDisk);
	    }
	}

    }

    private void replace(Resource localResource, Resource remoteResources) {
	System.out.println("replace ");

	if (localResource.getModified_InMilliseconds() > remoteResources.getModified_InMilliseconds()) {
	    log.log(Level.FINE, "a = {0} >, b = {1}", new Object[]{localResource.getModified(), remoteResources.getModified()});
	    copyFile(localResource, localDisk, remoteDisk);
	} else {
	    log.log(Level.FINE, "a = {0} <, b = {1}", new Object[]{localResource.getModified(), remoteResources.getModified()});
	    copyFile(remoteResources, remoteDisk, localDisk);
	}
    }

    //================================= actions ==================================
    protected void copyAll(List<Resource> resourceList, DiskForAll fromDisk, DiskForAll inDisk) {
	for (Resource resource : resourceList) {

	    copyFile(resource, fromDisk, inDisk);
	}

    }

    private void copyFile(Resource actual, DiskForAll fromDisk, DiskForAll inDisk) {
	String aPath = actual.getPath();
	System.out.println("copy");
	log.log(Level.INFO, "copy {0}{1}", new Object[]{aPath, actual});
	try {

	    inDisk.write(aPath, fromDisk.read(aPath));
	} catch (FileNotFoundException ex) {
	    Logger.getLogger(Synchronizer.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    private void deleteFile(Resource actual, DiskForAll fromDisk) {
	String aPath = actual.getPath();

	exResource.remove(aPath);

	log.log(Level.INFO, "delete {0}{1}", new Object[]{aPath, actual});
	fromDisk.deleteFolderOrFile(aPath);
    }

    //============================ serealisation =====================================
    private void saveStoryOfFiles() {

	Gson gson = new Gson();
	String sgs = gson.toJson(exResource);
	log.info(sgs);
	try (FileWriter fw = new FileWriter(new File(pathToFilesStory))) {
	    fw.write(sgs);
	    log.info("Save story of files");
	} catch (IOException ex) {
	    Logger.getLogger(Synchronizer.class.getName()).log(Level.SEVERE, null, ex);
	}

    }

    private Map<String, String> loadStoryOfFiles() {
	Map<String, String> result = null;
	BufferedReader br = null;
	StringBuilder sb = new StringBuilder();
	try {
	    br = new BufferedReader(new FileReader(new File(pathToFilesStory)));
	    while (br.ready()) {
		sb.append(br.readLine());
	    }

	    result = new Gson().fromJson(sb.toString(), new TypeToken<Map<String, String>>() {
	    }.getType());
	} catch (FileNotFoundException ex) {

	    result = new HashMap<>();
	    log.info("new Story");
	} catch (IOException ex) {
	    Logger.getLogger(Synchronizer.class.getName()).log(Level.SEVERE, null, ex);
	} finally {
	    if (br != null) {
		try {
		    br.close();
		} catch (IOException ex) {
		    Logger.getLogger(Synchronizer.class.getName()).log(Level.SEVERE, null, ex);
		}
	    }
	}
	log.info(result.toString());
	return result;

    }
//==============================================================================
    
    
}
