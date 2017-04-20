package io.sunc.mj.mvp.presenter;

import java.util.List;

import io.sunc.mj.api.Api;
import io.sunc.mj.api.BaseURL;
import io.sunc.mj.api.MusicApi;
import io.sunc.mj.api.model.RecommendList;
import io.sunc.mj.mvp.contract.RecommendContract;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by hefuyi on 2016/11/12.
 */

public class RecommendPresenter implements RecommendContract.Presenter {

    private RecommendContract.View mView;
    private CompositeSubscription mCompositeSubscription;
    private int mCurrentPage = 0, mPageSize = 20;

    public RecommendPresenter() {
    }

    @Override
    public void attachView(RecommendContract.View view) {
        mView = view;
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void subscribe() {
    }

    @Override
    public void unsubscribe() {
        mCompositeSubscription.clear();
    }

    @Override
    public void loadSongs() {
        mCompositeSubscription.clear();

        Subscription subscription = Api.getMusicApi(BaseURL.MUSIC_PLAY_LIST)
                .getPlayList("topPlayList", "全部", mCurrentPage * mPageSize, mPageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<MusicApi.RecommendListModel>() {
                    @Override
                    public void call(MusicApi.RecommendListModel response) {
                        if(response.isSuccuss()) {
                            mView.setHasMore(response.isMore());
                            List<RecommendList> list = response.getData();
                            if (list == null || list.size() == 0) {
                                mView.showEmptyView();
                            } else {
                                mCurrentPage++;
                                mView.showSongs(list);
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
