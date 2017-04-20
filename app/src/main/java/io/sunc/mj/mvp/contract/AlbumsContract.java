package io.sunc.mj.mvp.contract;

import java.util.List;

import io.sunc.mj.mvp.model.Album;
import io.sunc.mj.mvp.presenter.BasePresenter;
import io.sunc.mj.mvp.view.BaseView;

/**
 * Created by hefuyi on 2016/11/12.
 */

public interface AlbumsContract {

    interface View extends BaseView{

        void showAlbums(List<Album> albumList);

        void showEmptyView();
    }

    interface Presenter extends BasePresenter<View>{

        void loadAlbums(String action);

    }
}
