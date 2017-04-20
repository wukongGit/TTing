package io.sunc.mj.injector.module;

import dagger.Module;
import dagger.Provides;
import io.sunc.mj.mvp.contract.ArtistContract;
import io.sunc.mj.mvp.presenter.ArtistPresenter;
import io.sunc.mj.mvp.usecase.GetArtists;
import io.sunc.mj.respository.interfaces.Repository;

/**
 * Created by hefuyi on 2016/11/13.
 */
@Module
public class ArtistsModule {

    @Provides
    ArtistContract.Presenter getArtistPresenter(GetArtists getArtists) {
        return new ArtistPresenter(getArtists);
    }

    @Provides
    GetArtists getArtistsUsecase(Repository repository) {
        return new GetArtists(repository);
    }
}
