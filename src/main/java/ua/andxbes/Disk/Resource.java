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

    private String name,//<Имя>,
	    public_key, //<Ключ опубликованного ресурса>,
	    origin_path,//<Путь откуда был удалён ресурс>,
	    created,// <Дата создания>,
	    public_url,//<Публичный URL>,
	    modified,//<Дата изменения>,
	    md5,// <MD5-хэш>,
	    media_type,//<Определённый Диском тип файла>,
	    path,//<Путь к ресурсу>,
	    preview,//<URL превью файла>,
	    type,//<Тип>,
	    mime_type,//<MIME-тип файла>,
	    custom_properties;//<Пользовательские атрибуты ресурса.>,
    private int size;//<Размер файла>

    private ResourceList _embedded;//<Список вложенных ресурсов>,

    /**
     * @return the public_key
     */
    public String getPublic_key() {
	return public_key;
    }

    /**
     * @param public_key the public_key to set
     */
    public void setPublic_key(String public_key) {
	this.public_key = public_key;
    }

    /**
     * @return the origin_path
     */
    public String getOrigin_path() {
	return origin_path;
    }

    /**
     * @param origin_path the origin_path to set
     */
    public void setOrigin_path(String origin_path) {
	this.origin_path = origin_path;
    }

    /**
     * @return the created
     */
    public String getCreated() {
	return created;
    }

    /**
     * @param created the created to set
     */
    public void setCreated(String created) {
	this.created = created;
    }

    /**
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * @return the public_url
     */
    public String getPublic_url() {
	return public_url;
    }

    /**
     * @param public_url the public_url to set
     */
    public void setPublic_url(String public_url) {
	this.public_url = public_url;
    }

    /**
     * @return the modified
     */
    public String getModified() {
	return modified;
    }

    /**
     * @param modified the modified to set
     */
    public void setModified(String modified) {
	this.modified = modified;
    }

    /**
     * @return the md5
     */
    public String getMd5() {
	return md5;
    }

    /**
     * @param md5 the md5 to set
     */
    public void setMd5(String md5) {
	this.md5 = md5;
    }

    /**
     * @return the media_type
     */
    public String getMedia_type() {
	return media_type;
    }

    /**
     * @param media_type the media_type to set
     */
    public void setMedia_type(String media_type) {
	this.media_type = media_type;
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
     * @return the preview
     */
    public String getPreview() {
	return preview;
    }

    /**
     * @param preview the preview to set
     */
    public void setPreview(String preview) {
	this.preview = preview;
    }

    /**
     * @return the type
     */
    public String getType() {
	return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
	this.type = type;
    }

    /**
     * @return the mime_type
     */
    public String getMime_type() {
	return mime_type;
    }

    /**
     * @param mime_type the mime_type to set
     */
    public void setMime_type(String mime_type) {
	this.mime_type = mime_type;
    }

    /**
     * @return the custom_properties
     */
    public String getCustom_properties() {
	return custom_properties;
    }

    /**
     * @param custom_properties the custom_properties to set
     */
    public void setCustom_properties(String custom_properties) {
	this.custom_properties = custom_properties;
    }

    /**
     * @return the size
     */
    public int getSize() {
	return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(int size) {
	this.size = size;
    }

    /**
     * @return the _embedded
     */
    public ResourceList getEmbedded() {
	return _embedded;
    }

    /**
     * @param _embedded the _embedded to set
     */
    public void setEmbedded(ResourceList _embedded) {
	this._embedded = _embedded;
    }

    @Override
    public String toString() {
	return "Resource{" + "\n____ name=" + name + "\n____ public_key=" + public_key
		+ "\n____ origin_path=" + origin_path + "\n____ created=" + created
		+ "\n____ public_url=" + public_url + "\n____ modified=" + modified
		+ "\n____ md5=" + md5 + "\n____ media_type=" + media_type + "\n____ path="
		+ path + "\n____ preview=" + preview + "\n____ type=" + type + "\n____ mime_type="
		+ mime_type + "\n____ custom_properties=" + custom_properties
		+ "\n____ size=" + size + "\n____ _embedded=" + _embedded + '}';
    }

}
