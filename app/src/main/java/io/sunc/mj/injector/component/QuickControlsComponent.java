package io.sunc.mj.injector.component;

import dagger.Component;
import io.sunc.mj.injector.module.ActivityModule;
import io.sunc.mj.injector.module.QuickControlsModule;
import io.sunc.mj.injector.scope.PerActivity;
import io.sunc.mj.ui.fragment.QuickControlsFragment;

/**
 * Created by hefuyi on 2016/11/8.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, QuickControlsModule.class})
public interface QuickControlsComponent {

    void inject(QuickControlsFragment quickControlsFragment);

}
