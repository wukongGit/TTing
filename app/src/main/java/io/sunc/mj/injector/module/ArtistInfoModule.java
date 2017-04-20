package io.sunc.mj.injector.module;

import dagger.Module;
import dagger.Provides;
import io.sunc.mj.mvp.contract.ArtistDetailContract;
import io.sunc.mj.mvp.presenter.ArtistDetailPresenter;
import io.sunc.mj.mvp.usecase.GetArtistInfo;
import io.sunc.mj.respository.interfaces.Repository;

/**
 * Created by hefuyi on 2016/11/13.
 */
@Module
public class ArtistInfoModule {

    @Provides
    GetArtistInfo getArtistInfoUsecase(Repository repository) {
        return new GetArtistInfo(repository);
    }

    @Provides
    ArtistDetailContract.Presenter getArtistDetailPresenter() {
        return new ArtistDetailPresenter();
    }
}
