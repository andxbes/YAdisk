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
public class ResourceList {

    private String sort,//<Поле, по которому отсортирован список>
	    path;//<Путь к ресурсу, для которого построен список>,
    private Resource[] items;// <Элементы списка>,
    private int linit,// <Количество элементов на странице>,
	    offset,//: <Смещение от начала списка>,
	    total;//<Общее количество элементов в списке>

    /**
     * @return the sort
     */
    public String getSort() {
	return sort;
    }

    /**
     * @param sort the sort to set
     */
    public void setSort(String sort) {
	this.sort = sort;
    }

    /**
     * @return the path
     */
    public String getPath() {
	return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
	this.path = path;
    }

    /**
     * @return the items
     */
    public Resource[] getItems() {
	return items;
    }

    /**
     * @param items the items to set
     */
    public void setItems(Resource[] items) {
	this.items = items;
    }

    /**
     * @return the linit
     */
    public int getLinit() {
	return linit;
    }

    /**
     * @param linit the linit to set
     */
    public void setLinit(int linit) {
	this.linit = linit;
    }

    /**
     * @return the offset
     */
    public int getOffset() {
	return offset;
    }

    /**
     * @param offset the offset to set
     */
    public void setOffset(int offset) {
	this.offset = offset;
    }

    /**
     * @return the total
     */
    public int getTotal() {
	return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(int total) {
	this.total = total;
    }

    @Override
    public String toString() {

	StringBuilder itemsString = new StringBuilder();

	for (Resource item : items) {
	    itemsString.append(item.toString()).append("\n");
	}

	return "ResourceList{" + "sort=" + sort + ", path=" + path + ", linit=" + linit + ", offset=" + offset
		+ ", total=" + total + ",\n items= \n"
		+ itemsString.toString() + '}';
    }

}
