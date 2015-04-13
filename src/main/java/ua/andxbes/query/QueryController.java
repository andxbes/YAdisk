/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes.query;

import java.rmi.ConnectException;
import java.util.logging.Logger;

import ua.andxbes.DiskJsonObjects.Disk;
import ua.andxbes.DiskJsonObjects.FilesResouceList;
import ua.andxbes.DiskJsonObjects.LastUploadedResourceList;
import ua.andxbes.DiskJsonObjects.Link;
import ua.andxbes.DiskJsonObjects.Operation;
import ua.andxbes.DiskJsonObjects.PublicResourcesList;
import ua.andxbes.DiskJsonObjects.Resource;
import ua.andxbes.Token;
import ua.andxbes.fieldsForQuery.Field;
import ua.andxbes.fieldsForQuery.Fields;
import ua.andxbes.fieldsForQuery.Limit;
import ua.andxbes.fieldsForQuery.MediaType;
import ua.andxbes.fieldsForQuery.OperationId;
import ua.andxbes.fieldsForQuery.Path;

/**
 *
 * @author Andr
 */
public class QueryController {

    protected final Logger log = Logger.getLogger(this.getClass().getSimpleName());

    
    static final String baseUrl = "https://cloud-api.yandex.net:443";
    static String token;
    private static final Query GET = new Query();

    public QueryController(Token token) {
	QueryController.token = token.toString();
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
     */
    public Disk getDiskInfo() throws ConnectException {
	String operation = "/v1/disk";
	return GET.getObgect(Query.GET, operation, null, Disk.class);
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
	return GET.getObgect(Query.GET, operation, fields,Resource.class);
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
	return GET.getObgect(Query.GET, operation, fields,Link.class);
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
    public FilesResouceList getFiles(Field... fields) throws ConnectException{
	String operation = "/v1/disk/resources/files";
	return GET.getObgect(Query.GET, operation, fields,FilesResouceList.class);
    }

    /**
     * Query list of file by Last update
     *
     * @param fields hendled arguments (Fields , Limit , MediaType )
     * Offset , PrevievCroup , Previev_Size)
     * @return FilesResouceList
     * @see LastUploadedResourceList
     * @see Limit
     * @see Fields
     * @see MediaType
     *
     */
    public LastUploadedResourceList getLastUploadedList(Field... fields) throws ConnectException {
	String operation = "/v1/disk/resources/last-uploaded";
	return GET.getObgect(Query.GET, operation, fields,LastUploadedResourceList.class);
    }
    
    
    /**
     * Query list of public resources 
     *
     * @param fields hendled arguments (Fields , Limit ,Type ) 
     * Offset , PrevievCroup , Previev_Size)
     * @return FilesResouceList
     * @see PublicResourcesList
     * @see Limit
     * @see Fields
     * @see Type
     *
     */
    public PublicResourcesList getPublicResources(Field... fields) throws ConnectException {
	String operation = "/v1/disk/resources/last-uploaded";
	return GET.getObgect(Query.GET, operation, fields,PublicResourcesList.class);
    }
    
    /**
     *
     * Query link for file Upload
     *
     * @param fields Path(mandatory field)
     * @return Link
     * @throws NoSuchFieldError not Path field
     * @see Link
     */
    public Link getLinkForUpload(Field... fields)
	    throws NoSuchFieldError, ConnectException {
	
	if (!checkRequiredField(fields, new Class[]{Path.class})) {
	    throw new NoSuchFieldError();
	}
	String operation = "/v1/disk/resources/upload";
	return GET.getObgect(Query.GET, operation, fields,Link.class);
    }
    
    /**
     *
     * Query status asynchronous operation 
     *
     * @param fields Operation(mandatory field) , Fields
     * @return status asynchronous operation 
     * @throws NoSuchFieldError not OperationId field
     * @see Operation
     * @see OperationId
     */
    public Operation getStatusOperationId(Field... fields)//TODO Не на чём ,пока, тестировать . 
	    throws NoSuchFieldError, ConnectException {
	
	if (!checkRequiredField(fields, new Class[]{OperationId.class})) {
	    throw new NoSuchFieldError();
	}
	String operationId = "";
	for(Field field : fields){
	    if(OperationId.class.isInstance(field)) operationId = field.getField();
	}
	String operation = "/v1/disk/operations/" + operationId;
	return GET.getObgect(Query.GET, operation, fields,Operation.class);
    }
    

}