/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes;

import java.io.FileNotFoundException;
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
    private Map<String , Resource> exResource;
    private YaDisk remoteDisk;
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
	localDisk =  LocalDisk.getInstance();
	remoteDisk =  YaDisk.getInstance();
	loadExResource();
    }

    private void loadExResource() {
	exResource = new HashMap<>();
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
	compare(localTreeMap, remoteTreeMap);
	buildTree();
	compare(remoteTreeMap, localTreeMap);
    }
    
    
    // сдесь нужно будет заменить  выполнение задачь на  их планировку 
    protected void compare(Map<String, List<Resource>> AMap, Map<String, List<Resource>> BMap) {
	 
	for (Map.Entry<String, List<Resource>> AEntry : AMap.entrySet()) {
	    String AKey = AEntry.getKey();
	    List<Resource> AValue = AEntry.getValue();
	    boolean isCoincedenceKey = false;


	    for (Map.Entry<String, List<Resource>> BEntry : BMap.entrySet()) {
		String BKey = BEntry.getKey();
		List<Resource> BValue = BEntry.getValue();
		
		if (AKey.equals(BKey)) {
		    isCoincedenceKey = true;

		    for (Resource AValue1 : AValue) {
			boolean isCoincedenceValue = false;
			for (Resource BValue1 : BValue) {

			    if (AValue1.getName().equals(BValue1.getName())) {
				isCoincedenceValue = true;

				//проверить на то, кто кого старше и на контрольные ссыммы .
				// и заменить старый файл на новый 
				log.log(Level.FINE, "{0} = {1} = {2}", new Object[]{AValue1.getName(), AKey, BValue1.getName()});
				if (!(AValue1.getMd5().equals(BValue1.getMd5()))) {
				    replace(AValue1, BValue1);
				}

			    }

			}
			if (!isCoincedenceValue) {
			    //не нашли такого элемента в Б , скопировать из А в Б
			    log.log(Level.FINE, "\u043d\u0435 \u043d\u0430\u0448\u043b\u0438 \u0432 {0} : {1} , path - {2}"
				    , new Object[]{BKey, AValue1.getName(), AValue1.getPath()});
			    copyFile(AValue1);
			}
		    }

		}

	    }
	    if (!isCoincedenceKey) {
		//не нашли такой папки в Б , скопировать все содиржимое из А в Б 
		log.log(Level.FINE, "\u043d\u0435 \u043d\u0430\u0448\u043b\u0438 \u0432 B {0}", AKey);
		for (Resource AValue1 : AValue) {
		    copyFile(AValue1);
		}
	    }

	}

    }
//=============================================================================

    private void replace(Resource a, Resource b) {
	log.fine("replace ");

	if (a.getModified_InMilliseconds() > b.getModified_InMilliseconds()) {
	    log.log(Level.FINE, "a = {0} >, b = {1}", new Object[]{a.getModified(), b.getModified()});
	    copyFile(a);
	} else {
	    log.log(Level.FINE, "a = {0} <, b = {1}", new Object[]{a.getModified(), b.getModified()});
	    copyFile(b);
	}

    }

    private void copyFile(Resource actual) {
	String aPath = actual.getPath();
	
	exResource.put(aPath, actual);
	
	log.log(Level.INFO, "copy {0}{1}", new Object[]{aPath, actual});
	try {
	    actual.getToDisk().write(aPath, actual.getInDisk().read(aPath));
	} catch (FileNotFoundException ex) {
	    Logger.getLogger(Synchronizer.class.getName()).log(Level.SEVERE, null, ex);
	}
    }
    
     private void deleteFile(Resource actual) {
	String aPath = actual.getPath();
	
	exResource.remove(aPath);
	
	log.log(Level.INFO, "copy {0}{1}", new Object[]{aPath, actual});
	actual.getToDisk().deleteFolderOrFile(aPath);
    }
//=============================================================================
}
