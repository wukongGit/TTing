package io.sunc.mj.injector.module;

import dagger.Module;
import dagger.Provides;
import io.sunc.mj.mvp.contract.FoldersContract;
import io.sunc.mj.mvp.presenter.FolderPresenter;
import io.sunc.mj.mvp.usecase.GetFolders;
import io.sunc.mj.respository.interfaces.Repository;

/**
 * Created by hefuyi on 2016/12/11.
 */
@Module
public class FolderModule {

    @Provides
    GetFolders getFoldersUsecase(Repository repository) {
        return new GetFolders(repository);
    }

    @Provides
    FoldersContract.Presenter getFoldersPresenter(GetFolders getFolders) {
        return new FolderPresenter(getFolders);
    }
}
