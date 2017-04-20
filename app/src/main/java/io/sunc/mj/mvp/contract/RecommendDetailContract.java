package io.sunc.mj.mvp.contract;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.util.List;

import io.sunc.mj.api.model.RecommendDetailList;
import io.sunc.mj.mvp.presenter.BasePresenter;
import io.sunc.mj.mvp.view.BaseView;

/**
 * Created by hefuyi on 2016/12/3.
 */

public interface RecommendDetailContract {

    interface View extends BaseView{

        Context getContext();

        void showAlbumSongs(List<RecommendDetailList.Track> songList);

        void showAlbumArt(Drawable albumArt);

        void showAlbumArt(Bitmap bitmap);

        void startMusic(String url);

        String getCoverImg();
    }

    interface Presenter extends BasePresenter<View>{

        void subscribe(long albumID);

        void loadAlbumSongs(long albumID);

        void loadAlbumArt(long albumID);

        void loadMusic(long musicId);
    }
}
