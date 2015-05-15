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
	System.out.println(" \n remote \n"+remoteTreeMap + " \n\n\n\n\n\n locale \n" + localTreeMap);

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
			boolean isCoincedenceValue = false ;
			for (Resource BValue1 : BValue) {
			    if(AValue1.getName().equals(BValue1.getName())){
				isCoincedenceValue = true;
			        //проверить на то кто кого старше и на контрольные ссыммы .
				// и заменить старый файл на новый 
				System.out.println( AValue1.getName()+" = "+ BValue1.getName());
			    } 
			    
			}
			 if(!isCoincedenceValue){
			//не нашли такого элемента в Б , скопировать из А в Б
			System.out.println("не нашли в "+ BKey + " : " + AValue1.getName());
		    }
		    }
		   

		}

	    }
	    if(!isCoincedenceKey){
	       //не нашли такой папки в Б , скопировать все содиржимое из А в Б 
	       System.out.println("не нашли в B " + AKey);
	    }

	}

    }

}
