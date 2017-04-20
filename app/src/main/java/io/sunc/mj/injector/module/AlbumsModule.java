package io.sunc.mj.injector.module;

import dagger.Module;
import dagger.Provides;
import io.sunc.mj.mvp.contract.AlbumsContract;
import io.sunc.mj.mvp.presenter.AlbumsPresenter;
import io.sunc.mj.mvp.usecase.GetAlbums;
import io.sunc.mj.respository.interfaces.Repository;

/**
 * Created by hefuyi on 2016/11/12.
 */
@Module
public class AlbumsModule {

    @Provides
    AlbumsContract.Presenter getAlbumsPresenter(GetAlbums getAlbums) {
        return new AlbumsPresenter(getAlbums);
    }

    @Provides
    GetAlbums getAlbumsUsecase(Repository repository) {
        return new GetAlbums(repository);
    }
}
