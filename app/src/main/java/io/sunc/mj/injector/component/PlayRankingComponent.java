package io.sunc.mj.injector.component;

import dagger.Component;
import io.sunc.mj.injector.module.PlayRankingModule;
import io.sunc.mj.injector.scope.PerActivity;
import io.sunc.mj.ui.fragment.PlayRankingFragment;

/**
 * Created by hefuyi on 2016/12/9.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = PlayRankingModule.class)
public interface PlayRankingComponent {

    void inject(PlayRankingFragment playRankingFragment);
}
