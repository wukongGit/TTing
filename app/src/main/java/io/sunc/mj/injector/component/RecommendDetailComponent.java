package io.sunc.mj.injector.component;

import dagger.Component;
import io.sunc.mj.injector.module.RecommendDetailModule;
import io.sunc.mj.injector.scope.PerActivity;
import io.sunc.mj.ui.fragment.RecommendDetailFragment;

/**
 * Created by hefuyi on 2016/11/12.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {RecommendDetailModule.class})
public interface RecommendDetailComponent {

    void inject(RecommendDetailFragment songsFragment);
}
