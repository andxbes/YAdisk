/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes.Disk;

/**
 *
 * @author Andr
 */
public class Link {
    private String href,//<URL>,
	    method;// <HTTP-метод>,
	    
    private boolean templated;// <Признак шаблонизированного URL>

    @Override
    public String toString() {
	return "Link{" + "href=" + getHref() + ",\n method=" + getMethod() + ",\n templated=" + isTemplated() + '}';
    }

    /**
     * @return the href
     */
    public String getHref() {
	return href;
    }

    /**
     * @param href the href to set
     */
    public void setHref(String href) {
	this.href = href;
    }

    /**
     * @return the method
     */
    public String getMethod() {
	return method;
    }

    /**
     * @param method the method to set
     */
    public void setMethod(String method) {
	this.method = method;
    }

    /**
     * @return the templated
     */
    public boolean isTemplated() {
	return templated;
    }

    /**
     * @param templated the templated to set
     */
    public void setTemplated(boolean templated) {
	this.templated = templated;
    }
    
}
