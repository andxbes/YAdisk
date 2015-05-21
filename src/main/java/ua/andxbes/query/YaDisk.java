/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes.query;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.rmi.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import ua.andxbes.DiskJsonObjects.Disk;
import ua.andxbes.DiskJsonObjects.FilesResouceList;
import ua.andxbes.DiskJsonObjects.LastUploadedResourceList;
import ua.andxbes.DiskJsonObjects.Link;
import ua.andxbes.DiskJsonObjects.PublicResourcesList;
import ua.andxbes.DiskJsonObjects.Resource;
import ua.andxbes.Token;
import ua.andxbes.fieldsForQuery.Field;
import ua.andxbes.fieldsForQuery.Fields;
import ua.andxbes.fieldsForQuery.From;
import ua.andxbes.fieldsForQuery.Limit;
import ua.andxbes.fieldsForQuery.MediaType;
import ua.andxbes.fieldsForQuery.Overwrite;
import ua.andxbes.fieldsForQuery.Path;

/**
 *
 * @author Andr
 */
public class YaDisk implements ua.andxbes.DiskForAll {

    protected final Logger log = Logger.getLogger(this.getClass().getSimpleName());

    static final String baseUrl = "https://cloud-api.yandex.net:443";
    static String token;
    private final Query query;

    private static volatile YaDisk instance;

    public synchronized static YaDisk getInstance() {
	YaDisk localInstance = instance;
	if (localInstance == null) {
	    synchronized (YaDisk.class) {
		localInstance = instance;
		if (localInstance == null) {
		    instance = localInstance = new YaDisk();
		}
	    }
	}
	return localInstance;
    }

    private YaDisk() {
	this.token = Token.instance().toString();
	query = new Query();
    }

    /**
     * Checking <b> fields <\b> for the presence of specific types
     *
     * @param fields list checked items
     * @param cl by condition
     * @return is True ?
     */
    protected boolean checkRequiredField(Field[] fields, Class[] cl) {

	boolean temp = false;
	for (Class cl1 : cl) {
	    for (Field field : fields) {
		if ((temp = cl1.isInstance(field))) {
		    break;
		}
	    }
	    if (!temp) {
		return temp;
	    }
	}
	return temp;
    }

//==============================================================================
//============================  public methods  ================================
//==============================================================================
    /**
     *
     * Query information about the Ya-disk
     *
     * @return DiskForAll
     * @throws java.rmi.ConnectException
     */
    public Disk getDiskInfo() throws ConnectException {
	String operation = "/v1/disk";
	return query.getObgect(Query.GET, operation, null, Disk.class);
    }

    /**
     * Query information about file and directory of path
     *
     * @param fields Path(mandatory field) , Fields , Limit , Sort
     * @return Resource
     * @throws java.rmi.ConnectException
     * @throws NoSuchFieldError not Path field
     *
     */
    public Resource getResource(Field... fields)
	    throws ConnectException, NoSuchFieldError {

	if (!checkRequiredField(fields, new Class[]{Path.class})) {
	    throw new NoSuchFieldError();
	}
	String operation = "/v1/disk/resources";
	return query.getObgect(Query.GET, operation, fields, Resource.class);
    }

    /**
     *
     * Query link for file download
     *
     * @param fields Path(mandatory field)
     * @return Link
     * @throws java.rmi.ConnectException
     * @throws NoSuchFieldError not Path field
     * @see Link
     */
    public Link getLinkToDownload(Field... fields)
	    throws NoSuchFieldError, ConnectException {

	if (!checkRequiredField(fields, new Class[]{Path.class})) {
	    throw new NoSuchFieldError();
	}
	String operation = "/v1/disk/resources/download";
	return query.getObgect(Query.GET, operation, fields, Link.class);
    }

    /**
     * Query list of file by filter
     *
     * @param fields hendled arguments (Fields , Limit , MediaType , Sort )
     * Offset , PrevievCroup , Previev_Size)
     * @return FilesResouceList
     * @throws java.rmi.ConnectException
     * @see FilesResouceList
     * @see Limit
     * @see Fields
     * @see MediaType
     *
     */
    public FilesResouceList getFiles(Field... fields) throws ConnectException {
	String operation = "/v1/disk/resources/files";
	return query.getObgect(Query.GET, operation, fields, FilesResouceList.class, null);
    }

    /**
     * Query list of file by Last update
     *
     * @param fields hendled arguments (Fields , Limit , MediaType ) Offset ,
     * PrevievCroup , Previev_Size)
     * @return FilesResouceList
     * @throws java.rmi.ConnectException
     * @see LastUploadedResourceList
     * @see Limit
     * @see Fields
     * @see MediaType
     *
     */
    public LastUploadedResourceList getLastUploadedList(Field... fields) throws ConnectException {
	String operation = "/v1/disk/resources/last-uploaded";
	return query.getObgect(Query.GET, operation, fields, LastUploadedResourceList.class);
    }

