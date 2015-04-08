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
	StringBuilder sb = new StringBuilder();
	for (int i = 0;i<items.length;i++) {
	    sb.append("\n  item #").append((i+1)).append(items[i].toString()).append("\n    ");
	}
	
	return "LastUploadedResourceList{" + "items=" + sb.toString() + ", limit=" + limit + '}';
    }

}
