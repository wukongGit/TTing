package io.sunc.mj.injector.module;

import dagger.Module;
import dagger.Provides;
import io.sunc.mj.mvp.contract.PlayRankingContract;
import io.sunc.mj.mvp.presenter.PlayRankingPresenter;
import io.sunc.mj.mvp.usecase.GetSongs;
import io.sunc.mj.respository.interfaces.Repository;

/**
 * Created by hefuyi on 2016/12/9.
 */
@Module
public class PlayRankingModule {

    @Provides
    GetSongs getSongsUsecase(Repository repository) {
        return new GetSongs(repository);
    }

    @Provides
    PlayRankingContract.Presenter getPlayRankingPresenter(GetSongs getSongs) {
        return new PlayRankingPresenter(getSongs);
    }
}