    /**
     * Query list of public resources
     *
     * @param fields hendled arguments (Fields , Limit ,Type ) Offset ,
     * PrevievCroup , Previev_Size)
     * @return FilesResouceList
     * @throws java.rmi.ConnectException
     * @see PublicResourcesList
     * @see Limit
     * @see Fields
     * @see Type
     *
     */
    public PublicResourcesList getPublicResources(Field... fields) throws ConnectException {
	String operation = "/v1/disk/resources/last-uploaded";
	return query.getObgect(Query.GET, operation, fields, PublicResourcesList.class);
    }

    /**
     *
     * Query link for file Upload
     *
     * @param fields Path(mandatory field)
     * @return Link
     * @throws NoSuchFieldError not Path field
     * @throws java.rmi.ConnectException
     * @see Link
     */
    public Link getLinkForUpload(Field... fields)
	    throws NoSuchFieldError, ConnectException {
	if (!checkRequiredField(fields, new Class[]{Path.class})) {
	    throw new NoSuchFieldError();
	}
	String operation = "/v1/disk/resources/upload";
	Link link = null;

	try {
	    link = query.getObgect(Query.GET, operation, fields, Link.class);
	} catch (ConnectException ex) {
	    if (query.getCode() == 409) {

		String path = returnPath(fields);

		String pathEl[] = path.split("/");
		path = path.replaceAll(pathEl[pathEl.length - 1], "");

		try {
		    createFolderInDisk(new Path(path));
		    link = getLinkForUpload(fields);
		} catch (IOException ex1) {
		    Logger.getLogger(YaDisk.class.getName()).log(Level.SEVERE, null, ex1);
		}

	    } else {
		throw new ConnectException("Error", ex);
	    }
	}
	return link;
    }

    /**
     *
     * Query link for file copy disk to disk
     *
     * @param fields Path ,From(mandatory field)
     * @return Link Если копирование происходит асинхронно, то вернёт ответ с
     * кодом 202 и ссылкой на асинхронную операцию. Иначе вернёт ответ с кодом
     * 201 и ссылкой на созданный ресурс. в нашем случае определить можно по
     * флагу в обьекте
     * @throws NoSuchFieldError us not Path ,From
     * @throws java.rmi.ConnectException
     * @see Link
     */
    public Link postCopy(Field... fields)
	    throws NoSuchFieldError, ConnectException {
	if (!checkRequiredField(fields, new Class[]{Path.class, From.class})) {
	    throw new NoSuchFieldError();
	}
	String operation = "/v1/disk/resources/copy";
	Link link;
	link = query.getObgect(Query.POST, operation, fields, Link.class);
	return link;
    }

    /**
     *
     * Query link for file copy disk to disk
     *
     * @param fields Path ,From(mandatory field)
     * @return Link Если перемещение происходит асинхронно, то вернёт ответ с
     * кодом 202 и ссылкой на асинхронную операцию. Иначе вернёт ответ с кодом
     * 201 и ссылкой на созданный ресурс. в нашем случае определить можно по
     * флагу в обьекте
     * @throws NoSuchFieldError us not Path ,From
     * @throws java.rmi.ConnectException
     * @see Link
     */
    public Link postMove(Field... fields)
	    throws NoSuchFieldError, ConnectException {
	if (!checkRequiredField(fields, new Class[]{Path.class, From.class})) {
	    throw new NoSuchFieldError();
	}
	String operation = "/v1/disk/resources/move";
	Link link;
	link = query.getObgect(Query.POST, operation, fields, Link.class);
	return link;
    }

    /**
     *
     * Query status asynchronous operation result avalibel throught the
     * getCurrentTask .If it size is Empty , then all tasks completed
     *
     * @param link on assinchorous operation
     * @return Thread
     */
    public boolean refrashStatusOperationId(Link link) {

	/*
	 response = {"status":"in-progress"}
	 response = {"status":"success"}
	    
	 */
	Pattern pattern = Pattern.compile("[\"|}|{]");

	try {
	    query.query(link.getMethod(), new URL(link.getHref()), null);
	    String status = pattern.matcher(query.getResponse().split(":")[1]).replaceAll("");
	    log.log(Level.INFO, "\n s = {0}", status);

	    if (status.equals("success")) {
		return true;
	    }
	} catch (ConnectException | MalformedURLException ex) {
	    Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
	}
	return false;
    }

    public void putFileToServer(ReadableByteChannel fc, Field... fields) throws NoSuchFieldError, ConnectException, MalformedURLException {
	Link l = getLinkForUpload(fields);
	query.query(l.getMethod(), new URL(l.getHref()), fc);
	log.log(Level.INFO, "data = {0}code = {1}", new Object[]{query.getResponse(), query.getCode()});
    }

