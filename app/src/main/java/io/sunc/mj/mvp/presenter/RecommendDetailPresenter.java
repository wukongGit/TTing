package io.sunc.mj.mvp.presenter;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.List;

import io.sunc.mj.api.Api;
import io.sunc.mj.api.BaseURL;
import io.sunc.mj.api.MusicApi;
import io.sunc.mj.api.model.Music;
import io.sunc.mj.api.model.RecommendDetailList;
import io.sunc.mj.mvp.contract.RecommendDetailContract;
import io.sunc.mj.util.ATEUtil;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by hefuyi on 2016/12/3.
 */

public class RecommendDetailPresenter implements RecommendDetailContract.Presenter {

    private RecommendDetailContract.View mView;
    private CompositeSubscription mCompositeSubscription;

    @Override
    public void subscribe(long albumID) {
        loadAlbumArt(albumID);
        loadAlbumSongs(albumID);
    }

    @Override
    public void attachView(RecommendDetailContract.View view) {
        mView = view;
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void subscribe() {
        throw new RuntimeException("please call subscribe(long albumID)");
    }

    @Override
    public void unsubscribe() {
        mCompositeSubscription.clear();
    }

    @Override
    public void loadAlbumSongs(long albumID) {
        mCompositeSubscription.clear();
        Subscription subscription = Api.getMusicApi(BaseURL.MUSIC_PLAY_LIST_DETAIL)
                .getPlayListDetail(albumID, "playlist")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<MusicApi.RecommendDetailListModel>() {
                    @Override
                    public void call(MusicApi.RecommendDetailListModel response) {
                        if(response.isSuccuss()) {
                            RecommendDetailList list = response.getData();
                            if (list != null) {
                                mView.showAlbumSongs(list.getTracks());
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e("sunc", "throwable==" + throwable.getMessage());
                    }
                });
        mCompositeSubscription.add(subscription);
    }

    @Override
    public void loadAlbumArt(long albumID) {
        Glide.with(mView.getContext())
                .load(mView.getCoverImg())
                .asBitmap()
                .priority(Priority.IMMEDIATE)
                .error(ATEUtil.getDefaultAlbumDrawable(mView.getContext()))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        mView.showAlbumArt(errorDrawable);
                    }

                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        mView.showAlbumArt(resource);
                    }
                });
    }

    @Override
    public void loadMusic(long musicId) {
        Subscription subscription = Api.getMusicApi(BaseURL.MUSIC_PLAY_LIST)
                .getPlayList("url", musicId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<MusicApi.MusicModel>() {
                    @Override
                    public void call(MusicApi.MusicModel response) {
                        if (response.isSuccuss()) {
                            List<Music> musics = response.getData();
                            if(musics != null && musics.size() > 0) {
                                mView.startMusic(musics.get(0).getUrl());
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
        mCompositeSubscription.add(subscription);
    }
}
