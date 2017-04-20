package io.sunc.mj.injector.module;

import dagger.Module;
import dagger.Provides;
import io.sunc.mj.mvp.contract.PlayqueueSongContract;
import io.sunc.mj.mvp.presenter.PlayqueueSongPresenter;
import io.sunc.mj.mvp.usecase.GetSongs;
import io.sunc.mj.respository.interfaces.Repository;

/**
 * Created by hefuyi on 2016/12/27.
 */
@Module
public class PlayqueueSongModule {

    @Provides
    GetSongs getSongsUsecase(Repository repository) {
        return new GetSongs(repository);
    }

    @Provides
    PlayqueueSongContract.Presenter getPlayqueueSongUsecase(GetSongs getSongs) {
        return new PlayqueueSongPresenter(getSongs);
    }
}
