package io.sunc.mj.api.model;

import java.util.List;

/**
 * Description:
 * Date: 2017-04-12 16:22
 * Author: suncheng
 */
public class RecommendDetailList extends BaseBean {
    private List<Track> tracks;

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public static class SearchResult {
        private List<Track> songs;
        private int songCount;

        public int getSongCount() {
            return songCount;
        }

        public void setSongCount(int songCount) {
            this.songCount = songCount;
        }

        public List<Track> getSongs() {
            return songs;
        }

        public void setSongs(List<Track> songs) {
            this.songs = songs;
        }
    }

    public static class Track {
        private String name;
        private long id;
        private List<Ar> ar;
        private Al al;

        private String mp3_url;

        public String getMp3_url() {
            return mp3_url;
        }

        public void setMp3_url(String mp3_url) {
            this.mp3_url = mp3_url;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public List<Ar> getAr() {
            return ar;
        }

        public void setAr(List<Ar> ar) {
            this.ar = ar;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Al getAl() {
            return al;
        }

        public void setAl(Al al) {
            this.al = al;
        }
    }

    public static class Ar {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class Al {
        private String picUrl;

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }
    }
}
