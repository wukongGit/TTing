package io.sunc.mj.injector.component;

import dagger.Component;
import io.sunc.mj.injector.module.PlaylistModule;
import io.sunc.mj.injector.scope.PerActivity;
import io.sunc.mj.ui.fragment.PlaylistFragment;

/**
 * Created by hefuyi on 2016/12/5.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = PlaylistModule.class)
public interface PlaylistComponent {

    void inject(PlaylistFragment playlistFragment);
}
