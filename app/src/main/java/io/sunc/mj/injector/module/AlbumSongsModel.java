package io.sunc.mj.injector.module;

import dagger.Module;
import dagger.Provides;
import io.sunc.mj.mvp.contract.AlbumDetailContract;
import io.sunc.mj.mvp.presenter.AlbumDetailPresenter;
import io.sunc.mj.mvp.usecase.GetAlbumSongs;
import io.sunc.mj.respository.interfaces.Repository;

/**
 * Created by hefuyi on 2016/12/3.
 */
@Module
public class AlbumSongsModel {

    @Provides
    GetAlbumSongs getAlbumSongUsecase(Repository repository) {
        return new GetAlbumSongs(repository);
    }

    @Provides
    AlbumDetailContract.Presenter getAlbumDetailPresenter(GetAlbumSongs getAlbumSongs) {
        return new AlbumDetailPresenter(getAlbumSongs);
    }
}
