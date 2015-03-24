/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import ua.andxbes.Disk.Disk;
import ua.andxbes.Disk.Link;
import ua.andxbes.Disk.Resource;

/**
 *
 * @author Andr
 */
public class Query {

    private final Logger log = Logger.getLogger(this.getClass().getSimpleName());

    private final String baseUrl = "https://cloud-api.yandex.net:443";
    private final String token;

    public Query(Token token) {
	this.token = token.toString();
    }

    private String GET(String url) {

	String result = null;
	try {
	    HttpClient httpClient = new DefaultHttpClient();

	    HttpGet request = new HttpGet(url);

	    request.addHeader("User-Agent", "Mozilla/5.0");
	    request.addHeader("Authorization", "OAuth " + token);

	    HttpResponse response = httpClient.execute(request);

	    int code = response.getStatusLine().getStatusCode();
	    log.log(Level.INFO, "code = {0}", code);

	    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	    StringBuilder responseContent = new StringBuilder();
	    String line;
	    while ((line = rd.readLine()) != null) {
		responseContent.append(line);
	    }

	    result = responseContent.toString();

	    if (code != 200) {
		Error error = new Gson().fromJson(result, Error.class);
		throw new RuntimeException(error);
	    }

	} catch (IOException ex) {
	    Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
	}

	return result;
    }


    /**
     *
     * get information about the  user`s disk
     * 
     * @return Disk
     */
    public Disk getDiskInfo() {
	String operation = "/v1/disk";
	Disk disk = null;
	String responce = GET(baseUrl + operation);
	if (responce != null) {
	    disk = new Gson().fromJson(responce, Disk.class);
	}

	return disk;
    }

    /**
     * get information about file and directory 
     * @param path  request file or directory , like 
     * /v1/disk/resources?path=/dir
     * @return Resource
     * @throws java.io.FileNotFoundException
     */
    public Resource getResource (String path) throws FileNotFoundException {
	if(path==null)throw new FileNotFoundException();
	String operation = "/v1/disk/resources";
	Resource resourceList = null;
	String responce = GET(baseUrl + operation + "?path=" + path);
	
	if (responce != null) {
	    resourceList = new Gson().fromJson(responce, Resource.class);
	}

	return resourceList;
    }
    
    /**
     *
     * @param path
     * @return
     * @throws java.io.FileNotFoundException
     * 
     */
    public Link getLinkToDownload(String path) throws FileNotFoundException{
	if(path==null)throw new FileNotFoundException();
        Link link = null;
	String operation = "/v1/disk/resources/download";
	String responce = GET(baseUrl + operation + "?path=" + path);
	if (responce != null) {
	    link = new Gson().fromJson(responce, Link.class);
	}
	 
	return link;
    }

}
