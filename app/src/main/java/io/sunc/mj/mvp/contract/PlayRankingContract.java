package io.sunc.mj.mvp.contract;

import android.content.Context;

import java.util.List;

import io.sunc.mj.mvp.model.Song;
import io.sunc.mj.mvp.presenter.BasePresenter;
import io.sunc.mj.mvp.view.BaseView;

/**
 * Created by hefuyi on 2016/12/9.
 */

public interface PlayRankingContract {

    interface View extends BaseView {

        Context getContext();

        void showRanking(List<Song> songList);

        void showEmptyView();
    }

    interface Presenter extends BasePresenter<View>{

        void loadRanking();
    }

}
