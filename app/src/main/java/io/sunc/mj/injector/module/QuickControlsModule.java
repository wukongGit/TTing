package io.sunc.mj.injector.module;

import dagger.Module;
import dagger.Provides;
import io.sunc.mj.mvp.contract.QuickControlsContract;
import io.sunc.mj.mvp.presenter.QuickControlsPresenter;
import io.sunc.mj.mvp.usecase.GetLyric;
import io.sunc.mj.respository.interfaces.Repository;

/**
 * Created by hefuyi on 2016/11/7.
 */
@Module
public class QuickControlsModule {

    @Provides
    QuickControlsContract.Presenter getQuickControlsPresenter(GetLyric getLyric) {
        return new QuickControlsPresenter(getLyric);
    }

    @Provides
    GetLyric getLyricUsecase(Repository repository) {
        return new GetLyric(repository);
    }

}
