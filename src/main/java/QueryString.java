
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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

    public synchronized QueryString add(Object name, Object value)
	    throws UnsupportedEncodingException {
	if (!query.toString().trim().equals("")) {
	    query.append("&");
	}
	query.append(URLEncoder.encode(name.toString(), "UTF-8"));
	query.append("=");
	query.append(URLEncoder.encode(value.toString(), "UTF-8"));
	return this;
    }

    @Override
    public String toString() {
	return query.toString();
    }

}
