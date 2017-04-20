package io.sunc.mj.api;

import java.util.List;

import io.sunc.mj.api.model.BaseModel;
import io.sunc.mj.api.model.Music;
import io.sunc.mj.api.model.RecommendDetailList;
import io.sunc.mj.api.model.RecommendList;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Description:
 * Date: 2017-04-12 14:43
 * Author: suncheng
 */
public interface MusicApi {
    @GET("api.php")
    Observable<RecommendListModel> getPlayList(@Query("type") String type, @Query("cat") String cat, @Query("offset") int offset, @Query("limit") int limit);

    @GET("api.php")
    Observable<MusicModel> getPlayList(@Query("type") String type, @Query("id") long id);

    @GET("cloudmusic")
    Observable<RecommendDetailListModel> getPlayListDetail(@Query("id") long id, @Query("type") String type);

    @GET("cloudmusic")
    Observable<SearchModel> searchMusic(@Query("s") String musicName, @Query("type") String type,
                                                     @Query("offset") int page, @Query("limit") int limit);

    class RecommendListModel extends BaseModel {
        private List<RecommendList> playlists;

        public List<RecommendList> getData() {
            return playlists;
        }

        public void setData(List<RecommendList> playlists) {
            this.playlists = playlists;
        }
    }

    class RecommendDetailListModel extends BaseModel {
        private RecommendDetailList playlist;

        public RecommendDetailList getData() {
            return playlist;
        }

        public void setData(RecommendDetailList playlists) {
            this.playlist = playlists;
        }
    }

    class MusicModel extends BaseModel {
        private List<Music> data;

        public List<Music> getData() {
            return data;
        }

        public void setData(List<Music> data) {
            this.data = data;
        }

    }

    class SearchModel extends BaseModel {
        private RecommendDetailList.SearchResult result;

        public RecommendDetailList.SearchResult getData() {
            return result;
        }

        public void setData(RecommendDetailList.SearchResult data) {
            this.result = data;
        }

    }
}
