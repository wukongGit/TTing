package io.sunc.mj.mvp.presenter;

import android.util.Log;

import java.util.List;

import io.sunc.mj.api.Api;
import io.sunc.mj.api.BaseURL;
import io.sunc.mj.api.LoadType;
import io.sunc.mj.api.MusicApi;
import io.sunc.mj.api.model.Music;
import io.sunc.mj.api.model.RecommendDetailList;
import io.sunc.mj.mvp.contract.SearchContract;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by hefuyi on 2017/1/3.
 */

public class SearchPresenter implements SearchContract.Presenter{

    private SearchContract.View mView;
    private Subscription mSubscription;
    private CompositeSubscription mCompositeSubscription;

    public SearchPresenter() {
        mCompositeSubscription = new CompositeSubscription();
    }

    private int mCurrentPage = 0, mPageSize = 30;

    @Override
    public void attachView(SearchContract.View view) {
        mView = view;
    }

    @Override
    public void subscribe() {
    }

    @Override
    public void unsubscribe() {
        mCompositeSubscription.clear();
        if (mSubscription != null && mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void search(String queryString, LoadType type) {
        if(type == LoadType.New) {
            mCurrentPage = 0;
        }
        if (mSubscription != null && mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        mSubscription = Api.getMusicApi(BaseURL.MUSIC_PLAY_LIST_DETAIL)
                .searchMusic(queryString, "search", mCurrentPage * mPageSize, mPageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<MusicApi.SearchModel>() {
                    @Override
                    public void call(MusicApi.SearchModel response) {
                        if(response.isSuccuss()) {
                            List<RecommendDetailList.Track> list = response.getData().getSongs();
                            if (list == null && list.size() == 0) {
                                mView.setHasMore(false);
                                mView.showEmptyView();
                            } else {
                                mView.setHasMore(list.size() == mPageSize);
                                mCurrentPage++;
                                mView.showSearchResult(list);
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e("sunc", "throwable==" + throwable.getMessage());
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
