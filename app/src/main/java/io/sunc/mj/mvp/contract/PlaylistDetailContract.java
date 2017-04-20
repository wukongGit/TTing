package io.sunc.mj.mvp.contract;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.util.List;

import io.sunc.mj.mvp.model.Song;
import io.sunc.mj.mvp.presenter.BasePresenter;
import io.sunc.mj.mvp.view.BaseView;

/**
 * Created by hefuyi on 2016/12/5.
 */

public interface PlaylistDetailContract {

    interface View extends BaseView{

        Context getContext();

        void showPlaylistSongs(List<Song> songList);

        void showPlaylistArt(Drawable playlistArt);

        void showPlaylistArt(Bitmap bitmap);
    }

    interface Presenter extends BasePresenter<View>{

        void loadPlaylistSongs(long playlistID);

        void loadPlaylistArt(long firstAlbumID);
    }
}
