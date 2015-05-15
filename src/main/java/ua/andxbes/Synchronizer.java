/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import ua.andxbes.DiskJsonObjects.Resource;
import ua.andxbes.IO.FileWriteOrRead;
import ua.andxbes.query.QueryController;

/**
 *
 * @author Andr
 */
public class Synchronizer {

    private ExecutorService threads = Executors.newCachedThreadPool();
    private Map<String, List<Resource>> localTreeMap;
    private Map<String, List<Resource>> remoteTreeMap;
    private QueryController remoteDisk;
    private FileWriteOrRead localDisk;

    public Synchronizer() {
	this("./Ya-disk");
    }

    /**
     *
     * @param localePathToRootDir Like as './rootFolder'
     */
    public Synchronizer(String localePathToRootDir) {
	
	localePathToRootDir = checkCorrect(localePathToRootDir);
	localDisk = new FileWriteOrRead(localePathToRootDir);
    }

    protected String checkCorrect(String localePathToRootDir) {
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
	//System.out.println(" \n remote \n"+remoteTreeMap + " \n\n\n\n\n\n locale \n" + localTreeMap);

    }

    public void compare() {
	for (Map.Entry<String, List<Resource>> entrySet : localTreeMap.entrySet()) {
	    String key = entrySet.getKey().replace(localDisk.getPathToRootDir(), "").replace("\\", "/");
	    if (key.isEmpty()) {
		key = "/";
	    }
	    List<Resource> value = entrySet.getValue();
	    
	    
	    for (Map.Entry<String, List<Resource>> remoteEntry : remoteTreeMap.entrySet()) {
		String remoteKey = remoteEntry.getKey();
		List<Resource> remoteValue = remoteEntry.getValue();

		
		
	    }

	}

    }

}
