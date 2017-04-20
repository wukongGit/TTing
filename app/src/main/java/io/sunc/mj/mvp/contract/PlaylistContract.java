package io.sunc.mj.mvp.contract;

import java.util.List;

import io.sunc.mj.mvp.model.Playlist;
import io.sunc.mj.mvp.presenter.BasePresenter;
import io.sunc.mj.mvp.view.BaseView;

/**
 * Created by hefuyi on 2016/12/4.
 */

public interface PlaylistContract {

    interface View extends BaseView{

        void showPlaylist(List<Playlist> playlists);

        void showEmptyView();

    }

    interface Presenter extends BasePresenter<View>{

        void loadPlaylist();
    }
}
