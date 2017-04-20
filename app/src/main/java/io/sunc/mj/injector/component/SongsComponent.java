package io.sunc.mj.injector.component;

import dagger.Component;
import io.sunc.mj.injector.module.ActivityModule;
import io.sunc.mj.injector.module.SongsModule;
import io.sunc.mj.injector.scope.PerActivity;
import io.sunc.mj.ui.fragment.SongsFragment;

/**
 * Created by hefuyi on 2016/11/12.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, SongsModule.class})
public interface SongsComponent {

    void inject(SongsFragment songsFragment);
}
