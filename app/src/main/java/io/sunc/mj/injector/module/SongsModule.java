package io.sunc.mj.injector.module;

import dagger.Module;
import dagger.Provides;
import io.sunc.mj.mvp.contract.SongsContract;
import io.sunc.mj.mvp.presenter.SongsPresenter;
import io.sunc.mj.mvp.usecase.GetSongs;
import io.sunc.mj.respository.interfaces.Repository;

/**
 * Created by hefuyi on 2016/11/12.
 */
@Module
public class SongsModule {

    @Provides
    SongsContract.Presenter getSongsPresenter(GetSongs getSongs) {
        return new SongsPresenter(getSongs);
    }

    @Provides
    GetSongs getSongsUsecase(Repository repository) {
        return new GetSongs(repository);
    }
}
