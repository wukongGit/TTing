package io.sunc.mj.injector.module;

import dagger.Module;
import dagger.Provides;
import io.sunc.mj.mvp.contract.RecommendDetailContract;
import io.sunc.mj.mvp.presenter.RecommendDetailPresenter;

/**
 * Created by hefuyi on 2016/12/3.
 */
@Module
public class RecommendDetailModule {

    @Provides
    RecommendDetailContract.Presenter getRecommendDetailPresenter() {
        return new RecommendDetailPresenter();
    }
}
