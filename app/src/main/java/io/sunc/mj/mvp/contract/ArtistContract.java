package io.sunc.mj.mvp.contract;

import java.util.List;

import io.sunc.mj.mvp.model.Artist;
import io.sunc.mj.mvp.presenter.BasePresenter;
import io.sunc.mj.mvp.view.BaseView;

/**
 * Created by hefuyi on 2016/11/13.
 */

public interface ArtistContract {

    interface View extends BaseView{

        void showArtists(List<Artist> artists);

        void showEmptyView();
    }

    interface Presenter extends BasePresenter<View>{

        void loadArtists(String action);
    }
}
