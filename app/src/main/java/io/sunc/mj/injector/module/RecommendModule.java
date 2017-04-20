package io.sunc.mj.injector.module;

import dagger.Module;
import dagger.Provides;
import io.sunc.mj.mvp.contract.RecommendContract;
import io.sunc.mj.mvp.presenter.RecommendPresenter;

/**
 * Created by hefuyi on 2016/11/12.
 */
@Module
public class RecommendModule {

    @Provides
    RecommendContract.Presenter getSongsPresenter() {
        return new RecommendPresenter();
    }
}
