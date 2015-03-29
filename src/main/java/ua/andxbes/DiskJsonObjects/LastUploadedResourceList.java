/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes.DiskJsonObjects;

/**
 *
 * @author andr
 */
public class LastUploadedResourceList {
   private Resource items[];
   private long limit;

    /**
     * @return the items
     */
    public Resource[] getItems() {
	return items;
    }

    /**
     * @return the limit
     */
    public long getLimit() {
	return limit;
    }

    @Override
    public String toString() {
	return "LastUploadedResourceList{" + "items=" + items + ", limit=" + limit + '}';
    }

}
