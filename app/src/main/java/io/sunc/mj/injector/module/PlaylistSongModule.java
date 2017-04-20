package io.sunc.mj.injector.module;

import dagger.Module;
import dagger.Provides;
import io.sunc.mj.mvp.contract.PlaylistDetailContract;
import io.sunc.mj.mvp.presenter.PlaylistDetailPresenter;
import io.sunc.mj.mvp.usecase.GetPlaylistSongs;
import io.sunc.mj.respository.interfaces.Repository;

/**
 * Created by hefuyi on 2016/12/6.
 */
@Module
public class PlaylistSongModule {

    @Provides
    GetPlaylistSongs getPlaylistSongsUsecase(Repository repository) {
        return new GetPlaylistSongs(repository);
    }

    @Provides
    PlaylistDetailContract.Presenter getPlaylistDetailPresenter(GetPlaylistSongs getPlaylistSongs) {
        return new PlaylistDetailPresenter(getPlaylistSongs);
    }
}
