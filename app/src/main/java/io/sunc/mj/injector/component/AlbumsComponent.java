package io.sunc.mj.injector.component;

import dagger.Component;
import io.sunc.mj.injector.module.ActivityModule;
import io.sunc.mj.injector.module.AlbumsModule;
import io.sunc.mj.injector.scope.PerActivity;
import io.sunc.mj.ui.fragment.AlbumFragment;

/**
 * Created by hefuyi on 2016/11/12.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, AlbumsModule.class})
public interface AlbumsComponent {

    void inject(AlbumFragment albumFragment);
}
