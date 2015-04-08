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

    private StringBuffer query;

    public QueryString() {
	query = new StringBuffer();
    }

    public synchronized QueryString add(Object name, Object value) {

	if (!query.toString().trim().equals("")) {
	    query.append("&");
	}

	try {
	    query.append(URLEncoder.encode(name.toString(), "UTF-8"));
	    query.append("=");
	    query.append(URLEncoder.encode(value.toString(), "UTF-8"));

	} catch (UnsupportedEncodingException ex) {
	    Logger.getLogger(QueryString.class.getName()).log(Level.SEVERE, null, ex);
	}
	return this;
    }

    public synchronized QueryString add(Field... fields) {
	if(null == fields) return this;
	for (Field field : fields) {
	    add(field.getNameField(), field.getField());
	}
	return this;
    }

    @Override
    public String toString() {
	return query.toString();
    }

    public Map<String, String> parseURL(String url) {
	Map<String, String> result = new TreeMap<>();

	String response = url.split("#")[1];

	for (String s : response.split("\\&")) {
	    String[] keyValue = s.split("=");
	    result.put(keyValue[0], keyValue[1]);
	}

	return result;
    }

}
