package io.sunc.mj.mvp.contract;

import java.util.List;

import io.sunc.mj.api.LoadType;
import io.sunc.mj.api.model.RecommendDetailList;
import io.sunc.mj.mvp.presenter.BasePresenter;
import io.sunc.mj.mvp.view.BaseView;

/**
 * Created by hefuyi on 2017/1/3.
 */

public interface SearchContract {

    interface View extends BaseView {

        void showSearchResult(List<RecommendDetailList.Track> list);

        void showEmptyView();

        void startMusic(String url);

        void setHasMore(boolean hasMore);
    }

    interface Presenter extends BasePresenter<View> {

        void search(String queryString, LoadType type);

        void loadMusic(long musicId);
    }

}
