package io.sunc.mj.injector.component;

import dagger.Component;
import io.sunc.mj.injector.module.ActivityModule;
import io.sunc.mj.injector.module.ArtistsModule;
import io.sunc.mj.injector.scope.PerActivity;
import io.sunc.mj.ui.fragment.ArtistFragment;

/**
 * Created by hefuyi on 2016/11/13.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, ArtistsModule.class})
public interface ArtistComponent {

    void inject(ArtistFragment artistFragment);
}
