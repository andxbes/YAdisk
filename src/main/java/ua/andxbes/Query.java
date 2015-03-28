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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import java.util.logging.Level;
import java.util.logging.Logger;
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

    private String GET(String request) {

	StringBuilder result = new StringBuilder();
	URL url;
	HttpURLConnection conn;
	BufferedReader br;
	String line;
	try {
	    url = new URL(request);
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

    /**
     *
     * get information about the user`s disk
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
     *
     * @param path request file or directory , like /v1/disk/resources?path=/dir
     * @return Resource
     * @throws java.io.FileNotFoundException
     */
    public Resource getResource(String path) throws FileNotFoundException {
	if (path == null) {
	    throw new FileNotFoundException();
	}
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
    public Link getLinkToDownload(String path) throws FileNotFoundException {
	if (path == null) {
	    throw new FileNotFoundException();
	}
	Link link = null;
	String operation = "/v1/disk/resources/download";
	String responce = GET(baseUrl + operation + "?path=" + path);
	if (responce != null) {
	    link = new Gson().fromJson(responce, Link.class);
	}

	return link;
    }

}
