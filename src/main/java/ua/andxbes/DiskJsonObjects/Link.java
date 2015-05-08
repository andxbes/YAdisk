/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes.DiskJsonObjects;

/**
 *
 * @author Andr
 * <b>Объект содержит URL для запроса метаданных ресурса. </b>
 */
public class Link {
    private String href,
	    method;
	    
    private boolean templated;
    private boolean async = false;

    @Override
    public String toString() {
	return "Link{" + "href=" + href + ", method=" + method + ", templated=" + templated + ", async=" + async + '}';
    }

  

    /**
     * @return the href  <b>URL</b>
     */
    public String getHref() {
	return href;
    }

    /**
     * @return the method <b>HTTP-метод</b>
     */
    public String getMethod() {
	return method;
    }

    /**
     * @return the templated <b>Признак шаблонизированного URL</b>
     */
    public boolean isTemplated() {
	return templated;
    }

    /**
     * @return the async
     */
    public boolean isAsync() {
	return async;
    }

    /**
     * @param async the async to set
     */
    public void setAsync(boolean async) {
	this.async = async;
    }
}