    public Link createFolderInDisk(Field... field) throws NoSuchFieldError, ConnectException, FileNotFoundException, IOException {
	//201 - ок
	//409 - this folder there is , or is not
	String operation = "/v1/disk/resources";
	Link link = new Link();
	try {
	    link = query.getObgect(Query.PUT, operation, field, Link.class);
	} catch (ConnectException ex) {

	    if (query.getCode() == 409) {
		createTheMissingFolders(field);
	    } else {
		throw new ConnectException("error", ex);
	    }
	}
	return link;
    }

    private void createTheMissingFolders(Field[] field) throws NoSuchFieldError, IOException {
	String operation = "/v1/disk/resources";
	String path = returnPath(field);
	String elfromPath[] = path.split("/");
	String ccurentUrl = "";
	for (String elfromPath1 : elfromPath) {
	    try {
		ccurentUrl += elfromPath1 + "/";
		query.getObgect(Query.PUT, operation, new Field[]{new Path(ccurentUrl)}, Link.class);
	    } catch (ConnectException e) {
		if (query.getCode() != 409) {
		    throw new ConnectException("errrrr", e);
		}
	    }
	}
    }

    private String returnPath(Field[] field) {
	String path = null;
	for (Field field1 : field) {
	    if (field1.getNameField().equals("path")) {
		path = field1.getField();
	    }
	}
	return path;
    }

    /**
     *
     * @param field
     * @return Если удаление происходит асинхронно, то вернёт ответ со статусом
     * 202 и ссылкой на асинхронную операцию. Иначе вернёт ответ со статусом 204
     * и пустым телом.
     * @throws NoSuchFieldError
     * @throws ConnectException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public Link deleteFileOrFolder(Field... field) throws NoSuchFieldError, ConnectException, IOException {
	String operation = "/v1/disk/resources";
	return query.getObgect(Query.DELETE, operation, field, Link.class);
    }

    /**
     *
     * @param field Если параметр path не задан или указывает на корень Корзины,
     * то корзина будет полностью очищена, иначе из Корзины будет удалён только
     * тот ресурс, на который указывает path.
     * @return
     * @throws NoSuchFieldError
     * @throws ConnectException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public Link CleanTrashFolder(Field... field) throws NoSuchFieldError, ConnectException, FileNotFoundException, IOException {
	String operation = "/v1/disk/trash/resources";
	return query.getObgect(Query.DELETE, operation, field, Link.class);
    }

    public ReadableByteChannel downloadFile(Link link) {
	ReadableByteChannel rbch = null;
	try {
	    rbch = Channels.newChannel(query.downloadFile(new URL(link.getHref())));
	} catch (MalformedURLException ex) {
	    Logger.getLogger(YaDisk.class.getName()).log(Level.SEVERE, null, ex);
	}
	return rbch;

    }

    @Override
    public Map<String, List<Resource>> getResource() {
	return getResource("/");
    }

    private Map<String, List<Resource>> map = null;

    public Map<String, List<Resource>> getResource(String path) {
	map = new HashMap<>();
	readFolder(path);
	return map;
    }

    protected void readFolder(String path) {
	map.put(path, new ArrayList<>());
	Resource resource = null;
	try {
	    resource = getResource(new Path(path));
	} catch (ConnectException | NoSuchFieldError ex) {
	    Logger.getLogger(YaDisk.class.getName()).log(Level.SEVERE, null, ex);
	}
	Resource[] list = null;

	if (resource != null && (list = resource.getEmbedded().getItems()) != null) {
	    for (Resource list1 : list) {
		if (list1.getType().equals("dir")) {
		    readFolder(list1.getPath());
		} else {
		    map.get(path).add(list1);
		}
	    }
	}

    }

    @Override
    public ReadableByteChannel read(String path) throws FileNotFoundException {
	ReadableByteChannel result = null;
	try {
	    result = downloadFile(getLinkToDownload(new Path(path)));
	} catch (NoSuchFieldError ex) {
	    Logger.getLogger(YaDisk.class.getName()).log(Level.SEVERE, null, ex);
	} catch (ConnectException ex) {
	    throw new FileNotFoundException(ex.toString());
	}
	return result;
    }

    @Override
    public void write(String path, ReadableByteChannel i) {
	try {
	    putFileToServer(i, new Path(path), new Overwrite(true));
	} catch (NoSuchFieldError | ConnectException | MalformedURLException ex) {
	    Logger.getLogger(YaDisk.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    @Override
    public void deleteFolderOrFile(String path) {
	try {
	    deleteFileOrFolder(new Path(path));
	} catch (NoSuchFieldError | IOException ex) {
	    Logger.getLogger(YaDisk.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

}
