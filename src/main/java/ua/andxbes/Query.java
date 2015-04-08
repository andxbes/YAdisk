/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.rmi.ConnectException;
import java.util.logging.Level;
import java.util.logging.Logger;

import ua.andxbes.DiskJsonObjects.Disk;
import ua.andxbes.DiskJsonObjects.FilesResouceList;
import ua.andxbes.DiskJsonObjects.LastUploadedResourceList;
import ua.andxbes.DiskJsonObjects.Link;
import ua.andxbes.DiskJsonObjects.Operation;
import ua.andxbes.DiskJsonObjects.PublicResourcesList;
import ua.andxbes.DiskJsonObjects.Resource;
import ua.andxbes.fieldsForQuery.Field;
import ua.andxbes.fieldsForQuery.Fields;
import ua.andxbes.fieldsForQuery.Limit;
import ua.andxbes.fieldsForQuery.MediaType;
import ua.andxbes.fieldsForQuery.OperationId;
import ua.andxbes.fieldsForQuery.Path;
import ua.andxbes.util.QueryString;

/**
 *
 * @author Andr
 */
public class Query {

    protected final Logger log = Logger.getLogger(this.getClass().getSimpleName());

    public static final String PATH = "path",
	    MEDIA_TYPE = "media_type",
	    SORT = "sort";
    protected final String baseUrl = "https://cloud-api.yandex.net:443";
    protected final String token;

    public Query(Token token) {
	this.token = token.toString();
    }

    protected String GET(String operation, QueryString qParam) {
	String param = qParam.toString().isEmpty() ? "" : "?" + qParam.toString();
	StringBuilder result = new StringBuilder();
	URL url;
	HttpURLConnection conn;
	BufferedReader br;
	String line;
	try {

	    url = new URL(baseUrl + operation + param);
	    conn = (HttpURLConnection) url.openConnection();
	    conn.setRequestMethod("GET");
	    conn.addRequestProperty("Authorization", "OAuth " + token);

	    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

	    while ((line = br.readLine()) != null) {
		result.append(line);
	    }

	    int code = conn.getResponseCode();
	    log.log(Level.INFO, "code = {0}", code);
	    if (code != 200) {
		throw new ConnectException(new Gson().fromJson(result.toString(), ua.andxbes.DiskJsonObjects.Error.class).toString());
	    }

	} catch (MalformedURLException ex) {
	    Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
	} catch (ProtocolException ex) {
	    Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
	} catch (IOException ex) {
	    Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
	}

	return result.toString();
    }

    protected String GET(String operation, Field[] fields){
	QueryString qParams = new QueryString();
	qParams.add(fields);
	return GET(operation, qParams);
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

    protected <T> T getObgect(Class<T> clazz, String operation, Field[] fields){
	T object = null;

	String responce = GET(operation, fields);

	if (responce != null) {
	    object = new Gson().fromJson(responce, clazz);
	}
	return object;
    }
//==============================================================================
//============================  public methods  ================================
//==============================================================================

    /**
     *
     * Get information about the Ya-disk
     *
     * @return Disk
     */
    public Disk getDiskInfo() {
	String operation = "/v1/disk";
	return getObgect(Disk.class, operation, null);
    }

    /**
     * Get information about file and directory of path
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
	return getObgect(Resource.class, operation, fields);
    }

    /**
     *
     * Get link for file download
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
	return getObgect(Link.class, operation, fields);
    }

    /**
     * Get list of file by filter
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
    public FilesResouceList getFiles(Field... fields){
	String operation = "/v1/disk/resources/files";
	return getObgect(FilesResouceList.class, operation, fields);
    }

    /**
     * Get list of file by Last update
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
    public LastUploadedResourceList getLastUploadedList(Field... fields) {
	String operation = "/v1/disk/resources/last-uploaded";
	return getObgect(LastUploadedResourceList.class, operation, fields);
    }
    
    
    /**
     * Get list of public resources 
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
    public PublicResourcesList getPublicResources(Field... fields) {
	String operation = "/v1/disk/resources/last-uploaded";
	return getObgect(PublicResourcesList.class, operation, fields);
    }
    
    /**
     *
     * Get link for file Upload
     *
     * @param fields Path(mandatory field)
     * @return Link
     * @throws NoSuchFieldError not Path field
     * @see Link
     */
    public Link getLinkForUpload(Field... fields)
	    throws NoSuchFieldError {
	
	if (!checkRequiredField(fields, new Class[]{Path.class})) {
	    throw new NoSuchFieldError();
	}
	String operation = "/v1/disk/resources/upload";
	return getObgect(Link.class, operation, fields);
    }
    
    /**
     *
     * Get status asynchronous operation 
     *
     * @param fields Operation(mandatory field) , Fields
     * @return status asynchronous operation 
     * @throws NoSuchFieldError not OperationId field
     * @see Operation
     * @see OperationId
     */
    public Operation getStatusOperationId(Field... fields)//TODO Не на чём ,пока, тестировать . 
	    throws NoSuchFieldError {
	
	if (!checkRequiredField(fields, new Class[]{OperationId.class})) {
	    throw new NoSuchFieldError();
	}
	String operationId = "";
	for(Field field : fields){
	    if(OperationId.class.isInstance(field)) operationId = field.getField();
	}
	String operation = "/v1/disk/operations/" + operationId;
	return getObgect(Operation.class, operation, fields);
    }
    

}
