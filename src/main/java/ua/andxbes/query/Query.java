/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes.query;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.rmi.ConnectException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import ua.andxbes.fieldsForQuery.Field;
import ua.andxbes.util.QueryString;

/**
 *
 * @author Andr
 */
class Query {
    final static int OK_GET = 200,
	    OK_POST = 201,
	    ASSINC_OPERATION = 202;
	    

    final static String GET = "GET",
	    POST = "POST",
	    DELETE = "DELETE",
	    PUT = "PUT"
	    ,PATCH = "PATCH";
    private final Logger log = Logger.getLogger(this.getClass().getSimpleName());


    <T> T getObgect(String method,String operation, Field[] fields, Class<T> clazz) throws ConnectException {
	T object = null;

	String responce = query(method,operation, fields);

	if (!responce.equals("")) {
	    object = new Gson().fromJson(responce, clazz);
	}
	return object;
    }

    String query(String method, String operation, QueryString qParam) throws ConnectException{

	String param = qParam.toString().isEmpty() ? "" : "?" + qParam.toString();
	StringBuilder result = new StringBuilder();
	URL url;
	HttpURLConnection conn;
	BufferedReader br;
	String line;
	try {

	    if (method.equals(GET)) {
		url = new URL(QueryController.baseUrl + operation + param);
	    } else {
		url = new URL(QueryController.baseUrl + operation);
	    }
	    conn = (HttpURLConnection) url.openConnection();
	    conn.setRequestMethod(method);
	    //we dispatch our token
	    conn.addRequestProperty("Authorization", "OAuth " + QueryController.token);
	    
	    if (!method.equals(GET)) {
		for (Map.Entry<String, String> entrySet : qParam.getMap().entrySet()) {
		    conn.addRequestProperty(entrySet.getKey(), entrySet.getValue());
		    log.info(POST);
		}
	    }

	    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

	    while ((line = br.readLine()) != null) {
		result.append(line);
	    }

	    int code = conn.getResponseCode();
	    log.log(Level.INFO, "code = {0}", code);
	    if (code != OK_GET && code != OK_POST && code != ASSINC_OPERATION) {
		throw new ConnectException(new Gson().fromJson(result.toString(), ua.andxbes.DiskJsonObjects.Error.class).toString());
	    }else if(code != OK_GET){
	      log.log(Level.INFO, "Code = {0}\nresponse = {1}", new Object[]{code, result});
	    }

	} catch (MalformedURLException ex) {
	    Logger.getLogger(QueryController.class.getName()).log(Level.SEVERE, null, ex);
	} catch (ProtocolException ex) {
	    Logger.getLogger(QueryController.class.getName()).log(Level.SEVERE, null, ex);
	} catch (IOException ex) {
	    Logger.getLogger(QueryController.class.getName()).log(Level.SEVERE, null, ex);
	}

	return result.toString();
    }
    
      String query(String method, String operation, Field[] fields) throws ConnectException {
	QueryString qParams = new QueryString();
	qParams.add(fields);
	return query(method, operation, qParams);
    }

}
