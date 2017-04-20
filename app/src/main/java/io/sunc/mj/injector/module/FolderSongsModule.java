package io.sunc.mj.injector.module;

import dagger.Module;
import dagger.Provides;
import io.sunc.mj.mvp.contract.FolderSongsContract;
import io.sunc.mj.mvp.presenter.FolderSongsPresenter;
import io.sunc.mj.mvp.usecase.GetFolderSongs;
import io.sunc.mj.respository.interfaces.Repository;

/**
 * Created by hefuyi on 2016/12/12.
 */
@Module
public class FolderSongsModule {

    @Provides
    GetFolderSongs getFolderSongsUsecase(Repository repository) {
        return new GetFolderSongs(repository);
    }

    @Provides
    FolderSongsContract.Presenter getFolderSongsPresenter(GetFolderSongs getFolderSongs) {
        return new FolderSongsPresenter(getFolderSongs);
    }
}
