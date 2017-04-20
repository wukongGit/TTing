package io.sunc.mj.injector.component;

import dagger.Component;
import io.sunc.mj.injector.module.PlaylistSongModule;
import io.sunc.mj.injector.scope.PerActivity;
import io.sunc.mj.ui.fragment.PlaylistDetailFragment;

/**
 * Created by hefuyi on 2016/12/6.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = PlaylistSongModule.class)
public interface PlaylistSongComponent {

    void inject(PlaylistDetailFragment playlistDetailFragment);
}
