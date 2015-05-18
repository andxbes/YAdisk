/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes.DiskJsonObjects;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import ua.andxbes.DiskForAll;

/**
 *
 * @author Andr
 *
 * <b>Описание ресурса, мета-информация о файле или папке. Включается в ответ на
 * запрос метаинформации.</b>
 */
public class Resource {
    public final  static SimpleDateFormat sdf =  new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss'+00:00'");
    private DiskForAll inDisk ,toDisk;

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
	return "\nResource{" + "\n____ name = " + getName()
		+ "\nxxxxxx public_key = " + getPublic_key()
		+ "\nxxxxxx origin_path = " + getOrigin_path()
		+ "\nxxxxxx created = " + getCreated()
		+ "\nxxxxxx public_url = " + getPublic_url()
		+ "\nxxxxxx modified = " + getModified()
		+ "\nxxxxxx md5 = " + getMd5()
		+ "\nxxxxxx media_type = " + getMedia_type()
		+ "\nxxxxxx path = " + getPath()
		+ "\nxxxxxx preview = " + getPreview()
		+ "\nxxxxxx type = " + getType()
		+ "\nxxxxxx mime_type = " + getMime_type()
		+ "\nxxxxxx custom_properties = " + getCustom_properties()
		+ "\nxxxxxx size = " + getSize()
		+ "\nxxxxxx _embedded = " + getEmbedded() + '}';
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

    public long getCreated_InMilliseconds() {
	return unFormatDate(created);
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

    public long getModified_InMilliseconds() {
	return unFormatDate(modified);
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

	if (path.contains(":")) {
	    path = path.split(":")[1];
	}
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

    /**
     * @param name the name to set
     */
    public Resource setName(String name) {
	this.name = name;
	return this;
    }

    /**
     * @param public_key the public_key to set
     */
    public Resource setPublic_key(String public_key) {
	this.public_key = public_key;
	return this;
    }

    /**
     * @param origin_path the origin_path to set
     */
    public Resource setOrigin_path(String origin_path) {
	this.origin_path = origin_path;
	return this;
    }

    /**
     * @param created the created to set
     */
    public Resource setCreated(String created) {
	this.created = created;
	return this;
    }

    /**
     * @param public_url the public_url to set
     */
    public Resource setPublic_url(String public_url) {
	this.public_url = public_url;
	return this;
    }

    /**
     * @param modified the modified to set
     */
    public Resource setModified(String modified) {
	this.modified = modified;
	return this;
    }

    /**
     * @param md5 the md5 to set
     */
    public Resource setMd5(String md5) {
	this.md5 = md5;
	return this;
    }

    /**
     * @param media_type the media_type to set
     */
    public Resource setMedia_type(String media_type) {
	this.media_type = media_type;
	return this;
    }

    /**
     * @param path the path to set
     */
    public Resource setPath(String path) {

	if (path.contains("\\")) {
	    path = path.replace("\\", "/");
	}

	this.path = path;
	return this;
    }

    /**
     * @param preview the preview to set
     */
    public Resource setPreview(String preview) {
	this.preview = preview;
	return this;
    }

    /**
     * @param type the type to set
     */
    public Resource setType(String type) {
	this.type = type;
	return this;
    }

    /**
     * @param mime_type the mime_type to set
     */
    public Resource setMime_type(String mime_type) {
	this.mime_type = mime_type;
	return this;
    }

    /**
     * @param custom_properties the custom_properties to set
     */
    public Resource setCustom_properties(String custom_properties) {
	this.custom_properties = custom_properties;
	return this;
    }

    /**
     * @param size the size to set
     */
    public Resource setSize(long size) {
	this.size = size;
	return this;
    }

    /**
     * @param _embedded the _embedded to set
     */
    public Resource setEmbedded(ResourceList _embedded) {
	this._embedded = _embedded;
	return this;
    }

    private long unFormatDate(String data) {
	long result = 0;

	try {
	    result = sdf.parse(data).getTime();
	} catch (ParseException ex) {
	    Logger.getLogger(Resource.class.getName()).log(Level.SEVERE, null, ex);
	}

	return result;

    }

    /**
     * @return the inDisk
     */
    public DiskForAll getInDisk() {
	return inDisk;
    }

    /**
     * @param disk the inDisk to set
     */
    public Resource setInDisk(DiskForAll disk) {
	this.inDisk = disk;
	return this;
    }

    /**
     * @return the toDisk
     */
    public DiskForAll getToDisk() {
	return toDisk;
    }

    /**
     * @param toDisk the toDisk to set
     */
    public Resource setToDisk(DiskForAll toDisk) {
	this.toDisk = toDisk;
	return this;
    }

  

}
