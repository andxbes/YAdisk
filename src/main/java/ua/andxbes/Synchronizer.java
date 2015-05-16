/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import ua.andxbes.DiskJsonObjects.Resource;
import ua.andxbes.IO.FileWriteOrRead;
import ua.andxbes.query.QueryController;

/**
 *
 * @author Andr
 */
public class Synchronizer {

    private final static Logger log = Logger.getLogger("Synchronizer");
    private final  ExecutorService threads = Executors.newCachedThreadPool();
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
	remoteDisk = new QueryController(Token.instance());
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

    public void sync() {
	compare(localTreeMap, remoteTreeMap);
	compare(remoteTreeMap, localTreeMap);
    }

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
				//проверить на то кто кого старше и на контрольные ссыммы .
				// и заменить старый файл на новый 
				log.fine(AValue1.getName() + " = " + AKey + " = " + BValue1.getName());
				if (!(AValue1.getMd5().equals(BValue1.getMd5()))) {
				    substitude(AValue1, BValue1);
				}

			    }

			}
			if (!isCoincedenceValue) {
			    //не нашли такого элемента в Б , скопировать из А в Б
			    log.fine("не нашли в " + BKey + " : " + AValue1.getName());
			}
		    }

		}

	    }
	    if (!isCoincedenceKey) {
		//не нашли такой папки в Б , скопировать все содиржимое из А в Б 
		log.fine("не нашли в B " + AKey);
	    }

	}

    }
    
    
    private void substitude( Resource a , Resource b ){
          System.out.println("заменяем ");
	  
	  if(a.getModified_InMilliseconds() > b.getModified_InMilliseconds()){
	    log.fine("a = " + a.getModified_InMilliseconds() + " >, b = "+b.getModified_InMilliseconds());
	    log.fine("a = " + a.getModified() + " >, b = "+b.getModified());
	    copyFile(a, b);
	  }else{
	       log.fine("a = " + a.getModified_InMilliseconds() + " <, b = "+b.getModified_InMilliseconds());
	       log.fine("a = " + a.getModified() + " <, b = "+b.getModified());
	       copyFile(b, a);
	  }
	  
    }

    private void copyFile(Resource actual, Resource override) {
	String aPath = actual.getPath();
	try {
	    override.getDisk().write(aPath,actual.getDisk().read(aPath));
	} catch (FileNotFoundException ex) {
	    Logger.getLogger(Synchronizer.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

}
