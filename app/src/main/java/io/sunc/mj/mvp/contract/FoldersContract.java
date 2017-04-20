package io.sunc.mj.mvp.contract;

import android.content.Context;

import java.util.List;

import io.sunc.mj.mvp.model.FolderInfo;
import io.sunc.mj.mvp.presenter.BasePresenter;
import io.sunc.mj.mvp.view.BaseView;

/**
 * Created by hefuyi on 2016/12/11.
 */

public interface FoldersContract {

    interface View extends BaseView{

        Context getContext();

        void showEmptyView();

        void showFolders(List<FolderInfo> folderInfos);
    }

    interface Presenter extends BasePresenter<View>{

        void loadFolders();
    }
}
