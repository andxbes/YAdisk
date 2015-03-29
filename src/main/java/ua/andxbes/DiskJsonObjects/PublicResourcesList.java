/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes.DiskJsonObjects;

/**
 *
 * @author andr
 * <b> Список опубликованных файлов на Диске. </b>
 */
public class PublicResourcesList {
    private Resource items[];
    private String type;
    private long limit,
	    offset;

    /**
     * @return the items
     */
    public Resource[] getItems() {
	return items;
    }

    /**
     * @return the type (file or folder)
     */
    public String getType() {
	return type;
    }

    /**
     * @return the limit
     */
    public long getLimit() {
	return limit;
    }

    /**
     * @return the offset
     */
    public long getOffset() {
	return offset;
    }

    @Override
    public String toString() {
	return "PublicResourcesList{" + "items=" + items + ", type=" + type + ", limit=" + limit + ", offset=" + offset + '}';
    }
    
    
}
