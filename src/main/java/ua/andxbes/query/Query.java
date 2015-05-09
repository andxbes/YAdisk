/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes.query;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.rmi.ConnectException;
import java.util.logging.Level;
import java.util.logging.Logger;
import ua.andxbes.DiskJsonObjects.Link;
import ua.andxbes.fieldsForQuery.Field;
import ua.andxbes.util.QueryString;

/**
 *
 * @author Andr
 */
class Query {

    private String response;
    private int code;

    final static String GET = "GET",
	    POST = "POST",
	    DELETE = "DELETE",
	    PUT = "PUT", PATCH = "PATCH";

    private static final Logger log = Logger.getLogger("Qery");

    <T> T getObgect(String method, String operation, Field[] fields, Class<T> clazz, ReadableByteChannel data) throws ConnectException {
	T object = null;

	query(method, operation, fields, data);

	log.info(getResponse());
	object = new Gson().fromJson(getResponse(), clazz);

	if (Link.class.isInstance(object) && code == 202) {
	    ((Link) object).setAsync(true);
	}

	return object;
    }

    <T> T getObgect(String method, String operation, Field[] fields, Class<T> clazz) throws ConnectException {
	return getObgect(method, operation, fields, clazz, null);
    }

    Query query(String method, String operation, Field[] fields, ReadableByteChannel data) throws ConnectException {
	QueryString qParams = new QueryString();
	qParams.add(fields);

	return query(method, operation, qParams, data);
    }

    Query query(String method, String operation, QueryString qParam, ReadableByteChannel data) throws ConnectException {
	String param = qParam.toString().isEmpty() ? "" : "?" + qParam.toString();
	URL url = null;

	try {
	    url = new URL(QueryController.baseUrl + operation + param);
	} catch (MalformedURLException ex) {
	    Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
	}
	query(method, url, data);
	return this;
    }

    Query query(String method, URL url, ReadableByteChannel data) throws ConnectException {
	StringBuilder result = new StringBuilder();
	HttpURLConnection conn;
	BufferedReader br;
	String line;
	try {
	    conn = (HttpURLConnection) url.openConnection();
	    conn.setRequestMethod(method);

	    if (data != null) {
		writeData(conn, data);
	    } else {
		conn.addRequestProperty("Content-Type", "application/json ");
		conn.addRequestProperty("Authorization", "OAuth " + QueryController.token);
	    }

	    code = conn.getResponseCode();
	    log.log(Level.INFO, "code = {0} , method = {1} ,\n data = {2} ,\n url = {3} ", new Object[]{getCode(), conn.getRequestMethod(), data, conn.getURL()});

	    if (getCode() == HttpURLConnection.HTTP_OK || getCode() == HttpURLConnection.HTTP_ACCEPTED || getCode() == HttpURLConnection.HTTP_CREATED || getCode() == HttpURLConnection.HTTP_NO_CONTENT) {//202 or 200 or 201 
		br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		while ((line = br.readLine()) != null) {
		    result.append(line);
		}

	    } else {//error
		br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		while ((line = br.readLine()) != null) {
		    result.append(line);
		}
		throw new ConnectException((new Gson().fromJson(result.toString(), ua.andxbes.DiskJsonObjects.Error.class).toString()));
	    }
	} catch (MalformedURLException ex) {
	    Logger.getLogger(QueryController.class.getName()).log(Level.SEVERE, null, ex);
	} catch (ProtocolException ex) {
	    Logger.getLogger(QueryController.class.getName()).log(Level.SEVERE, null, ex);
	} catch (ConnectException ex) {
	    throw new ConnectException(ex.toString());
	} catch (IOException ex) {
	    Logger.getLogger(QueryController.class.getName()).log(Level.SEVERE, null, ex);
	}

	response = result.toString();
	return this;
    }

    private void writeData(HttpURLConnection conn, ReadableByteChannel data) throws IOException {
	conn.setDoOutput(true);
	try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
	    ByteBuffer bb = ByteBuffer.allocate(1024);
	    while (data.read(bb) != -1) {
		bb.flip();
		wr.write(bb.array());
		bb.clear();
	    }
	    wr.flush();
	}
    }

    InputStream downloadFile(URL url) {
	InputStream input = null;

	try {
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setRequestMethod(GET);
	    conn.setDoOutput(true);
	    conn.connect();
	    input = conn.getInputStream();
	} catch (IOException ex) {
	    Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
	}
	return input;

    }

    /**
     * @return the response
     */
    public String getResponse() {
	return response;
    }

    /**
     * @return the code
     */
    public int getCode() {
	return code;
    }
    
   

}
