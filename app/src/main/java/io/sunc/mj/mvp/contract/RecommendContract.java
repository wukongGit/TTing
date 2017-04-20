package io.sunc.mj.mvp.contract;

import java.util.List;

import io.sunc.mj.api.model.RecommendList;
import io.sunc.mj.mvp.presenter.BasePresenter;
import io.sunc.mj.mvp.view.BaseView;

/**
 * Created by hefuyi on 2016/11/12.
 */

public interface RecommendContract {

    interface View extends BaseView {

        void showSongs(List<RecommendList> songList);

        void showEmptyView();

        void setHasMore(boolean hasMore);
    }

    interface Presenter extends BasePresenter<View>{

        void loadSongs();
    }
}
