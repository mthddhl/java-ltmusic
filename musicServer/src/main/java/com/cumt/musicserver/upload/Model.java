package com.cumt.musicserver.upload;

public class Model {

    private String artist;

    private String cover;

    private String lrc;

    private String name;

    private String url;

    private String url_128;

    private String url_320;

    private String url_flac;

    private String url_m4a;

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getLrc() {
        return lrc;
    }

    public void setLrc(String lrc) {
        this.lrc = lrc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl_128() {
        return url_128;
    }

    public void setUrl_128(String url_128) {
        this.url_128 = url_128;
    }

    public String getUrl_320() {
        return url_320;
    }

    public void setUrl_320(String url_320) {
        this.url_320 = url_320;
    }

    public String getUrl_flac() {
        return url_flac;
    }

    public void setUrl_flac(String url_flac) {
        this.url_flac = url_flac;
    }

    public String getUrl_m4a() {
        return url_m4a;
    }

    public void setUrl_m4a(String url_m4a) {
        this.url_m4a = url_m4a;
    }

    @Override
    public String toString() {
        return "Model{" +
                "artist='" + artist + '\'' +
                ", cover='" + cover + '\'' +
                ", lrc='" + lrc + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", url_128='" + url_128 + '\'' +
                ", url_320='" + url_320 + '\'' +
                ", url_flac='" + url_flac + '\'' +
                ", url_m4a='" + url_m4a + '\'' +
                '}';
    }
}
