package io.sunc.mj.mvp.contract;

import java.util.List;

import io.sunc.mj.mvp.model.Song;
import io.sunc.mj.mvp.presenter.BasePresenter;
import io.sunc.mj.mvp.view.BaseView;

/**
 * Created by hefuyi on 2016/12/12.
 */

public interface FolderSongsContract {

    interface View extends BaseView{

        void showSongs(List<Song> songList);

    }

    interface Presenter extends BasePresenter<View>{

        void loadSongs(String path);

    }
}
