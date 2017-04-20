package io.sunc.mj.mvp.contract;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.graphics.Palette;

import java.io.File;

import io.sunc.mj.mvp.presenter.BasePresenter;
import io.sunc.mj.mvp.view.BaseView;

/**
 * Created by hefuyi on 2016/11/7.
 */

public interface QuickControlsContract {

    interface View extends BaseView {

        Context getContext();

        void setAlbumArt(Bitmap albumArt);

        void setAlbumArt(Drawable albumArt);

        void setTitle(String title);

        void setArtist(String artist);

        void setPalette(Palette palette);

        void showLyric(File file);

        void setPlayPauseButton(boolean isPlaying);

        boolean getPlayPauseStatus();

        void startUpdateProgress();

        void setProgressMax(int max);

    }

    interface Presenter extends BasePresenter<View> {

        void onPlayPauseClick();

        void onPreviousClick();

        void loadLyric();

        void onNextClick();

        void updateNowPlayingCard();

    }
}
