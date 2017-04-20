package io.sunc.mj.injector.component;

import dagger.Component;
import io.sunc.mj.injector.module.ActivityModule;
import io.sunc.mj.injector.module.RecommendModule;
import io.sunc.mj.injector.scope.PerActivity;
import io.sunc.mj.ui.fragment.RecommendFragment;

/**
 * Created by hefuyi on 2016/11/12.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, RecommendModule.class})
public interface RecommendComponent {

    void inject(RecommendFragment songsFragment);
}
