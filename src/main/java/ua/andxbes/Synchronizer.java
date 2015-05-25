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
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
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
public class Synchronizer extends Observable {

    private final Logger log = Logger.getLogger(this.getClass().getName());
    private final ExecutorService threads = Executors.newCachedThreadPool();//TODO не особо и нужен , заменить 

    private ExecutorService threadOfTasks;
    private final List<Runnable> listofTasks;
    private volatile String sb ;

    private Map<String, List<Resource>> localTreeMap;
    private Map<String, List<Resource>> remoteTreeMap;
    private Map<String, String> exResource;
    private YaDisk remoteDisk;
    private final String pathToFilesStory = "./filesStory";
    private LocalDisk localDisk;
    private volatile int inAll;

    public Synchronizer() {
	this("./Ya-disk");
    }

    /**
     *
     * @param localePathToRootDir Like as './rootFolder'
     */
    public Synchronizer(String localePathToRootDir) {
         
	//TODO если больше 1  потокa  ,то непредсказуемо ведет себя YaDisk , нужно синхронизировать 
	threadOfTasks = Executors.newFixedThreadPool(1);
	listofTasks = Collections.synchronizedList(new LinkedList<>());

	localePathToRootDir = checkCorrect(localePathToRootDir);
	LocalDisk.setRootDir(localePathToRootDir);
	localDisk = new LocalDisk();
	remoteDisk = new YaDisk();
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

    public void sync() {
	try {
	    compareDisks();
	    runTasks(); 
	} catch (InterruptedException | ExecutionException ex) {
	    Logger.getLogger(Synchronizer.class.getName()).log(Level.SEVERE, null, ex);
	}

    }

    private void runTasks() throws InterruptedException {
	inAll = listofTasks.size();
	for (Runnable listofTask : listofTasks) {
	     threadOfTasks.submit(listofTask);
	}
	

    }

    /**
     * Necessarity to close at the end of work
     *
     * @throws InterruptedException
     */
    public void close() {
	threadOfTasks.shutdown();
	while (!threadOfTasks.isTerminated()) {
	    log.log(Level.INFO, "task  = {0}", listofTasks.size());
	    try {
		Thread.sleep(500);
	    } catch (InterruptedException ex) {
		Logger.getLogger(Synchronizer.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}
	saveStoryOfFiles();
    }

//============================== logic =========================================
    protected void compareDisks() throws InterruptedException, ExecutionException {
	buildTree();
	if (localTreeMap.size() > 0) {
	    for (Map.Entry<String, List<Resource>> localEntry : localTreeMap.entrySet()) {
		String localKey = localEntry.getKey();
		List<Resource> localValue = localEntry.getValue();

		List<Resource> remoteValues;
		if ((remoteValues = remoteTreeMap.get(localKey)) == null) {

		    if (exResource.get(localKey) == null) {
			if (!localValue.isEmpty()) {
			    copyAll(localValue, localDisk, remoteDisk);
			    exResource.put(localKey, "");
			}
		    } else {
			deleteFile(localKey, localDisk);
		    }

		} else {
		    compareResources(localValue, remoteValues);
		}

	    }
	}

	//нужно только скопировать недостающие директории 
	if (remoteTreeMap.size() > 0) {
	    for (Map.Entry<String, List<Resource>> remoteEntry : remoteTreeMap.entrySet()) {
		String remoteKey = remoteEntry.getKey();
		List<Resource> remoteValue = remoteEntry.getValue();

		if ((localTreeMap.get(remoteKey)) == null) {
		    if (exResource.get(remoteKey) == null) {
			if (!remoteValue.isEmpty()) {
			    copyAll(remoteValue, remoteDisk, localDisk);
			    exResource.put(remoteKey, "");
			}
		    } else {
			deleteFile(remoteKey, remoteDisk);
		    }
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
		if (exResource.get(local.getPath()) == null) {
		    copyFile(local, localDisk, remoteDisk);
		} else {
		    deleteFile(local, localDisk);
		}
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
		if (exResource.get(remote.getPath()) == null) {
		    copyFile(remote, remoteDisk, localDisk);
		} else {
		    deleteFile(remote, remoteDisk);
		}
	    }
	}

    }

    private void replace(Resource localResource, Resource remoteResources) {

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

    private void deleteFile(Resource actual, DiskForAll fromDisk) {
	String aPath = actual.getPath();
	deleteFile(aPath, fromDisk);
    }

    private void copyFile(Resource actual, DiskForAll fromDisk, DiskForAll inDisk) {
	listofTasks.add(new Runnable() {
	    @Override
	    public void run() {
		String aPath = actual.getPath();
		sb = aPath;
		exResource.put(aPath, actual.getMd5());
		log.log(Level.INFO, "copy {0}{1}", new Object[]{aPath, actual});
		try {

		    inDisk.write(aPath, fromDisk.read(aPath));
		} catch (FileNotFoundException ex) {
		    Logger.getLogger(Synchronizer.class.getName()).log(Level.SEVERE, null, ex);
		}
		//delete this task when completed
		listofTasks.remove(this);
		deleteTask();
	    }
	});

    }

    private void deleteFile(String path, DiskForAll fromDisk) {
	listofTasks.add(new Runnable() {

	    @Override
	    public void run() {
		sb = path;
		exResource.remove(path);
		log.log(Level.INFO, "delete {0}", new Object[]{path});
		fromDisk.deleteFolderOrFile(path);
		//delete this task when completed
		listofTasks.remove(this);
		deleteTask();
	    }
	});

    }

    private void deleteTask() {
	//send  to the  observers
	
		setChanged();
	        double part = part(inAll, listofTasks.size());
	
	        notifyObservers(part);
	
    }

    private double part(double max, double current) {
	double result  = (1.0 - (current / max));
	return result;
    }

    //============================ serealisation =====================================
    private void saveStoryOfFiles() {

	Gson gson = new Gson();
	String sgs = gson.toJson(exResource);
	log.finer(sgs);
	try (FileWriter fw = new FileWriter(new File(pathToFilesStory))) {
	    fw.write(sgs);
	    log.info("Saving story of files in " + pathToFilesStory);
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
	log.finer(result.toString());
	return result;

    }
//==============================================================================

    
    public String  getStatus(){
       return sb;
    }
}
