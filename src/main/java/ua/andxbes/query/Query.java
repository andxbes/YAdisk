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
import java.rmi.ConnectException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import ua.andxbes.DiskJsonObjects.Link;
import ua.andxbes.fieldsForQuery.Field;
import ua.andxbes.util.QueryString;

/**
 *
 * @author Andr
 */
class Query {

    final static String GET = "GET",
	    POST = "POST",
	    DELETE = "DELETE",
	    PUT = "PUT", PATCH = "PATCH" ;
    private final Logger log = Logger.getLogger(this.getClass().getSimpleName());
    private Object query;

    <T> T getObgect(String method, String operation, Field[] fields, Class<T> clazz, String data) throws ConnectException {
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

    String query(String method, String operation, QueryString qParam, String data) throws ConnectException {
	String param = qParam.toString().isEmpty() ? "" : "?" + qParam.toString();
	URL url = null;

	try {
	    url = new URL(QueryController.baseUrl + operation + param);
	} catch (MalformedURLException ex) {
	    Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
	}
	return query(method, url, data);

    }

    String query(String method, URL url, String data) throws ConnectException {

	StringBuilder result = new StringBuilder();
	HttpURLConnection conn;
	BufferedReader br;
	String line;
	try {
	    conn = (HttpURLConnection) url.openConnection();
	    conn.setRequestMethod(method);
	    //we dispatch our token
	    conn.addRequestProperty("Authorization", "OAuth " + QueryController.token);
	    conn.addRequestProperty("Content-Type", "application/json ");

	    if (data != null) {
		conn.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
		wr.writeBytes(data);
		wr.flush();
		wr.close();
	    }

	    int code = conn.getResponseCode();
	    log.log(Level.INFO, "code = {0} , method = {1} ,\n data = {2} ,\n url = {3} ", new Object[]{code, conn.getRequestMethod(), data, conn.getURL()});

	    if (code == HttpURLConnection.HTTP_OK || code == HttpURLConnection.HTTP_CREATED) {//201 or 200
		br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		while ((line = br.readLine()) != null) {
		    result.append(line);
		}
	    } else if (code == HttpURLConnection.HTTP_ACCEPTED) {//202
		br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		while ((line = br.readLine()) != null) {
		    result.append(line);
		}

		getStatusOperationId(new Gson().fromJson(result.toString(), Link.class));

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

    String query(String method, String operation, Field[] fields, String data) throws ConnectException {
	QueryString qParams = new QueryString();
	qParams.add(fields);
	return query(method, operation, qParams, data);
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
    public void getStatusOperationId(Link link) throws ConnectException {
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
	String response = null;
	boolean end = false;
	while (!end) {
            
	    response = query(link.getMethod(), url, null);
	    
	    String status  = pattern.matcher(response.split(":")[1]).replaceAll("");
	    log.log(Level.INFO, "\n s = {0}",status);
	    
	    if(status.equals("success")){
	       end=true;
	    }
	    try {
		Thread.sleep(1000);
	    } catch (InterruptedException ex) {
		Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
	    }

	}
    }

}
