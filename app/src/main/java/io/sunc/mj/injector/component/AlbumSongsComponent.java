package io.sunc.mj.injector.component;

import dagger.Component;
import io.sunc.mj.injector.module.AlbumSongsModel;
import io.sunc.mj.injector.scope.PerActivity;
import io.sunc.mj.ui.fragment.AlbumDetailFragment;

/**
 * Created by hefuyi on 2016/12/3.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = AlbumSongsModel.class)
public interface AlbumSongsComponent {

    void inject(AlbumDetailFragment albumDetailFragment);

}
