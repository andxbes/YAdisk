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
public class Resource {

    private String name,
	    public_key, 
	    origin_path,
	    created,
	    public_url,
	    modified,
	    md5,
	    media_type,
	    path,
	    preview,
	    type,
	    mime_type,
	    custom_properties;
    private int size;

    private ResourceList _embedded;



    @Override
    public String toString() {
	return "Resource{" + "\n____ name=" + getName() + "\n____ public_key=" + getPublic_key()
		+ "\n____ origin_path=" + getOrigin_path() + "\n____ created=" + getCreated()
		+ "\n____ public_url=" + getPublic_url() + "\n____ modified=" + getModified()
		+ "\n____ md5=" + getMd5() + "\n____ media_type=" + getMedia_type() + "\n____ path="
		+ getPath() + "\n____ preview=" + getPreview() + "\n____ type=" + getType() + "\n____ mime_type="
		+ getMime_type() + "\n____ custom_properties=" + getCustom_properties()
		+ "\n____ size=" + getSize() + "\n____ _embedded=" + getEmbedded() + '}';
    }

    /**
     * @return the name <Имя ресурса>,
     */
    public String getName() {
	return name;
    }

    /**
     * @return the public_key <Ключ опубликованного ресурса>
     */
    public String getPublic_key() {
	return public_key;
    }

    /**
     * @return the origin_path <Путь откуда был удалён ресурс>
     */
    public String getOrigin_path() {
	return origin_path;
    }

    /**
     * @return the created <Дата создания>
     */
    public String getCreated() {
	return created;
    }

    /**
     * @return the public_url <Публичный URL>
     */
    public String getPublic_url() {
	return public_url;
    }

    /**
     * @return the modified <Дата изменения>
     */
    public String getModified() {
	return modified;
    }

    /**
     * @return the md5  <MD5-хэш>
     */
    public String getMd5() {
	return md5;
    }

    /**
     * @return the media_type <Определённый Диском тип файла>
     */
    public String getMedia_type() {
	return media_type;
    }

    /**
     * @return the path <Путь к ресурсу>
     */
    public String getPath() {
	return path;
    }

    /**
     * @return the preview <URL превью файла>
     */
    public String getPreview() {
	return preview;
    }

    /**
     * @return the type <Тип >(наверное, выдаст тип после точки)
     */
    public String getType() {
	return type;
    }

    /**
     * @return the mime_type <MIME-тип файла>
     */
    public String getMime_type() {
	return mime_type;
    }

    /**
     * @return the custom_properties <Пользовательские атрибуты ресурса.>
     */
    public String getCustom_properties() {
	return custom_properties;
    }

    /**
     * @return the size <Размер файла>
     */
    public int getSize() {
	return size;
    }

    /**
     * @return the _embedded <Список вложенных ресурсов>
     */
    public ResourceList getEmbedded() {
	return _embedded;
    }

}
