package io.sunc.mj.injector.module;

import dagger.Module;
import dagger.Provides;
import io.sunc.mj.mvp.contract.SearchContract;
import io.sunc.mj.mvp.presenter.SearchPresenter;

/**
 * Created by hefuyi on 2017/1/3.
 */
@Module
public class SearchModule {

    @Provides
    SearchContract.Presenter getSearchPresenter() {
        return new SearchPresenter();
    }
}
