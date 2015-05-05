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
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.rmi.ConnectException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import ua.andxbes.DiskJsonObjects.Link;
import ua.andxbes.fieldsForQuery.Field;
import static ua.andxbes.query.QueryController.getIn_progress;
import ua.andxbes.util.QueryString;

/**
 *
 * @author Andr
 */
class Query2 {

    final static String GET = "GET",
	    POST = "POST",
	    DELETE = "DELETE",
	    PUT = "PUT", PATCH = "PATCH";
    private static final Logger log = Logger.getLogger("Qery");

    <T> T getObgect(String method, String operation, Field[] fields, Class<T> clazz, FileChannel data) throws ConnectException {
	T object = null;

	String responce = query(method, operation, fields, data);

	if (!responce.equals("")) {
	    object = new Gson().fromJson(responce, clazz);
	}
	return object;
    }

    <T> T getObgect(String method, String operation, Field[] fields, Class<T> clazz) throws ConnectException {
	return getObgect(method, operation, fields, clazz, null);
    }

    String query(String method, String operation, QueryString qParam, FileChannel data) throws ConnectException {
	String param = qParam.toString().isEmpty() ? "" : "?" + qParam.toString();
	URL url = null;

	try {
	    url = new URL(QueryController.baseUrl + operation + param);
	} catch (MalformedURLException ex) {
	    Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
	}
	return query(method, url, data);

    }

    String query(String method, URL url, FileChannel data) throws ConnectException {
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

	    int code = conn.getResponseCode();
	    log.log(Level.INFO, "code = {0} , method = {1} ,\n data = {2} ,\n url = {3} ", new Object[]{code, conn.getRequestMethod(), data, conn.getURL()});

	    if (code == HttpURLConnection.HTTP_OK || code == HttpURLConnection.HTTP_ACCEPTED || code == HttpURLConnection.HTTP_CREATED) {//202 or 200 or 201 
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

	return result.toString();
    }

    private void writeData(HttpURLConnection conn, FileChannel data) throws IOException {
	conn.setDoOutput(true);
	DataOutputStream wr = new DataOutputStream(conn.getOutputStream());

	ByteBuffer bb = ByteBuffer.allocate(1024);
	while (data.read(bb) != -1) {
	    bb.flip();
	    wr.write(bb.array());
	    bb.clear();
	}
	wr.flush();
	wr.close();
    }

    String query(String method, String operation, Field[] fields, FileChannel data) throws ConnectException {
	QueryString qParams = new QueryString();
	qParams.add(fields);
	return query(method, operation, qParams, data);
    }
    
    
     private Thread refrashStatusOperationId(Link link) {
	
	Thread t = new Thread(() -> {
	    log.log(Level.INFO, "Start.Length of the  list = {0}", getIn_progress().size());
	    getIn_progress().add(link);
	    URL url = null;
	    try {
		url = new URL(link.getHref());
	    } catch (MalformedURLException ex) {
		Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
	    }
	    /*
	     response = {"status":"in-progress"}
	     response = {"status":"success"}
	    
	     */
	    Pattern pattern = Pattern.compile("[\"|}|{]");
	    boolean end = false;
	    while (!end) {
		try {

		    String response = query(link.getMethod(), url, null);

		    String status = pattern.matcher(response.split(":")[1]).replaceAll("");
		    log.log(Level.INFO, "\n s = {0}", status);

		    if (status.equals("success")) {
			end = true;
		    }
		    try {
			Thread.sleep(2000);
		    } catch (InterruptedException ex) {
			Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
		    }
		} catch (ConnectException ex) {
		    Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
		}

	    }
	    getIn_progress().remove(link);
	    log.log(Level.INFO, "Length of the  list = {0}", getIn_progress().size());
	});

	t.start();
	return t;
    }

}
