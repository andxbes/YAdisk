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
    private String href,
	    method;
	    
    private boolean templated;

    @Override
    public String toString() {
	return "Link{" + "href=" + getHref() + ",\n method=" + getMethod() + ",\n templated=" + isTemplated() + '}';
    }

    /**
     * @return the href < URL>
     */
    public String getHref() {
	return href;
    }

    /**
     * @return the method <HTTP-метод>
     */
    public String getMethod() {
	return method;
    }

    /**
     * @return the templated <Признак шаблонизированного URL>
     */
    public boolean isTemplated() {
	return templated;
    }

    
    
}