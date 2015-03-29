/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes.DiskJsonObjects;

/**
 *
 * @author Andr
 * 
 * <b>Описание ресурса, мета-информация о файле или папке. 
 * Включается в ответ на запрос метаинформации.</b>
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
    private long size;

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
     * @return the name <b>Имя ресурса</b>
     */
    public String getName() {
	return name;
    }

    /**
     * @return the public_key <b>Ключ опубликованного ресурса</b>
     */
    public String getPublic_key() {
	return public_key;
    }

    /**
     * @return the origin_path <b>Путь откуда был удалён ресурс</b>
     */
    public String getOrigin_path() {
	return origin_path;
    }

    /**
     * @return the created <b>Дата создания</b> like: 2014-01-14T06:36:29+00:00
     */
    public String getCreated() {
	return created;
    }

    /**
     * @return the public_url <b>Публичный URL</b>
     */
    public String getPublic_url() {
	return public_url;
    }

    /**
     * @return the modified <b>Дата изменения<b> like: 2014-01-14T06:36:29+00:00
     */
    public String getModified() {
	return modified;
    }

    /**
     * @return the md5 <b> MD5-хэш</b>
     */
    public String getMd5() {
	return md5;
    }

    /**
     * @return the media_type <b>Определённый Диском тип файла</b>
     */
    public String getMedia_type() {
	return media_type;
    }

    /**
     * @return the path .<b>Путь к ресурсу</b>.
     */
    public String getPath() {
	return path;
    }

    /**
     * @return the preview .URL превью файла.
     */
    public String getPreview() {
	return preview;
    }

    /**
     * @return the type .Наверное, выдаст тип после точки.
     */
    public String getType() {
	return type;
    }

    /**
     * @return the mime_type <b>MIME-тип файла</b>
     */
    public String getMime_type() {
	return mime_type;
    }

    /**
     * @return the custom_properties <b>Пользовательские атрибуты ресурса</b>.
     */
    public String getCustom_properties() {
	return custom_properties;
    }

    /**
     * @return the size <b>Размер файла</b> (bite)
     */
    public long getSize() {
	return size;
    }

    /**
     * @return the _embedded  <b>Список вложенных ресурсов</b>
     */
    public ResourceList getEmbedded() {
	return _embedded;
    }

}
