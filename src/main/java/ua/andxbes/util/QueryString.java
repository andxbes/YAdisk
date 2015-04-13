package ua.andxbes.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import ua.andxbes.fieldsForQuery.Field;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Andr
 */
public class QueryString {

    Map<String, String> result = new TreeMap<>();

    public QueryString() {

    }

    public synchronized QueryString add(Object name, Object value) {

	try {
	    result.put(URLEncoder.encode(name.toString(), "UTF-8"), URLEncoder.encode(value.toString(), "UTF-8"));
	} catch (UnsupportedEncodingException ex) {
	    Logger.getLogger(QueryString.class.getName()).log(Level.SEVERE, null, ex);
	}
	return this;
    }

    public synchronized QueryString add(Field... fields) {
	if (null == fields) {
	    return this;
	}
	for (Field field : fields) {
	    add(field.getNameField(), field.getField());
	}
	return this;
    }

    @Override
    public String toString() {
	StringBuilder query = new StringBuilder();
	for (Map.Entry<String, String> entrySet : result.entrySet()) {
	    if (!query.toString().trim().equals("")) {
		query.append("&");
	    }

	    query.append(entrySet.getKey());
	    query.append("=");
	    query.append(entrySet.getValue());

	}
	return query.toString();
    }

    public Map<String, String> parseURL(String url) {
	Map<String, String> map = new TreeMap<>();

	String response = url.split("#")[1];

	for (String s : response.split("\\&")) {
	    String[] keyValue = s.split("=");
	    map.put(keyValue[0], keyValue[1]);
	}

	return map;
    }

    public Map<String, String> getMap() {
	return result;
    }

}
