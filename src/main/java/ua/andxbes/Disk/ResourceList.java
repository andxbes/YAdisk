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

    private String sort,
	    path;
    private Resource[] items;
    private int limit,
	    offset,
	    total;

   

    @Override
    public String toString() {

	StringBuilder itemsString = new StringBuilder();

	for (Resource item : getItems()) {
	    itemsString.append(item.toString()).append("\n");
	}

	return "ResourceList{" + "sort=" + getSort() + ", path=" + getPath() + ", linit=" + getLimit() + ", offset=" + getOffset()
		+ ", total=" + getTotal() + ",\n items= \n"
		+ itemsString.toString() + '}';
    }

    /**
     * @return the sort <b>Поле, по которому отсортирован список</b>
     */
    public String getSort() {
	return sort;
    }

    /**
     * @return the path <b>Путь к ресурсу, для которого построен список</b>
     */
    public String getPath() {
	return path;
    }

    /**
     * @return the items  <b>Элементы списка</b>
     */
    public Resource[] getItems() {
	return items;
    }

    /**
     * @return the limit <b>Количество элементов на странице</b>
     */
    public int getLimit() {
	return limit;
    }

    /**
     * @return the offset <b>Смещение от начала списка</b>
     */
    public int getOffset() {
	return offset;
    }

    /**
     * @return the total <b>Общее количество элементов в списке</b>
     */
    public int getTotal() {
	return total;
    }

    
}
