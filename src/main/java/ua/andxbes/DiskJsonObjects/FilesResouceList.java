/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes.DiskJsonObjects;

/**
 *
 * @author andr
 * <b>Плоский список всех файлов на Диске в алфавитном порядке</b>
 */
public class FilesResouceList {
    private Resource[] items;
    private long limit,
	        offset;

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

    /**
     * @return the offset
     */
    public long getOffset() {
	return offset;
    }

    @Override
    public String toString() {
	return "FilesResouceList{" + "items=" + items + ", limit=" + limit + ", offset=" + offset + '}';
    }
    
    
    
    
}
