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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.rmi.ConnectException;
import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;
import ua.andxbes.DiskJsonObjects.Disk;
import ua.andxbes.DiskJsonObjects.FilesResouceList;
import ua.andxbes.DiskJsonObjects.Link;
import ua.andxbes.DiskJsonObjects.Resource;
import ua.andxbes.fieldsForQuery.Field;
import ua.andxbes.util.QueryString;

/**
 *
 * @author Andr
 */
public class Query {

    private final Logger log = Logger.getLogger(this.getClass().getSimpleName());

    public static final String PATH = "path",
	    MEDIA_TYPE = "media_type",
	    SORT = "sort";
    private final String baseUrl = "https://cloud-api.yandex.net:443";
    private final String token;

    public Query(Token token) {
	this.token = token.toString();
    }

    private String GET(String operation, QueryString qParam) {
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

	} catch (MalformedURLException ex) {
	    Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
	} catch (ProtocolException ex) {
	    Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
	} catch (IOException ex) {
	    Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
	}

	return result.toString();
    }

    private String GETv2(String operation,List<Field> fields) throws UnsupportedEncodingException, ConnectException {
	QueryString qParams = new QueryString();
	qParams.add(fields);

	String param = qParams.toString().isEmpty() ? "" : "?" + qParams.toString();

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
	    int code =  conn.getResponseCode();
	   log.log(Level.INFO, "code = {0}",code);
	   if(code!= 200 ) 
	       throw new ConnectException(new Gson().fromJson(result.toString(), ua.andxbes.DiskJsonObjects.Error.class).toString());

	} catch (MalformedURLException ex) {
	    Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
	} catch (ProtocolException ex) {
	    Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
	} catch (IOException ex) {
	    Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
	}

	return result.toString();
    }

    /**
     *
     * get information about the user`s disk
     *
     * @return Disk
     */
    public Disk getDiskInfo() {
	String operation = "/v1/disk";
	Disk disk = null;
	String responce = GET(operation, new QueryString());
	if (responce != null) {
	    disk = new Gson().fromJson(responce, Disk.class);
	}

	return disk;
    }

    /**
     * get information about file and directory
     *
     * @param path request file or directory , like /v1/disk/resources?path=/dir
     * @return Resource
     * 
     */
    public Resource getResource(String path) {
	if (path == null || path.isEmpty()) {
	    throw new NoSuchFieldError();
	}
	String operation = "/v1/disk/resources";
	Resource resourceList = null;
	QueryString qParam = new QueryString();

	try {
	    qParam.add(PATH, path);
	} catch (UnsupportedEncodingException ex) {
	    Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
	}

	String responce = GET(operation, qParam);

	if (responce != null) {
	    resourceList = new Gson().fromJson(responce, Resource.class);
	}

	return resourceList;
    }

  
    public Link getLinkToDownload(String path) {
	if (path == null || path.isEmpty()) {
	    throw new NoSuchFieldError();
	}
	Link result = null;
	String operation = "/v1/disk/resources/download";

	QueryString qParam = new QueryString();

	try {
	    qParam.add(PATH, path);
	} catch (UnsupportedEncodingException ex) {
	    Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
	}

	String responce = GET(operation, qParam);

	if (responce != null) {
	    result = new Gson().fromJson(responce, Link.class);
	}

	return result;
    }

    public FilesResouceList getFiles(List<Field> fields) throws UnsupportedEncodingException, ConnectException {
	FilesResouceList result = null;
	String operation = "/v1/disk/resources/files";

	String responce = GETv2(operation, fields);
	
	if (responce != null) {
	    result = new Gson().fromJson(responce, FilesResouceList.class);
	}
	
	return result;
    }
    

}
