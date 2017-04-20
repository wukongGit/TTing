package io.sunc.mj.injector.component;

import dagger.Component;
import io.sunc.mj.injector.module.SearchModule;
import io.sunc.mj.injector.scope.PerActivity;
import io.sunc.mj.ui.fragment.SearchFragment;

/**
 * Created by hefuyi on 2017/1/3.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = SearchModule.class)
public interface SearchComponent {

    void inject(SearchFragment searchFragment);
}
