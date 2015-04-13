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
import java.util.logging.Level;
import java.util.logging.Logger;
import ua.andxbes.fieldsForQuery.Field;
import ua.andxbes.util.QueryString;

/**
 *
 * @author Andr
 */
class Get {

    private final Logger log = Logger.getLogger(this.getClass().getSimpleName());

    String get(String operation, QueryString qParam) {
	String param = qParam.toString().isEmpty() ? "" : "?" + qParam.toString();
	StringBuilder result = new StringBuilder();
	URL url;
	HttpURLConnection conn;
	BufferedReader br;
	String line;
	try {

	    url = new URL(QueryController.baseUrl + operation + param);
	    conn = (HttpURLConnection) url.openConnection();
	    conn.setRequestMethod("GET");
	    conn.addRequestProperty("Authorization", "OAuth " + QueryController.token);

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
	    Logger.getLogger(QueryController.class.getName()).log(Level.SEVERE, null, ex);
	} catch (ProtocolException ex) {
	    Logger.getLogger(QueryController.class.getName()).log(Level.SEVERE, null, ex);
	} catch (IOException ex) {
	    Logger.getLogger(QueryController.class.getName()).log(Level.SEVERE, null, ex);
	}

	return result.toString();
    }

    String get(String operation, Field[] fields) {
	QueryString qParams = new QueryString();
	qParams.add(fields);
	return get(operation, qParams);
    }

    <T> T getObgectGetRequest(String operation, Field[] fields, Class<T> clazz) {
	T object = null;

	String responce = get(operation, fields);

	if (responce != null) {
	    object = new Gson().fromJson(responce, clazz);
	}
	return object;
    }
    
    
    

}
