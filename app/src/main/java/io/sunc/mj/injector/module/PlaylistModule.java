package io.sunc.mj.injector.module;

import dagger.Module;
import dagger.Provides;
import io.sunc.mj.mvp.contract.PlaylistContract;
import io.sunc.mj.mvp.presenter.PlaylistPresenter;
import io.sunc.mj.mvp.usecase.GetPlaylists;
import io.sunc.mj.respository.interfaces.Repository;

/**
 * Created by hefuyi on 2016/12/5.
 */
@Module
public class PlaylistModule {

    @Provides
    GetPlaylists getPlaylistUsecase(Repository repository) {
        return new GetPlaylists(repository);
    }

    @Provides
    PlaylistContract.Presenter getPlaylistPresenter(GetPlaylists getPlaylists) {
        return new PlaylistPresenter(getPlaylists);
    }
}
