/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import ua.andxbes.Disk.Disk;

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

    public Disk getDiskInfo() {
	String operation = "/v1/disk";
	Disk disk = null;
	try {
	    HttpClient httpClient = new DefaultHttpClient();

	    HttpGet request = new HttpGet(baseUrl + operation);

	    request.addHeader("User-Agent", "Mozilla/5.0");
	    request.addHeader("Authorization", "OAuth " + token);

	    HttpResponse response = httpClient.execute(request);

	    log.log(Level.INFO, "code = {0}", response.getStatusLine().getStatusCode());

	    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	    StringBuilder result = new StringBuilder();
	    String line;
	    while ((line = rd.readLine()) != null) {
		result.append(line);
	    }
	    log.log(Level.INFO, "result = {0}", result.toString());

	} catch (MalformedURLException | UnsupportedEncodingException  ex) {
	    Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
	} catch (IOException ex) {
	    Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
	}
	return disk;
    }

}
