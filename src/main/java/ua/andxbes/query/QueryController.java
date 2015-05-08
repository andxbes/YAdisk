/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes.query;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.rmi.ConnectException;
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
import ua.andxbes.fieldsForQuery.Path;

/**
 *
 * @author Andr
 */
public class QueryController {

    protected final Logger log = Logger.getLogger(this.getClass().getSimpleName());

    static final String baseUrl = "https://cloud-api.yandex.net:443";
    static String token;
    private  final Query query ;

    public QueryController(Token token) {
	QueryController.token = token.toString();
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
     * @return Disk
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
	return query.getObgect(Query.GET, operation, fields, Link.class);
    }

    /**
     *
     * Query link for file copy disk to disk
     *
     * @param fields Path ,From(mandatory field)
     * @return Link
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
	if (link.isAsync()) {
	    
	    log.log(Level.INFO, "info{0}", link.toString());
	    refrashStatusOperationId(link);
	}
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

    public void putFileToServer(File file, Field... field) throws NoSuchFieldError, ConnectException, FileNotFoundException, IOException {
	Link l = getLinkForUpload(field);
	FileChannel fc = new FileInputStream(file).getChannel();
	query.query(l.getMethod(), new URL(l.getHref()), fc);
	log.log(Level.INFO, "data = {0}code = {1}", new Object[]{query.getResponse(), query.getCode()});
    }

    public Link createFolderInDisk( Field... field) throws NoSuchFieldError, ConnectException, FileNotFoundException, IOException {
	String operation = "/v1/disk/resources";
	return query.getObgect(Query.PUT, operation, field, Link.class);
    }

    /**
     *
     * @param field
     * @return
     * @throws NoSuchFieldError
     * @throws ConnectException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void deleteFileOrFolder(Field... field) throws NoSuchFieldError, ConnectException, FileNotFoundException, IOException {
	String operation = "/v1/disk/resources";
        query.query(Query.DELETE, operation, field,null);
    }
    
    /**
     *
     * @param field
     * Если параметр path не задан или указывает на корень Корзины, 
     * то корзина будет полностью очищена, иначе из Корзины будет удалён только тот ресурс,
     * на который указывает path.
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
    
    



}
