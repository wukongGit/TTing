package io.sunc.mj.injector.module;

import dagger.Module;
import dagger.Provides;
import io.sunc.mj.mvp.contract.ArtistSongContract;
import io.sunc.mj.mvp.presenter.ArtistSongPresenter;
import io.sunc.mj.mvp.usecase.GetArtistSongs;
import io.sunc.mj.respository.interfaces.Repository;

/**
 * Created by hefuyi on 2016/11/25.
 */
@Module
public class ArtistSongModule {

    @Provides
    GetArtistSongs getArtistSongsUsecase(Repository repository) {
        return new GetArtistSongs(repository);
    }

    @Provides
    ArtistSongContract.Presenter getArtistSongPresenter(GetArtistSongs getArtistSongs) {
        return new ArtistSongPresenter(getArtistSongs);
    }
}
