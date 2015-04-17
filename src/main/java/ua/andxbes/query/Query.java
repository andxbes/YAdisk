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
	    PUT = "PUT", PATCH = "PATCH";
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

    String query(String method, String operation, QueryString qParam, String data) throws ConnectException {
	String param = qParam.toString().isEmpty() ? "" : "?" + qParam.toString();
	StringBuilder result = new StringBuilder();
	URL url;
	HttpURLConnection conn;
	BufferedReader br;
	String line;
	try {

	    url = new URL(QueryController.baseUrl + operation + param);

	    conn = (HttpURLConnection) url.openConnection();
	    conn.setRequestMethod(method);
	    //we dispatch our token
	    conn.addRequestProperty("Authorization", "OAuth " + QueryController.token);
	    conn.addRequestProperty("Content-Type", "application/json ");

	    if (data != null) {
		conn.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
		wr.writeBytes(qParam.toString());
		wr.flush();
		wr.close();
	    }

	    int code = conn.getResponseCode();
	    log.log(Level.INFO, "code = {0} , method = {1} ,\n param = {2} ,\n url = {3} ", new Object[]{code, conn.getRequestMethod(), qParam.toString(), conn.getURL()});

	    if (code == HttpURLConnection.HTTP_OK || code == HttpURLConnection.HTTP_CREATED) {
		br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		while ((line = br.readLine()) != null) {
		    result.append(line);
		}
	    } else if (code == HttpURLConnection.HTTP_ACCEPTED) {
		br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		while ((line = br.readLine()) != null) {
		    result.append(line);
		}
		//getStatusOperationId( new Gson().fromJson(result.toString(), Link.class));
	    } else {
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

    String query(String method, String operation, QueryString qParam) throws ConnectException {
	return query(method, operation, qParam, null);
    }

    String query(String method, String operation, Field[] fields, String data) throws ConnectException {
	QueryString qParams = new QueryString();
	qParams.add(fields);
	return query(method, operation, qParams, data);
    }

    String query(String method, String operation, Field[] fields) throws ConnectException {
	return query(method, operation, fields, null);
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
//    public void getStatusOperationId(Link link) throws ConnectException//TODO Не на чём ,пока, тестировать . 
//    {
//	
//
//	String response = null;
//	for (int i = 0; i < 4; i++) {
//
//	    {
//		response = query(GET, operation, new Field[]{new OperationId(operationId)});
//		log.info(operationId);
//		try {
//		    Thread.sleep(1000);
//		} catch (InterruptedException ex) {
//		    Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
//		}
//	    }
//	}
//    }
    
    

}
